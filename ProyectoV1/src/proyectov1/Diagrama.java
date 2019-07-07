/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectov1;

import Clases_Figura.Figura;
import Clases_Figura.Flujo;
import java.util.ArrayList;

/**
 *
 * @author Daniel-PC
 */
public class Diagrama {
    public int id;
    public ArrayList<Figura> formas = new ArrayList();// Coleccion de las Figuras que se van creando
    private ArrayList<Variable> variables = new ArrayList();// Coleccion de Variables ingresadas por el usuario
    public ArrayList<Flujo> enlaces = new ArrayList();// Coleccion de enlaces dentro del diagrama

    public Diagrama(int id) {
        this.id = id;
        this.formas = new ArrayList();
        this.variables = new ArrayList();
        this.enlaces = new ArrayList();
    }

    public Diagrama clonar(int id) {
        Diagrama aux = new Diagrama(id);
        for (Figura forma : formas) {
            aux.addForma(forma.clonar());

        }
        for (Flujo enlace : enlaces) {
            aux.addEnlace(enlace.clonar());
        }
        for (Variable variable : variables) {
            aux.addVariable(variable.clonar());
        }
        return aux;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Figura> getFormas() {
        return formas;
    }

    public void setFormas(ArrayList<Figura> formas) {
        this.formas = formas;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<Variable> variables) {
        this.variables = variables;
    }

    public ArrayList<Flujo> getEnlaces() {
        return enlaces;
    }

    public void setEnlaces(ArrayList<Flujo> enlaces) {
        this.enlaces = enlaces;
    }

    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    public void addForma(Figura figura) {
        formas.add(figura);
    }

    public void addEnlace(Flujo enlace) {
        enlaces.add(enlace);
    }

    public void clear() {
        formas.clear();
        enlaces.clear();
        variables.clear();
    }

}
