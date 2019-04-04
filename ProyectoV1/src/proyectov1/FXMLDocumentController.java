package proyectov1;

import Clases_Figura.EntradaSalida;
import Clases_Figura.Etapa;
import java.awt.MouseInfo;
import java.awt.Point;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class FXMLDocumentController implements Initializable {

    @FXML
    AnchorPane root;

    @FXML
    Canvas lienzo;

    @FXML
    Button rectangulo;

    @FXML
    Button rombo;

    @FXML Button Etapa;

    @FXML Button EntradaSalida;

    Boolean click = false;
    public void dibujarEtapa(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Etapa etapa = new Etapa();
        click=true;
        if(click==true){
            lienzo.setOnMouseClicked(e->{
                System.out.println("XY: "+e.getX()+","+e.getY());
                if(click==true){
                    etapa.dibujar(cuadro,(int)e.getX(),(int)e.getY());
                    click=false;
                }
            });
        }
        
        cuadro.beginPath();
        cuadro.bezierCurveTo(25, 70, 100,0, 150, 5);
        cuadro.stroke();
    }

    public void dibujarEntradaSalida(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        EntradaSalida entrada = new EntradaSalida();
          
    }
    
    private void dibujarLinea(ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void dibujarInicioFin(ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void dibujarDocumento(ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

   
}
