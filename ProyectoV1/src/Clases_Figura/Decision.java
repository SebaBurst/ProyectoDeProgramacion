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
import static proyectov1.FXMLDocumentController.enlaces;
import static proyectov1.FXMLDocumentController.formas;

/**
 *
 * @author Sebastian
 */
public class Decision extends Figura implements Cloneable {

    private int tipo = 1;

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    private Flujo finalDerecho = new Flujo();
    private Flujo finalIzquierdo = new Flujo();
    ArrayList<Figura> Verdaderas = new ArrayList();
    ArrayList<Figura> falsas = new ArrayList();
    Flujo ladoDerecho;
    Flujo ladoIzquierdo;

    boolean verdadero = false;

    public boolean isVerdadero() {
        return verdadero;
    }

    public void setVerdadero(boolean verdadero) {
        this.verdadero = verdadero;
    }

    public ArrayList<Figura> getFalsas() {
        return falsas;
    }

    public void setFalsas(Figura falsa) {
        falsas.add(falsa);
    }

    public Flujo getLadoIzquierdo() {
        return ladoIzquierdo;
    }

    public void setLadoIzquierdo(Flujo ladoIzquierdo) {
        this.ladoIzquierdo = ladoIzquierdo;
    }

    public ArrayList<Figura> getVerdaderas() {
        return Verdaderas;
    }

    public void setVerdaderas(Figura Verdadera) {
        Verdaderas.add(Verdadera);
    }

    public Flujo getLadoDerecho() {
        return ladoDerecho;
    }

    public void setLadoDerecho(Flujo ladoDerecho) {
        this.ladoDerecho = ladoDerecho;
    }

    public Decision() {
        this.color = "#572364";
        this.fondo = "#b44cd9";
        this.borde = "#8a08b8";
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

        lienzo.setStroke(Color.valueOf(this.getFondo()));
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

        //Lado Derecho enlace-enlace
        lienzo.setStroke(Color.valueOf("#01be9b"));
        lienzo.setFill(Color.valueOf("#01be9b"));
        lienzo.fillText("TRUE", Math.round(x + 120), Math.round(y + 22));
        lienzo.strokeLine(x + 100, y + 30, x + 180, y + 30);

        lienzo.setStroke(Color.valueOf("#ff0025"));
        lienzo.setFill(Color.valueOf("#ff0025"));
        lienzo.fillText("FALSE", Math.round(x - 160), Math.round(y + 22));
        lienzo.strokeLine(x - 100, y + 30, x - 180, y + 30);

        Flujo punto = new Flujo();
        //Dibujar linea inferior;
        for (int i = 0; i < enlaces.size(); i++) {
            if (enlaces.get(i).getId() == this.getFlujoInferior()) {
                punto = enlaces.get(i);

            }
        }

        Flujo der = new Flujo();
        Flujo izq = new Flujo();
        for (int i = 0; i < Verdaderas.size(); i++) {
            if (Verdaderas.get(i).getSiguiente() == -9) {
                for (int j = 0; j < enlaces.size(); j++) {
                    if (Verdaderas.get(i).getFlujoInferior() == enlaces.get(j).getId()) {
                        der = enlaces.get(j);

                    }
                }

            }
        }

        for (int i = 0; i < falsas.size(); i++) {
            if (falsas.get(i).getSiguiente() == -9) {
                for (int j = 0; j < enlaces.size(); j++) {
                    if (falsas.get(i).getFlujoInferior() == enlaces.get(j).getId()) {
                        izq = enlaces.get(j);

                    }
                }

            }
        }

        lienzo.setStroke(Color.valueOf("#01be9b"));
        lienzo.strokeLine(punto.getX(), punto.getY(), this.getFinalDerecho().getX1(), this.getFinalDerecho().getY2());
        lienzo.setStroke(Color.valueOf("#ff0025"));

        lienzo.strokeLine(punto.getX(), punto.getY(), this.getFinalIzquierdo().getX1(), this.getFinalIzquierdo().getY2());

        //
        lienzo.setStroke(Color.WHITE);
        lienzo.setFont(Font.font("Verdana", FontWeight.LIGHT, FontPosture.ITALIC, 15.0));
        lienzo.setFill(Color.BLACK);
        lienzo.fillText(this.getTextoFigura(), Math.round(ix + 25), Math.round(y + 35));//y

        //Dibujar flujo derecho;
    }

    public void nodo() {

        System.out.println(">> Entre a nodo");
        System.out.println(">> Cantidad de Verdaderas: " + Verdaderas.size());
        System.out.println(">> Cantidad de Falsas: " + falsas.size());
        if (Verdaderas.isEmpty()) {
            this.setLadoDerecho(this.getFinalDerecho());

        } else {
            //Ordenar verdaderos
            for (int i = 0; i < Verdaderas.size(); i++) {
                if (Verdaderas.get(i).getAnterior() == -8) {
                    for (int j = 0; j < enlaces.size(); j++) {
                        if (Verdaderas.get(i).getFlujoSuperior() == enlaces.get(j).getId()) {
                            this.setLadoDerecho(enlaces.get(j));
                        }
                    }
                }
            }

        }

        if (falsas.isEmpty()) {
            this.setLadoIzquierdo(this.getFinalIzquierdo());

        } else {
            for (int i = 0; i < falsas.size(); i++) {
                if (falsas.get(i).getAnterior() == -8) {
                    for (int j = 0; j < enlaces.size(); j++) {
                        if (falsas.get(i).getFlujoSuperior() == enlaces.get(j).getId()) {
                            this.setLadoIzquierdo(enlaces.get(j));
                        }
                    }
                }
            }
        }

        // Asignar falso
        //imprimir//
        System.out.println(">>Verdaderas");
        for (int i = 0; i < Verdaderas.size(); i++) {
            System.out.println(">> " + Verdaderas.get(i) + " Texto: " + Verdaderas.get(i).getTextoFigura());
            System.out.println(">> Anterior: " + Verdaderas.get(i).getAnterior());
            System.out.println(">> Siguiente: " + Verdaderas.get(i).getSiguiente());
            System.out.println("<< Flujo arriba: " + Verdaderas.get(i).getFlujoSuperior());
            System.out.println("<< Flujo abajo: " + Verdaderas.get(i).getFlujoInferior());
        }

        System.out.println(">>Falsas");
        for (int i = 0; i < falsas.size(); i++) {
            System.out.println(">> " + falsas.get(i) + " Texto: " + falsas.get(i).getTextoFigura());
            System.out.println(">> Anterior: " + falsas.get(i).getAnterior());
            System.out.println(">> Siguiente: " + falsas.get(i).getSiguiente());
            System.out.println("<< Flujo arriba: " + falsas.get(i).getFlujoSuperior());
            System.out.println("<< Flujo abajo: " + falsas.get(i).getFlujoInferior());
        }
    }

    public void actualizarFlujos() {
        ArrayList<Figura> nuevoDer = new ArrayList();
        ArrayList<Figura> nuevoIzq = new ArrayList();

        System.out.println(">> V <<<: " + Verdaderas.size());
        System.out.println(">> F <<<: " + falsas.size());

        for (int i = 0; i < formas.size(); i++) {
            for (int j = 0; j < Verdaderas.size(); j++) {
                if (formas.get(i).getID() == Verdaderas.get(j).getID()) {
                    nuevoDer.add(formas.get(i));
                }
            }
        }

        for (int i = 0; i < formas.size(); i++) {
            for (int j = 0; j < falsas.size(); j++) {
                if (formas.get(i).getID() == falsas.get(j).getID()) {
                    nuevoIzq.add(formas.get(i));
                }
            }
        }

        Verdaderas.clear();
        Verdaderas = nuevoDer;
        falsas.clear();
        falsas = (nuevoIzq);
        this.nodo();
        System.out.println(">> V <<<: " + Verdaderas.size());
        System.out.println(">> F <<<: " + falsas.size());

    }

    public Flujo getFinalDerecho() {
        return finalDerecho;
    }

    public void setFinalDerecho(Flujo finalDerecho) {
        this.finalDerecho = finalDerecho;
    }

    public Flujo getFinalIzquierdo() {
        return finalIzquierdo;
    }

    public void setFinalIzquierdo(Flujo finalIzquierdo) {
        this.finalIzquierdo = finalIzquierdo;
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
