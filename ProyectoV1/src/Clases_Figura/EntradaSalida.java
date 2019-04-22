package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


public class EntradaSalida extends Figura {

    @Override
    public void dibujar(GraphicsContext lienzo,int x,int y) {
        lienzo.setStroke(Color.valueOf("#75FFA1"));
        int x1=x;
        int y1=y;
        int x2=x;
        
        for (int j = 0; j < 190; j++) {
            x1=x2;
            y1=y;
            for (int i = 0; i < 70; i++) {
            lienzo.strokeLine(x1-i,y1+i, x1-i, y1+i);
            }
            x2+=1;
        }

        setX1(x);
        setY1(y);
        setX2(x+190);
        setY2(y);
        setX3(x-70);
        setY3(y+70);
        setX4(x+120);
        setY4(y+70);
        
        lienzo.setStroke(Color.valueOf("#0F3D1D"));
        lienzo.setLineWidth(3.0);
        lienzo.strokeLine(x,y, x+190, y);
        lienzo.strokeLine(x,y, x-70,y+70);
        lienzo.strokeLine(x-70,y+70, x+120, y+70);
        lienzo.strokeLine(x+120,y+70, x+190, y);
        lienzo.setStroke(Color.WHITE);
        lienzo.setFont(Font.font("Verdana", FontWeight.LIGHT, FontPosture.ITALIC, 15.0));
        lienzo.setFill(Color.BLACK);
        lienzo.fillText(this.getTextoFigura(), Math.round(x+25),Math.round(y+35));//y
    }

}
