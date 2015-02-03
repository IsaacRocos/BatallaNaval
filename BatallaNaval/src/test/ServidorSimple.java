package test;

import batallanaval.utileria.Mensaje;
import batallanaval.utileria.Utileria;
import java.net.*;
import java.io.*;

/**
 *
 * @author Isaac
 */
public class ServidorSimple {

    public static void main(String[] args) throws Exception {

        ServerSocket socketServidor = new ServerSocket(2222);
        System.out.println("\u00BBServidor iniciado en: " + socketServidor.getInetAddress() + ", esperando conexion de cliente...");
        ObjectInputStream obis = null;
        ObjectOutputStream oos = null;
        Socket cliente1 = null;
        Socket cliente2 = null;
        while (true) {
            System.out.println("Esperando nuevo cliente...");
            cliente1 = socketServidor.accept();
            System.out.println("\u00BBCliente conectado desde:" + cliente1.getInetAddress() + ":" + cliente1.getPort());
            oos = new ObjectOutputStream(cliente1.getOutputStream());
            obis = new ObjectInputStream(cliente1.getInputStream());
            cliente2 = socketServidor.accept();
            System.out.println("\u00BBCliente conectado desde:" + cliente2.getInetAddress() + ":" + cliente2.getPort());
            
            try {
                Mensaje mensaje = (Mensaje)obis.readObject();
                System.out.println(mensaje);
            } catch (Exception e) {
                System.out.println("Excepcion:" + e.getMessage());
            }
            //System.out.println(mensaje);
        }
    }
}
