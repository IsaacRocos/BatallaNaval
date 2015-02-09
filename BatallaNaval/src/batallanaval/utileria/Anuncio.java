package batallanaval.utileria;

import java.io.Serializable;

/**
 *
 * @author Mario Cantellano
 */
public class Anuncio implements Serializable {
    private int puerto;
    private boolean disponible;

/**
 * Anuncio que realiza el servidor.
 * @param puerto puerto del servidor.
 * @param disponible true si el servidor tiene partidas disponibles, false en otro caso.
 */
    public Anuncio(int puerto, boolean disponible) {
        this.puerto = puerto;
        this.disponible = disponible;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean bandera) {
        this.disponible = disponible;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    @Override
    public String toString() {
        return "Anuncio{" + "puerto=" + puerto + ", bandera=" + disponible + '}';
    }
    
}
