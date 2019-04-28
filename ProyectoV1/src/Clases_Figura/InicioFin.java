package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class InicioFin extends Figura {

     @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {
        
        this.setMedioX(x);
        this.setMedioY(y);
        lienzo.setStroke(Color.valueOf("#ffcf4c"));
        lienzo.setLineWidth(3.0);
        
        int x1= x-100;
        int y1=y;
        for (int i = 0; i < 70; i++) {
            for (int j = 0; j < 200; j++) {
                lienzo.strokeLine(x1+j,y1, x1+j, y1);
            }
            y1++;
        }
        
        this.setX1(x1);
        this.setY1(y);
        this.setX2(x1+200);
        this.setY2(y);
        this.setX3(x1);
        this.setY3(y+70);
        this.setX4(x1+200);
        this.setY4(y+70);        
        
        lienzo.setStroke(Color.valueOf("#5A660E"));
        lienzo.strokeLine(x1,y, x1+200, y);
        lienzo.strokeLine(x1, y+70, x1+200, y+70);
        
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
        lienzo.fillArc(x1-35, y, 70, 70, 90, 180, ArcType.OPEN);
        lienzo.fillArc(x1+165, y, 70, 70, 270, 180, ArcType.OPEN);
        lienzo.strokeArc(x1-35, y, 70, 70, 90, 180, ArcType.OPEN);
        lienzo.strokeArc(x1+165, y, 70, 70, 270, 180, ArcType.OPEN);
        
        lienzo.setStroke(Color.WHITE);
        lienzo.setFont(Font.font("Verdana", FontWeight.LIGHT, FontPosture.ITALIC, 15.0));
        lienzo.setFill(Color.BLACK);
        lienzo.fillText(this.getTextoFigura(), Math.round(x1+25),Math.round(y+35));//y        
    }


    
}
