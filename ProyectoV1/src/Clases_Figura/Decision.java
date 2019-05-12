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
public class Decision extends Figura{
    

    @Override
    public void dibujar(GraphicsContext lienzo,int x,int y) {
        lienzo.setStroke(Color.valueOf("#b44cd9"));
        this.setMedioX(x);
        this.setMedioY(y);
        int ix= x;
        int x1=x;
        int y1=y;
        int x2=x;
        
       
        this.setX1(ix);
        this.setY1(y);
        this.setX2(ix+190);
        this.setY2(y);
        this.setX3(ix-70);
        this.setY3(y+70);
        this.setX4(ix+120);
        this.setY4(y+70);
        
        lienzo.setStroke(Color.valueOf("#8a08b8"));
        lienzo.setLineWidth(3.0);
        
        for (int i = 0; i < 30; i++) {
            lienzo.strokeLine(x,y+i, x+100, y+30);
            lienzo.strokeLine(x,y+i, x-100,y+30);
        }
        int ys =y+70;
        for (int i = 0; i < 40; i++) {
            lienzo.strokeLine(x,ys-i, x+100, y+30);
            lienzo.strokeLine(x-100,y+30, x, ys-i);


        }
        
        System.out.println("ix: "+ix);
        lienzo.setStroke(Color.valueOf("#311740"));
        lienzo.strokeLine(x,y, x+100, y+30); //\
        lienzo.strokeLine(x,y, x-100,y+30);//
        lienzo.strokeLine(x-100,y+30, x, y+70);
        lienzo.strokeLine(x,y+70, x+100, y+30);
        lienzo.setStroke(Color.WHITE);
        lienzo.setFont(Font.font("Verdana", FontWeight.LIGHT, FontPosture.ITALIC, 15.0));
        lienzo.setFill(Color.BLACK);
        lienzo.fillText(this.getTextoFigura(), Math.round(ix+25),Math.round(y+35));//y
    }

}

