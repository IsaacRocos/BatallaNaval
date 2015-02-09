/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

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
public class PruebaAnunciador {

    static int port = 8888;
    static String group = "225.4.5.6";

    public static void main(String[] args) {
        try {
            Anuncio anuncio;
            MulticastSocket ms = new MulticastSocket(port);
            ms.joinGroup(InetAddress.getByName(group));
            while(true){
                    
                    
                    byte buf[] = new byte[256];
                    
                    DatagramPacket pack = new DatagramPacket(buf, buf.length);
                    ms.receive(pack);
                    anuncio = (Anuncio) Utileria.deserializarObjeto(pack.getData());
                    System.out.println(anuncio);
               
            }
        } catch (IOException ex) {
            Logger.getLogger(PruebaAnunciador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PruebaAnunciador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
