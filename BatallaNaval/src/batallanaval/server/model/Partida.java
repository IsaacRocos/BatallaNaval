package batallanaval.server.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Mario Cantellano
 */
public class Partida implements Runnable {

    protected Socket player1 = null;
    protected Socket player2 = null;

    public Partida(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void run() {
        try {
            InputStream in_1 = player1.getInputStream();
            InputStream in_2 = player2.getInputStream();
            OutputStream out_1 = player1.getOutputStream();
            OutputStream out_2 = player2.getOutputStream();

            out_1.close();
            in_1.close();
            out_2.close();
            in_2.close();
        } catch (IOException e) {
        }
    }
}
