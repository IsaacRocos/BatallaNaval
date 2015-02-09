package batallanaval.server.controller;

import batallanaval.server.model.Partida;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Mario Cantellano
 */
public class ThreadPoolServer {
    private static final int noDePartidas = 5;
    protected int serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    ThreadPool threadPool;
    Anunciador anunciador;
    public ThreadPoolServer(int port) {
        this.serverPort = port;
        this.threadPool = new ThreadPool(noDePartidas);
        this.anunciador = new Anunciador(serverPort, true);
    }

    public void run() {
        // Inicia los hilos. 
        threadPool.start();
        anunciador.start();
        
        System.out.print("<s>Iniciando servidor...");
        openServerSocket();
        System.out.println("[OK]");
        
        Socket client1Socket;
        Socket client2Socket;
        while (!isStopped()) {
            Partida partida = new Partida();
            try {
                //System.out.println(threadPool);

                if (threadPool.isAviable()) {
                    System.out.println("[ThreadPool Disponible]");
                    anunciador.getAnuncio().setDisponible(true);
                    anunciador.getAnuncio().setPuerto(serverPort);
                    
                    System.out.print("<s>Esperando Jugador 1...");
                    client1Socket = this.serverSocket.accept();
                    partida.setJugador(client1Socket, 1);
                    System.out.println("<s>[OK]");
                    
                    System.out.print("<s>Esperando Jugador 2...");
                    client2Socket = this.serverSocket.accept();
                    partida.setJugador(client2Socket, 2);
                    System.out.println("<s>[OK]");
                    
                    System.out.println("<s>Jugadores completos...[OK].");
                    System.out.print("<s>Atendiendo partida...");
                    threadPool.execute(partida);
                    System.out.println("[OK]");
                } else {
                    System.out.println("[No disponible]");
                    anunciador.getAnuncio().setPuerto(serverPort);
                    anunciador.getAnuncio().setDisponible(false);
                }

            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("<s>Server detenido.");
                    break;
                }
                throw new RuntimeException("<s>Error al establecer conexion con jugador ", e);
            } 
        }
        this.threadPool.shutdown();
        System.out.println("<s>Servidor detenido.");
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("<s>Error deteniendo servidor.", e);
        }
        this.isStopped = true;
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("<s>Error no se puede abrir puerto.", e);
        }
    }
}
