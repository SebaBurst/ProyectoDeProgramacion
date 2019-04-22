package proyectov1;

import Clases_Figura.Documento;
import Clases_Figura.EntradaSalida;
import Clases_Figura.Etapa;
import Clases_Figura.Figura;
import Clases_Figura.Flujo;
import Clases_Figura.InicioFin;
import Clases_Figura.Punto;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {
    public static String texto = "";
    //
    public static ArrayList<Figura> formas = new ArrayList();
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

    @FXML
    Button Line;

    ArrayList<Flujo> enlaces = new ArrayList();
    public boolean activarDrag = true;

    boolean click = false;
    @FXML

    int numero = 0;

    public void repintar(GraphicsContext lienzo) {
        for (int i = 0; i < enlaces.size(); i++) {
            Flujo enlace = enlaces.get(i);
            System.out.println("Enlace XY'S: " + enlace.getX() + ", " + enlace.getY() + " | " + enlace.getX1() + ", " + enlace.getY2());
            enlace.dibujar(enlace.getX(), enlace.getY(), enlace.getX1(), enlace.getY2(), lienzo);
        }
        for (int i = 0; i < formas.size(); i++) {

            formas.get(i).dibujar(lienzo, formas.get(i).getX1(), formas.get(i).getY1());
        }
    }

    public Figura detectarFigura2(int x, int y) {
        for (int i = 0; i < formas.size(); i++) {
            Figura aux = formas.get(i);
            System.out.println("Coordenadas " + i + " " + aux.getY1() + "," + aux.getY3());
            if (y >= aux.getY1() - 100 && y <= aux.getY3() + 20 && x >= aux.getX1() - 220 && x <= aux.getX2() + 80) {
                System.out.println("Espacio No disponible");

                System.out.println(aux.getNombre());
                return aux;
            } else {
                System.out.println("Espacio Disponible");
            }

        }
        return null;
    }

    public Figura detectarFigura1(int x, int y) {
        for (int i = 0; i < formas.size(); i++) {
            Figura aux = formas.get(i);
            System.out.println("Coordenadas " + i + " " + aux.getY1() + "," + aux.getY3());
            if (y >= aux.getY1() && y <= aux.getY3() && x >= aux.getX1() && x <= aux.getX2()) {
                System.out.println("Espacio No disponible");

                System.out.println(aux.getNombre());
                return aux;
            } else {
                System.out.println("Espacio Disponible");
            }

        }
        return null;
    }
    int x = 0, x4 = 0, x2 = 0, x3 = 0, y = 0, y4 = 0, y2 = 0, y3 = 0;

    public void moverFigura(GraphicsContext cuadro, Canvas lienzo) {
        lienzo.setOnMousePressed(e -> {
            System.out.println("Cantidad: " + numero);
            Figura Aux = detectarFigura1((int) e.getX(), (int) e.getY());

            if (Aux != null) {
                x = Aux.getX1();
                y = Aux.getY1();
                x2 = Aux.getX2();
                y2 = Aux.getY2();
                x3 = Aux.getX3();
                y3 = Aux.getY3();
                x4 = Aux.getX4();
                y4 = Aux.getY4();
            }

            //Se agrega este bloque para el movimiento de las Lineas.
            lienzo.setOnMouseDragged(en -> {
                cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());
                for (int i = 0; i < enlaces.size(); i++) {
                    Flujo link = enlaces.get(i);
                    if (link.getX() == Aux.getX1() && link.getY() == Aux.getY1()) {
                        link.dibujar((int) en.getX(), (int) en.getY(), link.getX1(), link.getY2(), cuadro);
                        System.out.println("Entre");
                    } else if (link.getX1() == Aux.getX1() && link.getY2() == Aux.getY1()) {
                        link.dibujar(link.getX(), link.getY(), (int) en.getX(), (int) en.getY(), cuadro);
                        System.out.println("Entre");
                    }
                    enlaces.set(i, link);
                }

                Aux.dibujar(cuadro, (int) en.getX(), (int) en.getY());
                repintar(cuadro);
                System.out.println("e: " + (int) en.getX() + "," + (int) en.getY());

            });
            System.out.println("!1=" + Aux);
            lienzo.setOnMouseReleased(p -> {
                System.out.println("Me detuve");
                if (Aux != null) {
                    Aux.setX1(x);
                    Aux.setY1(y);
                    Aux.setX2(x2);
                    Aux.setY2(y2);
                    Aux.setX3(x3);
                    Aux.setY3(y3);
                    Aux.setX4(x4);
                    Aux.setY4(y4);
                }
                Figura aux = detectarFigura2((int) p.getX(), (int) p.getY());
                System.out.println("!2=" + aux);
                if (Aux != null && aux == null) {
                    //debe mover la figura
                    System.out.println("Deberia moverse");
                    Aux.dibujar(cuadro, (int) p.getX(), (int) p.getY());
                } else if (Aux != null && aux != null) {
                    //existe una figura en donde se desea colocar la otra
                    System.out.println("No debe moverse");
                    Aux.dibujar(cuadro, (x), y);

                }
                if (Aux == null) {
                    //no existe una figura desde donde se clikeo
                    System.out.println("No hay nada que mover");
                }
                cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());
                repintar(cuadro);

            });

        });

    }

    @FXML
    private void dibujarEtapa(ActionEvent event) throws Exception {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Etapa etapa = new Etapa();
        NotificacionController ventana = new NotificacionController();
        ventana.popUp();
        etapa.setTextoFigura(texto);
        System.out.println("el texto encontrado en la barra es: "+texto);
        System.out.println("el texto que esta en la etapa es: "+etapa.getTextoFigura());
        texto = "";
        click = true;
        activarDrag = false;
        if (click == true) {
            lienzo.setOnMouseClicked(e -> {
                System.out.println("XY: " + e.getX() + "," + e.getY());
                Figura Aux = detectarFigura2((int) e.getX(), (int) e.getY());
                if (click == true && ((e.getX() + 180) < lienzo.getWidth()) && ((e.getY() + 70) < lienzo.getHeight())) {
                    if (Aux == null) {
                        etapa.dibujar(cuadro, (int) e.getX(), (int) e.getY());                       
                        etapa.setNombre("Rectangulo " + numero);
                        numero++;
                        formas.add(etapa);
                        click = false;
                        activarDrag = true;
                    }
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
    }

    @FXML
    private void dibujarEntradaSalida(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        EntradaSalida entrada = new EntradaSalida();
        click = true;
        activarDrag = false;
        if (click == true) {
            lienzo.setOnMouseClicked(e -> {
                Figura Aux = detectarFigura2((int) e.getX(), (int) e.getY());
                System.out.println("XY: " + e.getX() + "," + e.getY());
                if (click == true && ((e.getX() + 190) < lienzo.getWidth()) && ((e.getX() - 70) > 0) && ((e.getY() + 70) < lienzo.getHeight())) {
                    if (Aux == null) {
                        entrada.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                        entrada.setNombre("Rombo " + numero);

                        click = false;
                        formas.add(entrada);
                        activarDrag = true;
                    }
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
                Figura Aux = detectarFigura2((int) e.getX(), (int) e.getY());
                if (click == true && ((e.getX() + 235) < lienzo.getWidth()) && ((e.getX() - 40) > 0) && ((e.getY() + 70) < lienzo.getHeight())) {
                    if (Aux == null) {
                        inicioFin.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                        inicioFin.setNombre("Inicio " + numero);

                        click = false;
                        formas.add(inicioFin);
                        activarDrag = true;
                    }
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
                Figura Aux = detectarFigura2((int) e.getX(), (int) e.getY());
                if (click == true && ((e.getX() + 200) < lienzo.getWidth()) && ((e.getY() + 125) < lienzo.getHeight())) {
                    if (Aux == null) {
                    documento.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    documento.setNombre("Documento " + numero);

                    click = false;
                    formas.add(documento);
                    activarDrag = true;
                    }
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

    NotificacionController ventana = new NotificacionController();

    @FXML
    private void borrarFigura(ActionEvent event) throws Exception {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        borrar = false;
        if (borrar == false) {
            lienzo.setOnMouseClicked(e -> {
                System.out.println("************************************");
                detectarBorrar((int) e.getX(), (int) e.getY());
                borrar = true;
            });
        }
    }

    int d = 1;

    @FXML
    private void dibujarLinea(ActionEvent event) {

        click = true;
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        if (click == true) {
            System.out.println("Click: " + click);
            Flujo linea = new Flujo();
            ArrayList<Punto> puntos = new ArrayList();
            ArrayList<Figura> formasEnlace = new ArrayList();
            lienzo.setOnMouseClicked(e -> {
                if (click == true) {
                    Punto p = new Punto((int) e.getX(), (int) e.getY());
                    Figura Aux = detectarFigura1((int) e.getX(), (int) e.getY());
                    if (Aux != null) {
                        formasEnlace.add(Aux);
                    }
                    puntos.add(p);
                    System.out.println("Ingrese Punto: " + d);
                    d++;
                    if (d > 2 && formasEnlace.size() == 2) {
                        d = 1;
                        Figura p1 = formasEnlace.get(0);
                        Figura p2 = formasEnlace.get(1);
                        linea.dibujar(p1.getX1(), p1.getY1(), p2.getX1(), p2.getY1(), cuadro);
                        enlaces.add(linea);
                        puntos.removeAll(puntos);
                        formasEnlace.removeAll(formasEnlace);
                        click = false;
                    } else if (d > 2) {
                        d = 1;
                        System.out.println("No escogio dos figuras");
                        puntos.removeAll(puntos);
                        formasEnlace.removeAll(formasEnlace);
                        click = false;
                    }
                }
            });

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        moverFigura(cuadro, lienzo);

    }

}
