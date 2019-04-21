/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases_Figura;

import java.awt.Graphics;
import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import static proyectov1.FXMLDocumentController.formas;

/**
 *
 * @author Sebastian
 */
public class Flujo {
    int x,y,x1,y2;
    Figura inicio,fin;
    
    

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

  
    
    
    public static int detectar (int x, int y){
        for (int i = 0; i < formas.size(); i++) {
            Figura aux = formas.get(i);
            System.out.println("Coordenadas " + i + " " + aux.getY1() + "," + aux.getY3());
            if (y >= aux.getY1() && y <= aux.getY3()) {
                System.out.println("Espacio No disponible");
                if (x >= aux.getX1() && x <= aux.getX2()) {
                    System.out.println(aux.getNombre());
                    return i;
                }
            } else {
                System.out.println("Espacio Disponible");
            }

        }
        return -1;
    
    }
    static int i =0;
    static Figura aux;
    
    public static ArrayList<Figura> union = new ArrayList();
    
    public static int calcularCoordenadas(){
        return 0;
    
    }
    
    public void dibujar(int x,int y,int x2,int y2,GraphicsContext cuadro){
        this.setX(x);
        this.setX1(x2);
        this.setY(y);
        this.setY2(y2);
        cuadro.setStroke(Color.valueOf("#353333"));
        cuadro.strokeLine(x,y, x2,y2);

    
    
    }
    
    
}
