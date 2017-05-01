package server;

import java.net.*;
import java.io.*;

public class StServer {
	static final int PORT = 9595;

	public static void main(String[] args) throws IOException {
		ServerSocket s = new ServerSocket(PORT);
		System.out.println("Server Started");
		Students clt = new Students();
		try {
			while (true) {
				// Blocks until a connection occurs:
				Socket socket = s.accept();
				try {
					new ServeOneClient(socket, clt);
				} catch (IOException e) {
					// If it fails, close the socket,
					// otherwise the thread will close it:
					socket.close();
				}
			}
		} finally {
			s.close();
		}
	}
}