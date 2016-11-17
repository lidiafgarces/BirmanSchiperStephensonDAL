import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DA_Process extends UnicastRemoteObject implements DA_Process_RMI{
	
	public String name;
	
	protected DA_Process(String n) throws RemoteException {
		super();
		name=n;
	}
	
	protected DA_Process() throws RemoteException {
		super();
	}

	public boolean sendMessage(DA_Process process, String message, int pNumber, int[] timeVector) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean receiveMessage(DA_Process process, String message, int pNumber, int[] timeVector) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

}
