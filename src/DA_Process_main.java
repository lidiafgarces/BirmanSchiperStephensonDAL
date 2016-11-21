import java.rmi.Naming;
import java.rmi.RemoteException;

public class DA_Process_main {

	public static void main(String[] args) {
		int registryPort = Integer.parseInt(args[0]);
		String processName = args[1];
		
		String ipAddress1 = args[2];
		String rp1Name = args[3];
		String ipAddress2 = args[4];
		String rp2Name = args[5];
		
		try{
			//
			//server
			//

			System.setProperty("java.rmi.server.hostname","localhost");
			try {
			java.rmi.registry.LocateRegistry.createRegistry(registryPort);
			} catch (RemoteException e){
				e.printStackTrace();
			}

			DA_Process remoteProcess=new DA_Process();

			Naming.rebind("rmi://localhost/"+processName, remoteProcess);
			System.out.println("Server is Ready");
			//
			//client
			//
			DA_Process process1=new DA_Process("imed", ipAddress1, rp1Name, ipAddress2, rp2Name);
			System.out.println("Process-1 Running.....");
//			DA_Process_RMI process2=(DA_Process_RMI)Naming.lookup("rmi://"+ipAddress+"/process2");
//			DA_Process_RMI process3=(DA_Process_RMI)Naming.lookup("rmi://"+ipAddress+"/process3");
			System.out.println("Connected to Process-2 and Process-3 .....");
			
			process1.sendMessage(System.console().readLine());

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}