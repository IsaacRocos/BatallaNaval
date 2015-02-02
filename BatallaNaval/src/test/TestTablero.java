/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.BorderLayout;
import javax.swing.JButton;
/**
 *
 * @author Isaac
 */
class TestTablero {
    private JButton botones[][];
    public void ventana(int f, int c) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setTitle("Tablero");
        frame.setLayout(new BorderLayout());
        frame.setLayout(new java.awt.GridLayout(f, c));
        
        frame.setSize(1000, 550);
        frame.setDefaultCloseOperation(3);
        botones = new JButton[f][c];
        for (int i = 0; i < botones.length; i++) {
            for (int j = 0; j < botones[0].length; j++) {
                botones[i][j] = new JButton();
                botones[i][j].setName((i + 1) + ":" + (j + 1));
                botones[i][j].addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        botonActionPerformed(evt);
                    }
                });
                frame.add(botones[i][j]);
            }
        }
        frame.setVisible(true);
    }

    private void botonActionPerformed(java.awt.event.ActionEvent evt) {
        JButton boton = (JButton) evt.getSource().getClass().cast(evt.getSource());
        String [] nombre = boton.getName().split(":");
        System.out.println(nombre[0] + " , " + nombre[1]);
    }

    public static void main(String main[]) {
        new TestTablero().ventana(15, 15);
    }
}