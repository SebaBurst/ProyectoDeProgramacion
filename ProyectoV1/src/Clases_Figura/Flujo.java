/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author Sebastian
 */
public class Flujo {

    int decision =-19;
    int ciclo;
    int id;
    int x, y, x1, y2;
    boolean derecho = true;

    public boolean isDerecho() {
        return derecho;
    }

    
    public void setDerecho(boolean derecho) {
        this.derecho = derecho;
    }
    
    
    
    
    public int getDecision() {
        return decision;
    }

    public void setDecision(int decision) {
        this.decision = decision;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public void dibujar(int x, int y, int x2, int y2, GraphicsContext cuadro) {
        this.setX(x);
        this.setX1(x2);
        this.setY(y);
        this.setY2(y2);
        cuadro.setStroke(Color.valueOf("#353333"));
        cuadro.strokeLine(x, y, x2, y2);

        System.out.println("this:");
        double angle = Math.atan2((y2 - y), (x2 - x)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double fx = (-1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 10.0 + x2;
        double fy = (-1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 10.0 + y2-2;
        double fx2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 10.0 + x2;
        double fy2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 10.0 + y2-2;

        cuadro.setStroke(Color.valueOf("#ffffff"));
        cuadro.setLineWidth(1.3);
        for (int i = 0; i < 7; i++) {
            cuadro.strokeLine(fx2 - i, fy2, x2, y2 - 4);
            cuadro.strokeLine(fx + i, fy, x2, y2 - 4);
        }

        cuadro.setStroke(Color.valueOf("#353333"));
        cuadro.strokeLine(fx, fy, fx2, fy2);
        cuadro.strokeLine(fx, fy, x2, y2-2);
        cuadro.strokeLine(fx2, fy2, x2, y2-2);
        int diferenciax=(x2+x)/2;
        int diferenciaY=(y2+y)/2;
        
        
        cuadro.setLineWidth(3.3);
    
    }
        public void dibujarDecision(int x, int y, int y2, GraphicsContext lienzo) {
        lienzo.setStroke(Color.valueOf("#5F04B4"));
        lienzo.strokeLine(x-200, y + 120, x +200, y + 120);
        lienzo.strokeLine(x+200, y + 30, x +200, y + 120);
        lienzo.strokeLine(x-200, y + 30, x-200, y + 120);
        lienzo.strokeLine(x+200, y + 30, x+100, y+30);
        lienzo.strokeLine(x-100, y + 30, x-200, y+30);
        
      
        
       
        

    }

}
