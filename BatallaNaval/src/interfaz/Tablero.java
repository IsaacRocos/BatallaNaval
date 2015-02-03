package interfaz;

import java.util.ArrayList;
import java.util.Stack;


import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Isaac
 */
public class Tablero extends javax.swing.JFrame {

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
                if (getBarcosPendientes() != 0) {
                    System.out.print(" Posicion valida");
                    botonesFlota[coorX][coorY].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bh.png")));
                    posicionesBarcos.add(coorX + "," + coorY);
                    pilaDeSelecciones.push(posicion);
                    barcosPendientes--;
                    botonBNivel1.setEnabled(false);
                    botonBNivel2.setEnabled(true);
                }
                break;
            case 2:
                if (getBarcosPendientes() != 0) {
                    if (celdasPendientes == 2) {
                        System.out.print(" Posicion valida");
                        botonesFlota[coorX][coorY].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bh.png")));
                        posicionesBarcos.add(coorX + "," + coorY);
                        pilaDeSelecciones.push(posicion);
                        celdasPendientes--;
                    } else {
                        if (celdasPendientes != 0) {
                            if (((coorX == (antX + 1)) && coorY == antY) || ((coorX == (antX - 1)) && coorY == antY) || ((coorY == (antY + 1)) && coorX == antX) || ((coorY == (antY - 1)) && coorX == antX)) {
                                System.out.print(" Posicion valida");
                                botonesFlota[coorX][coorY].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bh.png")));
                                posicionesBarcos.add(coorX + "," + coorY);
                                pilaDeSelecciones.push(posicion);
                                celdasPendientes--;
                            }
                        } else {
                            barcosPendientes--;
                        }
                    }
                } else {
                    botonBNivel2.setEnabled(false);
                    BotonSubmarino.setEnabled(true);
                }
                break;
            case 3:
                if (getBarcosPendientes() != 0) {
                    if (celdasPendientes == 3) {
                        System.out.print("Posicion valida");
                        botonesFlota[coorX][coorY].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bh.png")));
                        posicionesBarcos.add(coorX + "," + coorY);
                        pilaDeSelecciones.push(posicion);
                        celdasPendientes--;
                    } else {
                        if (celdasPendientes != 0) {
                            if (((coorX == (antX + 1)) && coorY == antY) || ((coorX == (antX - 1)) && coorY == antY) || ((coorY == (antY + 1)) && coorX == antX) || ((coorY == (antY - 1)) && coorX == antX)) {
                                System.out.print(" Posicion valida");
                                botonesFlota[coorX][coorY].setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bh.png")));
                                posicionesBarcos.add(coorX + "," + coorY);
                                pilaDeSelecciones.push(posicion);
                                celdasPendientes--;
                            }
                        } else {
                            barcosPendientes--;
                        }
                    }
                } else {
                    BotonSubmarino.setEnabled(false);
                }
                break;
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
                } else {
                    flota[i][j].setEnabled(true);
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
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        botonBNivel1 = new javax.swing.JToggleButton();
        botonBNivel2 = new javax.swing.JToggleButton();
        BotonSubmarino = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Batalla Naval");
        setMinimumSize(new java.awt.Dimension(1100, 650));
        setPreferredSize(new java.awt.Dimension(1100, 600));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Flota", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14), new java.awt.Color(0, 204, 51))); // NOI18N
        jPanel1.setMinimumSize(new java.awt.Dimension(500, 500));
        jPanel1.setLayout(new java.awt.GridLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Flota Enemiga", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Calibri", 1, 14), new java.awt.Color(204, 0, 0))); // NOI18N
        jPanel3.setLayout(new java.awt.GridLayout());

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/1422930528_Bullet Bill.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(botonBNivel1)
                        .addGap(18, 18, 18)
                        .addComponent(botonBNivel2)
                        .addGap(18, 18, 18)
                        .addComponent(BotonSubmarino)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(botonBNivel1)
                            .addComponent(botonBNivel2)
                            .addComponent(BotonSubmarino))))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        synchronized(this){
            Thread.currentThread().notifyAll();
        }
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void botonBNivel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBNivel1ActionPerformed
        setBarcoActivo(1);
        cambiarBloqueoDeBotones(1, 1);
        setBarcosPendientes(2);
    }//GEN-LAST:event_botonBNivel1ActionPerformed

    private void botonBNivel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBNivel2ActionPerformed
        setBarcoActivo(2);
        setBarcosPendientes(1);
        setCeldasPendientes(2);
    }//GEN-LAST:event_botonBNivel2ActionPerformed

    private void BotonSubmarinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonSubmarinoActionPerformed
        setBarcoActivo(3);
        setBarcosPendientes(1);
        setCeldasPendientes(3);
    }//GEN-LAST:event_BotonSubmarinoActionPerformed

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
        return posicionesBarcos;
    }

    /**
     *
     * @param x
     * @param y
     * @param banderaAcertado
     * @param banderaDerribado
     */
    public void setCelda(int x, int y, boolean banderaAcertado, boolean banderaDerribado) {
    }
    //------------------------------
    //-- Variables de declaración --
    //------------------------------
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton BotonSubmarino;
    private javax.swing.JToggleButton botonBNivel1;
    private javax.swing.JToggleButton botonBNivel2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
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
    private int celdasPendientes = 0; // barcos de un nivel, por colocar
}
