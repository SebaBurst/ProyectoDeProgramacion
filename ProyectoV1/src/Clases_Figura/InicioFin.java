package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class InicioFin extends Figura {

    @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {
        lienzo.setStroke(Color.valueOf("#ffcf4c"));
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
        
        /*
        lienzo.beginPath();
        lienzo.moveTo(x, y);
        lienzo.bezierCurveTo(x-35, y+25, x-35, y+45, x, y+70);
        lienzo.stroke(); 
        lienzo.moveTo(x+200, y);
        lienzo.bezierCurveTo(x+235, y+25, x+235, y+45, x+200, y+70);
        lienzo.stroke();
        */
        lienzo.setFill(Color.valueOf("#ffcf4c"));
        lienzo.fillArc(x-35, y, 70, 70, 90, 180, ArcType.OPEN);
        lienzo.fillArc(x+165, y, 70, 70, 270, 180, ArcType.OPEN);
        lienzo.strokeArc(x-35, y, 70, 70, 90, 180, ArcType.OPEN);
        lienzo.strokeArc(x+165, y, 70, 70, 270, 180, ArcType.OPEN);
        lienzo.fillText(this.getTextoFigura(), Math.round(x+25),Math.round(y+35));//y        
    }

    
}
