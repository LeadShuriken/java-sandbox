/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;

/**
 * @author dsmis Method handling the all the logic for the app
 * @member default
 * @name BattleShipGameLogic
 * @description This class is the owner of all the logic allowing treads
 * movement and proper synchronous move between states for client.
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
    
    // Constructor //
    public BattleShipGameLogic() {
        JustShot = "";
        GAME_IS_ON = false;
        Clients = new HashMap<String, PrintWriter>();
        PlacedShip = new LinkedHashMap<String, String>();
    }
    
    // Handler for incommig commands //
    public void setCommand(String cmd) {
        String[] commandStrSplit = cmd.split(":");
        if (STAGE_STATE.equals("STAGE_PLACE")) {
            PlacedShip.put(commandStrSplit[0], commandStrSplit[1]);
        } else if (STAGE_STATE.equals("STAGE_SHOOT")) {
            JustShot = commandStrSplit[0];
            sendCommand(commandStrSplit[2] + ":SHOT_AT:" + commandStrSplit[1]);
        }
    }
    
    // Commands sender for the clients //
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
    
    // Handler for adding a client //
    public void addConnection(PrintWriter p) {
        Clients.put(("pl_" + PlayerC++), p);
        if (Clients.size() == 2 ? true : false) {
            startGame();
        }
    }
    
    // Handler for removing a client/
    public void removeClient(PrintWriter p) {
        Clients.remove(p);
        GAME_IS_ON = Clients.size() < 2 ? false : true;
    }
    
    // getter for game on/
    public synchronized boolean isGameOn() {
        return GAME_IS_ON;
    }
    
    // mettod starting the bootsrap of the widget//
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

    public synchronized void threadBlocker() {
        try {
            wait();
        } catch (InterruptedException e) {
            System.err.println(e);
        }
        notifyAll();
    }

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
