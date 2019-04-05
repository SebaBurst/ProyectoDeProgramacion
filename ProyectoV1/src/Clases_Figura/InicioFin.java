package Clases_Figura;


import Clases_Figura.Irregular;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class InicioFin extends Irregular {

    @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {
        lienzo.setStroke(Color.valueOf("#000000"));
        lienzo.setLineWidth(3.0);
        
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
