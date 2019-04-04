package Clases_Figura;


import Clases_Figura.Irregular;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class InicioFin extends Irregular {

    @Override
    public void dibujar(GraphicsContext lienzo) {
        lienzo.setStroke(Color.valueOf("#000000"));
        lienzo.setLineWidth(3.0);

        
        lienzo.strokeLine(100,250, 300, 250);
        lienzo.strokeLine(100, 350, 300, 350);
        //lienzo.strokeLine(300,250, 300, 320);
        //lienzo.strokeLine(100,250, 100, 320);
        
        lienzo.beginPath();
        lienzo.moveTo(100, 250);
        lienzo.bezierCurveTo(65, 280, 65, 320, 100, 350);
        lienzo.stroke(); 
        lienzo.moveTo(300, 250);
        lienzo.bezierCurveTo(335, 280, 335, 320, 300, 350);
        lienzo.stroke();
    }

    
}
