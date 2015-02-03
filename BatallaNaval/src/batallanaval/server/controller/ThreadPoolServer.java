package batallanaval.server.controller;

import batallanaval.server.model.Partida;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Mario Cantellano
 */
public class ThreadPoolServer{

    protected int serverPort = 2222;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;
    protected ExecutorService threadPool = Executors.newFixedThreadPool(5);

    public ThreadPoolServer(int port) {
        this.serverPort = port;
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        System.out.print("<s>Iniciando servidor...");
        openServerSocket();
        System.out.println("<s>[OK]");

        while (!isStopped()) {
            Socket client1Socket = null;
            Socket client2Socket = null;
            Partida partida = new Partida();
            try {
                System.out.print("<s>Esperando Jugador 1...");
                client1Socket = this.serverSocket.accept();
                partida.setJugador(client1Socket, 1);
                System.out.println("<s>[OK]");
                System.out.print("<s>Esperando Jugador 2...");
                client2Socket = this.serverSocket.accept();
                partida.setJugador(client2Socket, 2);
                System.out.println("<s>[OK]");
                System.out.println("<s>Jugadores completos [OK].Atendiendo partida...");
                threadPool.execute(partida);
                System.out.println("<s>[OK]");
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
