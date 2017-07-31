package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.AssetPOJO;

public interface ServerIF extends Remote {

	public void registerUser(String[] userData) throws RemoteException;

	public void leaveChat(String userName) throws RemoteException;

	public java.util.Date getDate() throws RemoteException;

	public AssetPOJO getAsset(String assetType) throws RemoteException;

	public void sendToChat(String name, String chatMessage) throws RemoteException;
}