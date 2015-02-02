package test;

import batallanaval.utileria.Mensaje;
import battallanaval.client.controller.Cliente;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 *
 * @author Isaac
 */
public class PruebaFlujos {
    public static void main(String[] args) throws InterruptedException {
        //Cliente c = new Cliente(2222, "230.1.1.1");
        Cliente c = new Cliente(3333, "127.0.0.1");
        try {
            c.conectarAServidor();
            c.inicializarFlujos();
            c.enviarMensaje(new Mensaje(1, 23, 23, true, true, true, true));
        } catch (UnknownHostException ex) {
            System.err.println("error UHE");
        } catch (IOException ex) {   
            System.err.println("error IOE");
            ex.printStackTrace();
        }
        Thread.sleep(1000);
    }
    
}
