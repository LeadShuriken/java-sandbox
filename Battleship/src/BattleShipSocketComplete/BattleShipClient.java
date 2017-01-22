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

    // CONNECTION/RECONNECTION HANDLERS // 
    private static int MAX_RECONNECTIONS = 10;
    private int RECONECTIONS = 0;

    private String HOSTNAME;
    private int PORT;

    public static int counter;

    BattleShipClient(String hostName, int port) {
        //this.PLAYER_NAME = JOptionPane.showInputDialog("Enter a new name.");
        this.HOSTNAME = hostName;
        this.PORT = port;
        this.PLAYER_NAME = "a" + counter++;
        if (this.PLAYER_NAME != null) {
            connect();
        }
    }

    private synchronized void connect() {
        try {
            InetAddress addr = InetAddress.getByName(HOSTNAME);
            socket = new Socket(addr, PORT);

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);

            GUI = new BattleShipGUI(PLAYER_NAME, out);
            out.println("NAME:" + PLAYER_NAME);
            start();
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

    private synchronized void tryToReconnect() {
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

    private synchronized void listenForGUIMesseges() {
        try {
            while (true) {
                String cmd0 = in.readLine();
                String[] commands0 = cmd0.split(":");
                System.out.println(commands0[1]);
                if (commands0[0].equals(PLAYER_NAME) && commands0[1].equals("CHANGE_BUTTONS_STATE")) {
                    Boolean condON = Boolean.parseBoolean(commands0[2]);
//                    ArrayList<String> = new ArrayList<String>(commands[3]);
                    GUI.changeButtonsState(condON, null);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    private synchronized void waitForPlayerChoice() {
        while (!GUI.END_TURN) {
            try {
                wait(100);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
//        GUI.changeButtonsState(false, null);
    }

    private synchronized void listenToServer() {
        while (GAME_IS_ON) {
            try {
                String str = in.readLine();
                if (str.equals("GAME_IS_OFF")) {
                    GAME_IS_ON = false;
                } else {
                    String[] commands0 = str.split(":");
                    if (commands0[0].equals(PLAYER_NAME)) {
                        if (commands0[1].equals("CHANGE_BUTTONS_STATE")) {
                            Boolean condON = Boolean.parseBoolean(commands0[2]);
                            //ArrayList<String> = new ArrayList<String>(commands[3]);
                            GUI.changeButtonsState(condON, null);
                            if (condON) {
                                waitForPlayerChoice();
                            }
                        } else if (commands0[1].equals("STAGE_PLACE")) {
                            GUI.STAGE_PLACE = true;
                        } else if (commands0[1].equals("STAGE_SHOOT")) {
                            GUI.STAGE_PLACE = false;
                            GUI.STAGE_SHOOT = true;
                        }
                    }
                }
                wait(100);
            } catch (InterruptedException e) {
                System.err.println(e);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        GUI.changeButtonsState(false, null);
        try {
            socket.close();
        } catch (Exception e) {
        }
    }

    private synchronized void confirmCondition(String condition) {
        try {
            while (true) {
                String str = in.readLine();
                if (str.equals(condition)) {
                    if (condition.equals("GAME_IS_ON")) {
                        GAME_IS_ON = true;
                    }
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    public void run() {
        confirmCondition("GAME_IS_ON");
        listenToServer();
    }
}
