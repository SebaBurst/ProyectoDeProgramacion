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
        
        
        
        
        int x1,x2,x3,x4,y1,y2,y3,y4, y5;
        x1=x;
        y1=y;
        x2=x+200;
        y2=y;
        y3=y+65;
        
        
        for (int i = 0; i<55; i++) {
            lienzo.strokeLine(x1,y1, x2, y2);
            lienzo.strokeLine(x2,y1, x2, y3);
            y1=y1+1;
            y2=y2+1;
        }
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
        lienzo.setFill(Color.valueOf("#FA6C61"));

        lienzo.fillArc(x, y+23, 110, 100, 180, 175, ArcType.OPEN);        
        lienzo.strokeArc(x, y+23, 110, 100, 180, 175, ArcType.OPEN);
        lienzo.strokeArc(x+110, y+55, 102, 50, 42, 135, ArcType.OPEN);
        
        
    }
}
