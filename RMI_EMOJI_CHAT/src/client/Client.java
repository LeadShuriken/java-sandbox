package client;

import common.ClientIF;
import common.AssetPOJOIF;
import common.ServerIF;

import java.io.*;
import java.math.BigInteger;
import java.rmi.*;

import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

/**
 * @author Deian Mishev INFM219 - Assignment
 * @version 1.0
 * @description Class handling RMI related and IO operations for the user
 * @implements ClientIF
 * @extends UnicastRemoteObject
 */
public class Client extends UnicastRemoteObject implements ClientIF {
	private static final long serialVersionUID = 1L;
	private final ClientGUI chatGUI;
	private final String name;
	private final EmojiHandling emoHandling;
	private final String clientName;
	private final SimpleDateFormat sm;
	private final String hostname;
	private final String serverName = "Chat_Server";
	private final SecureRandom random = new SecureRandom();
	
	public boolean noConnection = false;
	public ServerIF server;

	// ImageFolder Assets of the user //
	protected final String IMAGE_FOLDER = System.getProperty("user.dir") + "/client/img/";

	public Client(ClientGUI chatGUI, String userName, String hostname) throws RemoteException {
		super();
		this.hostname = hostname;
		this.name = userName;
		this.chatGUI = chatGUI;
		this.sm = new SimpleDateFormat("dd.MM,k:mm");
		this.emoHandling = new EmojiHandling(IMAGE_FOLDER);
		this.chatGUI.initSmileyListener(this.emoHandling);
		this.clientName = "Client_" + userName + nextSessionId();
	}


    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
    
	/**
	 * @method startClient
	 * @description Method bootstrapping the RMI Client. This method binds the
	 *              client to the RMI registry if the server is currently
	 *              available in the RMI registry
	 * @return void
	 */
	public void startClient() {
		String[] userData = { name, hostname, clientName };
		try {
			Naming.rebind("rmi://" + hostname + "/" + clientName, this);
			server = (ServerIF) Naming.lookup("rmi://" + hostname + "/" + serverName);
		} catch (ConnectException e) {
			JOptionPane.showMessageDialog(chatGUI.frame, "The server is not up!\nTry again later!",
					"Connection problem", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (Exception ex) {
			noConnection = true;
			ex.printStackTrace();
		}
		if (!noConnection) {
			try {
				server.registerUser(userData);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Client is running...");
		}
	}

	/**
	 * @method leaveChat
	 * @description Method unbinding the user from the RMI registry and leaving the chat
	 * @return void
	 */
	public void leaveChat() {
		try {
			server.leaveChat(name);
			Naming.unbind("rmi://" + hostname + "/" + clientName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @method saveEmoticonAsset
	 * @description Method saving the byte[] data send from the server as an
	 *              image on the local files system.
	 * @param data
	 *            byte[] containing the file from the server
	 * @param filePath
	 *            desired file path for saving the asset
	 * @return void
	 */
	private void saveEmoticonAsset(byte[] data, String filePath) {
		try {
			OutputStream out = new FileOutputStream(IMAGE_FOLDER + filePath);
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @method messageFromServer
	 * @description Marshaled. Method used by the server to send data to the
	 *              users.
	 * @param message
	 *            string message send by the server through RMI
	 * @throws RemoteExceptiopm
	 * @return void
	 */
	@Override
	public void messageFromServer(String message) throws RemoteException {
		String[] emojis = emoHandling.getEmojis(message);
		for (int i = 0; i < emojis.length; i++) {
			String key = emojis[i];
			if (!emoHandling.emojiIcons.contains(key)) {
				AssetPOJOIF emoj = server.getAsset(key);
				String file = emoj.getFilename();
				if (file != null) {
					if (!emoHandling.emojiAssets.contains(file)) {
						emoHandling.emojiAssets.add(file);
						emoj.init();
						byte[] data = emoj.getData();
						saveEmoticonAsset(data, file);
					}
				}
				emoHandling.emojiIcons.add(key);
				emoHandling.assets.put(key, IMAGE_FOLDER + file);
			}
		}
		message = message.replace("~", "");
		chatGUI.appendText("[" + sm.format(server.getDate()) + "] " + message);
	}
}
