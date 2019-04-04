package Clases_Figura;


import Clases_Figura.Irregular;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Documento extends Irregular {

    @Override
    public void dibujar(GraphicsContext lienzo) {
        
        lienzo.setStroke(Color.valueOf("#000000"));
        lienzo.setLineWidth(3.0);

        
        lienzo.strokeLine(500,250, 700, 250);
        lienzo.strokeLine(700,250, 700, 320);
        lienzo.strokeLine(500,250, 500, 340);
        
        lienzo.beginPath();
        lienzo.moveTo(500, 340);
        lienzo.bezierCurveTo(570, 400, 600, 340, 700, 320);
        lienzo.stroke();
    }

}
