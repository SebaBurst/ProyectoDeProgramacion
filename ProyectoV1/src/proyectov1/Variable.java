/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectov1;

/**
 *
 * @author Daniel-PC
 */
public class Variable  implements java.io.Serializable{
    String nombre = "";
    String texto = "";
    String tipo = "";

    public Variable() {
   
    
    }

    
    public Variable clonar(){
        Variable aux = new Variable();
        aux.setNombre(nombre);
        aux.setTexto(texto);
        aux.setTipo(tipo);
        return aux;
    
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
      
}
