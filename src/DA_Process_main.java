import java.rmi.Naming;
import java.rmi.RemoteException;

public class DA_Process_main {

	public static void main(String[] args) {
		int registryPort = Integer.parseInt(args[0]);
		int processNumber = Integer.parseInt(args[1]);
		
		String ipAddress1 = args[2];
		int rp1Number = Integer.parseInt(args[3]);
		String ipAddress2 = args[4];
		int rp2Number = Integer.parseInt(args[5]);
		
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

			Naming.rebind("rmi://145.94.172.45/"+processNumber, remoteProcess);
			System.out.println("Server is Ready");
			//
			//client
			//
			DA_Process process1=new DA_Process(processNumber, ipAddress1, rp1Number, ipAddress2, rp2Number);
			System.out.println("Running process "+processNumber+".....");
//			DA_Process_RMI process2=(DA_Process_RMI)Naming.lookup("rmi://"+ipAddress+"/process2");
//			DA_Process_RMI process3=(DA_Process_RMI)Naming.lookup("rmi://"+ipAddress+"/process3");
			System.out.println("Connected to process "+rp1Number+" and process "+rp2Number+".....");
			
			process1.sendMessage(System.console().readLine());

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}