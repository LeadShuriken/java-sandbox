
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.net.*;

/**
 *
 * @author dsmis Method created to report to TCP request and open sockets for
 * communication with clients and such.
 */
public class BattleShipServer extends Thread {

    private int PORT;
    private BattleShipGameLogic gameLogic;
    private ServerSocket welcomeSocket;

    public static void main(String arg[]) throws Exception {
        BattleShipServer server = new BattleShipServer();

        BattleShipClient client1 = new BattleShipClient("localhost", 9000);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        BattleShipClient client2 = new BattleShipClient("localhost", 9000);

        server.start();
    }

    public BattleShipServer() {
        gameLogic = new BattleShipGameLogic();
        PORT  = 9000;
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
