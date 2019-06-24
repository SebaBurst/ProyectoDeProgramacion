/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectov1;

import Clases_Figura.Flujo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Sebastian
 */
public class ProyectoV1 extends Application {
        public static Flujo dFlujo= new Flujo();
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image("/Clases_Figura/Estilos/ICON.png"));
        stage.setTitle("Diagramas de Flujo (By Enfermos)");
        stage.setScene(scene);
        stage.setMaxWidth(1205.0);
        stage.setMaxHeight(702.0);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
