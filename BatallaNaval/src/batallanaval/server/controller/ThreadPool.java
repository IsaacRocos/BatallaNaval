package batallanaval.server.controller;

import java.lang.Thread.State;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Mario Cantellano
 */
public class ThreadPool {

    BlockingQueue<Object> taskQueue;
    private final List<Thread> pool;

    public ThreadPool(int noOfThreads) {
        taskQueue = new ArrayBlockingQueue(1); // Almacena una sola tarea.
        pool = new ArrayList();
        for (int i = 0; i < noOfThreads; i++) {
            pool.add(new Executor(taskQueue));
        }
    }

    public synchronized void start() {
        for (Thread thread : pool) {
            thread.start();
        }
    }

    public boolean execute(Runnable command) {
        try {
            System.out.print("Iniciando ejecucion...");
            taskQueue.put(command);
            sleep(400);
            return true;
        } catch (InterruptedException ex) {
            System.out.println("Error al intentar almacenar una nueva tarea.");
        }
        return false;
    }

    public synchronized boolean isAviable() {
        boolean is = false;
        for (Thread exec : pool) {
            System.out.println("Thread name: "+ exec.getName() +" ExecutorState: "+ exec.getState());
            if (exec.getState() == State.WAITING) {
                is = true;
            }
        }
        return is;
    }

    public synchronized void shutdown() {
        for (Thread thread : pool) {
            thread.interrupt();
        }
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("ThreadPool.toString() = ");
        for(Thread thread : pool) {
            sb.append(thread.toString());
        }
        return sb.toString();
    }
}
