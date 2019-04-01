/*
Clase que se encarga de dibujar las figuras regulares 
*/
package Clases_Figura;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Sebastian
 */
public abstract class Regular {
    
    int x1,x2,x3,x4,y1,y2,y3,y4;
    
    
    /*
    Metodo que se encarga de dibujar la figura en primera instancia
    */
    public abstract void dibujar(GraphicsContext lienzo);
    
}
