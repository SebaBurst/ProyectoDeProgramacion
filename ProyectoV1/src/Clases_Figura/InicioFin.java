package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class InicioFin extends Figura implements java.io.Serializable{

    public InicioFin() {
        this.color=("#929903");
        this.fondo="#f8f76a";
        this.borde="#b4b314";
    }

    @Override
    public InicioFin clonar(){
        InicioFin aux = new InicioFin();
        aux.setFlujoInferior(FlujoInferior);
        aux.setFlujoSuperior(FlujoSuperior);
        aux.setID(ID);
        aux.setMedioX(medioX);
        aux.setMedioY(medioY);
        aux.setTextoFigura(textoFigura);
        aux.setSiguiente(siguiente);
        aux.setAnterior(anterior);
        aux.setX1(x1);
        aux.setX2(x2);
        aux.setX3(x3);
        aux.setX4(x4);
        aux.setY1(y1);
        aux.setY2(y2);
        aux.setY3(y3);
        aux.setY4(y4);
        aux.setNombre(nombre);
        aux.setBorde(borde);
        aux.setColor(color);
        aux.setFondo(fondo);
        return aux;
    }
    
    @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {
        
        this.setMedioX(x);
        this.setMedioY(y);
        lienzo.setStroke(Color.valueOf(this.getFondo()));
        lienzo.setLineWidth(3.0);
        
        int x1= x-100;
        int y1=y;
        for (int i = 0; i < 70; i++) {
            for (int j = 0; j < 200; j++) {
                lienzo.strokeLine(x1+j,y1, x1+j, y1);
            }
            y1++;
        }
        
        this.setX1(x1);
        this.setY1(y);
        this.setX2(x1+200);
        this.setY2(y);
        this.setX3(x1);
        this.setY3(y+70);
        this.setX4(x1+200);
        this.setY4(y+70);        
        
        lienzo.setStroke(Color.valueOf(this.getBorde()));
        lienzo.strokeLine(x1,y, x1+200, y);
        lienzo.strokeLine(x1, y+70, x1+200, y+70);
        lienzo.setFill(Color.valueOf(this.getFondo()));
        lienzo.fillArc(x1-35, y, 70, 70, 90, 180, ArcType.OPEN);
        lienzo.fillArc(x1+165, y, 70, 70, 270, 180, ArcType.OPEN);
        lienzo.strokeArc(x1-35, y, 70, 70, 90, 180, ArcType.OPEN);
        lienzo.strokeArc(x1+165, y, 70, 70, 270, 180, ArcType.OPEN);
        
        lienzo.setStroke(Color.WHITE);
        lienzo.setFont(Font.font("Verdana", FontWeight.LIGHT, FontPosture.ITALIC, 15.0));
        lienzo.setFill(Color.BLACK);
        lienzo.fillText(this.getTextoFigura(), Math.round(x1+25),Math.round(y+35));//y        
    }

    @Override
    public void isPressed(GraphicsContext lienzo) {
        int xt= this.getMedioX()-100;
        lienzo.setStroke(Color.valueOf("#E30000"));
        lienzo.strokeLine(xt,this.getMedioY(), xt+200, this.getMedioY());
        lienzo.strokeLine(xt, this.getMedioY()+70, xt+200, this.getMedioY()+70);
        lienzo.strokeArc(xt-35, this.getMedioY(), 70, 70, 90, 180, ArcType.OPEN);
        lienzo.strokeArc(xt+165, this.getMedioY(), 70, 70, 270, 180, ArcType.OPEN);
        
    }


    
}
