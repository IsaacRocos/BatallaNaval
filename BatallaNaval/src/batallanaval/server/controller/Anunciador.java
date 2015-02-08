/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batallanaval.server.controller;

import batallanaval.utileria.Anuncio;
import batallanaval.utileria.Utileria;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mario Cantellano
 */
public class Anunciador extends Thread {

    private static final int port = 1111;
    private MulticastSocket ms;
    private Anuncio anuncio;
    private InetAddress addr;

    public Anunciador(int serverPort, boolean estado) {
        try {
            anuncio = new Anuncio(serverPort, estado);
            ms = new MulticastSocket();
            addr = InetAddress.getByName("225.4.5.6");
            //ms.joinGroup(InetAddress.getByName("localhost"));
        } catch (IOException ex) {

        }

    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buf = Utileria.serailizarObjeto(anuncio);
                DatagramPacket pack = new DatagramPacket(buf, buf.length, addr, 8888);
                ms.send(pack);
                try {
                    this.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Anunciador.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(Anunciador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

}
