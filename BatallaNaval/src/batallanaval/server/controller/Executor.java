package batallanaval.server.controller;

import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Mario Cantellano
 */
public class Executor extends Thread {

    private final BlockingQueue taskQueue;

    public Executor(BlockingQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while(true){
        try {
            //System.out.println("ExecutorHilo: "+this.getName()+" Estado:"+ this.getState());
            // Estado es runnable.
            Runnable runnable = (Runnable) taskQueue.take(); // intenta sacar una tarea de la cola si está vacia Estado es WATING.
            runnable.run();   
        
        } catch (InterruptedException ex) {
            System.err.println("Interrupcion al obtener tarea de la cola.");;
        }
    }}
    
}
