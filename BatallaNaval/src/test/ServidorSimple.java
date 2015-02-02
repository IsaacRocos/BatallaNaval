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

        ServerSocket socketServidor = new ServerSocket(3333);
        System.out.println("\u00BBServidor iniciado en: " + socketServidor.getInetAddress() + ", esperando conexion de cliente...");
        ObjectInputStream obis = null;
        ObjectOutputStream oos = null;
        Socket cliente = null;
        while (true) {
            System.out.println("Esperando nuevo cliente...");
            cliente = socketServidor.accept();
            System.out.println("\u00BBCliente conectado desde:" + cliente.getInetAddress() + ":" + cliente.getPort());
            oos = new ObjectOutputStream(cliente.getOutputStream());
            obis = new ObjectInputStream(cliente.getInputStream());
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
