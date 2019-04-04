package proyectov1;

import Clases_Figura.Documento;
import Clases_Figura.EntradaSalida;
import Clases_Figura.Etapa;
import Clases_Figura.InicioFin;
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

    @FXML Button Etapa;

    @FXML Button EntradaSalida;
    
    @FXML Button Documento;
    
    @FXML Button InicioFin;
    
    boolean click = false;
    @FXML
    private void dibujarEtapa(ActionEvent event) {
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
        
    }
    @FXML
    private void dibujarEntradaSalida(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        EntradaSalida entrada = new EntradaSalida();   
        click=true;
        if(click==true){
            lienzo.setOnMouseClicked(e->{
                System.out.println("XY: "+e.getX()+","+e.getY());
                if(click==true){
                    entrada.dibujar(cuadro,(int)e.getX(),(int)e.getY());
                    click=false;
                }
            });
        }
        
    }
    
    @FXML
    private void dibujarLinea(ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @FXML
    private void dibujarInicioFin(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        InicioFin inicioFin = new InicioFin();
        inicioFin.dibujar(cuadro);
    }

    @FXML
    private void dibujarDocumento(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Documento documento = new Documento();
        documento.dibujar(cuadro);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

   
}
