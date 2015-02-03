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
        System.out.print("Iniciando servidor...");
        openServerSocket();
        System.out.println("[OK]");

        while (!isStopped()) {
            Socket client1Socket = null;
            Socket client2Socket = null;
            try {
                System.out.print("Esperando Jugador 1...");
                client1Socket = this.serverSocket.accept();
                System.out.println("[OK]");
                System.out.print("Esperando Jugador 2...");
                client2Socket = this.serverSocket.accept();
                System.out.println("[OK]");
                System.out.print("Iniciando partida...");
                threadPool.execute(new Partida(client1Socket, client2Socket));
                System.out.println("[OK]");
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server detenido.");
                    break;
                }
                throw new RuntimeException("Error al establecer conexion con jugador ", e);
            }
        }
        this.threadPool.shutdown();
        System.out.println("Servidor detenido.");
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error deteniendo servidor.", e);
        }
        this.isStopped = true;
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Error no se puede abrir puerto.", e);
        }
    }
}
