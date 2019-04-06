package Clases_Figura;


import Clases_Figura.Irregular;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Documento extends Irregular {

    @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {

        lienzo.setStroke(Color.valueOf("#FA6C61"));
        lienzo.setLineWidth(3.0);
 

        lienzo.strokeLine(x,y, x+200, y);
        lienzo.strokeLine(x+200,y, x+200, y+50);
        lienzo.strokeLine(x,y, x, y+70);

        lienzo.beginPath();
        lienzo.moveTo(x, y+70);
        lienzo.bezierCurveTo(x+50, y+150, x+100, y+70, x+200, y+50);
        lienzo.stroke();
    }

}
