/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases_Figura;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Sebastian
 */
public class Rectangulo extends Regular {

    @Override
    public void dibujar(GraphicsContext lienzo) {
        lienzo.strokeRect(320,53, 500, 53);
        lienzo.strokeRect(320,120, 500,120);
        lienzo.strokeRect(500,53, 500, 120);
        lienzo.strokeRect(320,120, 500, 120);
        
    
    }
    
}
