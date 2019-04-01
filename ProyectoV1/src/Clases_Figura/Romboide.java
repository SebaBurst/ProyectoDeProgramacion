/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Sebastian
 */
public class Romboide extends Regular {

    @Override
    public void dibujar(GraphicsContext lienzo) {
        
        lienzo.strokeLine(370,250, 530, 250);
        lienzo.strokeLine(320,320, 370,250);
        lienzo.strokeLine(320,320, 490, 320);
        lienzo.strokeLine(490,320, 530, 250);
    }
    
}
