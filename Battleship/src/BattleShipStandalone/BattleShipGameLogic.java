/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;

/**
 *
 * @author dsmis Method handling the all the logic for the app
 */
class BattleShipGameLogic {

    Map<String, BattleShipGUI> ClientsGUI = new HashMap<String, BattleShipGUI>();
    LinkedHashMap<String, String> PlacedShip = new LinkedHashMap<String, String>();
    LinkedHashMap<String, ArrayList<String>> ShotSpots = new LinkedHashMap<String, ArrayList<String>>();

    private ArrayList<PrintWriter> pW;
    private String commandStr = "NONE";
    private String GAME_START_STATE = "Place your ship...";
    private boolean GAME_IS_ON = false;
    private String timedOutThread = "";

    public BattleShipGameLogic() {
        pW = new ArrayList<PrintWriter>(10);
    }

    public synchronized void setCommand(String c) {
        commandStr = c;
    }

    public synchronized boolean getGameStatus() {
        return GAME_IS_ON;
    }

    public synchronized void addC(PrintWriter p) {
        pW.add(p);
        GAME_IS_ON = pW.size() < 2 ? false : true;
    }

    public synchronized void rmvC(PrintWriter p) {
        pW.remove(p);
        GAME_IS_ON = pW.size() < 2 ? false : true;
    }

    public synchronized void sendC(String s, String n) {
        ClientsGUI.get(s).send(n);
    }

    public synchronized int nCl() {
        return pW.size();
    }

    public synchronized void waitForNextTurn() {
        ClientsGUI.get(Thread.currentThread().getName()).updateLabels("Wait for next turn...");
        try {
            wait();
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    public synchronized boolean onSamePlace() {
        if (PlacedShip.size() == 2) {
            ArrayList<String> pos = new ArrayList<String>(PlacedShip.values());
            if (pos.get(0).equals(pos.get(1))) {
                timedOutThread = "";
                GAME_START_STATE = "Game restarted, choose again...";
                commandStr = "NONE";
                PlacedShip.clear();
                return true;
            }
            return false;
        }
        return true;
    }

    private String getRandomPosition() {
        int size = ClientsGUI.get(timedOutThread).gridSize;
        return "[" + ((int) (Math.random() * (size))) + "," + ((int) (Math.random() * (size))) + "]";
    }

    public void turnTimedOut(String threadName) {
        timedOutThread = threadName;
    }

    public synchronized void placeShip() {
        String threadName = Thread.currentThread().getName();

        ClientsGUI.get(threadName).changeButtonsState(true, ShotSpots.get(threadName), PlacedShip.get(threadName), this);
        ClientsGUI.get(threadName).updateLabels(GAME_START_STATE);

        while (commandStr == "NONE" && timedOutThread != threadName) {
            try {
                wait(100);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

        String[] com = {null, null};
        if (timedOutThread != threadName) {
            com = commandStr.toString().split(":");
        } else {
            com[0] = timedOutThread;
            com[1] = getRandomPosition();
            timedOutThread = "";
        }

        PlacedShip.put(com[0], com[1]);
        ClientsGUI.get(threadName).changeButtonsState(false, ShotSpots.get(threadName), PlacedShip.get(threadName), this);
        commandStr = "NONE";
        notifyAll();
    }

    public synchronized void waitForGameStart() {
        String threadName = Thread.currentThread().getName();
        ClientsGUI.get(threadName).changeButtonsState(false, ShotSpots.get(threadName), PlacedShip.get(threadName), this);
        ClientsGUI.get(threadName).updateLabels("Waiting for the other players to join...");
        while (!GAME_IS_ON) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        System.out.println(threadName + " entering the game.");
    }

    public synchronized void playOneTurn() {
        String threadName = Thread.currentThread().getName();
        ClientsGUI.get(threadName).changeButtonsState(true, ShotSpots.get(threadName),
                PlacedShip.get(threadName), this);
        ClientsGUI.get(threadName).updateLabels("Shoot somewhere!");

        while (commandStr == "NONE" && timedOutThread != threadName) {
            try {
                wait(100);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

        String[] com = {null, null};
        String otherTreadName = "";
        for (String key : ClientsGUI.keySet()) {
            if (key != threadName) {
                otherTreadName = key;
            }
        }

        if (timedOutThread != threadName) {
            com = commandStr.toString().split(":");
            if (PlacedShip.get(otherTreadName).equals(com[1])) {
                sendC(otherTreadName, threadName + ":END");
            } else if (ShotSpots.get(otherTreadName) != null) {
                ShotSpots.get(otherTreadName).add(com[1]);
            } else {
                ShotSpots.put(otherTreadName, new ArrayList<String>(Arrays.asList(com[1])));
            }
        } else {
            timedOutThread = "";
        }

        ClientsGUI.get(threadName).changeButtonsState(false, ShotSpots.get(threadName), PlacedShip.get(threadName), this);
        commandStr = "NONE";
        notifyAll();
    }
}