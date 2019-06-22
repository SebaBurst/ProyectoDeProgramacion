/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectov1;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Sebastian
 */
public class CambiarColorController implements Initializable {

    
    
    
    @FXML public ColorPicker fondo;
    @FXML public ColorPicker border;
    @FXML public Button aceptarBorde;
    @FXML public Button aceptarFondo;
    
    
    @FXML public void colorFondo(ActionEvent event){
        Color c = fondo.getValue();
        String fondito = c.toString();
        fondito = fondito.replaceAll("0x", "#");
        String n="";
        for (int i = 0; i < 7; i++) {
            n = n+fondito.charAt(i);
            
        }
        
        FXMLDocumentController.recolor.setFondo(n);
        System.out.println(" >> ColorN: "+n);
    
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
