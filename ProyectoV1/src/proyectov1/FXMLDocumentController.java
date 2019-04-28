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
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

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

            formas.get(i).dibujar(lienzo, formas.get(i).getMedioX(), formas.get(i).getY1());
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

    /**
     * Metodo que se encarga de mover las figuras arrastrando el mouse.
     *
     * @param cuadro
     * @param lienzo
     */
    public void moverFigura(GraphicsContext cuadro, Canvas lienzo) {

        lienzo.setOnMousePressed(e -> {
            Figura Aux = detectarFigura1((int) e.getX(), (int) e.getY());
            System.out.println("Cantidad: " + numero);

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
            if (Aux != null) {
                lienzo.setOnMouseDragged(en -> {
                    for (int i = 0; i < enlaces.size(); i++) {
                        Flujo link = enlaces.get(i);
                        if (link.getX() == Aux.getMedioX() && link.getY() == Aux.getMedioY()) {
                            link.dibujar((int) en.getX(), (int) en.getY(), link.getX1(), link.getY2(), cuadro);
                            System.out.println("Entre");
                        } else if (link.getX1() == Aux.getMedioX() && link.getY2() == Aux.getMedioY()) {
                            link.dibujar(link.getX(), link.getY(), (int) en.getX(), (int) en.getY(), cuadro);
                            System.out.println("Entre");
                        } else if (link.getX() == Aux.getMedioX() && link.getY() == Aux.getMedioY() + 70) {
                            link.dibujar((int) en.getX(), (int) en.getY(), link.getX1(), link.getY2(), cuadro);
                            System.out.println("Entre");
                        } else if (link.getX1() == Aux.getMedioX() && link.getY2() == Aux.getMedioY() + 70) {
                            link.dibujar(link.getX(), link.getY(), (int) en.getX(), (int) en.getY(), cuadro);
                            System.out.println("Entre");
                        }
                        enlaces.set(i, link);
                    }

                    cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());
                    Aux.dibujar(cuadro, (int) en.getX(), (int) en.getY());
                    repintar(cuadro);
                });
                lienzo.setOnMouseReleased(p -> {
                    Figura b = detectarFigura1((int) p.getX(), (int) p.getY());
                    int px = (int) p.getX();
                    int py = (int) p.getY();
                    if (b != null) {
                        System.out.println("Superposicion");
                    }
                    if (Aux != null && b != null) {
                        if (py >= b.getY1() && py <= b.getY3() && px >= b.getX1() && px <= b.getX2()) {
                            System.out.println("Sobre mi mismo");
                        } else {
                            System.out.println("Dibujar");
                        }

                    }

                });

            }
        });

    }

    @FXML
    private void dibujarEtapa(ActionEvent event) throws Exception {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Etapa etapa = new Etapa();
        /*
        NotificacionController ventana = new NotificacionController();
        ventana.popUp();
        etapa.setTextoFigura(texto);
         */
        String respuestaEtapa = JOptionPane.showInputDialog("Ingrese texto: ");
        etapa.setTextoFigura(respuestaEtapa);
        System.out.println("el texto en esta etapa es: " + etapa.getTextoFigura());
        //texto = "";
        click = true;

        if (etapa.getTextoFigura() == null || etapa.getTextoFigura().replaceAll(" ", "").equals("")) {
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("El objeto no puede no tener texto o ser blanco!.");

            alert.showAndWait();

        } else if (etapa.getTextoFigura().length() > 15) {
            System.out.println("soy muy grande");
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("La cantidad de caracteres no puede ser mayor a 15!.");

            alert.showAndWait();
        }

        activarDrag = false;
        if (click == true) {
            cut(etapa);
        }
    }

    @FXML
    private void dibujarEntradaSalida(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        EntradaSalida entrada = new EntradaSalida();
        String respuesta = JOptionPane.showInputDialog("Ingrese texto: ");
        entrada.setTextoFigura(respuesta);
        System.out.println("el texto en esta entrada o salida es: " + entrada.getTextoFigura());
        click = true;

        if (entrada.getTextoFigura() == null || entrada.getTextoFigura().replaceAll(" ", "").equals("")) {
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("El objeto no puede no tener texto o ser blanco!.");

            alert.showAndWait();

        } else if (entrada.getTextoFigura().length() > 15) {
            System.out.println("soy muy grande");
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("La cantidad de caracteres no puede ser mayor a 15!.");

            alert.showAndWait();
        }

        activarDrag = false;
        if (click == true) {
            cut(entrada);
        }

    }

    @FXML
    private void dibujarInicioFin(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        InicioFin inicioFin = new InicioFin();
        String respuesta = JOptionPane.showInputDialog("Ingrese texto: ");
        inicioFin.setTextoFigura(respuesta);
        System.out.println("el texto en este inicio o fin es: " + inicioFin.getTextoFigura());
        click = true;

        if (inicioFin.getTextoFigura() == null || inicioFin.getTextoFigura().replaceAll(" ", "").equals("")) {
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("El objeto no puede no tener texto o ser blanco!.");

            alert.showAndWait();

        } else if (inicioFin.getTextoFigura().length() > 15) {
            System.out.println("soy muy grande");
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("La cantidad de caracteres no puede ser mayor a 15!.");

            alert.showAndWait();
        }

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
        String respuesta = JOptionPane.showInputDialog("Ingrese texto: ");
        documento.setTextoFigura(respuesta);
        System.out.println("el texto en este documento es: " + documento.getTextoFigura());
        click = true;

        if (documento.getTextoFigura() == null || documento.getTextoFigura().replaceAll(" ", "").equals("")) {
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("El objeto no puede no tener texto o ser blanco!.");

            alert.showAndWait();

        } else if (documento.getTextoFigura().length() > 15) {
            System.out.println("soy muy grande");
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("La cantidad de caracteres no puede ser mayor a 15!.");

            alert.showAndWait();
        }

        activarDrag = true;
        if (click == true) {
            cut(documento);
        }
    }

    public void detectarBorrar(int x, int y) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        int px = 0;
        int py = 0;
        for (int i = 0; i < formas.size(); i++) {
            Figura aux = formas.get(i);
            if (aux instanceof InicioFin) {
                System.out.println("No se debe borrar");
            } else {
                if (y >= aux.getY1() && y <= aux.getY3()) {
                    if (x >= aux.getX1() && x <= aux.getX2()) {
                        for (int j = 0; j < enlaces.size(); j++) {
                            Flujo b = enlaces.get(j);
                            if (enlaces.get(j).getX() == aux.getMedioX() && enlaces.get(j).getY() == aux.getY1()) {
                                px = b.getX();
                                py = b.getY();
                                System.out.println("Entre 1");
                                enlaces.remove(j);

                            } else if (enlaces.get(j).getX1() == aux.getMedioX() && enlaces.get(j).getY2() == aux.getY1()) {
                                px = b.getX();
                                py = b.getY();
                                System.out.println("Entre 2");
                                enlaces.remove(j);
                            } else if (enlaces.get(j).getX() == aux.getMedioX() && enlaces.get(j).getY() == aux.getMedioY() + 70) {
                                px = b.getX();
                                py = b.getY();
                                System.out.println("Entre 3");
                                enlaces.remove(j+1);
                            } else if (enlaces.get(j).getX1() == aux.getMedioX() && enlaces.get(j).getY2() == aux.getMedioY() + 70) {
                                px = b.getX();
                                py = b.getY();
                                System.out.println("Entre 4");
                                enlaces.remove(j);
                            }
                            
                        }

                        Figura d = formas.get(i - 1);
                        System.out.println("Nueva Funcion");
                        Figura e = formas.get(i + 1);
                        Flujo nuevo = new Flujo();

                        for (int j = 0; j < enlaces.size(); j++) {
                            Flujo auxflujo = enlaces.get(j);
                            if (auxflujo.getY2() == e.getY1() && auxflujo.getX1() == e.getMedioX()) {
                                enlaces.remove(j);
                            }

                        }
                        nuevo.dibujar(d.getMedioX(), d.getMedioY() + 70, e.getMedioX(), e.getMedioY(), cuadro);
                        enlaces.add(nuevo);

                        /*for (int j = 0; j < enlaces.size(); j++) {
                            Flujo a = enlaces.get(j);
                            if(a.getX()==aux.getMedioX()&& a.getY()==aux.getMedioY()+70){
                                enlaces.get(j).dibujar(px, py, a.getX1(), a.getY2(), cuadro);
                            
                            
                            }
                            
                        }*/
                        formas.remove(i);
                        if (formas.size() == 2) {
                            System.out.println("Entrar");
                            enlaces.removeAll(enlaces);
                            InicioFin a = (InicioFin) formas.get(0);
                            InicioFin b = (InicioFin) formas.get(1);
                            Flujo total = new Flujo();
                            System.out.println("Coordenadas: ");
                            total.dibujar(a.getMedioX(), a.getMedioY() + 70, b.getMedioX(), b.getMedioY(), cuadro);
                            enlaces.add(total);
                        }
                        cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());
                        repintar(cuadro);
                    }

                } else {
                    System.out.println("Espacio Disponible");
                }
            }
            lienzo.setOnMouseClicked(null);

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
                int i = 1;
                System.out.println("************************************");
                detectarBorrar((int) e.getX(), (int) e.getY());
                borrar = true;

            });
        }
    }

    @FXML
    private void mouse(ActionEvent event) throws Exception {
        lienzo.setOnMouseClicked(e -> {
        });
    }

    int d = 1;

    @FXML
    private void dibujarLinea(ActionEvent event
    ) {
        /*
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

        }*/
    }

    Flujo c;
    int h = 0;

    /**
     * Metodo que se encarga de dibujar una figura dentro de una linea de flujo
     * y separarla en dos.
     *
     * @param n - figura a dibujar.
     */
    public void cut(Figura n) {//Metodo para dentro de un flujo
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el cuadro del canvas
        lienzo.setOnMouseClicked(e -> {// se usa una funcion lambda para poder detectar XY de un click
            for (int i = 0; i < enlaces.size(); i++) {// se recorre el arreglo de lineas de flujo
                Flujo aux = enlaces.get(i);// Se guarda el enlace i en una variable auxiliar
                if ((int) e.getX() >= aux.getX() && (int) e.getY() >= aux.getY() && (int) e.getY() <= aux.getY2()) {// se pregunta si el xy del Click esta dentro de un enlace
                    c = enlaces.get(i);// se guarda el enlace en una objeto auxiliae
                    h = i;// se guarda el indice

                    // se guardan el X e Y en una variable individual
                    int f = (int) e.getY();
                    int o = (int) e.getX();

                    Flujo p1 = new Flujo();// se declara un nuevo flujo
                    cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());// se limpia el canvas
                    n.dibujar(cuadro, o, f);// se dibuja la figura 

                    //se dibuja un nuevo flujo desde la figura al inicio superior
                    p1.dibujar(c.getX(), c.getY(), (int) e.getX(), (int) e.getY(), cuadro);
                    //El enlace actual se modifica para comenzar desde abajo de la figura creada
                    enlaces.get(i).dibujar(o, f + 70, c.getX1(), c.getY2() + 70, cuadro);

                    //se agrega el nuevo enlace a su lista
                    enlaces.add(p1);

                    // se recorre la nueva lista de enlaces para verificar si algun enlace se sale del 
                    //canvas
                    for (int j = 0; j < enlaces.size(); j++) {
                        Flujo a = enlaces.get(j);
                        // pregunta si el enlace actual es mayor al y del click y si es distinto al enlace actual
                        if (a.getY() >= f && a != enlaces.get(i)) {
                            if (a.getY2() + 70 > lienzo.getHeight()) {// ahora pregunta el punto final del enlace es mayor al del canvas
                                lienzo.setHeight(lienzo.getHeight() + 190);// si ese es el caso el canvas aumenta en 190;
                            }   // luego se dibuja nuevamente el enlace
                            a.dibujar(a.getX(), a.getY() + 70, a.getX1(), a.getY2() + 70, cuadro);
                        }
                    }
                    // se agrega la figura al arreglo de figuras
                    formas.add(n);

                    //Funcion que ordena la lista con las nuevas figuras
                    Collections.sort(formas, new Comparator<Figura>() {
                        @Override
                        public int compare(Figura t, Figura t1) {
                            return new Integer(t.getY3()).compareTo(t1.getY3());
                        }
                    });

                    //luego se mueven las figuras que esten debajo de la figura creada
                    moverabajo(o, f, n);
                    // luego se limpia el canvas
                    cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());
                    // Se vuelven a dibujar todas las figuras y los enlaces de flujos
                    repintar(cuadro);
                    // se anula la posibilidad de seguir presionando el canvas
                    lienzo.setOnMouseClicked(null);
                    // se detiene el metodo para que no entre a un ciclo infinito.
                    break;

                }
            }

        });

    }

    /**
     * Metodo que se encarga de mover todas las figuras hacia abajo en caso de
     * que se dibuje una nueva figura sobre estas.
     *
     * @param x - coordenada x
     * @param y - coordenada y
     * @param n - nueva figura creada
     */
    public void moverabajo(int x, int y, Figura n) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// se declara el espacio del canvas
        for (int i = 0; i < formas.size(); i++) { // se recorre el arreglo de figuras
            Figura a = formas.get(i);// se guarda la figura actual en un 
            // se pregunta si la parte superior de la figura es mayor que el nuevo enlace y la figura es distinta a la nueva
            if (a.getY1() >= y && a != n) {
                if (a.getY1() + 180 > lienzo.getHeight()) {// ahora pregunta si al mover la figura esta se saldria del canvas
                    lienzo.setHeight(lienzo.getHeight() + 190);// si se sale del canvas a este ultimo se le da mas altura

                }
                a.dibujar(cuadro, a.getMedioX(), a.getY1() + 70);// se dibuja la figura actual con nuevas coordenadas
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Flujo crear = new Flujo();
        boolean validacion = false;
        InicioFin inicio = new InicioFin();
        String respuesta = "";
        while (validacion == false) {
            respuesta = JOptionPane.showInputDialog("Ingrese texto que va en el inicio: ");
            validacion = true;
            if (respuesta == null || respuesta.replaceAll(" ", "").equals("")) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Cantidad de caracteres.");
                alert.setHeaderText("Ocurrio un error.");
                alert.setContentText("Inicio no puede no tener texto o ser blanco!.");
                validacion = false;
                alert.showAndWait();
            } else if (respuesta.length() > 15) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Cantidad de caracteres.");
                alert.setHeaderText("Ocurrio un error.");
                alert.setContentText("La cantidad de caracteres no puede ser mayor a 15!.");
                validacion = false;
                alert.showAndWait();
            }
        }
        inicio.textoFigura = respuesta;
        inicio.dibujar(cuadro, 351, 41);

        InicioFin fin = new InicioFin();
        fin.setTextoFigura("         Fin");
        int g = inicio.getX1();
        int d = inicio.getX2();
        int f = (int) ((g + d) / 2);
        crear.dibujar(351, 41, 351, 400, cuadro);
        fin.dibujar(cuadro, 351, 400);
        inicio.dibujar(cuadro, 351, 41);
        enlaces.add(crear);
        formas.add(fin);
        formas.add(inicio);
        moverFigura(cuadro, lienzo);
    }

}
