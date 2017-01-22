/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dsmis
 */
public class BattleShipClientThread extends Thread {

    String PLAYER_NAME;
    BattleShipGameLogic GAME_LOGIC;

    public BattleShipClientThread(String name, BattleShipGameLogic logic) {
        this.PLAYER_NAME = name;
        this.GAME_LOGIC = logic;
        super.setName(PLAYER_NAME);
        start();
    }

    public void run() {
        while (GAME_LOGIC.onSamePlace()) {
            GAME_LOGIC.waitForGameStart();
            GAME_LOGIC.placeShip();
            GAME_LOGIC.waitForNextTurn();
        }

        while (GAME_LOGIC.isGameOn()) {
//            GAME_LOGIC.playOneTurn();
//            GAME_LOGIC.waitForNextTurn();
        }
    }
}
