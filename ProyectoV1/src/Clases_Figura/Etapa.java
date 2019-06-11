package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Etapa extends Figura {

    public Etapa() {
        this.color="1b5583";
    }

     @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {
        lienzo.setStroke(Color.valueOf("#61bbef"));
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
                        int diferencia = this.getTextoFigura().length()-15;

                        ix= x-90-(diferencia*6);

        System.out.println("*******************************");
        System.out.println("xi; " + ix);
        x1 = ix;
     
int total = (180)+(diferencia*12);
            for (int i = 0; i < 71; i++) {
                for (int j = 0; j < total+1; j++) {
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
            lienzo.setStroke(Color.valueOf("#3b83ad"));
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

            lienzo.setStroke(Color.valueOf("#3b83ad"));
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

   
}
