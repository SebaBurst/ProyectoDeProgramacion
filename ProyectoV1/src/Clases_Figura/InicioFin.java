package Clases_Figura;


import Clases_Figura.Irregular;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class InicioFin extends Irregular {

    @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {
        lienzo.setStroke(Color.valueOf("#E2F75B"));
        lienzo.setLineWidth(3.0);
        
        
        int x1= x;
        int y1=y;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 200; j++) {
                lienzo.strokeLine(x1+j,y1, x1+j, y1);
            }
            y1++;
        }
        
        lienzo.setStroke(Color.valueOf("#5A660E"));
        lienzo.strokeLine(x,y, x+200, y);
        lienzo.strokeLine(x, y+100, x+200, y+100);
        
        
        
        lienzo.beginPath();
        lienzo.moveTo(x, y);
        lienzo.bezierCurveTo(x-35, y+30, x-35, y+70, x, y+100);
        
        lienzo.stroke(); 
        lienzo.moveTo(x+200, y);
        lienzo.bezierCurveTo(x+235, y+30, x+235, y+70, x+200, y+100);
        lienzo.stroke();
    }

    
}
