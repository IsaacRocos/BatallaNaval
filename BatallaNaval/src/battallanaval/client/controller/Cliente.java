package battallanaval.client.controller;

import batallanaval.utileria.Mensaje;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Isaac
 */
public class Cliente {
    private Socket cliente = null;
    private String ip = "";
    private int puerto;
    private BufferedOutputStream bufferSalida;
    private BufferedInputStream bufferEntradaStr;
    private ObjectOutputStream obos;
    private ObjectInputStream obis;
    
    public Cliente(int puerto, String ip){
      this.puerto = puerto;
      this.ip = ip;
      
    }
    
    public void conectarAServidor() throws UnknownHostException, IOException{
        cliente = new Socket(InetAddress.getByName(ip), puerto);
    }
    
    public void enviarMensaje(Mensaje mensaje){
        
    }
    
    
    
}
