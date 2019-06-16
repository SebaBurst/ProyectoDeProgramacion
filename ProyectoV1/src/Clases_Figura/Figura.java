package Clases_Figura;

import javafx.scene.canvas.GraphicsContext;

public abstract class Figura implements java.io.Serializable{
    String color="#353333";

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    public String textoFigura;
    public int medioY=0;
    public int medioX=0;
    public int FlujoSuperior;
    public int FlujoInferior;
    public int anterior=-8;
    public int siguiente=-9;
    private int ID;
    

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public int getAnterior() {
        return anterior;
    }

    public void setAnterior(int anterior) {
        this.anterior = anterior;
    }

    public int getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(int siguiente) {
        this.siguiente = siguiente;
    }
    
    
    
    
    public int getFlujoSuperior() {
        return FlujoSuperior;
    }
    public void setFlujoSuperior(int FlujoSuperior) {
        this.FlujoSuperior = FlujoSuperior;
    }

    public int getFlujoInferior() {
        return FlujoInferior;
    }

    public void setFlujoInferior(int FlujoInferior) {
        this.FlujoInferior = FlujoInferior;
    }
    
    public int getMedioY(){
        return medioY;
    }

    public void setMedioY(int medioY) {
        this.medioY = medioY;
    }

    public int getMedioX() {
        return medioX;
    }

    public void setMedioX(int medioX) {
        this.medioX = medioX;
    }

    public String getTextoFigura() {
        return textoFigura;
    }

    public void setTextoFigura(String textoFigura) {
        this.textoFigura = textoFigura;
    }
    

    int x1, x2, x3, x4, y1, y2, y3, y4;
    String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public abstract void dibujar(GraphicsContext lienzo, int x, int y);
    
    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getX3() {
        return x3;
    }

    public void setX3(int x3) {
        this.x3 = x3;
    }

    public int getX4() {
        return x4;
    }

    public void setX4(int x4) {
        this.x4 = x4;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getY3() {
        return y3;
    }

    public void setY3(int y3) {
        this.y3 = y3;
    }

    public int getY4() {
        return y4;
    }

    public void setY4(int y4) {
        this.y4 = y4;
    }

    
    
}
