/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Sebastian
 */
public class Ciclo extends Figura {

    Flujo conexionH;

    public Flujo getConexionH() {
        return conexionH;
    }

    public void setConexionH(Flujo conexionH) {
        this.conexionH = conexionH;
    }
    
    
    @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {
        lienzo.setStroke(Color.valueOf("#ffa8b8"));
        this.setMedioX(x);
        this.setMedioY(y);
        int ix = x;
        int x1 = x;
        int y1 = y;
        int x2 = x;

        this.setX1(ix);
        this.setY1(y);
        this.setX2(ix + 190);
        this.setY2(y);
        this.setX3(ix - 70);
        this.setY3(y + 70);
        this.setX4(ix + 120);
        this.setY4(y + 70);
        lienzo.setLineWidth(3.0);

        for (int i = 0; i < 30; i++) {
            lienzo.strokeLine(x, y + i, x + 100, y + 30);
            lienzo.strokeLine(x, y + i, x - 100, y + 30);
        }
        int ys = y + 70;
        for (int i = 0; i < 40; i++) {
            lienzo.strokeLine(x, ys - i, x + 100, y + 30);
            lienzo.strokeLine(x - 100, y + 30, x, ys - i);

        }

        System.out.println("ix: " + ix);
        lienzo.setStroke(Color.valueOf("#e9748a"));
        lienzo.strokeLine(x, y, x + 100, y + 30); //\
        lienzo.strokeLine(x, y, x - 100, y + 30);//
        lienzo.strokeLine(x - 100, y + 30, x, y + 70);
        lienzo.strokeLine(x, y + 70, x + 100, y + 30);
        lienzo.setStroke(Color.WHITE);
        lienzo.setFont(Font.font("Verdana", FontWeight.LIGHT, FontPosture.ITALIC, 15.0));
        lienzo.setFill(Color.BLACK);
        lienzo.fillText(this.getTextoFigura(), Math.round(ix + 25), Math.round(y + 35));//y
        
        
        
        int diferenciax=(this.getConexionH().getX1()+this.getConexionH().getX())/2;
        int diferenciaY=(this.getConexionH().getY2()+this.getConexionH().getY())/2;
        
        
        lienzo.setLineWidth(3.3);
        lienzo.setStroke(Color.valueOf("#FF3342"));

        lienzo.strokeLine(x-200,y+30,diferenciax-200,diferenciaY);
        lienzo.strokeLine(diferenciax-200,diferenciaY,diferenciax,diferenciaY);
                lienzo.strokeLine(x-100,y+30,x-200,y+30);

        
        

        
    }

}
