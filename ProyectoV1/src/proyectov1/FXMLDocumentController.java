/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectov1;

import Clases_Figura.Rectangulo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Sebastian
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML AnchorPane root;
    @FXML Canvas lienzo;
    @FXML Button rectangulo;
    @FXML Button rombo;
    
    
    @FXML private void dibujarRectangulo(ActionEvent event){
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Rectangulo etapa = new Rectangulo();
        etapa.dibujar(cuadro);

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
