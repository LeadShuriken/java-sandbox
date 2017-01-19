/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.*;
import java.io.*;
import javax.swing.*;

/**
 *
 * @author dsmis
 */
public class BattleShipClient extends Thread {

    private String PLAYER_NAME = "";
    private int PORT;

    // CONNECTION/COMUNICATION HANDLERS //
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    private BattleShipGUI GUI;

    // CONNECTION/RECONNECTION HANDLERS // 
    private static int MAX_RECONNECTIONS = 10;
    private int RECONECTIONS = 0;

    private BattleShipGameLogic GAME_LOGIC = null;

    BattleShipClient(int PORT) {
        this.PLAYER_NAME = JOptionPane.showInputDialog("Enter an new name.");
        if (this.PLAYER_NAME != null) {
            this.GAME_LOGIC = BattleShipServer.gameLogic;
            this.PORT = PORT;
            connect();
        }
    }

    private void connect() {
        try {
            String server = null;
            InetAddress addr = InetAddress.getByName(server);

            socket = new Socket(addr, PORT);

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);

            if (!GAME_LOGIC.getGameStatus()) {
                System.out.println(PLAYER_NAME + ": connected... ");
                GUI = new BattleShipGUI(PLAYER_NAME, socket, in, out);
                GUI.changeButtonsState(false);
                GAME_LOGIC.ClientsGUI.put(PLAYER_NAME, GUI);
                super.setName(PLAYER_NAME);
                start();
            }
        } catch (ConnectException e) {
            System.out.println(PLAYER_NAME + ": Error while connecting. " + e.getMessage());
            tryToReconnect();
        } catch (SocketTimeoutException e) {
            System.out.println(PLAYER_NAME + ": " + e.getMessage() + ".");
            tryToReconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tryToReconnect() {
        System.out.println(PLAYER_NAME + " will try to reconnect in 5 seconds... (" + RECONECTIONS + "/10)");
        try {
            Thread.sleep(5000); //milliseconds
        } catch (InterruptedException e) {
        }

        if (RECONECTIONS < MAX_RECONNECTIONS) {
            RECONECTIONS++;
            connect();
        } else {
            System.out.println(PLAYER_NAME + ": reconnection failed, exeeded max reconnection tries. Shutting down.");
            System.exit(0);
            return;
        }
    }

    public void run() {
        GAME_LOGIC.waitForGameStart();
        try {
            sleep((int) (Math.random() * 100));
        } catch (InterruptedException e) {
        }
        System.out.println(PLAYER_NAME);
        GAME_LOGIC.placeShip();
        try {
            sleep((int) (Math.random() * 100));
        } catch (InterruptedException e) {
        }
    }
}
