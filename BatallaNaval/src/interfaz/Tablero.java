package interfaz;

import battallanaval.client.controller.Cliente;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Isaac
 */
public class Tablero extends javax.swing.JFrame {

    private Cliente cliente = null;
    private Tablero tablero;
    
    public Tablero() {
        pilaDeSelecciones = new Stack();
        posicionesBarcos = new ArrayList<>();
        initComponents();
        crearMatrizFlota(jPanel1, 1);
        crearMatrizFlota(jPanel3, 0);
    }
    
    public void arrancarTablero(){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tablero().setVisible(true);
            }
        });
    
    }
    
    private void crearMatrizFlota(JPanel jpanel, int tipo) {
        jpanel.setLayout(new java.awt.GridLayout(15, 15));
        JButton[][] flota;
        if (tipo == 1) {
            flota = botonesFlota;
        } else {
            flota = botonesFlotaEnemiga;
        }
        for (int i = 0; i < flota.length; i++) {
            for (int j = 0; j < flota[0].length; j++) {
                flota[i][j] = new JButton();
                flota[i][j].setName((i) + ":" + (j));
                flota[i][j].setEnabled(false);
                flota[i][j].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/wave.png")));
                if (tipo == 1) {
                    flota[i][j] = agregarActonListenerFlota(flota[i][j]);
                } else {
                    flota[i][j] = agregarActonListenerFlotaEnemiga(flota[i][j]);
                }
                jpanel.add(flota[i][j]);
            }
        }
        if (tipo == 1) {
            botonesFlota = flota;
        } else {
            botonesFlotaEnemiga = flota;
        }
    }

    public JButton agregarActonListenerFlota(JButton flota) {
        flota.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonActionPerformedFlota(evt);
            }
        });
        return flota;
    }

    public JButton agregarActonListenerFlotaEnemiga(JButton flota) {
        flota.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonActionPerformedEnemigo(evt);
            }
        });
        return flota;
    }

    private void botonActionPerformedFlota(java.awt.event.ActionEvent evt) {
        JButton boton = (JButton) evt.getSource().getClass().cast(evt.getSource());
        String[] nombre = boton.getName().split(":");
        int coorX = Integer.parseInt(nombre[0]);
        int coorY = Integer.parseInt(nombre[1]);
        System.out.println("Posicion activa en flota: " + coorX + " , " + coorY);
        int[] posicion = {coorX, coorY};
        int[] posicionanterior = new int[2];
        try {
            posicionanterior = (int[]) pilaDeSelecciones.pop();
        } catch (Exception e) {
        }
        int antX = posicionanterior[0];
        int antY = posicionanterior[1];
        switch (getBarcoActivo()) {
            case 1:
                if (getBarcosPendientes() > 0) {
                    System.out.println("Niv1-Posicion valida");
                    botonesFlota[coorX][coorY].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bh.png")));
                    posicionesBarcos.add(coorX + "," + coorY);
                    pilaDeSelecciones.push(posicion);
                    --barcosPendientes;
                    ++barcoListos;
                    if(barcosPendientes==0){
                        botonBNivel2.setEnabled(true);
                    }
                    
                }
                break;
            case 2:
                if (getBarcosPendientes() > 0) {
                    if (celdasPendientes == 2) {
                        System.out.println("Niv2-Posicion valida");
                        botonesFlota[coorX][coorY].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bh.png")));
                        posicionesBarcos.add(coorX + "," + coorY);
                        pilaDeSelecciones.push(posicion);
                        --celdasPendientes;
                    } else {
                        if (celdasPendientes > 0) {
                            if (((coorX == (antX + 1)) && coorY == antY) || ((coorX == (antX - 1)) && coorY == antY) || ((coorY == (antY + 1)) && coorX == antX) || ((coorY == (antY - 1)) && coorX == antX)) {
                                System.out.println("Niv2-Posicion valida");
                                botonesFlota[coorX][coorY].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bh.png")));
                                posicionesBarcos.add(coorX + "," + coorY);
                                pilaDeSelecciones.push(posicion);
                                --celdasPendientes;
                            }
                        } else {
                            --barcosPendientes;
                            ++barcoListos;
                        }
                    }
                } else {
                    botonBNivel2.setEnabled(false);
                    BotonSubmarino.setEnabled(true);
                    setBarcoActivo(0);
                }
                break;
            case 3:
                if (getBarcosPendientes() > 0) {
                    if (celdasPendientes == 3) {
                        System.out.println("Sub-Posicion valida");
                        botonesFlota[coorX][coorY].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bh.png")));
                        posicionesBarcos.add(coorX + "," + coorY);
                        pilaDeSelecciones.push(posicion);
                        --celdasPendientes;
                    } else {
                        if (celdasPendientes != 0) {
                            if (((coorX == (antX + 1)) && coorY == antY) || ((coorX == (antX - 1)) && coorY == antY) || ((coorY == (antY + 1)) && coorX == antX) || ((coorY == (antY - 1)) && coorX == antX)) {
                                System.out.println("Sub-Posicion valida");
                                botonesFlota[coorX][coorY].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bh.png")));
                                posicionesBarcos.add(coorX + "," + coorY);
                                pilaDeSelecciones.push(posicion);
                                --celdasPendientes;
                            }
                        } else {
                            --barcosPendientes;
                            ++barcoListos;
                        }
                    }
                } else {
                    BotonSubmarino.setEnabled(false);
                    setBarcoActivo(0);
                }
                break;
        }
        if(barcoListos==totalDeBarcos){
            botonListo.setEnabled(true);
        }
    }

    private void botonActionPerformedEnemigo(java.awt.event.ActionEvent evt) {
        JButton boton = (JButton) evt.getSource().getClass().cast(evt.getSource());
        String[] nombre = boton.getName().split(":");
        int coorX = Integer.parseInt(nombre[0]);
        int coorY = Integer.parseInt(nombre[1]);
        System.out.println("Posicion activa Enemiga: " + coorX + " , " + coorY);
        int[] posicion = {coorX, coorY};
        setPosicionActiva(posicion);
        botonesFlotaEnemiga[coorX][coorY].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pointer.png")));
        
    }

    public void cambiarBloqueoDeBotones(int tablero, int tipoBloqueo) {
        JButton[][] flota;
        if (tablero == 1) {
            flota = botonesFlota;
        } else {
            flota = botonesFlotaEnemiga;
        }
        for (int i = 0; i < flota.length; i++) {
            for (int j = 0; j < flota[0].length; j++) {
                if (tipoBloqueo == 0) {
                    flota[i][j].setEnabled(false);
                    if(tablero == 2){
                        botonDisparar.setEnabled(false);
                    }
                } else {
                    flota[i][j].setEnabled(true);
                    if(tablero == 2){
                        botonDisparar.setEnabled(true);
                    }
                }
            }
        }
        if (tablero == 1) {
            botonesFlota = flota;
        } else {
            botonesFlotaEnemiga = flota;
        }
    }

//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Tablero().setVisible(true);
//            }
//        });
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        botonDisparar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        botonBNivel1 = new javax.swing.JToggleButton();
        botonBNivel2 = new javax.swing.JToggleButton();
        BotonSubmarino = new javax.swing.JToggleButton();
        botonListo = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Batalla Naval");
        setMinimumSize(new java.awt.Dimension(1100, 650));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Flota", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14), new java.awt.Color(0, 204, 51))); // NOI18N
        jPanel1.setMinimumSize(new java.awt.Dimension(500, 500));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Flota Enemiga", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Calibri", 1, 14), new java.awt.Color(204, 0, 0))); // NOI18N
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        botonDisparar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/1422930528_Bullet Bill.png"))); // NOI18N
        botonDisparar.setEnabled(false);
        botonDisparar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDispararActionPerformed(evt);
            }
        });

        jLabel1.setText("Disparar");

        botonBNivel1.setText("Barco Nivel 1");
        botonBNivel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBNivel1ActionPerformed(evt);
            }
        });

        botonBNivel2.setText("Barco Nivel 2");
        botonBNivel2.setEnabled(false);
        botonBNivel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBNivel2ActionPerformed(evt);
            }
        });

        BotonSubmarino.setText("Submarino (Nivel 3)");
        BotonSubmarino.setEnabled(false);
        BotonSubmarino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonSubmarinoActionPerformed(evt);
            }
        });

        botonListo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/listo.png"))); // NOI18N
        botonListo.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pirate.png"))); // NOI18N
        botonListo.setEnabled(false);
        botonListo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonListoActionPerformed(evt);
            }
        });

        jLabel2.setText("Listo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(botonBNivel1)
                            .addGap(18, 18, 18)
                            .addComponent(botonBNivel2)
                            .addGap(18, 18, 18)
                            .addComponent(BotonSubmarino)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botonListo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonDisparar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botonDisparar, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(botonListo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(botonBNivel1)
                            .addComponent(botonBNivel2)
                            .addComponent(BotonSubmarino))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonDispararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDispararActionPerformed
        System.out.println("Objetivo fijado: " + posicionActiva[0] +","+ posicionActiva[1]);
        cliente.disparar(posicionActiva[0],posicionActiva[1]);
    }//GEN-LAST:event_botonDispararActionPerformed

    private void botonBNivel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBNivel1ActionPerformed
        setBarcoActivo(1);
        cambiarBloqueoDeBotones(1, 1);
        setBarcosPendientes(2);
        JOptionPane.showMessageDialog(null, "Coloca dos barcos de nivel 1");
        botonBNivel1.setEnabled(false);
    }//GEN-LAST:event_botonBNivel1ActionPerformed

    private void botonBNivel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBNivel2ActionPerformed
        setBarcoActivo(2);
        setBarcosPendientes(1);
        setCeldasPendientes(2);
        JOptionPane.showMessageDialog(null, "Coloca un barco de nivel 2");
        botonBNivel2.setEnabled(false);
    }//GEN-LAST:event_botonBNivel2ActionPerformed

    private void BotonSubmarinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonSubmarinoActionPerformed
        setBarcoActivo(3);
        setBarcosPendientes(1);
        setCeldasPendientes(3);
        JOptionPane.showMessageDialog(null, "Coloca un barco de nivel 3");
        BotonSubmarino.setEnabled(false);
    }//GEN-LAST:event_BotonSubmarinoActionPerformed

    private void botonListoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonListoActionPerformed
        cliente = new Cliente(2222,"localhost");
        cliente.setTablero(this);
        cliente.start();
    }//GEN-LAST:event_botonListoActionPerformed

    /**
     *
     * @return
     */
    private int getCeldasPendientes() {
        return celdasPendientes;
    }

    /**
     *
     * @param celdasPendientes
     */
    private void setCeldasPendientes(int celdasPendientes) {
        this.celdasPendientes = celdasPendientes;
    }

    /**
     *
     * @return
     */
    private int getBarcosPendientes() {
        return barcosPendientes;
    }

    /**
     *
     * @param barcosPendientes
     */
    private void setBarcosPendientes(int barcosPendientes) {
        this.barcosPendientes = barcosPendientes;
    }

    /**
     *
     * @return
     */
    private int getBarcoActivo() {
        return barcoActivo;
    }

    /**
     *
     * @param barcoActivo
     */
    private void setBarcoActivo(int barcoActivo) {
        this.barcoActivo = barcoActivo;
    }

    private int[] getPosicionActiva() {
        return posicionActiva;
    }

    private void setPosicionActiva(int[] posiciconActiva) {
        this.posicionActiva[0] = posiciconActiva[0];
        this.posicionActiva[1] = posiciconActiva[1];
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getListaCoordenadasBarcos() {
        System.out.print("Posiciones de barcos: ");
        System.out.println(posicionesBarcos);
        return posicionesBarcos;
    }

    public JButton[][] getCampo1(){
        return botonesFlota;
    }
    
    public JButton[][] getCampo2(){
        return botonesFlotaEnemiga;
    }
    
    /**
     *
     * @param x
     * @param y
     * @param banderaAcertado
     * @param banderaDerribado
     */
    public void setCelda(int x, int y, boolean banderaAcertado, int tablero) {
        if(banderaAcertado){
            if(tablero == 1){
                getCampo1()[x][y].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bdr.png")));
            }else{
                getCampo2()[x][y].setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/img/skull.png")));
                getCampo2()[x][y].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/skull.png")));
                getCampo2()[x][y].setEnabled(false);
            }
        }else{
            if(tablero == 1){
                getCampo1()[x][y].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/danger.png")));
            }else{
                getCampo2()[x][y].setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fail.png")));
                getCampo2()[x][y].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fail.png")));
                getCampo2()[x][y].setEnabled(false);
            }
        }
    }
    
    
    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }
    
    public Cliente getCliente(){
        return this.cliente;
    }
    
    public void desbloquearBotonInicial() {
        botonBNivel1.setEnabled(true);
    }
    
    public Tablero getTablero(){
        return this.tablero;
    }
    
    
    public void setBotonDispararDisabled(){
        botonDisparar.setEnabled(false);
    }
    
    //------------------------------
    //-- Variables de declaración --
    //------------------------------
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton BotonSubmarino;
    private javax.swing.JToggleButton botonBNivel1;
    private javax.swing.JToggleButton botonBNivel2;
    private javax.swing.JButton botonDisparar;
    private javax.swing.JButton botonListo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
    private JButton botonesFlota[][] = new JButton[15][15];
    private JButton botonesFlotaEnemiga[][] = new JButton[15][15];
    private Stack pilaDeSelecciones;
    private int totalDeBarcos = 4;
    private ArrayList<String> posicionesBarcos;
    private int[] posicionActiva = new int[2];
    private int barcoActivo = 0; // 1 barco nivel 1, 2 barco nivel2, etc 
    private int barcosPendientes = 0; // barcos de un nivel, por colocar
    private int barcoListos = 0;
    private int celdasPendientes = 0; // barcos de un nivel, por colocar
    
}
