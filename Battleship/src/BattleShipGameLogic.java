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
import org.json.JSONArray;

class BattleShipGameLogic {

    Map<String, BattleShipGUI> ClientsGUI = new HashMap<String, BattleShipGUI>();
    Map<String, String> PlacedShip = new HashMap<String, String>();

    private ArrayList<PrintWriter> pW;
    private String commandStr;
    private boolean GAME_IS_ON = false;

    public BattleShipGameLogic() {
        pW = new ArrayList<PrintWriter>(10);
    }

    public synchronized void setGameStatus(boolean status) {
        this.GAME_IS_ON = status;
    }

    public synchronized void setCommand(String c) {
        this.commandStr = c;
    }

    public synchronized boolean getGameStatus() {
        return this.GAME_IS_ON;
    }

    public synchronized void addC(PrintWriter p) {
        pW.add(p);
        this.GAME_IS_ON = pW.size() < 2 ? false : true;
        notifyAll();
    }

    public synchronized void rmvC(PrintWriter p) {
        pW.remove(p);
        this.GAME_IS_ON = pW.size() < 2 ? false : true;
        notifyAll();
    }

    public synchronized void placeShip() {
        String threadName = Thread.currentThread().getName();
        while (commandStr == null) {
            try {
                wait(100);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        
        String[] com = commandStr.toString().split(":");
        while (!com[0].equals(threadName)) {
            try {
                wait(100);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        JSONArray obj = new JSONArray(com[0]);
        PlacedShip.put(threadName, com[0]);
        notifyAll();
    }

    public synchronized void waitForGameStart() {
        while (!this.GAME_IS_ON) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        notifyAll();
        System.out.println(Thread.currentThread().getName() + " entering the game.");
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
}
