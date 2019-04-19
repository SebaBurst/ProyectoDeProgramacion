package proyectov1;

import Clases_Figura.Documento;
import Clases_Figura.EntradaSalida;
import Clases_Figura.Etapa;
import Clases_Figura.Figura;
import Clases_Figura.InicioFin;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class FXMLDocumentController implements Initializable {

    //
    ArrayList<Figura> formas = new ArrayList();
    @FXML
    AnchorPane root;

    @FXML
    Canvas lienzo;

    @FXML
    Button Etapa;

    @FXML
    Button EntradaSalida;

    @FXML
    Button Documento;

    @FXML
    Button InicioFin;

    public boolean activarDrag = true;

    boolean click = false;
    @FXML

    int numero = 0;

    public void repintar(GraphicsContext lienzo) {
        for (int i = 0; i < formas.size(); i++) {

            formas.get(i).dibujar(lienzo, formas.get(i).getX1(),
                    formas.get(i).getY1());

        }

    }

    public Figura detectarFigura(int x, int y) {
        for (int i = 0; i < formas.size(); i++) {
            Figura aux = formas.get(i);
            System.out.println("Coordenadas " + i + " " + aux.getY1() + "," + aux.getY3());
            if (y >= aux.getY1() && y <= aux.getY3()) {
                System.out.println("Espacio No disponible");
                if (x >= aux.getX1() && x <= aux.getX2()) {
                    System.out.println(aux.getNombre());
                    return aux;
                }
            } else {
                System.out.println("Espacio Disponible");
            }

        }
        return null;
    }

    public void moverFigura(GraphicsContext cuadro, Canvas lienzo) {
        if (activarDrag == true) {
            lienzo.setOnMousePressed(e -> {
                System.out.println("Cantidad: " + numero);
                Figura Aux = detectarFigura((int) e.getX(), (int) e.getY());
                if (Aux != null) {
                    lienzo.setOnMouseDragged(en -> {
                        cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());
                        Aux.dibujar(cuadro, (int) en.getX(), (int) en.getY());
                        repintar(cuadro);

                    });
                    lienzo.setOnMouseReleased(p -> {
                        lienzo.setOnMouseDragged(null);

                    });

                }
            });
        }
    }

    @FXML
    private void dibujarEtapa(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Etapa etapa = new Etapa();
        click = true;
        activarDrag = false;
        if (click == true) {
            lienzo.setOnMouseClicked(e -> {
                System.out.println("XY: " + e.getX() + "," + e.getY());
                if (click == true && ((e.getX() + 180) < lienzo.getWidth()) && ((e.getY() + 70) < lienzo.getHeight())) {
                    etapa.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    etapa.setNombre("Rectangulo " + numero);
                    numero++;
                    formas.add(etapa);
                    click = false;
                    activarDrag = true;
                } else if ((e.getY() + 70) > lienzo.getHeight()) {
                    lienzo.setHeight(lienzo.getHeight() + 80);
                    etapa.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    etapa.setNombre("Rectangulo " + numero);
                    numero++;
                    formas.add(etapa);
                    click = false;
                    activarDrag = true;
                } else if ((e.getX() + 70) > lienzo.getWidth()) {
                    lienzo.setWidth(lienzo.getWidth() + 200);
                    etapa.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    etapa.setNombre("Rectangulo " + numero);
                    numero++;
                    formas.add(etapa);
                    click = false;
                    activarDrag = true;
                }
            });
        }
        moverFigura(cuadro, lienzo);

    }

    @FXML
    private void dibujarEntradaSalida(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        EntradaSalida entrada = new EntradaSalida();
        click = true;
        activarDrag = false;
        if (click == true) {
            lienzo.setOnMouseClicked(e -> {
                System.out.println("XY: " + e.getX() + "," + e.getY());
                if (click == true && ((e.getX() + 190) < lienzo.getWidth()) && ((e.getX() - 70) > 0) && ((e.getY() + 70) < lienzo.getHeight())) {
                    entrada.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    entrada.setNombre("Rombo " + numero);

                    click = false;
                    formas.add(entrada);
                    activarDrag = true;
                } else if ((e.getY() + 70) > lienzo.getHeight()) {
                    lienzo.setHeight(lienzo.getHeight() + 80);
                    entrada.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    entrada.setNombre("Rombo " + numero);
                    numero++;
                    formas.add(entrada);
                    activarDrag = true;
                    click = false;
                } else if ((e.getX() + 70) > lienzo.getWidth()) {
                    lienzo.setWidth(lienzo.getWidth() + 200);
                    entrada.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    entrada.setNombre("Rombo " + numero);
                    numero++;
                    formas.add(entrada);
                    activarDrag = true;
                    click = false;
                }

            });
        }
        moverFigura(cuadro, lienzo);

    }

    @FXML
    private void dibujarInicioFin(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        InicioFin inicioFin = new InicioFin();
        click = true;
        activarDrag = false;
        if (click == true) {
            lienzo.setOnMouseClicked(e -> {
                System.out.println("XY: " + e.getX() + "," + e.getY());
                if (click == true && ((e.getX() + 235) < lienzo.getWidth()) && ((e.getX() - 40) > 0) && ((e.getY() + 70) < lienzo.getHeight())) {
                    inicioFin.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    inicioFin.setNombre("Inicio " + numero);

                    click = false;
                    formas.add(inicioFin);
                    activarDrag = true;
                } else if ((e.getY() + 70) > lienzo.getHeight()) {
                    lienzo.setHeight(lienzo.getHeight() + 80);
                    inicioFin.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    inicioFin.setNombre("Inicio " + numero);
                    numero++;
                    formas.add(inicioFin);
                    click = false;
                    activarDrag = true;
                } else if ((e.getX() + 70) > lienzo.getWidth()) {
                    lienzo.setWidth(lienzo.getWidth() + 250);
                    inicioFin.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    inicioFin.setNombre("Inicio " + numero);
                    numero++;
                    formas.add(inicioFin);
                    click = false;
                    activarDrag = true;
                }
            });
        }
        moverFigura(cuadro, lienzo);
    }

    @FXML
    private void dibujarDocumento(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Documento documento = new Documento();
        click = true;
        activarDrag = true;
        if (click == true) {
            lienzo.setOnMouseClicked(e -> {
                System.out.println("XY: " + e.getX() + "," + e.getY());
                if (click == true && ((e.getX() + 200) < lienzo.getWidth()) && ((e.getY() + 125) < lienzo.getHeight())) {
                    documento.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    documento.setNombre("Documento " + numero);

                    click = false;
                    formas.add(documento);
                    activarDrag = true;
                } else if ((e.getY() + 70) > lienzo.getHeight()) {
                    lienzo.setHeight(lienzo.getHeight() + 80);
                    documento.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    documento.setNombre("Documento " + numero);
                    numero++;
                    formas.add(documento);
                    click = false;
                    activarDrag = true;
                } else if ((e.getX() + 70) > lienzo.getWidth()) {
                    lienzo.setWidth(lienzo.getWidth() + 220);
                    documento.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    documento.setNombre("Documento " + numero);
                    numero++;
                    formas.add(documento);
                    click = false;
                    activarDrag = true;
                }
            });
        }
        moverFigura(cuadro, lienzo);
    }

    public void detectarBorrar(int x, int y) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        for (int i = 0; i < formas.size(); i++) {
            Figura aux = formas.get(i);
            if (y >= aux.getY1() && y <= aux.getY3()) {
                if (x >= aux.getX1() && x <= aux.getX2()) {
                    formas.remove(i);
                    cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());

                    repintar(cuadro);
                }

            } else {
                System.out.println("Espacio Disponible");
            }

        }

    }

    @FXML
    Button cut;
    boolean borrar = false;

    @FXML
    private void borrarFigura(ActionEvent event) {
        borrar = false;
        if (borrar == false) {
            lienzo.setOnMouseClicked(e -> {
                System.out.println("************************************");
                detectarBorrar((int) e.getX(), (int) e.getY());
                borrar = true;
            });
        }
    }

    @FXML
    private void dibujarLinea(ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        moverFigura(cuadro, lienzo);
    }

}
