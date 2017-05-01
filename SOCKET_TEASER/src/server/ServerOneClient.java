package server;

import java.io.*;
import java.net.*;
import stud.*;

class ServeOneClient extends Thread {
	private static int number = 0;
	private int num;
	private Socket socket;

	private BufferedReader in;
	private PrintWriter out;

	private ObjectInputStream iStr;
	private Students clt;

	public ServeOneClient(Socket s, Students clt) throws IOException {
		num = ++number;
		System.out.println("join a new client - with number " + num);
		socket = s;
		this.clt = clt;

		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

		iStr = new ObjectInputStream(socket.getInputStream());

		start();
	}

	@Override
	public void run() {
		try {
			while (true) {
				System.out.println("waiting for new student from client " + num);
				Student s = null;
				try {
					s = (Student) iStr.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				if (s == null)
					break;
				System.out.println("receive student from client " + num + "\t" + s);
				out.println("Server echoes: " + s.toString());
				clt.addS(s);
				clt.printSt();
			}
		} catch (IOException e) {
		} finally {
			out.println("Server echoes all: " + clt.getStudents());
			System.out.println("disconecting  client " + num);
		}
	}
}