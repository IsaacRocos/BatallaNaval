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

    private static final int totalBloquesEmbarcaciones = 7;
    protected Socket player1 = null;
    protected Socket player2 = null;
    private boolean turno;
    private boolean finPartida;
    private int derribados1;
    private int derribados2;
    private Mensaje msj;
    private ObjectInputStream in_1, in_2 = null;
    private ObjectOutputStream out_1, out_2 = null;
    private boolean banderaEsperandoJugador = false;

    public Partida() {
        derribados1 = 0;
        derribados2 = 0;
        turno = true;
        finPartida = false;
        in_1 = null;
        in_2 = null;
        out_1 = null;
        out_2 = null;
        banderaEsperandoJugador = true;
    }

    public void setJugador(Socket player, int numJugador) {
        System.out.println("<p>Inicializando elementos de partida para jugador nuevo...");
        if (numJugador == 1) {
            this.player1 = player;
            banderaEsperandoJugador = true;
        } else {
            this.player2 = player;
            banderaEsperandoJugador = false;
        }
        inicializarFlujo(numJugador);
        if (banderaEsperandoJugador == false) {
            System.out.println("<p>[OK] partida por comenzar");
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("<p>Arrancando partida...");
            //Envia turno habilitado al primer socket conectado.
            out_1.writeBoolean(true);
            out_1.flush();
            out_2.writeBoolean(false);
            out_2.flush();
            while (!finPartida) {
                if (turno) {
                    //Jugador 1.
                    out_2.writeObject(in_1.readObject()); // Jugador 1 dispara y jugador 2 recibe
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
            //Fin de partida
            out_1.close();
            in_1.close();
            out_2.close();
            in_2.close();
        } catch (IOException e) {
            System.err.println("<p>Error en flujo de partida");
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
        //if (msj.getBanderaDerribado()) {
        if (msj.getBanderaAcertado()) {
            if (turno) {
                derribados1++;
            } else {
                derribados2++;
            }
        }
        if (derribados1 == totalBloquesEmbarcaciones || derribados2 == totalBloquesEmbarcaciones) {
            finPartida = true;
            msj.setBanderaVictoria(true);
            enviarDerrota(true);
        } else {
            enviarDerrota(false);
        }
    }

    /**
     * Envia mensaje de derrota al jugador con el turno activo.
     */
    private void enviarDerrota(boolean banderaDerrota) {
        Mensaje msjDerrota = new Mensaje();
        msjDerrota.setBanderaDerrota(banderaDerrota);
        msjDerrota.setBanderaVictoria(false);
        try {
            if (!turno) { // envia derrota al que no tenia el turno.
                out_1.writeObject(msjDerrota);
                out_1.flush();
            } else {
                out_2.writeObject(msjDerrota);
                out_2.flush();
            }
        } catch (IOException ex) {
           System.err.println("<p>Error en flujo de partida al intentar enviar derrota");
        }
    }

    private void inicializarFlujo(int numJugador) {
        try {
            if (numJugador == 2) {
                System.out.println("<p>Inicializando flujos de jugador 2 ...");
                out_2 = new ObjectOutputStream(player2.getOutputStream());
                out_2.flush();
                in_2 = new ObjectInputStream(player2.getInputStream());
            } else {
                System.out.println("<p>Inicializando flujos de jugador 1 ...");
                out_1 = new ObjectOutputStream(player1.getOutputStream());
                out_1.flush();
                in_1 = new ObjectInputStream(player1.getInputStream());
            }
        } catch (IOException ex) {
            System.out.println("<p>Problemas al inicializar flujos");
        }
    }
}
