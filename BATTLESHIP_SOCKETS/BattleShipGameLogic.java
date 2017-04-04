/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;

/**
 * @author dsmis
 * @member default
 * @name BattleShipGameLogic
 * @description Method handling the all the logic for the application This class
 * is the owner of all the logic allowing treads movement and proper synchronous
 * move between states for client.
 */
class BattleShipGameLogic {

    public boolean GAME_IS_ON; // Is the game on

    // Holder for the PrintWriters(Output) and reference to the name of the player//
    private Map<String, PrintWriter> Clients = new HashMap<String, PrintWriter>();

    // Both positions in string of the placed ships//
    private LinkedHashMap<String, String> PlacedShip = new LinkedHashMap<String, String>();

    private int PlayerC; // Used as a counter in authentification for client //
    private String STAGE_STATE; // The stage at which the scene is //
    private String JustShot; // Has the player just fired a shot(manual at shoot) //

    /**
     * @author dsmis
     * @member BattleShipGameLogic
     * @name BattleShipGameLogic
     * @description Constructor
     */
    public BattleShipGameLogic() {
        JustShot = "";
        GAME_IS_ON = false;
        Clients = new HashMap<String, PrintWriter>();
        PlacedShip = new LinkedHashMap<String, String>();
    }

    /**
     * @author dsmis
     * @member BattleShipGameLogic
     * @name setCommand
     * @description Handler for incoming commands
     * @param cmd {String} received command
     */
    public void setCommand(String cmd) {
        String[] commandStrSplit = cmd.split(":");
        if (STAGE_STATE.equals("STAGE_PLACE")) {
            PlacedShip.put(commandStrSplit[0], commandStrSplit[1]);
        } else if (STAGE_STATE.equals("STAGE_SHOOT")) {
            JustShot = commandStrSplit[0];
            sendCommand(commandStrSplit[2] + ":SHOT_AT:" + commandStrSplit[1]);
        }
    }

    /**
     * @author dsmis
     * @member BattleShipGameLogic
     * @name setCommand
     * @description Commands sender to the clients
     * @param cmd {String} command to send
     */
    public void sendCommand(String cmd) {
        String[] commandStr = cmd.split(":");
        if (commandStr.length > 1) {
            Clients.get(commandStr[0]).println(cmd);
        } else {
            for (String key : Clients.keySet()) {
                Clients.get(key).println(cmd);
            }
        }
    }

    /**
     * @author dsmis
     * @member BattleShipGameLogic
     * @name setCommand
     * @description Handler for adding a client
     * @param p {PrintWriter} to client communication stream
     */
    public void addConnection(PrintWriter p) {
        Clients.put(("pl_" + PlayerC++), p);
        if (Clients.size() == 2 ? true : false) {
            startGame();
        }
    }

    /**
     * @author dsmis
     * @member BattleShipGameLogic
     * @name removeClient
     * @description Handler for removing a client
     * @param p {PrintWriter} to client communication stream
     */
    public void removeClient(PrintWriter p) {
        Clients.remove(p);
        GAME_IS_ON = Clients.size() < 2 ? false : true;
    }

    /**
     * @author dsmis
     * @member BattleShipGameLogic
     * @name startGame
     * @description Handler for starting the bootstrap for the game start
     */
    private void startGame() {
        Iterator a = Clients.keySet().iterator();
        String b = a.next().toString();
        String c = a.next().toString();

        BattleShipClientThread e = new BattleShipClientThread(c, this);
        BattleShipClientThread d = new BattleShipClientThread(b, this);

        e.start();
        d.start();

        GAME_IS_ON = true;
    }

    // THREAD SYNCHRONIZED HANDLERS //
    /**
     * @author dsmis
     * @member BattleShipGameLogic
     * @name isGameOn
     * @description Getter for whether the game is on
     */
    public synchronized boolean isGameOn() {
        return GAME_IS_ON;
    }

    /**
     * @author dsmis
     * @member BattleShipGameLogic
     * @name onSamePlace
     * @description Method checking whether the ships are on the same place
     */
    public synchronized boolean onSamePlace() {
        if (PlacedShip.size() == 2) {
            ArrayList<String> pos = new ArrayList<String>(PlacedShip.values());
            if (pos.get(0).equals(pos.get(1))) {
                PlacedShip.clear();
                sendCommand("CLEAR_PLACED");
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * @author dsmis
     * @member BattleShipGameLogic
     * @name waitToContinue
     * @description Method acting as a gate for incoming threads
     */
    public synchronized void waitToContinue() {
        notifyAll();
        try {
            String threadName = Thread.currentThread().getName();
            sendCommand(threadName + ":SET_WAITING");
            wait();
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    /**
     * @author dsmis
     * @member BattleShipGameLogic
     * @name continueToWait
     * @description Method acting as a gate for incoming threads
     */
    public synchronized void continueToWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            System.err.println(e);
        }
        notifyAll();
    }

    /**
     * @author dsmis
     * @member BattleShipGameLogic
     * @name placeShip
     * @description Method acting as a gate waiting for the user to place the
     * tread
     */
    public synchronized void placeShip() {
        String threadName = Thread.currentThread().getName();
        sendCommand(threadName + ":STAGE_PLACE");
        STAGE_STATE = "STAGE_PLACE";

        while (PlacedShip.get(threadName) == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }

    /**
     * @author dsmis
     * @member BattleShipGameLogic
     * @name playOneTurn
     * @description Method acting as a gate waiting for the user 
     * to play one turn AKA shoot once
     */
    public synchronized void playOneTurn() {
        String threadName = Thread.currentThread().getName();
        sendCommand(threadName + ":STAGE_SHOOT");
        STAGE_STATE = "STAGE_SHOOT";

        while (!JustShot.equals(threadName)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        JustShot = "";
    }
}
