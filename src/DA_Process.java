import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class DA_Process extends UnicastRemoteObject implements DA_Process_RMI{
	
	private static final long serialVersionUID = 6384248030531941625L;
	private final int MAXBROADCASTS = 100;
	private int currentBroadcasts = 0;
	public int number; 
	private DA_Process_RMI rp1;
	private DA_Process_RMI rp2;
	public static final int FACTOR = 1;
	private int[] vectorClock = new int[3];
	private static final String NAMING = "proc";
	
	//TODO automatize for different number of processes
	
	private ArrayList<BufferElement> buffer = new ArrayList<BufferElement>();
	private class BufferElement{
		private String message = "";
		private int[] vector;
		public BufferElement(String m, int[] v){
			message=m;
			vector=v;		
		}
		public String getMessage(){
			return message;
		}
		public int[] getVector(){
			return vector;
		}
	}

	protected DA_Process(int n, String ipAddress1, int proc1, String ipAddress2, int proc2) throws RemoteException {
		super();
		number=n;
		createProcesses( n,  ipAddress1,  proc1, ipAddress2, proc2);
	}

	private void createProcesses(int n, String ipAddress1, int proc1, String ipAddress2, int proc2) throws RemoteException{
		try {
			rp1=(DA_Process_RMI)Naming.lookup("rmi://"+ipAddress1+"/"+NAMING+proc1);
			rp2=(DA_Process_RMI)Naming.lookup("rmi://"+ipAddress2+"/"+NAMING+proc2);

		} catch (MalformedURLException mue){
			mue.printStackTrace();
		} catch (Exception e) {
			long time = System.currentTimeMillis();
			while(System.currentTimeMillis()-time <5000){}
			System.out.println("polling...");
			createProcesses( n,  ipAddress1,  proc1, ipAddress2, proc2);
		}
	}

	protected DA_Process() throws RemoteException {
		super();
	}

	public boolean sendMessage(String message) throws RemoteException {
		System.out.println("\n");
		System.out.println("SENDING MESSAGE");
		int[] vector = getVectorClock();
		System.out.println("Old vector clock "+vectorToString(vectorClock) + " from process "+ number);
		System.out.println("Old vector clock from getVectorsClock "+vectorToString(vector));
		vector[number-1] ++;
		System.out.println("New vector clock "+vectorToString(vector));
		setVectorClock(vector);
		rp1.receiveMessage(message, getVectorClock());
		rp2.receiveMessage(message, getVectorClock());
		System.out.println("Sent message '"+message+"' with time vector "+ vectorToString(getVectorClock()));
		return false;
	}
	
	private String vectorToString(int[] vector){
		String ret = "[ ";
		for(int e: vector){
			ret += ""+e+" ";
		}
		return ret+"]";
	}
	
	private int[] getVectorClock() {
		return vectorClock;
	}

	public boolean receiveMessage(String message, int[] timeVector) throws RemoteException {
		System.out.println("\n");
		System.out.println("RECEIVING MESSAGE");
		System.out.println("Received message '"+message+"' with time vector "+ vectorToString(timeVector));
		if(checkCondition(timeVector)){
			save(message, timeVector);
			//the while from the slides
			for(int i=0; i<buffer.size(); i++){
				if(checkCondition(buffer.get(i).getVector())){
					save(buffer.get(i).getMessage(), buffer.get(i).getVector());
					i=0;
				}
			}	
		}
		else {
			saveToBuffer(message, timeVector);
		}
		return false;
	}
	
	private void saveToBuffer(String message, int[] timeVector) {
		System.out.println("Add message '"+message+"' to buffer, with time vector "+ vectorToString(timeVector));

		BufferElement be = new BufferElement(message, timeVector);
		buffer.add(be);
	}

	private void save(String m, int[] tv){
		System.out.println("Saved message '"+m+"' with time vector "+ vectorToString(tv));
		// We were setting the vector clock here, but actually it should be set before. Now it is in cekcCondition provisionally.
		//setVectorClock(tv);
	}

	private void setVectorClock(int [] newVector) {
		System.out.println("Global vector clock " + vectorToString(vectorClock) + " from process "+ number);
		this.vectorClock = newVector;
		System.out.println("Set clock vector to '"+vectorToString(newVector));
		System.out.println("New global vector clock " + vectorToString(vectorClock));


	}

	private boolean checkCondition(int[] receivedVector) {
		
		int[] helpVector = getVectorClock().clone();
		for(int i=0; i<helpVector.length; i++){
			if(receivedVector[i]>helpVector[i]){
				helpVector[i]+=1;
				break;
			}			
		}
		
		for(int i=0; i<helpVector.length; i++){
			if(receivedVector[i]>helpVector[i]){
				System.out.println("Condition not fulfilled");
				return false;
			}			
		}
		System.out.println("Condition fulfilled");
		
		//unclean
		System.out.println("We have compared "+vectorToString(receivedVector) + " with " +vectorToString(vectorClock));
		setVectorClock(helpVector);

		return true;
	}

}
