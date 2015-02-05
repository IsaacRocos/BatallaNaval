package test;

import batallanaval.server.controller.ThreadPoolServer;

/**
 *
 * @author Isaac
 */
public class PruebaBatallaNavalSERVER {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolServer servidor = new ThreadPoolServer(2222);
        servidor.run();
    }
}
