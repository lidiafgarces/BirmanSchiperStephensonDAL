import java.rmi.Naming;
import java.rmi.RemoteException;

public class DA_Process_main {

	public static void main(String[] args) {
		try{
			//
			//server
			//

			System.setProperty("java.rmi.server.hostname","localhost");
			try {
			java.rmi.registry.LocateRegistry.createRegistry(1098);
			} catch (RemoteException e){
				e.printStackTrace();
			}

			DA_Process remoteProcess=new DA_Process();

			Naming.rebind("rmi://localhost/process2", remoteProcess);
			System.out.println("Server is Ready");

			//
			//client
			//
			DA_Process process2=new DA_Process("imed");
			System.out.println("Process-2 Running.....");
			DA_Process_RMI process1=(DA_Process_RMI)Naming.lookup("rmi://localhost/process1");
			DA_Process_RMI process3=(DA_Process_RMI)Naming.lookup("rmi://localhost/process3");
			System.out.println("Connected to Process-1 and Process-3 .....");

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}