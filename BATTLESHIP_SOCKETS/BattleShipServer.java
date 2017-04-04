
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.net.*;

/**
 * @author dsmis
 * @member default
 * @name BattleShipServer
 * @description Method created to report to TCP requests and open sockets for
 * communication with clients and such (Server)
 */
public class BattleShipServer extends Thread {

    // Port to listen on for clients
    private int PORT;

    // The logic handler
    private BattleShipGameLogic gameLogic;

    // The socket waiting for a ping of the port
    private ServerSocket welcomeSocket;

    // ENTRY POINT FOR THE APPLICATION //
    public static void main(String arg[]) throws Exception {
        BattleShipServer server = new BattleShipServer(9000);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        BattleShipClient client1 = new BattleShipClient("localhost", 9000);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        BattleShipClient client2 = new BattleShipClient("localhost", 9000);

        server.start();
    }

    /**
     * @author dsmis
     * @member BattleShipServer
     * @name BattleShipServer
     * @description Constructor
     */
    public BattleShipServer(int PORT) {
        gameLogic = new BattleShipGameLogic();
        this.PORT = PORT;
    }

    /**
     * @author dsmis
     * @member BattleShipServer
     * @name closeServerSocket
     * @description Method closing the server socket
     */
    private void closeServerSocket() {
        try {
            welcomeSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @author dsmis
     * @member BattleShipServer
     * @name letClientIn
     * @description Method letting one client in
     */
    private void letClientIn() {
        try {
            while (!gameLogic.GAME_IS_ON) {
                Socket socket = welcomeSocket.accept();
                try {
                    (new ServeOneClient(socket, gameLogic)).start();
                } catch (Exception e) {
                    socket.close();
                }
            }
            System.out.println("Server is going idle...");
            while (gameLogic.GAME_IS_ON) {
                Thread.sleep(1000);
            }
            letClientIn();
        } catch (IOException | InterruptedException e) {
            closeServerSocket();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Server Started...");
            this.welcomeSocket = new ServerSocket(PORT);
            letClientIn();
        } catch (IOException e) {
            closeServerSocket();
        }
    }
}
