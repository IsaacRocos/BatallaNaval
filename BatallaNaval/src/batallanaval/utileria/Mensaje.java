/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package batallanaval.utileria;

import java.io.Serializable;

/**
 *
 * @author Isaac
 */
public class Mensaje implements Serializable {

    int coorX;
    int coorY;
    boolean banderaAcertado;
    boolean banderaDerribado;
    boolean banderaVictoria;
    private boolean banderaDerrota;
    boolean banderaEstadoConexion;
    
    public Mensaje(){
        this.coorX=0;
        this.coorY=0;
        banderaAcertado = false;
        banderaDerribado= false;
        banderaVictoria= false;
        banderaDerrota= false;
        banderaEstadoConexion= true;
    }
    
    public Mensaje(int coorX, int coorY){
        this.coorX=coorX;
        this.coorY=coorY;
        banderaAcertado = false;
        banderaDerribado= false;
        banderaVictoria= false;
        banderaDerrota= false;
        banderaEstadoConexion= true;
    }
    public Mensaje(int coorX, int coorY, boolean banderaAcertado, boolean banderaDerribado, boolean banderaVictoria, boolean banderaEstadoConexion) {
        this.coorX = coorX;
        this.coorY = coorY;
        this.banderaAcertado = banderaAcertado;
        this.banderaDerribado = banderaDerribado;
        this.banderaVictoria = banderaVictoria;
        this.banderaEstadoConexion = banderaEstadoConexion;
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

    public boolean getBanderaDerrota() {
        return banderaDerrota;
    }

    public void setBanderaDerrota(boolean banderaDerrota) {
        this.banderaDerrota = banderaDerrota;
    }

    public boolean getBanderaEstadoConexion() {
        return banderaEstadoConexion;
    }

    public void setBanderaEstadoConexion(boolean banderaEstadoConexion) {
        this.banderaEstadoConexion = banderaEstadoConexion;
    }

    @Override
    public String toString() {
        return  "<Mensaje> coorX: " + coorX + " coorY: " + coorY + " BanderaAcertado: " + banderaAcertado + " BanderaVictoria: " + banderaVictoria + "BanderaDerrota: " + banderaDerrota+  " BanderaEstadoConexion: " + banderaEstadoConexion;
    }

}
