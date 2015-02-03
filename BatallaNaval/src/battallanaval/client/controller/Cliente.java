package battallanaval.client.controller;

import batallanaval.utileria.Mensaje;
import interfaz.Tablero;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


/**
 *
 * @author Isaac
 */
public class Cliente{

    private Socket cliente = null;
    private String ip = "";
    private int puerto;
    private ObjectOutputStream obos=null;
    private ObjectInputStream obis=null;
    private ByteArrayOutputStream baos;
    private ByteArrayInputStream bais;
    private int turno;
    private ArrayList<String> listaPosicionesBarcos;
    private Tablero tablero;

    public Cliente(int puerto, String ip) {
        this.puerto = puerto;
        this.ip = ip;
    }

    public void run() {
        try {
            //Conectarse al servidor.
            conectarAServidor();
        } catch (IOException ex) {
            System.err.println("Error al conectarse con el servidor");
        }
        try {
            //Inicializar flujos
            inicializarFlujos();
        } catch (IOException ex) {
            System.err.print("Error al inicializar flujos");
        }
        try {
            //Ejecutar interfaz
            ejecutarTablero();
        } catch (Exception ex) {
            System.err.println("Ocurrio un error al intentar cargar el tablero:" + ex.getMessage());
        }
        try {
            turno = recibirTurno();
        } catch (IOException ex) {
            System.err.println("Error al recibir turno");
            turno = -1;
        }
        while (turno != -1) {
            //Recibo turno
            switch (turno) {
                case 1:
                    //Desbloqueo mapa de disparo.
                    tablero.cambiarBloqueoDeBotones(2, 1);
                    //Creo mensaje a partir de lo seleccionado en la IU.
                    synchronized (tablero) {
                        try {
                            tablero.wait();
                        } catch (InterruptedException ex) {
                            System.err.println("Ocurrio un problema mientras se esperaba disparo");
                        }catch(Exception ex){
                            System.out.println("Ocurrio un problema noesperado mientras se esperaba disparo" + ex.getMessage());
                        }
                    }
                    System.out.println("LISTO!!!");
                    //Envio disparo
                    //Recibo confirmacion.
                    //Verifico bandera victoria. Si gano turno = -1 si no turno = 0;
                    break;
                case 0:
                    // Bloqueo mapa de disparo.
                    tablero.cambiarBloqueoDeBotones(2, 0);
                    // Recibo disparo.
                    // Envio confirmacion.
                    // Recibo y verifico mensaje derrota.
                    // Si pierdo rompo ciclo turno = -1. Si no turno = 1;
                    break;
            }
        }
    }

    public void conectarAServidor() throws UnknownHostException, IOException {
        System.out.print("Conectando a servidor...");
        cliente = new Socket(InetAddress.getByName(ip), puerto);//InetAddress.getByName(ip), puerto);
        System.out.println("[OK]");
    }

    public void inicializarFlujos() throws IOException {
        System.out.print("Inicializando flujos...");
        obis = new ObjectInputStream(cliente.getInputStream());
        obos = new ObjectOutputStream(cliente.getOutputStream());
        System.out.println("[OK]");
    }

    public void enviarMensaje(Mensaje mensaje) throws IOException {
        System.out.println("Enviando mensaje...");
        obos.writeObject((Object) mensaje);
        obos.flush();
        System.out.println("[OK]");
    }

    public int recibirTurno() throws IOException {
        System.out.print("Recibiendo turno...");
        if (obis.readBoolean()) {
            System.out.print("[OK]");
            return 1;
        } else {
            System.out.println("[OK]");
            return 0;
        }
    }

    public Mensaje recibirMensaje() {
        Mensaje mensaje = null;
        try {
            mensaje = (Mensaje) obis.readObject();
        } catch (IOException ex) {
            System.err.println("Error al recibir mensje");
            return null;
        } catch (ClassNotFoundException ex) {
            System.err.println("El objeto recibido no es de tipo Mensaje");
            return null;
        }
        return mensaje;
    }

    public boolean esperarTurno() {
        while (true) {
            try {
                Mensaje mensaje = recibirMensaje();
                if (mensaje.getTipoMensaje() == 2) {  //recibe disparo
                    return true; // la siguiente secuencia se sigue en la interfaz
                }
            } catch (Exception e) {
            }
        }
    }

    private void ejecutarTablero() {
        tablero = new Tablero();
    }
}//clase
