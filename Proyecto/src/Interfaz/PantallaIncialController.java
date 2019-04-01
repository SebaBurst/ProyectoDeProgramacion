/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Clases_Figura.Rectangulo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Sebastian
 */
public class PantallaIncialController implements Initializable {

    
    @FXML Canvas lienzo;
    @FXML Button rectangulo;
    @FXML Button rombo;
    GraphicsContext cuadro = lienzo.getGraphicsContext2D();
    
    
    @FXML public void dibujarRectangulo(ActionEvent event){
        Rectangulo etapa = new Rectangulo();
        etapa.dibujar(cuadro);
    
    
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
