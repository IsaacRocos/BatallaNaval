package battallanaval.client.controller;

import batallanaval.utileria.Mensaje;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Isaac
 */
public class Cliente {

    private Socket cliente = null;
    private String ip = "";
    private int puerto;
    private ObjectOutputStream obos;
    private ObjectInputStream obis;
    private ByteArrayOutputStream baos;
    private ByteArrayInputStream bais;
    private int turno;

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
        turno = recibirTurno();
        while (turno != -1) {
            //Recibo turno
            switch (turno) {
                case 1:
                    //Desbloqueo mapa de disparo.
                    //Creo mensaje a partir de lo seleccionado en la IU.
                    //Envio disparo
                    //Recibo confirmacion.
                    //Verifico bandera victoria. Si gano turno = -1 sino turno = 0;
                    break;
                case 0:
                    // Bloqueo mapa de disparo.
                    // Recibo disparo.
                    // Envio confirmacion.
                    // Recibo y verifico mensaje derrota.
                    // Si pierdo rompo ciclo turno = -1. Si no turno = 1;
                    break;
            }
        }
    }

    public void conectarAServidor() throws UnknownHostException, IOException {
        System.out.println("Conectando a servidor...");
        cliente = new Socket(InetAddress.getByName(ip), puerto);//InetAddress.getByName(ip), puerto);
    }

    public void inicializarFlujos() throws IOException {
        System.out.println("Inicializando flujos...");
        obos = new ObjectOutputStream(cliente.getOutputStream());
        obis = new ObjectInputStream(cliente.getInputStream());
    }

    public void enviarMensaje(Mensaje mensaje) throws IOException {
        System.out.println("Enviando mensaje...");
        obos.writeObject((Object) mensaje);
        obos.flush();
    }

    public int recibirTurno() {
        try {
            if (obis.readBoolean()) {
                return 1;
            } else {
                return 0;
            }
        } catch (IOException ex) {
            System.err.println("Error al recibir turno");
            return -1;
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

}//clase
