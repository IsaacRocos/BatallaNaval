/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package batallanaval.utileria;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Isaac
 */
public class Utileria {
    
     /**
     * Convierte una variable tipo Object a un flujo de bytes.
     *
     * @param obj obj es el objeto que se tratara de convertir a bytes
     * @return Regrea un flujo de bytes con los datos del objeto
     * @throws IOException
     */
    public static byte[] serailizarObjeto(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        return baos.toByteArray();
    }

    /**
     * Convierte un flujo de Bytes a una varible tipo Object.
     *
     * @param objBytes objBytes Es el flujo de bytes que se convertiran a objeto
     * @return Un variable tipo Object con los datos del arreglo de bytes
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deseralizarObjeto(byte[] objBytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }
    
}
