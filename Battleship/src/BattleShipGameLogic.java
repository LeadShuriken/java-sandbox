/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dsmis
 */
import java.io.*;
import java.util.*;

class BattleShipGameLogic {

    public static int counter;
    Map<String, BattleShipGUI> ClientsGUI = new HashMap<String, BattleShipGUI>();
    Map<String, String> PlacedShip = new HashMap<String, String>();
//    Map<String, boolean> PlayerState = new HashMap<String, String>();

    private ArrayList<PrintWriter> pW;
    private String commandStr = "NONE";
    private boolean GAME_IS_ON = false;
    private boolean HOLD_GAME = true;
    private int continueCounter = 0;

    private void canContinue(Boolean state) {
        if (state) {
            if ((++continueCounter) == 2) {

            }
        } else {
            continueCounter = 0;
        }
    }

    public BattleShipGameLogic() {
        counter++;
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

    public synchronized void sendC(String s) {
        Iterator<PrintWriter> itr = pW.iterator();
        while (itr.hasNext()) {
            PrintWriter p = (PrintWriter) itr.next();
            p.println(s);
        }
    }

    public synchronized int nCl() {
        return pW.size();
    }

    public synchronized void placeShip() {
        String threadName = Thread.currentThread().getName();

        ClientsGUI.get(threadName).updateLabels("Place your ship somewhere...");
        ClientsGUI.get(threadName).changeButtonsState(true);

        while (commandStr == "NONE") {
            try {
                wait(100);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

        String[] com = commandStr.toString().split(":");
        char[] shipSpot = com[1].toCharArray();
        
        ClientsGUI.get(threadName).changeButtonsState(false);
        commandStr = "NONE";
        notifyAll();
    }

    public synchronized void waitForGameStart() {
        String threadName = Thread.currentThread().getName();
        ClientsGUI.get(threadName).updateLabels("Wait for the other player...");

        while (!GAME_IS_ON) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

        System.out.println(threadName + " entering the game.");
    }
}
