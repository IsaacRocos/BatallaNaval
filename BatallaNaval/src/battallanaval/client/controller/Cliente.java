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
public class Cliente extends Thread {

    private Socket socketCliente = null;
    private String ip = "";
    private int puerto;
    private ObjectOutputStream obos = null;
    private ObjectInputStream obis = null;
    private ByteArrayInputStream bais;
    private int turno;
    private ArrayList<String> listaPosicionesBarcos;
    private Tablero tablero;

    /**
     *
     * @return
     */
    public ObjectOutputStream getObos() {
        return obos;
    }

    /**
     *
     * @return
     */
    public ObjectInputStream getObis() {
        return obis;
    }

    /**
     *
     * @param puerto
     * @param ip
     */
    public Cliente(int puerto, String ip) {
        this.tablero = null;
        this.puerto = puerto;
        this.ip = ip;
    }

    @Override
    public void run() {
        try {
            //Conectarse al servidor.
            this.conectarAServidor();
        } catch (IOException ex) {
            System.err.println("Error al conectarse con el servidor");
        }
        try {
            //Inicializar flujos
            this.inicializarFlujos();
        } catch (IOException ex) {
            System.err.print("Error al inicializar flujos");
        }
        try {
            //recibir turno inicial y lista de barcos 
            this.listaPosicionesBarcos = tablero.getListaCoordenadasBarcos();
            turno = recibirTurno();
        } catch (IOException ex) {
            System.err.println("Error al recibir turno");
            turno = -1;
        }
        System.out.println("Turno recibido:" + turno);
        while (turno != -1) {
            //Recibo turno
            switch (turno) {
                case 1:
                    //Desbloqueo mapa de disparo.
                    System.out.println("<C>Preparandose para disparar...");
                    tablero.cambiarBloqueoDeBotones(2, 1);
                    System.out.println("<C>Esperando a interfaz para disparar");
                    synchronized (tablero) {
                        try {
                            tablero.wait();
                            System.out.println("<C>Interfaz lista");
                        } catch (InterruptedException ex) {
                            System.out.println("<C>Espera interrumpida");
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.out.println("<C>Espera interrumpida en lectura");
                    }
                    //Recibir confirmación
                    System.out.println("<C>Esperando confirmacion de enemigo...");
                    Mensaje confirmacion = recibirMensaje();
                    System.out.println("Recibido:" + confirmacion);
                    //Verifico bandera victoria. Si gano turno = -1 si no turno = 0;
                    System.out.println("<C>Verificando victoria o derrota...");
                    boolean victoria = traducirMensaje(confirmacion, 1);
                    System.out.println("Victoria?" + victoria);
                    if (victoria) {
                        turno = -1;
                    } else {
                        turno = 0;
                    }
                    break;
                case 0:
                    // Bloqueo mapa de disparo.
                    tablero.cambiarBloqueoDeBotones(2, 0);
                    // Recibo disparo.
                    System.out.println("<C>Esperando disparo...");
                    Mensaje disparo = recibirMensaje();
                    System.out.println("<C>Disparo recibido...\n" + disparo);
                    // Envio confirmacion.
                    System.out.println("<C>Enviando confirmacion...");
                    Mensaje confirmacionDisparo = traducirDisparo(disparo);
                    try {
                        enviarMensaje(disparo);
                    } catch (IOException ex) {
                        System.err.println("<C>Error al enviar confrmacion");
                    }
                    // Recibo y verifico mensaje derrota.
                    System.out.println("<C>Verificando victoria o derrota...");
                    Mensaje respuesta = recibirMensaje();
                    System.out.println("Recibido (V/D)" + respuesta);
                    // Si pierdo rompo ciclo turno = -1. Si no turno = 1;
                    victoria = traducirMensaje(respuesta, 2);
                    System.out.println("Derrota?" + victoria);
                    if (victoria) {
                        turno = -1;
                    } else {
                        turno = 1;
                    }
                    break;
            }
        }

    }

    /**
     *
     * @throws UnknownHostException
     * @throws IOException
     */
    public void conectarAServidor() throws UnknownHostException, IOException {
        System.out.println("Conectando a servidor...");
        socketCliente = new Socket(InetAddress.getByName(ip), puerto);//InetAddress.getByName(ip), puerto);
        System.out.println("[OK]");
    }

    /**
     *
     * @throws IOException
     */
    public void inicializarFlujos() throws IOException {
        System.out.println("Inicializando flujos...");
        obos = new ObjectOutputStream(socketCliente.getOutputStream());
        obos.flush();
        obis = new ObjectInputStream(socketCliente.getInputStream());
        System.out.println("[OK]");
    }

    /**
     *
     * @param mensaje
     * @throws IOException
     */
    public void enviarMensaje(Mensaje mensaje) throws IOException {
        System.out.println("Enviando mensaje...");
        obos.writeObject((Object) mensaje);
        obos.flush();
        System.out.println("[OK]");
    }

    /**
     *
     * @return @throws IOException
     */
    public int recibirTurno() throws IOException {
        System.out.println("Recibiendo turno...");
        if (obis.readBoolean()) {
            System.out.print("[OK]");
            return 1;
        } else {
            System.out.println("[OK]");
            return 0;
        }
    }

    /**
     *
     * @return
     */
    public Mensaje recibirMensaje() {
        Mensaje mensaje = null;
        try {
            mensaje = (Mensaje) obis.readObject();
        } catch (IOException ex) {
            System.err.println("<C>Error al recibir mensje");
            return null;
        } catch (ClassNotFoundException ex) {
            System.err.println("<C>El objeto recibido no es de tipo Mensaje");
            return null;
        }
        return mensaje;
    }

    /**
     *
     * @param aThis
     */
    public void setTablero(Tablero aThis) {
        this.tablero = aThis;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void disparar(int x, int y) {
        System.out.println("<C>Creando disparo");
        Mensaje disparo = crearDisparo(x, y);
        try {
            enviarMensaje(disparo);
        } catch (IOException ex) {
            System.out.println("<C>Error al disparar");
        }
        synchronized (tablero) {
            tablero.notifyAll();
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public Mensaje crearDisparo(int x, int y) {
        return new Mensaje(x, y);

    }

    private boolean traducirMensaje(Mensaje confirmacion, int tipoVerif) {
        int x, y;
        boolean resultado;

        if (tipoVerif == 1) { //Verificar victoria
            resultado = confirmacion.getBanderaVictoria();
        } else { //Verificar derrota
            resultado = confirmacion.getBanderaDerrota();
        }
        x = confirmacion.getCoorX();
        y = confirmacion.getCoorY();

        return resultado;
    }

    private Mensaje traducirDisparo(Mensaje disparo) {
        Mensaje confirmacion = new Mensaje();
        int x = disparo.getCoorX();
        int y = disparo.getCoorY();
        boolean banderaAcertado = false;
        String coordenada = x + "," + y;
        if (listaPosicionesBarcos.contains(coordenada)) {
            banderaAcertado = true;
        }
        confirmacion.setCoorX(x);
        confirmacion.setCoorY(y);
        confirmacion.setBanderaAcertado(banderaAcertado);
        return confirmacion;
    }
}//clase

