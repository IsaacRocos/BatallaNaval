package batallanaval.server.model;

import batallanaval.utileria.Mensaje;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mario Cantellano
 */
public class Partida implements Runnable {

    private static final int totalEmbarcaciones = 5;
    protected Socket player1 = null;
    protected Socket player2 = null;
    boolean turno = true;
    boolean finPartida = false;
    private Mensaje msj;
    private int derribados1;
    private int derribados2;

    public Partida(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in_1 = new ObjectInputStream(player1.getInputStream());
            ObjectOutputStream out_1 = new ObjectOutputStream(player1.getOutputStream());
            ObjectInputStream in_2 = new ObjectInputStream(player2.getInputStream());
            ObjectOutputStream out_2 = new ObjectOutputStream(player2.getOutputStream());

            while (!finPartida) {
                if (turno) {
                    //Jugador 1.
                    out_2.writeObject(in_1.readObject()); // Jugador 1 dispara
                    out_2.flush();

                    // Jugador 2 confirma
                    msj = (Mensaje) in_2.readObject();
                    verificaFinDePartida(msj);
                    out_1.writeObject(msj);
                    out_1.flush();
                    turno = false;
                } else {
                    //Jugador 2.
                    out_1.writeObject(in_2.readObject()); // Jugador 2 dispara
                    out_1.flush();

                    // Jugador 1 confirma
                    msj = (Mensaje) in_1.readObject();
                    verificaFinDePartida(msj);
                    out_2.writeObject(msj);
                    out_2.flush();
                    turno = true;
                }
            }
            enviarDerrota(out_1,out_2);
            out_1.close();
            in_1.close();
            out_2.close();
            in_2.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Verifica si el mensaje tiene la bandera de derribado y aumenta el
     * contador de cada jugador, cuando se alcanza el maximo de embarcaciones
     * derribadas cambia la bandera de finPartida a true y cambia la
     * banderaVictoria del mensaje.
     *
     * @param msj Mensaje de confirmacion de disparo.
     * @param jugador true si es jugador 1 y false si es jugador 2.
     */
    private void verificaFinDePartida(Mensaje msj) {
        if (msj.getBanderaDerribado() && turno) {
            derribados1++;
        } else {
            derribados2++;
        }
        if (derribados1 == totalEmbarcaciones || derribados2 == totalEmbarcaciones) {
            finPartida = true;
            msj.setBanderaVictoria(true);
        }
    }
/**
 * Envia mensaje de derrota al jugador con el turno activo.
 * @param p1
 * @param p2 
 */
    private void enviarDerrota(ObjectOutputStream p1, ObjectOutputStream p2) {
        msj = new Mensaje();
        msj.setBanderaDerrota(true);
        msj.setBanderaVictoria(false);
        try {
            if (turno) {
                p1.writeObject(msj);
                p1.flush();
            } else {
                p1.writeObject(msj);
                p1.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
