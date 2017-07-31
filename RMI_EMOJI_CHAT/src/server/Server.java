package server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import common.ClientIF;
import common.ServerIF;

import java.util.ArrayList;

/**
 * @author Deian Mishev INFM219 - Assignment
 * @version 1.0
 * @description Server class for the chat server
 * @implements ServerIF
 * @extends UnicastRemoteObject
 */
public class Server extends UnicastRemoteObject implements ServerIF {

	private static final long serialVersionUID = 1L;

	private ArrayList<UserPOJO> clients; // Clients to the server
	private final String IMAGE_FOLDER = System.getProperty("user.dir") + "/server/img/"; // Image
																							// Folder

	private final String[] emojiText = { ":)", ":D", ";)", ":o", "ZZ", ":`", ":p", ":(" }; // EmojiMapString
	private final String[] emojiFiles = { "sm0.jpg", "sm1.jpg", "sm2.jpg", "sm3.jpg", "sm4.jpg", "sm5.jpg", "sm6.jpg",
			"sm7.jpg" }; // FileMapString

	public Server() throws RemoteException {
		super();
		clients = new ArrayList<UserPOJO>(1);
	}

	public static void main(String[] args) {
		startRMIRegistry();
		try {
			Naming.rebind("Chat_Server", new Server());
			System.out.println("RMI Server is running...");
		} catch (Exception e) {
			System.out.println("Server cannot start");
		}
	}

	/**
	 * @method startRMIRegistry
	 * @description Method starting the RMI registry to the default port of
	 *              1099.
	 * @return void
	 */
	private static void startRMIRegistry() {
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI Registry strarted...");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @method sendMessege
	 * @param newMessage
	 *            The message that was just received by a user.
	 * @description Method sending a received message to all users.
	 * @return void
	 */
	private void sendMessege(String newMessage) {
		for (UserPOJO c : clients) {
			try {
				c.getClient().messageFromServer(newMessage);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @method getIndex
	 * @description Method finding the index of a string inside a string array
	 *              or -1 if not there.
	 * @param a
	 *            A string array to look for the string into.
	 * @param b
	 *            The string to look for in the string array.
	 * @return int
	 */
	private int getIndex(String[] a, String b) {
		for (int i = 0; i < a.length; i++) {
			if (a[i].equals(b)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @method sendToChat
	 * @description Method preparing the message for the chat users. Looking for
	 *              Emojis etc..
	 * @param name
	 *            Who the message came from
	 * @param message
	 *            The message that the user sent.
	 * @return void
	 */
	public void sendToChat(String name, String message) {
		for (int i = 0; i < emojiText.length; i++) {
			String t = emojiText[i];
			message = message.replace(t, "~" + t);
		}

		String newMessage = name + " : " + message + "\n";
		sendMessege(newMessage);
	}

	/**
	 * @method registerUser
	 * @description Marshaled. Method getting the user from the rmi registry and
	 *              adding it to the know users of the server.
	 * @param userData
	 *            { name, hostname, clientName } of the user.
	 * @throws RemoteException
	 */
	@Override
	public void registerUser(String[] userData) throws RemoteException {
		try {
			ClientIF nextClient = (ClientIF) Naming.lookup("rmi://" + userData[1] + "/" + userData[2]);
			clients.add(new UserPOJO(userData[0], nextClient));
			sendMessege("[Server] : " + userData[0] + " has joined the group.\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @method leaveChat
	 * @description Marshaled. Method removing the client from the clients stack.
	 * @param userName Name of the user who wants to leave.
	 * @throws RemoteException
	 */
	@Override
	public void leaveChat(String userName) throws RemoteException {
		for (UserPOJO c : clients) {
			if (c.getName().equals(userName)) {
				System.out.println(userName + " left the chat session");
				System.out.println(new Date(System.currentTimeMillis()));
				clients.remove(c);
				break;
			}
		}
	}

	/**
	 * @method getDate
	 * @description Marshaled. Method returning the current time at the server.
	 * @return java.util.Date
	 * @throws RemoteException
	 */
	@Override
	public java.util.Date getDate() throws RemoteException {
		return new java.util.Date();
	}

	/**
	 * @method getEmojiAsset
	 * @description Marshaled. Method giving the user the getAssetPOJO holding the byte[] with the asset. This is a last resort.
	 * @param assetType String definition of the asset type. Mapped at the server
	 * @return AssetPOJO 
	 * @throws RemoteException
	 */
	@Override
	public AssetPOJO getAsset(String assetType) throws RemoteException {
		int index = getIndex(emojiText, assetType);
		AssetPOJO pojo = new AssetPOJO(IMAGE_FOLDER);
		if (index != -1) {
			pojo.setFile(emojiFiles[index]);
		}
		return pojo;
	}
}
