import java.io.*;
import java.net.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dsmis
 */
public class BattleShipServer extends Thread {

    public static int PORT = 8080;
    public static BattleShipGameLogic gameLogic = null;
    
    private ServerSocket welcomeSocket = null;

    public static void main(String arg[]) throws Exception {
        BattleShipGameLogic gameLogic = new BattleShipGameLogic();
        BattleShipServer server = new BattleShipServer(gameLogic);

        BattleShipClient client1 = new BattleShipClient(server.PORT);
        BattleShipClient client2 = new BattleShipClient(server.PORT);
    }
    
    public BattleShipServer(BattleShipGameLogic gameLogic) {
        this.gameLogic = gameLogic;
        start();
    }

    public void letClientIn() throws Exception {
        while (!gameLogic.getGameStatus()) {
            try {
                Socket socket = welcomeSocket.accept();
                try {
                    new ServeOneClient(socket, gameLogic);
                } catch (Exception e) {
                    socket.close();
                }
            } catch (Exception e) {
            }
        }
        System.out.println("Server is going idle...");
        while (gameLogic.getGameStatus()) {
            Thread.sleep(1000);
        }
        letClientIn();
    }

    public void run() {
        try {
            System.out.println("Server Started...");
            this.welcomeSocket = new ServerSocket(PORT);
            letClientIn();
        } catch (Exception e) {
            try {
                welcomeSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
