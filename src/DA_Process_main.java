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

			System.setProperty("java.rmi.server.hostname","145.94.172.45");
			try {
				java.rmi.registry.LocateRegistry.createRegistry(registryPort);
			} catch (RemoteException e){
				e.printStackTrace();
			} catch (Exception e){
				
			}

			DA_Process remoteProcess=new DA_Process();

			Naming.rebind("rmi://145.94.172.45/"+processName, remoteProcess);
			System.out.println("Server is Ready");
			//
			//client
			//
			DA_Process process1=new DA_Process("imed", ipAddress1, rp1Name, ipAddress2, rp2Name);
			System.out.println("Running  "+processName+".....");
//			DA_Process_RMI process2=(DA_Process_RMI)Naming.lookup("rmi://"+ipAddress+"/process2");
//			DA_Process_RMI process3=(DA_Process_RMI)Naming.lookup("rmi://"+ipAddress+"/process3");
			System.out.println("Connected to "+rp1Name+" and "+rp2Name+".....");
			
			process1.sendMessage(System.console().readLine());

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}