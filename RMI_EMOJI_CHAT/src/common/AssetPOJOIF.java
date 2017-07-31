package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AssetPOJOIF extends Remote {
	public void init() throws RemoteException;
	
	public byte[] getData() throws RemoteException;

	public String getFilename() throws RemoteException;
}
