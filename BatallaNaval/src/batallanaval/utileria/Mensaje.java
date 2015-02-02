/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package batallanaval.utileria;

/**
 *
 * @author Isaac
 */

public class Mensaje {
    int tipoMensaje;
    int coorX;
    int coorY;
    boolean banderaAcertado;
    boolean banderaDerribado;
    boolean banderaVictoria;
    boolean banderaEstadoConexion;

    public Mensaje(int tipoMensaje, int coorX, int coorY, boolean banderaAcertado, boolean banderaDerribado, boolean banderaVictoria, boolean banderaEstadoConexion) {
        this.tipoMensaje = tipoMensaje;
        this.coorX = coorX;
        this.coorY = coorY;
        this.banderaAcertado = banderaAcertado;
        this.banderaDerribado = banderaDerribado;
        this.banderaVictoria = banderaVictoria;
        this.banderaEstadoConexion = banderaEstadoConexion;
    }

    
    public int getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(int tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public int getCoorX() {
        return coorX;
    }

    public void setCoorX(int coorX) {
        this.coorX = coorX;
    }

    public int getCoorY() {
        return coorY;
    }

    public void setCoorY(int coorY) {
        this.coorY = coorY;
    }

    public boolean getBanderaAcertado() {
        return banderaAcertado;
    }

    public void setBanderaAcertado(boolean banderaAcertado) {
        this.banderaAcertado = banderaAcertado;
    }

    public boolean getBanderaDerribado() {
        return banderaDerribado;
    }

    public void setBanderaDerribado(boolean banderaDerribado) {
        this.banderaDerribado = banderaDerribado;
    }

    public boolean getBanderaVictoria() {
        return banderaVictoria;
    }

    public void setBanderaVictoria(boolean banderaVictoria) {
        this.banderaVictoria = banderaVictoria;
    }

    public boolean getBanderaEstadoConexion() {
        return banderaEstadoConexion;
    }

    public void setBanderaEstadoConexion(boolean banderaEstadoConexion) {
        this.banderaEstadoConexion = banderaEstadoConexion;
    }
    
    
}
