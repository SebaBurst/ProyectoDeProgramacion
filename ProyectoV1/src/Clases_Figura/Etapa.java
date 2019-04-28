package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Etapa extends Figura {

   @Override
    public void dibujar(GraphicsContext lienzo,int x,int y) {
        lienzo.setStroke(Color.valueOf("#79A6FF"));
        this.setMedioX(x);
        this.setMedioY(y);
        System.out.println("X: "+x);
        int ix= x-90;
        System.out.println("*******************************");
        System.out.println("xi; "+ix);
        int x1= ix;
        int y1=y;
        int aux=y;
        
        this.setX1(ix);
        this.setY1(y1);
        this.setX2(ix+180);
        this.setY2(y1);
        this.setX3(ix);
        this.setY3(y1+70);
        this.setX4(ix+180);
        this.setY4(y1+70);
        
        System.out.println("X1: "+this.getX1());
        System.out.println("X2: "+this.getX2());
        System.out.println("X3: "+this.getX3());
        System.out.println("X4: "+this.getX4());
        System.out.println("Y1: "+this.getY1());
        System.out.println("Y2: "+this.getY2());
        System.out.println("Y3: "+this.getY3());
        System.out.println("Y4: "+this.getY4());
        
        
        
        
        for (int i = 0; i < 71; i++) {
            for (int j = 0; j < 181; j++) {
                lienzo.strokeLine(ix+j,aux, ix+j, aux);
            }
            aux++;
        }
        
        lienzo.setStroke(Color.valueOf("#021E56"));
        lienzo.setLineWidth(3.0);
        System.out.println("X1; "+x1);
        lienzo.strokeLine(x1,y1+70, x1+180,y1+70);
        lienzo.strokeLine(x1,y1,x1+180, y1);
        lienzo.strokeLine(x1+180,y1, x1+180, y1+70);
        lienzo.strokeLine(x1,y1, x1, y1+70);
        
        
        lienzo.setStroke(Color.BLACK);
        lienzo.setFont(Font.font("Verdana", FontWeight.LIGHT, FontPosture.ITALIC, 15.0));
        lienzo.setFill(Color.BLACK);
        lienzo.fillText(this.getTextoFigura(), Math.round(x+25),Math.round(y+35));//y
    }


   
}
