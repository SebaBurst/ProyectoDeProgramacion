package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class EntradaSalida extends Regular {

    @Override
    public void dibujar(GraphicsContext lienzo,int x,int y) {
        lienzo.setStroke(Color.valueOf("#75FFA1"));
        x1=x;
        y1=y;
        int x2=x;
        
        for (int j = 0; j < 190; j++) {
            x1=x2;
            y1=y;
            for (int i = 0; i < 70; i++) {
            lienzo.strokeLine(x1-i,y1+i, x1-i, y1+i);
            }
            x2+=1;
        }
        
        lienzo.setStroke(Color.valueOf("#0F3D1D"));
        lienzo.setLineWidth(3.0);
        lienzo.strokeLine(x,y, x+190, y);
        lienzo.strokeLine(x,y, x-70,y+70);
        
        lienzo.strokeLine(x-70,y+70, x+120, y+70);
        lienzo.strokeLine(x+120,y+70, x+190, y);
    }

}
