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

    Map<String, PrintWriter> ClientsGUI = new HashMap<String, PrintWriter>();
    private ArrayList<PrintWriter> pW;

    LinkedHashMap<String, String> PlacedShip = new LinkedHashMap<String, String>();
    LinkedHashMap<String, ArrayList<String>> ShotSpots = new LinkedHashMap<String, ArrayList<String>>();

    private String commandStr = "NONE";
    private boolean GAME_IS_ON = false;
    private String timedOutThread = "";
    private String GAME_START_STATE = "Place your ship...";

    public BattleShipGameLogic() {
        pW = new ArrayList<PrintWriter>(2);
    }

    public synchronized void sendCommand(String cmd) {
        String[] commandStr = cmd.split(":");
        if (commandStr.length > 1) {
            ClientsGUI.get(commandStr[0]).println(cmd);
        } else {
            for (String key : ClientsGUI.keySet()) {
                ClientsGUI.get(key).println(cmd);
            }
        }
    }

    public synchronized int nCl() {
        return pW.size();
    }

    public synchronized void setCommand(String c) {
        String[] commandStr = c.split(":");
        if (commandStr[0].equals("NAME")) {
            addClient(commandStr[1]);
        }
    }

    public synchronized boolean isGameOn() {
        return GAME_IS_ON;
    }

    public synchronized void addConnection(PrintWriter p) {
        pW.add(p);
    }

    public synchronized void removeClient(PrintWriter p) {
        pW.remove(p);
        ClientsGUI.remove(p);
        GAME_IS_ON = pW.size() < 2 ? false : true;
    }

    public synchronized void addClient(String clientName) {
        ClientsGUI.put(clientName, pW.get(pW.size() - 1));
        GAME_IS_ON = ClientsGUI.size() < 2 ? false : true;
        if (GAME_IS_ON) {
            sendCommand("GAME_IS_ON");
            Iterator a = ClientsGUI.keySet().iterator();
            String b = a.next().toString();
            String c = a.next().toString();
            (new BattleShipClientThread(b, this)).start();
            (new BattleShipClientThread(c, this)).start();
            commandStr = "NONE";
        }
    }

    public synchronized boolean onSamePlace() {
        if (PlacedShip.size() == 2) {
            ArrayList<String> pos = new ArrayList<String>(PlacedShip.values());
            if (pos.get(0).equals(pos.get(1))) {
                timedOutThread = "";
                commandStr = "NONE";
                PlacedShip.clear();
                return true;
            }
            return false;
        }
        return true;
    }

    public synchronized void waitForGameStart() {
        String threadName = Thread.currentThread().getName();
        sendCommand(threadName + ":CHANGE_BUTTONS_STATE" + ":true:null:null");
        while (!GAME_IS_ON) {
            try {
                System.out.println(threadName + " waiting to enter the game.");
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        System.out.println(threadName + " entering the game.");
        commandStr = "NONE";
    }

    public synchronized void waitForNextTurn() {
//        ClientsGUI.get(Thread.currentThread().getName()).updateLabels("Wait for next turn...");
        try {
            wait();
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    public synchronized void placeShip() {
        String threadName = Thread.currentThread().getName();
        sendCommand(threadName + ":STAGE_PLACE");
        System.out.println(threadName + " placing ship");
//        ClientsGUI.get(threadName).changeButtonsState(true,
//                ShotSpots.get(threadName), PlacedShip.get(threadName), this);
//        ClientsGUI.get(threadName).updateLabels(GAME_START_STATE);

        while (commandStr.equals("NONE")) {
            try {
                wait(100);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

        String[] com = {null, null};
        if (!timedOutThread.equals(threadName)) {
            com = commandStr.toString().split(":");
        } else {
            com[0] = timedOutThread;
//            com[1] = getRandomPosition();
            timedOutThread = "";
        }
//
//        PlacedShip.put(com[0], com[1]);
//        ClientsGUI.get(threadName).changeButtonsState(false, ShotSpots.get(threadName), PlacedShip.get(threadName), this);
//        commandStr = "NONE";
//        notifyAll();
    }
}
