
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.net.*;

/**
 *
 * @author dsmis Method created to report to TCP request and open sockets for
 * communication with clients and such.
 */
public class BattleShipServer extends Thread {

    private static int PORT = 8080;
    private BattleShipGameLogic gameLogic;
    private ServerSocket welcomeSocket;

    // Bootstrap for quick launch of the application.
    public static void main(String arg[]) throws Exception {
        BattleShipServer server = new BattleShipServer();

        BattleShipClient client1 = new BattleShipClient("localhost", 8080);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        BattleShipClient client2 = new BattleShipClient("localhost", 8080);
    }

    public BattleShipServer() {
        this.gameLogic = new BattleShipGameLogic();
        start();
    }

    private void closeServerSocket() {
        try {
            welcomeSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void letClientIn() {
        try {
            while (!gameLogic.isGameOn()) {
                Socket socket = welcomeSocket.accept();
                try {
                    new ServeOneClient(socket, gameLogic);
                } catch (Exception e) {
                    socket.close();
                }
            }
            System.out.println("Server is going idle...");
            while (gameLogic.isGameOn()) {
                Thread.sleep(1000);
            }
            letClientIn();
        } catch (Exception e) {
            closeServerSocket();
        }
    }

    public void run() {
        try {
            System.out.println("Server Started...");
            this.welcomeSocket = new ServerSocket(PORT);
            letClientIn();
        } catch (Exception e) {
            closeServerSocket();
        }
    }
}
