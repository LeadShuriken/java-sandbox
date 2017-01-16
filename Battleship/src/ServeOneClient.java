
import java.io.*;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dsmis
 */
class ServeOneClient extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    BattleShipGameLogic gameLogic;

    public ServeOneClient(Socket s, BattleShipGameLogic gameLogic) throws Exception {
        socket = s;
        this.gameLogic = gameLogic;
        in = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));

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
