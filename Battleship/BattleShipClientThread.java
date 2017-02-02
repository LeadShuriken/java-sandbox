/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author dsmis
 * @member default
 * @name BattleShipClientThread
 * @description A thread launch server side to allow for async handling of each
 * user (PROXY thread).
 */
public class BattleShipClientThread extends Thread {

    String PLAYER_NAME; // Name of the client
    BattleShipGameLogic GAME_LOGIC;  // The game logic (game handler)

    /**
     * @author dsmis
     * @member BattleShipClientThread
     * @name BattleShipClientThread
     * @description Constructor
     * @param name {String} Name of the client
     * @param logic {BattleShipGameLogic} -The game logic (game handler)
     */
    public BattleShipClientThread(String name, BattleShipGameLogic logic) {
        this.PLAYER_NAME = name;
        this.GAME_LOGIC = logic;
        super.setName(PLAYER_NAME);
    }

    @Override
    public void run() {
        while (GAME_LOGIC.onSamePlace()) {
            GAME_LOGIC.placeShip();
            GAME_LOGIC.waitToContinue();
        }

        while (GAME_LOGIC.isGameOn()) {
            GAME_LOGIC.playOneTurn();
            GAME_LOGIC.waitToContinue();
        }
    }
}
