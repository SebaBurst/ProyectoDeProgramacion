package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Etapa extends Figura implements java.io.Serializable {

    public Etapa() {
        this.color = "1b5583";
        this.fondo="#61bbef";
        this.borde="#3b83ad";
    }

    @Override
    public Etapa clonar (){
        
        Etapa aux = new Etapa();
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
        lienzo.setStroke(Color.valueOf(this.getFondo()));
        this.setMedioX(x);
        this.setMedioY(y);
        System.out.println("X: " + x);
        int ix = x - 90;
        System.out.println("*******************************");
        System.out.println("xi; " + ix);
        int x1 = ix;
        int y1 = y;
        int aux = y;
        if (this.getTextoFigura().length() > 15) {
            int diferencia = this.getTextoFigura().length() - 15;

            ix = x - 90 - (diferencia * 6);

            System.out.println("*******************************");
            System.out.println("xi; " + ix);
            x1 = ix;

            int total = (180) + (diferencia * 12);
            for (int i = 0; i < 71; i++) {
                for (int j = 0; j < total + 1; j++) {
                    lienzo.strokeLine(ix + j, aux, ix + j, aux);
                }
                aux++;
            }

            this.setX1(ix);
            this.setY1(y1);
            this.setX2(ix + total);
            this.setY2(y1);
            this.setX3(ix);
            this.setY3(y1 + 70);
            this.setX4(ix + total);
            this.setY4(y1 + 70);
            lienzo.setStroke(Color.valueOf(this.getBorde()));
            lienzo.setLineWidth(3.0);
            System.out.println("X1; " + x1);
            lienzo.strokeLine(x1, y1 + 70, x1 + total, y1 + 70);
            lienzo.strokeLine(x1, y1, x1 + total, y1);
            lienzo.strokeLine(x1 + total, y1, x1 + total, y1 + 70);
            lienzo.strokeLine(x1, y1, x1, y1 + 70);

        } else {

            this.setX1(ix);
            this.setY1(y1);
            this.setX2(ix + 180);
            this.setY2(y1);
            this.setX3(ix);
            this.setY3(y1 + 70);
            this.setX4(ix + 180);
            this.setY4(y1 + 70);

            for (int i = 0; i < 71; i++) {
                for (int j = 0; j < 181; j++) {
                    lienzo.strokeLine(ix + j, aux, ix + j, aux);
                }
                aux++;
            }

            lienzo.setStroke(Color.valueOf(this.getBorde()));
            lienzo.setLineWidth(3.0);
            System.out.println("X1; " + x1);
            lienzo.strokeLine(x1, y1 + 70, x1 + 180, y1 + 70);
            lienzo.strokeLine(x1, y1, x1 + 180, y1);
            lienzo.strokeLine(x1 + 180, y1, x1 + 180, y1 + 70);
            lienzo.strokeLine(x1, y1, x1, y1 + 70);

        }

        lienzo.setStroke(Color.BLACK);
        lienzo.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 15.0));
        lienzo.setFill(Color.BLACK);
        lienzo.fillText(this.getTextoFigura(), Math.round(ix + 25), Math.round(y + 35));//y
    }

    @Override
    public void isPressed(GraphicsContext lienzo) {
        int ix = this.getMedioX() - 90;
        System.out.println("*******************************");
        System.out.println("xi; " + ix);
        int x1 = ix;
        int y1 = this.getMedioY();
        int aux = this.getMedioY();
        if (this.getTextoFigura().length() > 15) {
            int diferencia = this.getTextoFigura().length() - 15;

            ix = this.getMedioX() - 90 - (diferencia * 6);

            System.out.println("*******************************");
            System.out.println("xi; " + ix);
            x1 = ix;
             int total = (180) + (diferencia * 12);
              lienzo.setStroke(Color.valueOf("#E30000"));
            lienzo.setLineWidth(3.0);
              lienzo.strokeLine(x1, y1 + 70, x1 + total, y1 + 70);
            lienzo.strokeLine(x1, y1, x1 + total, y1);
            lienzo.strokeLine(x1 + total, y1, x1 + total, y1 + 70);
            lienzo.strokeLine(x1, y1, x1, y1 + 70);

        }
        else{
                lienzo.setStroke(Color.valueOf("#E30000"));
            lienzo.setLineWidth(3.0);
            System.out.println("X1; " + x1);
            lienzo.strokeLine(x1, y1 + 70, x1 + 180, y1 + 70);
            lienzo.strokeLine(x1, y1, x1 + 180, y1);
            lienzo.strokeLine(x1 + 180, y1, x1 + 180, y1 + 70);
            lienzo.strokeLine(x1, y1, x1, y1 + 70);
        
        }

    }

}
