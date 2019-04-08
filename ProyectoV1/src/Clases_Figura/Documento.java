package Clases_Figura;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Documento extends Figura {

    @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {
        lienzo.setStroke(Color.valueOf("#FA6C61"));
        lienzo.setLineWidth(3.0);
 
        setX1(x);
        setY1(y);
        setX2(x+200);
        setY2(y);
        setX3(x);
        setY3(y+70);
        setX4(x+200);
        setY4(y+65);
        
        lienzo.strokeLine(x,y, x+200, y);
        lienzo.strokeLine(x+200,y, x+200, y+65);
        lienzo.strokeLine(x,y, x, y+70);
        
        /*
        lienzo.beginPath();
        lienzo.moveTo(x, y+70);
        lienzo.bezierCurveTo(x+50, y+150, x+100, y+70, x+200, y+50);
        lienzo.stroke();
        */
        
        //lienzo.strokeArc(x, y+23, 110, 100, 180, 180, ArcType.OPEN);
        //lienzo.strokeArc(x+110, y+50, 90, 40, 0, 180, ArcType.OPEN);
        lienzo.strokeArc(x, y+23, 110, 100, 180, 175, ArcType.OPEN);
        lienzo.strokeArc(x+110, y+55, 102, 50, 42, 135, ArcType.OPEN);
        
    }
}
