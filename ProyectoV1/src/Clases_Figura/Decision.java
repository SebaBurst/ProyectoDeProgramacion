/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases_Figura;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Sebastian
 */
public class Decision extends Figura {

    boolean tipoFlujo;
    int agrandar=0;
    int idF;
    int idT;
    ArrayList<Flujo> flujosT = new ArrayList();
    ArrayList<Flujo> flujosF = new ArrayList();
    ArrayList<Figura> figurasT = new ArrayList();
    ArrayList<Figura> figurasF = new ArrayList();
    Flujo flujoT = new Flujo();
    Flujo flujoF = new Flujo();

    public int getAgrandar() {
        return agrandar;
    }

    public void setAgrandar(int agrandar) {
        this.agrandar = agrandar+60;
    }

    public ArrayList<Flujo> getFlujosT() {
        return flujosT;
    }

    public void setFlujosT(ArrayList<Flujo> flujosT) {
        this.flujosT = flujosT;
    }

    public ArrayList<Flujo> getFlujosF() {
        return flujosF;
    }

    public void setFlujosF(ArrayList<Flujo> flujosF) {
        this.flujosF = flujosF;
    }

    public Flujo getFlujoT() {
        return flujoT;
    }

    public void setFlujoT(Flujo flujoT) {
        this.flujoT = flujoT;
    }

    public Flujo getFlujoF() {
        return flujoF;
    }

    public void setFlujoF(Flujo flujoF) {
        this.flujoF = flujoF;
    }

    public ArrayList<Figura> getFigurasT() {
        return figurasT;
    }

    public void setFigurasT(ArrayList<Figura> figurasT) {
        this.figurasT = figurasT;
    }

    public ArrayList<Figura> getFigurasF() {
        return figurasF;
    }

    public void setFigurasF(ArrayList<Figura> figurasF) {
        this.figurasF = figurasF;
    }

    public int getIdF() {
        return idF;
    }

    public void setIdF(int idF) {
        this.idF = idF;
    }

    public int getIdT() {
        return idT;
    }

    public void setIdT(int idT) {
        this.idT = idT;
    }

    public boolean getTipoFlujo() {
        return tipoFlujo;
    }

    public void setTipoFlujo(boolean tipoFlujo) {
        this.tipoFlujo = tipoFlujo;
    }

    @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {
        lienzo.setStroke(Color.valueOf("#b44cd9"));
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

        lienzo.setStroke(Color.valueOf("#8a08b8"));
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
        lienzo.setStroke(Color.valueOf("#311740"));
        lienzo.strokeLine(x, y, x + 100, y + 30); //\
        lienzo.strokeLine(x, y, x - 100, y + 30);//
        lienzo.strokeLine(x - 100, y + 30, x, y + 70);
        lienzo.strokeLine(x, y + 70, x + 100, y + 30);
        lienzo.setStroke(Color.WHITE);
        lienzo.setFont(Font.font("Verdana", FontWeight.LIGHT, FontPosture.ITALIC, 15.0));
        lienzo.setFill(Color.BLACK);
        lienzo.fillText(this.getTextoFigura(), Math.round(ix + 25), Math.round(y + 35));//y
        int sumar = getAgrandar();
        this.flujoT.dibujar(x + 300, y + 30, x + 300, y+200+sumar, lienzo);
        flujosT.add(flujoT);
        lienzo.setStroke(Color.valueOf("#298A08"));

        lienzo.strokeLine(x + 100, y + 30, x + 300, y + 30);

        this.flujoF.dibujar(x - 300, y + 30, x - 300, y+200+sumar, lienzo);
        flujosF.add(flujoF);
        lienzo.setStroke(Color.valueOf("#DF0101"));

        lienzo.strokeLine(x - 100, y + 30, x - 300, y + 30);
        
        dibujarAbajo(lienzo,x,y,sumar);

    }

    public void dibujarAbajo(GraphicsContext lienzo, int x, int y,int sumar) {
        //t
        lienzo.setStroke(Color.valueOf("#298A08"));
        lienzo.strokeLine(x + 300, y + 200+sumar, x, y + 200+sumar);

        //f
        lienzo.setStroke(Color.valueOf("#DF0101"));
        lienzo.strokeLine(x - 300, y + 200+sumar, x, y + 200+sumar);
    }

}
