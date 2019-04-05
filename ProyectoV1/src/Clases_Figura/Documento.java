package Clases_Figura;


import Clases_Figura.Irregular;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Documento extends Irregular {

    @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {

        lienzo.setStroke(Color.valueOf("#000000"));
        lienzo.setLineWidth(3.0);


        lienzo.strokeLine(x,y, x+200, y);
        lienzo.strokeLine(x+200,y, x+200, y+70);
        lienzo.strokeLine(x,y, x, y+90);

        lienzo.beginPath();
        lienzo.moveTo(x, y+90);
        lienzo.bezierCurveTo(x+70, y+150, x+100, y+90, x+200, y+70);
        lienzo.stroke();
    }

}
