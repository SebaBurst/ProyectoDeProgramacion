package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


public class Entrada extends Figura implements java.io.Serializable{

    public Entrada() {
        this.color="#1D8348";
        this.fondo="#adfaaa";
        this.borde="#4dc66c";
    }
    
     @Override
    public Entrada clonar (){
        
        Entrada aux = new Entrada();
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
        int ix= x-90;
        int x1=ix;
        int y1=y;
        int x2=ix;
        if(this.getTextoFigura().length()>15){
            System.out.println("Punto Medio: "+x);
            int diferencia = this.getTextoFigura().length()-15;
            System.out.println("Diferencia actual: "+diferencia);
            ix= x-90-(diferencia*6);
            System.out.println("Diferencia: "+diferencia);
            System.out.println("X: "+ix);
            x1=ix;
            y1=y;
            x2=ix;
            int total = (190)+(diferencia*12);
            int total2 = (total)-70;
            System.out.println("Total: "+total);
        for (int j = 0; j < total; j++) {
            x1=x2;
            y1=y;
            for (int i = 0; i < 70; i++) {
            lienzo.strokeLine(x1-i,y1+i, x1-i, y1+i);
            }
            x2+=1;
        }

      
        this.setX1(ix);
        this.setY1(y);
        this.setX2(ix+total);
        this.setY2(y);
        this.setX3(ix-70);
        this.setY3(y+70);
        this.setX4(ix+total2);
        this.setY4(y+70);
        
        lienzo.setStroke(Color.valueOf(this.getBorde()));
        lienzo.setLineWidth(3.0);
        System.out.println("ix: "+ix);
        lienzo.strokeLine(ix,y, ix+total, y);
        
        
        lienzo.strokeLine(ix,y, ix-70,y+70);
        lienzo.strokeLine(ix-70,y+70, ix+total2, y+70);
        lienzo.strokeLine(ix+total2,y+70, ix+total, y);
           
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
        
        lienzo.setStroke(Color.valueOf(this.getBorde()));
        lienzo.setLineWidth(3.0);
        System.out.println("ix: "+ix);
        lienzo.strokeLine(ix,y, ix+190, y);
        
        
        lienzo.strokeLine(ix,y, ix-70,y+70);
        lienzo.strokeLine(ix-70,y+70, ix+120, y+70);
        lienzo.strokeLine(ix+120,y+70, ix+190, y);
        }
        
       
        lienzo.setStroke(Color.WHITE);
        lienzo.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 15.0));
        lienzo.setFill(Color.BLACK);
        lienzo.fillText(this.getTextoFigura(), Math.round(ix),Math.round(y+35));//y
    }

    @Override
    public void isPressed(GraphicsContext lienzo) {
        int x = this.getMedioX();
        int y = this.getMedioY();
        
        
        int ix= x-90;
        int x1=ix;
        int y1=y;
        int x2=ix;
        if(this.getTextoFigura().length()>15){
           
            int diferencia = this.getTextoFigura().length()-15;
            ix= x-90-(diferencia*6);
          
            x1=ix;
            y1=y;
            x2=ix;
            int total = (190)+(diferencia*12);
            int total2 = (total)-70;
  
      
        
        lienzo.setStroke(Color.valueOf("##E30000"));
        lienzo.setLineWidth(3.0);
        System.out.println("ix: "+ix);
        lienzo.strokeLine(ix,y, ix+total, y);
        
        
        lienzo.strokeLine(ix,y, ix-70,y+70);
        lienzo.strokeLine(ix-70,y+70, ix+total2, y+70);
        lienzo.strokeLine(ix+total2,y+70, ix+total, y);
           
        }
        else{
             ix= x-90;
             x1=ix;
             y1=y;
             x2=ix;
              
  
        lienzo.setStroke(Color.valueOf("#E30000"));
        lienzo.setLineWidth(3.0);
        System.out.println("ix: "+ix);
        lienzo.strokeLine(ix,y, ix+190, y);
        
        
        lienzo.strokeLine(ix,y, ix-70,y+70);
        lienzo.strokeLine(ix-70,y+70, ix+120, y+70);
        lienzo.strokeLine(ix+120,y+70, ix+190, y);
        }
        

    }
}
