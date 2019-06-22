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
import static proyectov1.FXMLDocumentController.formas;

/**
 *
 * @author Sebastian
 */
public class Ciclo extends Figura implements java.io.Serializable{

    int aumento =0;

    public Ciclo() {
        this.color="#E519B1";
        this.fondo="#ffa8b8";
        this.borde="#e9748a";
    }
    
    
    Flujo conexionH;

    
    boolean verdadero= false;

    public int getAumento() {
        return aumento;
    }

    public void setAumento(int aumento) {
        this.aumento = aumento;
    }

    public boolean isVerdadero() {
        return verdadero;
    }

    public void setVerdadero(boolean verdadero) {
        this.verdadero = verdadero;
    }
    public ArrayList<Integer> idsFiguras= new ArrayList();

    public ArrayList<Integer> getIdsFiguras() {
        return idsFiguras;
    }

    public void setIdsFiguras(int id) {
        idsFiguras.add(id);
    }
    
    public Flujo getConexionH() {
        return conexionH;
    }

    public void setConexionH(Flujo conexionH) {
        this.conexionH = conexionH;
    }
    
    
    @Override
    public void dibujar(GraphicsContext lienzo, int x, int y) {
        lienzo.setStroke(Color.valueOf(this.getFondo()));
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
        lienzo.setStroke(Color.valueOf(this.getBorde()));
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
        
        int incremento = this.getAumento()*20;
        
        lienzo.setLineWidth(3.3);
        lienzo.setStroke(Color.valueOf("#FF1493"));

        lienzo.strokeLine(x-(130+incremento),y+30,diferenciax-(130+incremento),diferenciaY);
        
        
        int xd = diferenciax-(130+incremento);
        System.out.println("this:");
        double angle = Math.atan2((diferenciaY - diferenciaY), ( diferenciax- xd)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double fx = (-1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 10.0 + diferenciax;
        double fy = (-1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 10.0 + diferenciaY-2;
        double fx2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * 10.0 + diferenciax;
        double fy2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * 10.0 + diferenciaY-2;
                lienzo.setLineWidth(1);

        lienzo.strokeLine(fx, fy, fx2, fy2);
        lienzo.strokeLine(fx, fy, diferenciax, diferenciaY-2);
        lienzo.strokeLine(fx2, fy2, diferenciax, diferenciaY-2);
        lienzo.setLineWidth(3.3);
        
        lienzo.strokeLine(diferenciax-(130+incremento),diferenciaY,diferenciax,diferenciaY);
        
        lienzo.strokeLine(x-100,y+30,x-(130+incremento),y+30);
 
    }
    
    
    public void actualizarBloque(){
        ArrayList<Integer> nuevosIds = new ArrayList();
        for (int i = 0; i < idsFiguras.size(); i++) {
            for (int j = 0; j < formas.size(); j++) {
                if(formas.get(j).getID()==idsFiguras.get(i)){
                    nuevosIds.add(formas.get(j).getID());
                
                }
            }
        }
        
        idsFiguras.clear();
        idsFiguras=nuevosIds;
    
    }

    @Override
    public void isPressed(GraphicsContext lienzo) {
        int x = this.getMedioX();
        int y = this.getMedioY();
        lienzo.setStroke(Color.valueOf("#E30000"));
        lienzo.strokeLine(x, y, x + 100, y + 30); //\
        lienzo.strokeLine(x, y, x - 100, y + 30);//
        lienzo.strokeLine(x - 100, y + 30, x, y + 70);
        lienzo.strokeLine(x, y + 70, x + 100, y + 30);

    }

}
