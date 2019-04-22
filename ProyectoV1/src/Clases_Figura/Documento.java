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
        
        int x1,x2,x3,x4,y1,y2,y3,y4;
        x1=x;
        y1=y;
        x2=x+200;
        y2=y;
        y3=y+65;
        
        //Ciclo para pintar un rectangulo//
        for (int i = 0; i<55; i++) {
            lienzo.strokeLine(x1,y1, x2, y2);
            lienzo.strokeLine(x2,y1, x2, y3);
            y1=y1+1;
            y2=y2+1;
        }
        lienzo.strokeLine(x,y, x+200, y);
        lienzo.strokeLine(x+200,y, x+200, y+65);
        x1=x;
        y1=y;
        x2=x;
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
        lienzo.setFill(Color.valueOf("#FA6C61"));
        lienzo.fillArc(x, y+23, 110, 100, 180, 175, ArcType.OPEN);        
        lienzo.strokeArc(x, y+23, 110, 100, 180, 175, ArcType.OPEN);
        lienzo.strokeArc(x+110, y+55, 102, 50, 42, 135, ArcType.OPEN);
        lienzo.strokeArc(x+105, y+53, 102, 50, 42, 135, ArcType.OPEN);

       
        //Ciclo que pinta el semicirculo hacia arriba
        int xy=x+110;
        int z =102;
        int f=50;
        for (int i = 0; i < 30; i++) {
             lienzo.strokeArc(xy, y+49, z,f, 42, 135, ArcType.OPEN);
             xy-=1;
             z-=1;
             f-=1;
        }
        
        
        // Pintar los bordes//
        lienzo.setStroke(Color.valueOf("#85100B"));
        lienzo.setLineWidth(3.3);

        lienzo.strokeLine(x,y, x+200, y);
        lienzo.strokeLine(x+200,y, x+200, y+65);
        lienzo.strokeArc(x+110, y+55, 102, 50, 42, 135, ArcType.OPEN);
        lienzo.strokeArc(x, y+23, 110, 100, 180, 175, ArcType.OPEN);
        lienzo.strokeLine(x,y, x, y+70);




        lienzo.fillText(this.getTextoFigura(), Math.round(x+25),Math.round(y+35));//y                
    }
}
