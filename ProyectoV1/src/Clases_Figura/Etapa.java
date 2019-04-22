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
        int x1= x;
        int y1=y;
        int aux=y;
        for (int i = 0; i < 71; i++) {
            for (int j = 0; j < 181; j++) {
                lienzo.strokeLine(x+j,aux, x+j, aux);
            }
            aux++;
        }
        
        Punto xy1=new Punto(x,y);
        Punto xy2 = new Punto(x+180,y);
        Punto xy3 = new Punto(x,y+70);
        Punto xy4 = new Punto(x+180,y+70);
        this.setCoordenadas(xy1);
        this.setCoordenadas(xy2);
        this.setCoordenadas(xy3);
        this.setCoordenadas(xy4);
        
        setX1(x);
        setY1(y);
        setX2(x+180);
        setY2(y);
        setX3(x);
        setY3(y+70);
        setX4(x+180);
        setY4(y+70);
        lienzo.setStroke(Color.valueOf("#021E56"));
        lienzo.setLineWidth(3.0);
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
