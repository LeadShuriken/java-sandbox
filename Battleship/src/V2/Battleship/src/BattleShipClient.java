/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;

/**
 *
 * @author dsmis The client of the application.
 */
public class BattleShipClient extends Thread {

    // CONNECTION/COMUNICATION HANDLERS //
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private Boolean GAME_IS_ON;

    private BattleShipGUI GUI;
    private String PLAYER_NAME;

    private String PLACE_MESSEGE = "Place your ship...";
    // CONNECTION/RECONNECTION HANDLERS // 
    private static int MAX_RECONNECTIONS = 10;
    private int RECONECTIONS = 0;

    private String HOSTNAME;
    private int PORT;

    public static int counter;

    BattleShipClient(String hostName, int port) {
        this.PLAYER_NAME = JOptionPane.showInputDialog("Enter a new name.");
        this.HOSTNAME = hostName;
        this.PORT = port;
        if (this.PLAYER_NAME != null) {
            start();
        }
    }

    private void connect() {
        try {
            InetAddress addr = InetAddress.getByName(HOSTNAME);
            socket = new Socket(addr, PORT);

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
            GUI = new BattleShipGUI(PLAYER_NAME, out);
            GAME_IS_ON = true;
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
            Thread.sleep(5000);
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

    private void listenToServer() {
        while (GAME_IS_ON) {
            try {
                String str = in.readLine();
                if (str.equals("GAME_IS_OFF")) {
                    GAME_IS_ON = false;
                } else if (str.equals("CLEAR_PLACED")) {
                    GUI.switchButtonsLock(false);
                    GUI.resetButtons();
                    PLACE_MESSEGE = "SAME PLACE ..try again...";
                } else if (str.equals("END_TURN")) {
                    GUI.switchButtonsLock(false);
                    GUI.resetButtons();
                    PLACE_MESSEGE = "SAME PLACE ..try again...";
                } else {   
                    String[] commands0 = str.split(":");
                    if (commands0[1].equals("STAGE_PLACE")) {
                        GUI.updateLabels(PLACE_MESSEGE);
                        GUI.STAGE_PLACE = true;
                        GUI.switchButtonsLock(true);
                    } else if (commands0[1].equals("STAGE_SHOOT")) {
                        GUI.updateLabels("Shoot somewhere...");
                        GUI.STAGE_PLACE = false;
                        GUI.STAGE_SHOOT = true;
                        GUI.switchButtonsLock(true);
                    } else if (commands0[1].equals("SET_WAITING")) {
                        GUI.updateLabels("Wait for the other player...");
                        GUI.switchButtonsLock(false);
                    } else if (commands0[1].equals("SHOT_AT")) {
                        GUI.placeShot(commands0[2]);
                    }
                }

                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println(e);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        GUI.switchButtonsLock(false);
        try {
            socket.close();
        } catch (Exception e) {
        }
    }

    public void run() {
        connect();
        listenToServer();
    }
}
