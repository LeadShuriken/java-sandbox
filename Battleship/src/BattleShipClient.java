/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dsmis
 */
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class BattleShipClient extends Thread {

    private String PLAYER_NAME = "";
    private int PORT;

    // IO HANDLERS //
    private BufferedReader in;
    private PrintWriter out;

    private BattleShipGUI gui;

    // CONNECTION/COMUNICATION HANDLERS //
    private Socket socket;

    // CONNECTION/RECONNECTION HANDLERS // 
    private int reconnections = 0;
    private static int MAX_RECONNECTIONS = 10;

    private BattleShipGameLogic gameLogic = null;

    BattleShipClient(int PORT, BattleShipGameLogic gameLogic) {
        this.PLAYER_NAME = JOptionPane.showInputDialog("Enter an new name.");
        this.gameLogic = gameLogic;
        this.PORT = PORT;
        connect();
    }

    private void connect() {
        try {
            String server = null;
            InetAddress addr = InetAddress.getByName(server);
            socket = new Socket(addr, this.PORT);

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);

            if (!gameLogic.getGameStatus()) {
                System.out.println(this.PLAYER_NAME + ": connected... ");
                this.gui = new BattleShipGUI(this.PLAYER_NAME, socket, in, out);
                this.gui.setStage();
                this.gameLogic.ClientsGUI.put(this.PLAYER_NAME, this.gui);
                super.setName(this.PLAYER_NAME);
                start();
            }
        } catch (ConnectException e) {
            System.out.println(PLAYER_NAME + ": Error while connecting. " + e.getMessage());
            this.tryToReconnect();
        } catch (SocketTimeoutException e) {
            System.out.println(this.PLAYER_NAME + ": " + e.getMessage() + ".");
            this.tryToReconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tryToReconnect() {
        System.out.println(this.PLAYER_NAME + " will try to reconnect in 5 seconds... (" + this.reconnections + "/10)");
        try {
            Thread.sleep(5000); //milliseconds
        } catch (InterruptedException e) {
        }

        if (this.reconnections < this.MAX_RECONNECTIONS) {
            this.reconnections++;
            this.connect();
        } else {
            System.out.println(this.PLAYER_NAME + ": reconnection failed, exeeded max reconnection tries. Shutting down.");

            System.exit(0);
            return;
        }
    }

    public void run() {
        gameLogic.waitForGameStart();
        try {
            sleep((int) (1000));
        } catch (InterruptedException e) {
        }
        this.gui.enableAll();
        gameLogic.placeShip();
    }
}
