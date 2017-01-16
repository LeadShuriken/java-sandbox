
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private ServerSocket welcomeSocket = null;
    private BattleShipGameLogic gameLogic = null;

    public BattleShipServer(BattleShipGameLogic gameLogic) {
        this.gameLogic = gameLogic;
        start();
    }

    public static void main(String arg[]) throws Exception {
        BattleShipGameLogic gameLogic = new BattleShipGameLogic();

        BattleShipServer server = new BattleShipServer(gameLogic);

        BattleShipClient client1 = new BattleShipClient(PORT, gameLogic);
        BattleShipClient client2 = new BattleShipClient(PORT, gameLogic);
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
