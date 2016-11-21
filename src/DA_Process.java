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
	public String name; 
	private DA_Process_RMI rp1;
	private DA_Process_RMI rp2;
	public static final int FACTOR = 1;
	private int[] vectorClock = new int[3];
	
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
	
	protected DA_Process(String n, String ipAddress1, String proc1, String ipAddress2, String proc2) throws RemoteException {
		super();
		name=n;
		try {
			rp1=(DA_Process_RMI)Naming.lookup("rmi://"+ipAddress1+"/"+proc1);
			rp2=(DA_Process_RMI)Naming.lookup("rmi://"+ipAddress2+"/"+proc2);

		} catch (MalformedURLException | NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	protected DA_Process() throws RemoteException {
		super();
	}

	public boolean sendMessage(String message) throws RemoteException {
		rp1.receiveMessage(message, getVectorClock());
		rp2.receiveMessage(message, getVectorClock());
		System.out.println("Sent message '"+message+"' with time vector "+ getVectorClock().toString());
		return false;
	}
	
	private int[] getVectorClock() {
		return vectorClock;
	}

	public boolean receiveMessage(String message, int[] timeVector) throws RemoteException {
		System.out.println("Received message '"+message+"' with time vector "+ timeVector.toString());
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
		System.out.println("Add message '"+message+"' to buffer, with time vector "+ timeVector.toString());

		BufferElement be = new BufferElement(message, timeVector);
		buffer.add(be);
	}

	private void save(String m, int[] tv){
		System.out.println("Saved message '"+m+"' with time vector "+ tv.toString());
		setVectorClock(tv);
	}

	private void setVectorClock(int [] newVector) {
		vectorClock = newVector;
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

		return true;
	}

}
