package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Etapa extends Regular {

    @Override
    public void dibujar(GraphicsContext lienzo) {
        lienzo.setStroke(Color.valueOf("#79A6FF"));
        int x1=320;
        int x2 =50;
        for (int i = 0; i < 71; i++) {
            for (int j = 0; j < 181; j++) {
                lienzo.strokeLine(x1+j,x2, x1+j, x2);
            }
            x2=x2+1;
        }
        lienzo.setStroke(Color.valueOf("#021E56"));
        lienzo.setLineWidth(3.0);

        lienzo.strokeLine(320,120, 500,120);
        lienzo.strokeLine(320,50, 500, 50);
        lienzo.strokeLine(500,50, 500, 120);
        lienzo.strokeLine(320,50, 320, 120);
    }

   
}
