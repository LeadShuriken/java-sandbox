/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

/**
 *
 * @author dsmis
 */
public class BattleShipRunner {

    public static void main(String[] args) throws Exception {
        ClientHandling clt = new ClientHandling();
        BattleShipClient clientOne = new BattleShipClient(clt, "localhost", 8080);
//        BattleShipClient clientTwo = new BattleShipClient(clt, "localhost", 8080);
    }
}
