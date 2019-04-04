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

    public void dibujarEtapa(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Etapa etapa = new Etapa();
        MouseEvent evento = null;
        Point punto= MouseInfo.getPointerInfo().getLocation();
        int x= punto.x;
        int y= punto.y;
        System.out.println("xy: "+x+" , "+y);
        etapa.dibujar(cuadro);
    }

    public void dibujarEntradaSalida(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        EntradaSalida entrada = new EntradaSalida();
        entrada.dibujar(cuadro);    
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
