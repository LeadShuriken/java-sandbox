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
 * The client of the application.
 */
public class BattleShipClient extends Thread {

    private String PLAYER_NAME = "";
    private int PORT; // Port on to which the client connects.

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
        this.PLAYER_NAME = JOptionPane.showInputDialog("Enter a new name.");
        if (this.PLAYER_NAME != null) {
            this.GAME_LOGIC = BattleShipServer.gameLogic;
            this.PORT = PORT;
            connect();
        }
    }
    
    // Method starting the connection to the server.
    private void connect() {
        try {
            String server = null;
            InetAddress addr = InetAddress.getByName(server);

            socket = new Socket(addr, PORT);
            
            // Client line in;
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            
            // Client line out;
            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
            
            // Connection has been succesfull thus continue with the application;
            if (!GAME_LOGIC.getGameStatus()) {
                System.out.println(PLAYER_NAME + ": connected... ");
                GUI = new BattleShipGUI(PLAYER_NAME, socket, out, in);
                GAME_LOGIC.ClientsGUI.put(PLAYER_NAME, GUI);
                GUI.changeButtonsState(false, null, null, null);
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

    // Method reigniting a TCP call to the server//hostname/port on SocketTiemouts an ConnectExceptions;
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
        while (GAME_LOGIC.onSamePlace()) {
            GAME_LOGIC.waitForGameStart();
            GAME_LOGIC.placeShip();
            GAME_LOGIC.waitForNextTurn();
        }

        while (GAME_LOGIC.getGameStatus()) {
            GAME_LOGIC.playOneTurn();
            GAME_LOGIC.waitForNextTurn();
        }
    }
}
