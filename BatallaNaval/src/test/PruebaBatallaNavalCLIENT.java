/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import battallanaval.client.controller.Cliente;


/**
 *
 * @author Isaac
 */
public class PruebaBatallaNavalCLIENT {
    public static void main(String[] args) {
        Cliente cliente = new Cliente(2222, "127.0.0.1");
        cliente.start();
        Cliente cliente2 = new Cliente(2222, "127.0.0.2");
        cliente2.start();
    }
}
