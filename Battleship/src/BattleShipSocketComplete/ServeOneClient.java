/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.Socket;

/**
 *
 * @author dsmis Class handling the loading of the elements to the handling for
 * the game logic and a messenger store for the socket communication.
 */
class ServeOneClient extends Thread {

    private Socket servedSocket;
    BattleShipGameLogic gameLogic;
    private BufferedReader in;
    private PrintWriter out;

    public ServeOneClient(Socket s, BattleShipGameLogic gameLogic) throws Exception {
        this.servedSocket = s;
        this.gameLogic = gameLogic;

        in = new BufferedReader(
                new InputStreamReader(
                        servedSocket.getInputStream()));

        out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(
                        servedSocket.getOutputStream()
                )
        ), true);

        gameLogic.addConnection(out);
        start();
    }

    public void run() {
        try {
            while (true) {
                String str = in.readLine();
                if (str.equals("GAME_IS_OFF")) {
                    break;
                }
                System.out.println(str);
                gameLogic.setCommand(str);
            }
            gameLogic.removeClient(out);
            System.out.println("disconected a client. Total number " + gameLogic.nCl());
            servedSocket.close();
        } catch (Exception e) {
        }
    }
}
