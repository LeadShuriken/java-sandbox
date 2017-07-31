package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.rmi.RemoteException;

/**
 * @author Deian Mishev INFM219 - Assignment
 * @version 1.0
 * @description Class handling GUI operations for the user
 * @implements ActionListener
 * @extends JFrame
 */
public class ClientGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private String name, message;
	public Container c;

	protected JFrame frame;
	private JTextField textField;
	protected JTextArea userArea;
	protected JEditorPane textPane;
	protected String hostname;

	protected JButton startButton, sendButton;
	protected JPanel clientPanel, userPanel;

	private Client client;

	public static void main(String args[]) {
		if (args.length != 0) {
			new ClientGUI(args[0]);
		} else {
			System.out.println("No hostname givven");
		}
	}

	public ClientGUI(String hostname) {
		this.hostname = hostname;
		frame = new JFrame("Client UI");
		frame.setResizable(false);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (client != null) {
					try {
						sendMessage("...leaving...");
						client.leaveChat();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				System.exit(0);
			}
		});

		c = getContentPane();
		JPanel outerPanel = new JPanel(new BorderLayout());

		outerPanel.add(createInputPanel(), BorderLayout.CENTER);
		outerPanel.add(createTextPanel(), BorderLayout.NORTH);

		c.setLayout(new BorderLayout());
		c.add(outerPanel, BorderLayout.CENTER);
		c.add(makeButtons(), BorderLayout.SOUTH);

		frame.add(c);
		frame.pack();
		frame.setAlwaysOnTop(false);
		frame.setLocation(150, 150);
		textField.requestFocus();

		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * @method makeButtons
	 * @description Method making the buttons for the chat GUI.
	 * @return JPanel
	 */
	private JPanel makeButtons() {
		sendButton = new JButton("Send ");
		sendButton.addActionListener(this);
		sendButton.setEnabled(false);

		startButton = new JButton("Start ");
		startButton.addActionListener(this);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 1));
		buttonPanel.add(startButton);
		buttonPanel.add(sendButton);
		return buttonPanel;
	}

	/**
	 * @method initSmileyListener
	 * @description Method making the buttons for the chat GUI.
	 * @param emo
	 *            EmojiHandling needed for the mapping of the available assets
	 *            for event handling
	 * @return void
	 */
	public void initSmileyListener(EmojiHandling emo) {
		textPane.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent event) {
				final DocumentEvent e = event;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (e.getDocument() instanceof StyledDocument) {
							try {
								StyledDocument doc = (StyledDocument) e.getDocument();
								int start = Utilities.getRowStart(textPane, Math.max(0, e.getOffset() - 1));
								int end = Utilities.getWordStart(textPane, e.getOffset() + e.getLength());
								String text = doc.getText(start, end - start);
								for (int i = 0; i < emo.emojiIcons.size(); i++) {
									String key = emo.emojiIcons.get(i);
									ImageIcon img = new ImageIcon(emo.assets.get(key));
									int m = text.indexOf(key);
									while (m >= 0) {
										final SimpleAttributeSet attrs = new SimpleAttributeSet(
												doc.getCharacterElement(start + m).getAttributes());
										if (StyleConstants.getIcon(attrs) == null) {
											StyleConstants.setIcon(attrs, img);
											doc.remove(start + m, 2);
											doc.insertString(start + m, key, attrs);
										}
										m = text.indexOf(key, m + 2);
									}
								}
							} catch (BadLocationException e1) {
								e1.printStackTrace();
							}
						}
					}
				});
			}

			public void removeUpdate(DocumentEvent e) {
			}

			public void changedUpdate(DocumentEvent e) {
			}
		});
	}

	/**
	 * @method appendText
	 * @description Method appending the message to the chat box.
	 * @param message
	 *            Message to be appended to the user chat box
	 * @return void
	 */
	public void appendText(String message) {
		try {
			Document doc = textPane.getDocument();
			doc.insertString(doc.getLength(), message, null);
		} catch (BadLocationException exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * @method createTextPanel
	 * @description Method creating the text panel (chat-box view)
	 * @return JPanel
	 */
	private JPanel createTextPanel() {
		textPane = new JTextPane();
		textPane.setContentType("text/html");
		String welcome = "Welcome enter your name and press Start to begin...\n";
		appendText(welcome);
		textPane.setMargin(new Insets(10, 10, 10, 10));
		textPane.setPreferredSize(new Dimension(400, 300));
		textPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textPane);
		JPanel textPanel = new JPanel();
		textPanel.add(scrollPane);
		return textPanel;
	}

	/**
	 * @method createInputPanel
	 * @description Method creating the text panel (input view)
	 * @return JPanel
	 */
	public JPanel createInputPanel() {
		JPanel inputPanel = new JPanel(new GridLayout(1, 1, 5, 5));
		inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		textField = new JTextField();
		textField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (startButton.isEnabled()) {
						startButton.doClick();
					} else {
						sendButton.doClick();
					}
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		inputPanel.add(textField);
		return inputPanel;
	}

	/**
	 * @method sendMessage
	 * @param chatMessage
	 *            Message to send to the chat
	 * @description Method sending a chat message to all user hooked to the
	 *              server
	 * @throws RemoteException
	 */
	private void sendMessage(String chatMessage) throws RemoteException {
		client.server.sendToChat(name, chatMessage);
	}

	/**
	 * @method getConnected
	 * @param userName
	 *            Name of the user to connect to the chat room.
	 * @description Method connecting the made GUI with an RMI adress
	 * @throws RemoteException
	 */
	private void getConnected(String userName, String hostname) throws RemoteException {
		// remove whitespace and non word characters to avoid malformed url
		String cleanedUserName = userName.replaceAll("\\s+", "_");
		cleanedUserName = userName.replaceAll("\\W+", "_");
		try {
			client = new Client(this, cleanedUserName, hostname);
			client.startClient();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @method actionPerformed
	 * @param e
	 *            Action event listener for the entire JFrame(GUI)
	 * @description Method arrived from ActionListener IF. Handles global
	 *              actions.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			// get connected to chat service
			if (e.getSource() == startButton) {
				name = textField.getText();
				if (name.length() != 0) {
					frame.setTitle(name + "'s console ");
					textField.setText("");
					appendText("username : " + name + " connecting to chat...\n");
					getConnected(name, hostname);
					if (!client.noConnection) {
						startButton.setEnabled(false);
						sendButton.setEnabled(true);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Enter your name to Start");
				}
			}
			// get text and clear textField
			if (e.getSource() == sendButton) {
				message = textField.getText();
				if (!message.equals("")) {
					textField.setText("");
					sendMessage(message);
					System.out.println("Sending message : " + message);
				}
			}
		} catch (RemoteException remoteExc) {
			remoteExc.printStackTrace();
		}
	}
}
