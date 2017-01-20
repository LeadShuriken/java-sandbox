/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

/**
 *
 * @author dsmis
 */
import java.io.*;
import java.net.*;

public class BattleShipClient extends Thread {

    private Socket socket;

    String server;
    int PORT;

    ClientHandling cltHandling;

    private BufferedReader in;
    private PrintWriter out;

    private boolean connected;

    private static int MAX_RECONNECTIONS = 10;
    private int reconnections = 0;

    public BattleShipClient(ClientHandling clt, String server, int PORT) throws Exception {

        this.PORT = PORT;
        this.server = server;
        this.cltHandling = clt;

        start();
    }

    private void connect() {

        try {
            this.socket = new Socket();
            InetSocketAddress sa = new InetSocketAddress(this.server, this.PORT);
            this.socket.connect(sa, 500);
            this.connected = true;

            this.in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            this.out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream()
                    )
            ), true);
        } catch (ConnectException e) {
            System.out.println("Error while connecting. " + e.getMessage());
            this.tryToReconnect();
        } catch (SocketTimeoutException e) {
            System.out.println("Connection: " + e.getMessage() + ".");
            this.tryToReconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void disconnect() {
        try {
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        };
    }

    private void tryToReconnect() {
        this.disconnect();
        System.out.println("I will try to reconnect in 10 seconds... (" + this.reconnections + "/10)");
        try {
            Thread.sleep(10000); //milliseconds
        } catch (InterruptedException e) {
        }

        if (this.reconnections < this.MAX_RECONNECTIONS) {
            this.reconnections++;
            this.connect();
        } else {
            System.out.println("Reconnection failed, exeeded max reconnection tries. Shutting down.");
            this.disconnect();
            System.exit(0);
            return;
        }
    }

    public void run() {
        this.connect();
        try {
            while (true) {
                String str = in.readLine();
                if (str.equals("END")) {
                    break;
                }
                System.out.println("Echoing: " + str);
                //out.println(str+" total:"+c.getC());
                cltHandling.sendC(str);
            }
            cltHandling.rmvC(out);
            System.out.println("closing...");
        } catch (IOException e) {
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

//        try {
//            while (true) {
//                String str = in.readLine();
//                if (str.equals("END")) {
//                    break;
//                }
//                System.out.println("Echoing: " + str);
//                //out.println(str+" total:"+c.getC());
//                cltHandling.sendC(str);
//            }
//            cltHandling.rmvC(out);
//            System.out.println("closing...");
//        } catch (IOException e) {
//        } finally {
//            try {
//                socket.close();
//            } catch (IOException e) {
//            }
//        }
}
