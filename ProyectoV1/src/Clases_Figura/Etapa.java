package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Etapa extends Regular {

    @Override
    public void dibujar(GraphicsContext lienzo,int x,int y) {
        lienzo.setStroke(Color.valueOf("#79A6FF"));
        //x=320;
        //y=50;
        int x1= x;
        int y1=y;
        for (int i = 0; i < 71; i++) {
            for (int j = 0; j < 181; j++) {
                lienzo.strokeLine(x+j,y, x+j, y);
            }
            y++;
        }
        lienzo.setStroke(Color.valueOf("#021E56"));
        lienzo.setLineWidth(3.0);
        lienzo.strokeLine(x1,y1+70, x1+180,y1+70);
        lienzo.strokeLine(x1,y1,x1+180, y1);
        lienzo.strokeLine(x1+180,y1, x1+180, y1+70);
        lienzo.strokeLine(x1,y1, x1, y1+70);
    
    }

   
}
