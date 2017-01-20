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

    private Socket socket; // Serve socket open for comunication.
    BattleShipGameLogic gameLogic; // The working logic of the application.
    private BufferedReader in;
    private PrintWriter out;

    public ServeOneClient(Socket s, BattleShipGameLogic gameLogic) throws Exception {
        socket = s;
        this.gameLogic = gameLogic;

        // Server oneClient line in.
        in = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));
        
        // Server oneClient line out.
        out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(
                        socket.getOutputStream()
                )
        ),
                true);

        gameLogic.addC(out);
        start();
    }

    public void run() {
        try {
            while (true) {
                String str = in.readLine();
                if (str.equals("END")) {
                    break;
                }
                gameLogic.setCommand(str);
            }
            gameLogic.rmvC(out);
            System.out.println("disconected a client. Total number " + gameLogic.nCl());
            socket.close();
        } catch (Exception e) {
        }
    }
}
