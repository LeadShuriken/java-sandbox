package client;

import java.net.*;
import java.io.*;
import javax.swing.*;
import stud.*;

public class Client extends Thread {
	BufferedReader in;
	PrintWriter out;
	ObjectOutputStream oStr;
	InetAddress addr;
	Socket socket;
	String name;

	Client() {
		try {
			String server = null;
			InetAddress addr = InetAddress.getByName(server);
			System.out.println("addr = " + addr);
			socket = new Socket(addr, 9595);
			System.out.println("socket = " + socket);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

			oStr = new ObjectOutputStream(socket.getOutputStream());

		} catch (Exception e) {
			System.out.println("exception: " + e);
			System.out.println("closing...");
			try {
				socket.close();
			} catch (Exception e2) {
				System.out.println("no server running");
				System.exit(5);
			}
		}
	}

	void send(Student s) throws IOException {
		oStr.writeObject(s);
		oStr.flush();
		getAnswer();
	}

	void endGracefully() {
		try {
			// Forces stream to break (header broken)
			out.println();
			getAnswer();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		Client cl = new Client();
		for (;;) {
			if (JOptionPane.showConfirmDialog(null, "New student?") != 0) {
				cl.endGracefully();
				break;
			} else {
				Student st = new Student();
				cl.send(st);
				JOptionPane.showMessageDialog(null, st + " is send");
			}
		}
	}

	public void getAnswer() {
		try {
			String str = in.readLine();
			System.out.println(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}