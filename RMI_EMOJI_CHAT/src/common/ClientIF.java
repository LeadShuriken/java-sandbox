package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientIF extends Remote {
	public void messageFromServer(String message) throws RemoteException;
}