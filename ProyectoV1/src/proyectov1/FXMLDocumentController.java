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
                if(click==true && ((e.getX()+ 180)< 870) && ((e.getY()+70)<660)){
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
                if(click==true && ((e.getX()+190)< 870) && ((e.getX()-70)> 0) && ((e.getY()+70)< 660)){
                    entrada.dibujar(cuadro,(int)e.getX(),(int)e.getY());
                    click=false;
                }
            });
        }
        
    }

    @FXML
    private void dibujarInicioFin(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        InicioFin inicioFin = new InicioFin();
        click=true;
        if(click==true){
            lienzo.setOnMouseClicked(e->{
                System.out.println("XY: "+e.getX()+","+e.getY());
                if(click==true && ((e.getX()+235)< 870) && ((e.getX()-40)> 0) && ((e.getY()+70)< 660)){
                    inicioFin.dibujar(cuadro,(int)e.getX(),(int)e.getY());
                    click=false;
                }
            });
        }
    }

    @FXML
    private void dibujarDocumento(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Documento documento = new Documento();
        click=true;
        if(click==true){
            lienzo.setOnMouseClicked(e->{
                System.out.println("XY: "+e.getX()+","+e.getY());
                if(click==true && ((e.getX()+ 200) < 870) && ((e.getY()+ 125) < 660)){
                    documento.dibujar(cuadro,(int)e.getX(),(int)e.getY());
                    click=false;
                }
            });
        }
    }
    
    
    @FXML
    private void dibujarLinea(ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

   
}
