/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
