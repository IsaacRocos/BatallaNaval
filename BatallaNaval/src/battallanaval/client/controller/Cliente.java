package battallanaval.client.controller;

import batallanaval.utileria.Anuncio;
import batallanaval.utileria.Mensaje;
import batallanaval.utileria.Utileria;
import interfaz.Tablero;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import test.PruebaAnunciador;

/**
 *
 * @author Isaac
 */
public class Cliente extends Thread {

    private Socket socketCliente = null;
    private String ip = "";
    private int puertoGrupo;
    private ObjectOutputStream obos = null;
    private ObjectInputStream obis = null;
    private ByteArrayInputStream bais;
    private int turno;
    private ArrayList<String> listaPosicionesBarcos;
    private Tablero tablero;
    static String group = "225.4.5.6";
    private int puertoServidor;
    /**
     *
     * @param puerto
     * @param ip
     */
    public Cliente(int puerto, String ip) {
        this.tablero = null;
        this.puertoGrupo = puerto;
        this.ip = ip;
    }

    
    @Override
    public void run() {
        try {
            
            System.out.println("Seleccionando entre servidores disponibles...");
            if(buscarServidor()){
                System.out.println("Conectado a servidor en puerto: " + puertoServidor);
            }else{
                System.out.println("Hay problemas para conectarse a algun servior...");
                throw new RuntimeException("Sesion cerrada");
            }
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
            boolean victoria;
            switch (turno) {
                case 1:
                    System.out.println("--------NUEVO TURNO---------");
                    //Desbloqueo mapa de disparo.
                    System.out.println("<C>Preparandose para disparar...");
                    tablero.cambiarBloqueoDeBotones(2, 1);
                    System.out.println("<C>Esperando coordenadas desde campo de batalla para disparar...");
                    synchronized (tablero) {
                        try {
                            tablero.wait();
                            System.out.println("<C>Misil enviado...");
                        } catch (InterruptedException ex) {
                            System.out.println("<C>Algo inesperado le ocurrio al misil. El lanzamiento fue interrumpido");
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.out.println("<C>Parece que el misil no llego al campo enemigo");
                    }
                    //Recibir confirmación
                    System.out.println("<C>Esperando confirmacion de impacto en flota enemiga...");
                    Mensaje confirmacion = recibirMensaje();
                    System.out.println("Mensaje recibido:" + confirmacion);
                    //Verifico bandera victoria. Si gano turno = -1 si no turno = 0;
                    System.out.println("<C>Verificando victoria o derrota de la flota...");
                    victoria = traducirMensaje(confirmacion, 1);
                    System.out.println("¿Victoria?" + victoria);
                    if (victoria) {
                        turno = -1;
                        JOptionPane.showMessageDialog(tablero, "¡¡Has ganado esta Batalla!!");
                        tablero.setBotonDispararDisabled();
                    } else {
                        turno = 0;
                    }
                    System.out.println("----------------");
                    break;
                case 0:
                    // Bloqueo mapa de disparo.
                    tablero.cambiarBloqueoDeBotones(2, 0);
                    // Recibo disparo.
                    System.out.println("<C>Esperando disparo de flota enemiga...");
                    Mensaje disparo = recibirMensaje();
                    System.out.println("<C>Disparo detectado...\n" + disparo);
                    // Envio confirmacion.
                    System.out.println("<C>Verificando danios en la flota...");
                    Mensaje confirmacionDisparo = verificarDaniosDisparo(disparo);
                    try {
                        enviarMensaje(confirmacionDisparo);
                    } catch (IOException ex) {
                        System.err.println("<C>Error al enviar amenaza. Los enemigos no se asustaran...");
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        System.out.println("<C>Parece que el misil no llego al campo enemigo");
                    }
                    // Recibo y verifico mensaje derrota.
                    System.out.println("<C>Verificando victoria o derrota de la flota...");
                    Mensaje respuesta = recibirMensaje();
                    System.out.println("Recibido:" + respuesta);
                    // Si pierdo rompo ciclo turno = -1. Si no turno = 1;
                    boolean derrota = traducirMensaje(respuesta, 2);
                    System.out.println("¿Derrota?" + derrota);
                    if (derrota) {
                        turno = -1;
                        JOptionPane.showMessageDialog(tablero, "¡¡Has perdido esta Batalla!!");
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
        socketCliente = new Socket(InetAddress.getByName(ip), puertoServidor);//InetAddress.getByName(ip), puerto);
        System.out.print("[OK]");
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
        System.out.print("[OK]");
    }

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
     * @param aThis
     */
    public void setTablero(Tablero aThis) {
        this.tablero = aThis;
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
     * @param mensaje
     * @throws IOException
     */
    public void enviarMensaje(Mensaje mensaje) throws IOException {
        System.out.println("Enviando...");
        obos.writeObject((Object) mensaje);
        obos.flush();
        System.out.print("[OK]");
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
     * @param x
     * @param y
     */
    public void disparar(int x, int y) {
        System.out.println("<C>Cargando y alineando misil...");
        Mensaje disparo = crearDisparo(x, y);
        try {
            enviarMensaje(disparo);
        } catch (IOException ex) {
            System.out.println("<C>¡Error al disparar!");
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
        boolean banderaAcertado = confirmacion.getBanderaAcertado();
        if (tipoVerif == 1) { //Verificar victoria
            resultado = confirmacion.getBanderaVictoria();
        } else { //Verificar derrota
            resultado = confirmacion.getBanderaDerrota();
        }
        x = confirmacion.getCoorX();
        y = confirmacion.getCoorY();
		// Agregar llamada a actualizacion de botones en interfaz
        
        if(tipoVerif != 2){tablero.setCelda(x, y, banderaAcertado, 2);}
        
        return resultado;
    }

    private Mensaje verificarDaniosDisparo(Mensaje disparo) {
        Mensaje confirmacion = new Mensaje();
        int x = disparo.getCoorX();
        int y = disparo.getCoorY();
        boolean banderaAcertado = false;
        String coordenada = x + "," + y;
        if (listaPosicionesBarcos.contains(coordenada)) {
            System.out.println("Coordenada alcanzada por el enemigo: " + coordenada);
            banderaAcertado = true;
        }
        confirmacion.setCoorX(x);
        confirmacion.setCoorY(y);
        confirmacion.setBanderaAcertado(banderaAcertado);
        // programacion para actualizar tablero propio
	tablero.setCelda(x, y, banderaAcertado, 1);
        return confirmacion;
    }
    
    
    private boolean buscarServidor(){
       try {
            Anuncio anuncio;
            MulticastSocket ms = new MulticastSocket(puertoGrupo);
            ms.joinGroup(InetAddress.getByName(group));
            System.out.println("Conectado a grupo ..." + ms.getLocalPort() + " " + ms.getPort());
            while(true){
                System.out.println(".");
                    byte buf[] = new byte[256];
                    DatagramPacket pack = new DatagramPacket(buf, buf.length);
                    ms.receive(pack);
                    anuncio = (Anuncio) Utileria.deserializarObjeto(pack.getData());
                    System.out.println("Servidorencontrado: " + anuncio);
                    if(anuncio.getDisponible()){
                        this.puertoServidor = anuncio.getPuerto();
                        break;
                    }else{
                        System.out.println("NO");
                    }
            }
            return true;
        } catch (IOException ex) {
            Logger.getLogger(PruebaAnunciador.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PruebaAnunciador.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
}//clase

