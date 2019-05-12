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
public class Salida extends Figura{

    @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {
        lienzo.setStroke(Color.valueOf("#fec973"));
        this.setMedioX(x);
        this.setMedioY(y);
        int ix= x-90;
        int x1=ix;
        int y1=y;
        int x2=ix;
        if(this.getTextoFigura().length()>15){
            ix= x-180;
            x1=ix;
            y1=y;
            x2=ix;
             
        for (int j = 0; j < 370; j++) {
            x1=x2;
            y1=y;
            for (int i = 0; i < 70; i++) {
            lienzo.strokeLine(x1-i,y1+i, x1-i, y1+i);
            }
            x2+=1;
        }

        this.setX1(ix);
        this.setY1(y);
        this.setX2(ix+370);
        this.setY2(y);
        this.setX3(ix-70);
        this.setY3(y+70);
        this.setX4(ix+120);
        this.setY4(y+70);
        
        lienzo.setStroke(Color.valueOf("#d5700d"));
        lienzo.setLineWidth(3.0);
        System.out.println("ix: "+ix);
        lienzo.strokeLine(ix,y, ix+370, y);
        
        
        lienzo.strokeLine(ix,y, ix-70,y+70);
        lienzo.strokeLine(ix-70,y+70, ix+300, y+70);
        lienzo.strokeLine(ix+300,y+70, ix+370, y);
            
        }
        else{
             ix= x-90;
             x1=ix;
             y1=y;
             x2=ix;
              
        for (int j = 0; j < 190; j++) {
            x1=x2;
            y1=y;
            for (int i = 0; i < 70; i++) {
            lienzo.strokeLine(x1-i,y1+i, x1-i, y1+i);
            }
            x2+=1;
        }

        this.setX1(ix);
        this.setY1(y);
        this.setX2(ix+190);
        this.setY2(y);
        this.setX3(ix-70);
        this.setY3(y+70);
        this.setX4(ix+120);
        this.setY4(y+70);
        
        lienzo.setStroke(Color.valueOf("#d5700d"));
        lienzo.setLineWidth(3.0);
        System.out.println("ix: "+ix);
        lienzo.strokeLine(ix,y, ix+190, y);
        
        
        lienzo.strokeLine(ix,y, ix-70,y+70);
        lienzo.strokeLine(ix-70,y+70, ix+120, y+70);
        lienzo.strokeLine(ix+120,y+70, ix+190, y);
        }
        
       
        lienzo.setStroke(Color.WHITE);
        lienzo.setFont(Font.font("Arial", FontWeight.LIGHT, FontPosture.REGULAR, 15.0));
        lienzo.setFill(Color.BLACK);
        lienzo.fillText(this.getTextoFigura(), Math.round(ix),Math.round(y+35));//y
    }
    
}
