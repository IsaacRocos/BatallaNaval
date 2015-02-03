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
    private boolean turno;
    private boolean finPartida;
    private int derribados1;
    private int derribados2;
    private Mensaje msj;
    private ObjectInputStream in_1, in_2;
    private ObjectOutputStream out_1, out_2;

    public Partida(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;
        derribados1 = 0;
        derribados2 = 0;
        turno = true;
        finPartida = false;
        in_1 = null;
        in_2 = null;
        out_1 = null;
        out_2 = null;
    }

    @Override
    public void run() {
        try {
            inicializarFlujos();

            //Envia turno habilitado al primer socket conectado.
            out_1.writeBoolean(true);
            out_1.flush();
            out_2.writeBoolean(false);
            out_2.flush();
            while (!finPartida) {
                if (turno) {
                    //Jugador 1.
                    out_2.writeObject(in_1.readObject()); // Jugador 1 dispara
                    out_2.flush();

                    // Jugador 2 confirma
                    msj = (Mensaje) in_2.readObject();
                    verificarFinDePartida();
                    out_1.writeObject(msj);
                    out_1.flush();
                    turno = false;
                } else {
                    //Jugador 2.
                    out_1.writeObject(in_2.readObject()); // Jugador 2 dispara
                    out_1.flush();

                    // Jugador 1 confirma
                    msj = (Mensaje) in_1.readObject();
                    verificarFinDePartida();
                    out_2.writeObject(msj);
                    out_2.flush();
                    turno = true;
                }
            }
            enviarDerrota();
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
     */
    private void verificarFinDePartida() {
        if (msj.getBanderaDerribado()) {
            if (turno) {
                derribados1++;
            } else {
                derribados2++;
            }
        }
        if (derribados1 == totalEmbarcaciones || derribados2 == totalEmbarcaciones) {
            finPartida = true;
            msj.setBanderaVictoria(true);
        }
    }

    /**
     * Envia mensaje de derrota al jugador con el turno activo.
     */
    private void enviarDerrota() {
        msj = new Mensaje();
        msj.setBanderaDerrota(true);
        msj.setBanderaVictoria(false);
        try {
            if (turno) {
                out_1.writeObject(msj);
                out_1.flush();
            } else {
                out_2.writeObject(msj);
                out_2.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void inicializarFlujos() {
        try {
            in_1 = new ObjectInputStream(player1.getInputStream());
            out_1 = new ObjectOutputStream(player1.getOutputStream());
            in_2 = new ObjectInputStream(player2.getInputStream());
            out_2 = new ObjectOutputStream(player2.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
