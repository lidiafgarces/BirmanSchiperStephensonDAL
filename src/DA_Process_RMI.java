import java.rmi.Remote;
import java.rmi.RemoteException;
 
public interface DA_Process_RMI extends Remote{
 
	public boolean sendMessage(DA_Process process, String message, int processNumber, int[] timeVector) throws RemoteException;
	public boolean receiveMessage(DA_Process process, String message, int processNumber, int[] timeVector) throws RemoteException;

}