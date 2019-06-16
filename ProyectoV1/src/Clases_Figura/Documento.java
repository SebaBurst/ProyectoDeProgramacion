package Clases_Figura;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Documento extends Figura implements java.io.Serializable{

    public Documento() {
        this.color="#ff0025";
    }

    @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {
        
        this.setMedioX(x);
        this.setMedioY(y);
        
        int ix = x-100;
        
        lienzo.setStroke(Color.valueOf("#f95363"));
        lienzo.setLineWidth(3.0);
 
        setX1(ix);
        setY1(y);
        setX2(ix+200);
        setY2(y);
        setX3(ix);
        setY3(y+70);
        setX4(ix+200);
        setY4(y+65);
        
        int x1,x2,x3,x4,y1,y2,y3,y4;
        x1=ix;
        y1=y;
        x2=ix+200;
        y2=y;
        y3=y+65;
        
        //Ciclo para pintar un rectangulo//
        for (int i = 0; i<55; i++) {
            lienzo.strokeLine(x1,y1, x2, y2);
            lienzo.strokeLine(x2,y1, x2, y3);
            y1=y1+1;
            y2=y2+1;
        }
        lienzo.strokeLine(ix,y, ix+200, y);
        lienzo.strokeLine(ix+200,y, ix+200, y+65);
        x1=ix;
        y1=y;
        x2=ix;
        y2=y;
        y3=y+77;
        lienzo.setLineWidth(3.3);

        //Ciclo que se encarga de dibujar lineas hacia el lado hasta el semicirculo hacia arriba
        for (int i = 0; i < 112; i++) {
           lienzo.strokeLine(x1,y1, x2, y3);
           x1+=1;
           x2+=1;
        }
        
        // Se aumenta el grosor de las lineas.
        lienzo.setLineWidth(4.6);

        // Se dibujan los arcos
        lienzo.setFill(Color.valueOf("#f95363"));
        lienzo.fillArc(ix, y+23, 110, 100, 180, 175, ArcType.OPEN);        
        lienzo.strokeArc(ix, y+23, 110, 100, 180, 175, ArcType.OPEN);
        lienzo.strokeArc(ix+110, y+55, 102, 50, 42, 135, ArcType.OPEN);
        lienzo.strokeArc(ix+105, y+53, 102, 50, 42, 135, ArcType.OPEN);

       
        //Ciclo que pinta el semicirculo hacia arriba
        int xy=ix+110;
        int z =102;
        int f=50;
        for (int i = 0; i < 30; i++) {
             lienzo.strokeArc(xy, y+49, z,f, 42, 135, ArcType.OPEN);
             xy-=1;
             z-=1;
             f-=1;
        }
        
        
        // Pintar los bordes//
        lienzo.setStroke(Color.valueOf("#c31c2c"));
        lienzo.setLineWidth(3.3);

        lienzo.strokeLine(ix,y, ix+200, y);
        lienzo.strokeLine(ix+200,y, ix+200, y+65);
        lienzo.strokeArc(ix+110, y+55, 102, 50, 42, 135, ArcType.OPEN);
        lienzo.strokeArc(ix, y+23, 110, 100, 180, 175, ArcType.OPEN);
        lienzo.strokeLine(ix,y, ix, y+70);
        
        lienzo.setStroke(Color.WHITE);
        lienzo.setStroke(Color.valueOf("#FFFFFF"));
        
        lienzo.setFont(Font.font("Verdana", FontWeight.LIGHT, FontPosture.ITALIC, 15.0));
                lienzo.setFill(Color.BLACK);

        lienzo.fillText(this.getTextoFigura(), ix+15, y+35);


    }
}
