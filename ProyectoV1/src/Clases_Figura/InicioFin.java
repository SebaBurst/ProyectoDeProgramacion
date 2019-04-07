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
        for (int i = 0; i < 70; i++) {
            for (int j = 0; j < 200; j++) {
                lienzo.strokeLine(x1+j,y1, x1+j, y1);
            }
            y1++;
        }
        
        setX1(x);
        setY1(y);
        setX2(x+200);
        setY2(y);
        setX3(x);
        setY3(y+70);
        setX4(x+200);
        setY4(y+70);        
        
        lienzo.setStroke(Color.valueOf("#5A660E"));
        lienzo.strokeLine(x,y, x+200, y);
        lienzo.strokeLine(x, y+70, x+200, y+70);
        lienzo.beginPath();
        lienzo.moveTo(x, y);
        lienzo.bezierCurveTo(x-35, y+25, x-35, y+45, x, y+70);
        lienzo.stroke(); 
        lienzo.moveTo(x+200, y);
        lienzo.bezierCurveTo(x+235, y+25, x+235, y+45, x+200, y+70);
        lienzo.stroke();
    }

    
}
