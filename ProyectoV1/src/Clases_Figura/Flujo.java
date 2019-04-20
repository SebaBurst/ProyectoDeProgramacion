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
import static proyectov1.FXMLDocumentController.formas;

/**
 *
 * @author Sebastian
 */
public class Flujo {
    int x,y,x1,y2;
    
    
    public static Figura detectar (int x, int y){
        for (int i = 0; i < formas.size(); i++) {
            Figura aux = formas.get(i);
            System.out.println("Coordenadas " + i + " " + aux.getY1() + "," + aux.getY3());
            if (y >= aux.getY1() && y <= aux.getY3()) {
                System.out.println("Espacio No disponible");
                if (x >= aux.getX1() && x <= aux.getX2()) {
                    System.out.println(aux.getNombre());
                    return aux;
                }
            } else {
                System.out.println("Espacio Disponible");
            }

        }
        return null;
    
    }
    static int i =0;
    static Figura aux;
    
    public static ArrayList<Figura> union = new ArrayList();
    public static void dibujar(Canvas gc){ 
        GraphicsContext cuadro = gc.getGraphicsContext2D();
        gc.setOnMousePressed(e ->{
                  i+=1;
                  aux=detectar((int)e.getX(),(int)e.getY());
                  if(aux!=null){
                      union.add(aux);
                      System.out.println("Union Size: "+union.size());
                      aux=null;
                  }
                  else{
                      System.out.println("Error figura no encontrada");
                      i=2;
                  }
                  System.out.println("Resulto i: "+i+"");
                  if(i==2){
                       i=0;
                       System.out.println("Listo");
                       if(union.size()==2){
                        Figura a= union.get(0);
                        Figura b = union.get(1);
                        cuadro.strokeLine(a.getX1(),a.getY1(), b.getX1(),b.getY1());
                        union.removeAll(union);
             }
                      
                  }
                  });
  
        
            
        
            
    }
    
    
}
