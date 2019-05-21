package proyectov1;

import Clases_Figura.Ciclo;
import Clases_Figura.Decision;
import Clases_Figura.Documento;
import Clases_Figura.Entrada;
import Clases_Figura.Etapa;
import Clases_Figura.Figura;
import Clases_Figura.Flujo;
import Clases_Figura.InicioFin;
import Clases_Figura.Salida;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * Controlador de las funciones principales dentro del programa
 *
 * @author Enfermos
 */
public class FXMLDocumentController implements Initializable {

    private ArrayList<Figura> formas = new ArrayList();// Coleccion de las Figuras que se van creando
    private ArrayList<Variable> variables = new ArrayList();// Coleccion de Variables ingresadas por el usuario
    private ArrayList<Flujo> enlaces = new ArrayList();// Coleccion de enlaces dentro del diagrama
    private boolean click = false; // Booleano que desactiva un boton cuando se hace click
    private boolean reiniciarHilo = true;// Booleano que verifica para 
    private int idFlujos = 0;
    @FXML
    Button borrarAll;
    @FXML
    AnchorPane root;
    @FXML
    TextArea consola;
    @FXML
    Canvas lienzo;
    @FXML
    Button Etapa;
    @FXML
    Button Decision;
    @FXML
    Button Entrada;
    @FXML
    Button moverTodo;
    @FXML
    Button Correr;
    @FXML
    Button Salida;
    @FXML
    Button Ciclo;
    @FXML
    Button Documento;

    /**
     * Clase interna Hilo que implementa Runnable para crear la funcionalidad
     * "Correr" se implementa la clase Abstracta "Run" para ejecutarlo ademas se
     * usa sleep para que tenga una "Descanso" Durante cada iteracion del ciclo.
     */
    class hilo implements Runnable {

        @Override
        public void run() {// Se implementa el Metodo Run

            GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el Lienzo del programa

            Figura aux = formas.get(0);
            System.out.println("Figura: " + formas.get(0).getTextoFigura());
            for (int i = 0; aux.getSiguiente() != -1; i++) {
                for (int j = 0; j < formas.size(); j++) {
                    if (formas.get(j).getID() == aux.getSiguiente()) {
                        System.out.println("Figura: " + formas.get(j).getTextoFigura());
                        aux = formas.get(j);
                        try {
                            Figura corriendo = formas.get(j);
                            if (corriendo instanceof InicioFin) {
                                consola.setText(consola.getText() + "\n" + corriendo.getTextoFigura());

                                Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha.png"));
                                cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());
                            }
                            if (corriendo instanceof Etapa) {
                                consola.setText(consola.getText() + "\n" + "Etapa: " + corriendo.getTextoFigura());

                                Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_azul.png"));
                                cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());
                            }
                            if (corriendo instanceof Entrada) {

                                Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_verde.png"));
                                cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());
                            }
                            if (corriendo instanceof Documento) {
                                consola.setText(consola.getText() + "\n" + "Documento" + corriendo.getTextoFigura());

                                Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_roja.png"));
                                cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());

                            }
                            if (corriendo instanceof Decision) {
                                Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_morado.png"));
                                cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());
                            }
                            if (corriendo instanceof Ciclo) {
                                Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_rosa.png"));
                                cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());
                            }
                            if (corriendo instanceof Salida) {

                                Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_naranja.png"));
                                cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());
                            }
                            Thread.sleep(2000);
                            cuadro.clearRect(corriendo.getMedioX() - 230, corriendo.getMedioY(), 60, 60);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

            }
            reiniciarHilo = true;
        }

    }

    /**
     * Metodo que se encarga de crear un crear un nuevo Thread y ejecutar la
     * funcion correr dentro del diagrama
     *
     * @param event
     */
    @FXML
    private void correr(ActionEvent event) {
        reiniciarHilo = false;// se convierte el Boolean en false para que se pueda ejecutar
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// se declara el lienzo.
        Thread a = new Thread(new hilo());// Se crea un Thread con la clase Hilo como argumento
        a.start();// Se "Ejecuta el Hilo"

    }

    boolean inicio = true;
    int in = 0;
    int yn = 0;

    @FXML
    private void moverDiagrama(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// se declara el lienzo.
        lienzo.setOnMouseDragged(e -> {
            if (inicio == true) {
                System.out.println("Entre a In");
                in = (int) e.getX();
                yn = (int) e.getY();
                System.out.println("Innn: " + in);
                inicio = false;

            } else {
                int diferencia = (int) e.getX();
                int df = diferencia - (in);

                int diferencia2 = (int) e.getY();
                int df2 = diferencia2 - (yn);
                for (int i = 0; i < formas.size(); i++) {
                    Figura a = formas.get(i);
                    System.out.println("In: " + in);
                    System.out.println("Medio: " + (a.getMedioX() + df));
                    System.out.println("Diferencia suma: " + df);

                    for (int j = 0; j < enlaces.size(); j++) {
                        Flujo aux = enlaces.get(j);
                        if (aux.getId() == a.getFlujoInferior()) {
                            aux.dibujar(aux.getX() + df, aux.getY() + df2, aux.getX1(), aux.getY2(), cuadro);
                        }
                        if (aux.getId() == a.getFlujoSuperior()) {
                            aux.dibujar(aux.getX(), aux.getY(), aux.getX1() + df, aux.getY2() + df2, cuadro);

                        }
                    }
                    a.dibujar(cuadro, a.getMedioX() + df, a.getMedioY() + df2);
                    repintar(cuadro);
                    inicio = true;
                }
            }
            lienzo.setOnMouseReleased(t -> {
                lienzo.setOnMouseDragged(null);
                in = 0;
                inicio = true;
            });

        });

    }

    /**
     * Metodo que se encarga de dibujar todos los objetos en la pantalla del
     * canvas
     *
     * @param cuadro
     */
    public void repintar(GraphicsContext cuadro) {
        cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());// Se limpia la pantalla
        for (int i = 0; i < enlaces.size(); i++) {//Se recorre la lista de enlaces
            Flujo enlace = enlaces.get(i);//Se obtiene el enlace de la posicion i
            //Se dibuja el enlace con las coordenadas correspondientes
            enlace.dibujar(enlace.getX(), enlace.getY(), enlace.getX1(), enlace.getY2(), cuadro);
        }
        for (int i = 0; i < formas.size(); i++) {// Se recorre la lista de las figuras
            //Se dibuja la figura con las coordenadas correspondientes
            formas.get(i).dibujar(cuadro, formas.get(i).getMedioX(), formas.get(i).getY1());
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

            }
        }
        return null;
    }

    /**
     * Metodo que detecta una figura en base a una par de coordenadas x e y
     *
     * @param x
     * @param y
     * @return
     */
    public Figura detectarFigura1(int x, int y) {
        for (int i = 0; i < formas.size(); i++) {//Se recorre la lista de las figuras
            Figura aux = formas.get(i);//  se guarda en una figura aux
            if (y >= aux.getY1() && y <= aux.getY3() && x >= aux.getX1() && x <= aux.getX2()) {// se pregunta si las coordenadas estan contenidas dentro de la figura
                return aux;// se retorna la figura.
            } else {
                System.out.println("Espacio Disponible");
            }

        }
        return null;
    }
    int x = 0, x4 = 0, x2 = 0, x3 = 0, y = 0, y4 = 0, y2 = 0, y3 = 0;
    Figura Aux;

    /**
     * Metodo que se encarga de mover los enlaces asociados a una figura en
     * movimiento
     *
     * @param move - Figura en movimiento
     * @param x - coordenada x actual
     * @param y - coordenada y actual
     */
    public void moverEnlaces(Figura move, int x, int y) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// se declara el lienzo
        for (int i = 0; i < enlaces.size(); i++) {// se recorren la lista de enlaces
            Flujo fAux = enlaces.get(i);// se guarda en un enlace auxiliar
            if (fAux.getId() == move.getFlujoSuperior()) {// se pregunta si el ID del Auxiliar es igual al flujo superior de la figura
                fAux.dibujar(fAux.getX(), fAux.getY(), x, y, cuadro);
            }
            if (fAux.getId() == move.getFlujoInferior()) {// se pregunta si el Id del Aux es igual al Flujo inferior del eliminar
                fAux.dibujar(x, y + 70, fAux.getX1(), fAux.getY2(), cuadro);

            }
        }

        repintar(cuadro);// Se vuelven a pintar todos los objetos dentro del canvas
    }

    int mx = 0, my = 0;

    /**
     * Metodo que se encarga de mover las figuras arrastrando el mouse.
     *
     * @param cuadro
     * @param lienzo
     */
    public void moverFigura(GraphicsContext cuadro, Canvas lienzo) {
        lienzo.setOnMousePressed(e -> {
            Aux = detectarFigura1((int) e.getX(), (int) e.getY());
            System.out.println("Aux mover:" + Aux);
            if (Aux != null) {
                if (Aux != null) {
                    x = Aux.getX1();
                    y = Aux.getY1();
                    x2 = Aux.getX2();
                    y2 = Aux.getY2();
                    x3 = Aux.getX3();
                    y3 = Aux.getY3();
                    x4 = Aux.getX4();
                    y4 = Aux.getY4();
                    mx = Aux.getMedioX();
                    my = Aux.getMedioY();
                }
                if (Aux != null) {
                    System.out.println("Entreee");
                    lienzo.setOnMouseDragged(en -> {
                        if (Aux != null && reiniciarHilo == true) {

                            moverEnlaces(Aux, (int) en.getX(), (int) en.getY());
                            cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());
                            Aux.dibujar(cuadro, (int) en.getX(), (int) en.getY());
                            repintar(cuadro);
                        }
                    });
                    lienzo.setOnMouseReleased(p -> {

                        System.out.println("Me detuve");
                        if (Aux != null) {
                            if (Aux.getY3() >= lienzo.getHeight() || Aux.getMedioY() >= lienzo.getHeight()) {
                                lienzo.setHeight(Aux.getY3() + 50);
                                repintar(cuadro);
                            }
                            if (Aux.getX2() >= lienzo.getWidth() || Aux.getX1() >= lienzo.getWidth()) {
                                lienzo.setWidth(Aux.getX2() + 60);
                                repintar(cuadro);
                            }
                            int mp = Aux.getMedioX();
                            int mo = Aux.getMedioY();
                            Aux.setMedioX(mx);
                            Aux.setMedioY(my);
                            Aux.setX1(x);
                            Aux.setY1(y);
                            Aux.setX2(x2);
                            Aux.setY2(y2);
                            Aux.setX3(x3);
                            Aux.setY3(y3);
                            Aux.setX4(x4);
                            Aux.setY4(y4);
                            Figura b = detectarFigura2((int) p.getX(), (int) p.getY());
                            if ((Aux != null && b != null && b != Aux) || (mo < 0 || mo + 90 < 0) || (mp < 0)) {
                                System.out.println("Entrennnn");
                                if (enlaces.size() != 0) {
                                    for (int t = 0; t < enlaces.size(); t++) {
                                        Flujo link = enlaces.get(t);
                                        // mover enlace superior

                                        if (link.getId() == Aux.getFlujoSuperior()) {
                                            link.dibujar(link.getX(), link.getY(), mx, my, cuadro);
                                        }
                                        if (link.getId() == Aux.getFlujoInferior()) {
                                            link.dibujar(mx, my + 70, link.getX1(), link.getY2(), cuadro);

                                        }

                                        enlaces.set(t, link);
                                        repintar(cuadro);

                                    }
                                }

                                Aux.dibujar(cuadro, (mx), my);
                                repintar(cuadro);

                            } else {
                                Aux.dibujar(cuadro, mp, mo);
                                repintar(cuadro);
                            }

                        }
                        Aux = null;

                    });

                } else {
                    System.out.println("Error");
                }
            }
        });

    }

    /**
     * Metodo que se encarga de recibir y validar el texto ingresa por el
     * usuario mediante ventanas emergentes
     *
     * @param crear
     * @param texto
     * @return
     */
    private boolean ingresarTexto(Figura crear, String texto) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Texto de " + texto);
        dialog.setHeaderText("");
        dialog.setContentText("Ingrese el texto que va en " + texto + ":");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            crear.setTextoFigura(result.get());
            System.out.println("Figurita: " + crear.getTextoFigura());
        }
        if (crear.getTextoFigura() == null || crear.getTextoFigura().replaceAll(" ", "").equals("")) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("El objeto no puede no tener texto o ser blanco!.");
            alert.showAndWait();
            return false;

        } else if (crear.getTextoFigura().length() > 60) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("La cantidad de caracteres no puede ser mayor a 15!.");
            alert.showAndWait();
            return false;
        }
        return true;

    }

    /**
     * Metodo que se encarga de dibujar una etapa
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void dibujarEtapa(ActionEvent event) throws Exception {
        Variable variable = new Variable();
        int cantidad = 0;
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Etapa etapa = new Etapa();
        click = ingresarTexto(etapa, "etapa");
        if (click == true) {
            separarFlujo(etapa, cantidad);
        }
    }

    /**
     * Metodo que se encarga de dibujar una entrada
     *
     * @param event
     */
    @FXML
    private void dibujarEntrada(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Entrada entrada = new Entrada();
        int cantidad = 0;
        click = ingresarTexto(entrada, "Entrada");
        String aux = entrada.getTextoFigura();
        System.out.println("Aux: " + aux);
        if (click == true) {
            boolean valida = true;
            Pattern p = Pattern.compile("[A-Za-z]{1,7}\\=[A-Za-z0-9|0-9]{1,7}((\\,)?[A-Za-z]{1,7}\\=[A-Za-z|0-9]{1,7}){0,3}$");
            Matcher matcher = p.matcher(entrada.getTextoFigura());
            boolean cadenaValida = matcher.matches();
            String comas = "";
            int cantidadComas;
            if (cadenaValida) {
                comas = entrada.getTextoFigura().replaceAll("[\\sA-Za-z0-9\\=]", "");
                cantidadComas = comas.length();
                if (cantidadComas == 0) {
                    if (!variables.isEmpty()) {
                        int posicionValidar = entrada.getTextoFigura().indexOf("=");
                        for (int i = 0; i < variables.size(); i++) {
                            if (entrada.getTextoFigura().substring(0, posicionValidar).equals(variables.get(i).getNombre())) {
                                System.out.println("izquierdo = " + entrada.getTextoFigura().substring(0, posicionValidar));
                                System.out.println("la lista tiene: " + (i + 1) + ". " + variables.get(i).getNombre());
                                click = false;
                                valida = false;
                            }
                        }
                        if (valida == false) {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                            ImageView imageVie = new ImageView(images);
                            alert.setGraphic(imageVie);
                            alert.setTitle("Nombre.");
                            alert.setHeaderText("Ocurrio un error.");
                            alert.setContentText("El nombre ingresado ya esta en uso.");
                            alert.showAndWait();
                        }
                    }
                    if (valida) {
                        int posicion = entrada.getTextoFigura().indexOf("=");
                        Variable variable = new Variable();
                        variable.setNombre(entrada.getTextoFigura().substring(0, posicion));
                        variable.setTexto(entrada.getTextoFigura().substring(entrada.getTextoFigura().indexOf("=") + 1));
                        variables.add(variable);
                        cantidad = 1;
                    }
                } else if (cantidadComas == 1) {
                    int lastComa = entrada.getTextoFigura().lastIndexOf(",");
                    int lastEqual = entrada.getTextoFigura().lastIndexOf("=");
                    int firstEqual = entrada.getTextoFigura().indexOf("=");
                    String nombre2 = entrada.getTextoFigura().substring(lastComa + 1, lastEqual);
                    String der2 = entrada.getTextoFigura().substring(lastEqual + 1);
                    String nombre1 = entrada.getTextoFigura().substring(0, firstEqual);
                    String der1 = entrada.getTextoFigura().substring(firstEqual + 1, lastComa);
                    System.out.println("la var1 nombre: " + nombre1);
                    System.out.println("la var1 texto: " + der1);
                    System.out.println("la var2 nombre: " + nombre2);
                    System.out.println("la var2 texto: " + der2);
                    if (nombre1.equals(nombre2)) {
                        click = false;
                        valida = false;
                        Alert alert = new Alert(AlertType.INFORMATION);
                        Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                        ImageView imageVie = new ImageView(images);
                        alert.setGraphic(imageVie);
                        alert.setTitle("Nombre.");
                        alert.setHeaderText("Ocurrio un error.");
                        alert.setContentText("Los nombres ingresados no pueden ser iguales.");
                        alert.showAndWait();
                    }
                    if (!variables.isEmpty()) {
                        for (int i = 0; i < variables.size(); i++) {
                            if (nombre1.equals(variables.get(i).getNombre())) {
                                click = false;
                                valida = false;
                            }
                        }
                        for (int i = 0; i < variables.size(); i++) {
                            if (nombre2.equals(variables.get(i).getNombre())) {
                                click = false;
                                valida = false;
                            }
                        }
                    }
                    if (valida == false) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                        ImageView imageVie = new ImageView(images);
                        alert.setGraphic(imageVie);
                        alert.setTitle("Nombre.");
                        alert.setHeaderText("Ocurrio un error.");
                        alert.setContentText("Alguno de los nombres ingresados ya esta en uso.");
                        alert.showAndWait();
                    }
                    if (valida) {
                        Variable variable1 = new Variable();
                        Variable variable2 = new Variable();
                        variable1.setNombre(nombre1);
                        variable1.setTexto(der1);
                        variable2.setNombre(nombre2);
                        variable2.setTexto(der2);
                        variables.add(variable1);
                        variables.add(variable2);
                        cantidad = 2;
                    }
                } else if (cantidadComas == 2) {
                    int lastComa = entrada.getTextoFigura().lastIndexOf(",");
                    int lastEqual = entrada.getTextoFigura().lastIndexOf("=");
                    int firstEqual = entrada.getTextoFigura().indexOf("=");
                    String nombre3 = entrada.getTextoFigura().substring(lastComa + 1, lastEqual);
                    String der3 = entrada.getTextoFigura().substring(lastEqual + 1);
                    entrada.setTextoFigura(entrada.getTextoFigura().substring(0, lastComa - 1));
                    lastComa = entrada.getTextoFigura().lastIndexOf(",");
                    lastEqual = entrada.getTextoFigura().lastIndexOf("=");
                    firstEqual = entrada.getTextoFigura().indexOf("=");
                    String nombre2 = entrada.getTextoFigura().substring(lastComa + 1, lastEqual);
                    String der2 = entrada.getTextoFigura().substring(lastEqual + 1);
                    String nombre1 = entrada.getTextoFigura().substring(0, firstEqual);
                    String der1 = entrada.getTextoFigura().substring(firstEqual + 1, lastComa);
                    System.out.println("la var1 nombre: " + nombre1);
                    System.out.println("la var1 texto: " + der1);
                    System.out.println("la var2 nombre: " + nombre2);
                    System.out.println("la var2 texto: " + der2);
                    System.out.println("la var3 nombre: " + nombre3);
                    System.out.println("la var3 texto: " + der3);
                    if (nombre1.equals(nombre2) || nombre1.equals(nombre3) || nombre2.equals(nombre3)) {
                        click = false;
                        valida = false;
                        Alert alert = new Alert(AlertType.INFORMATION);
                        Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                        ImageView imageVie = new ImageView(images);
                        alert.setGraphic(imageVie);
                        alert.setTitle("Nombre.");
                        alert.setHeaderText("Ocurrio un error.");
                        alert.setContentText("Los nombres ingresados no pueden ser iguales.");
                        alert.showAndWait();
                    }
                    if (!variables.isEmpty()) {
                        for (int i = 0; i < variables.size(); i++) {
                            if (nombre1.equals(variables.get(i).getNombre())) {
                                click = false;
                                valida = false;
                            }
                        }
                        for (int i = 0; i < variables.size(); i++) {
                            if (nombre2.equals(variables.get(i).getNombre())) {
                                click = false;
                                valida = false;
                            }
                        }
                        for (int i = 0; i < variables.size(); i++) {
                            if (nombre3.equals(variables.get(i).getNombre())) {
                                click = false;
                                valida = false;
                            }
                        }
                    }
                    if (valida == false) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                        ImageView imageVie = new ImageView(images);
                        alert.setGraphic(imageVie);
                        alert.setTitle("Nombre.");
                        alert.setHeaderText("Ocurrio un error.");
                        alert.setContentText("Alguno de los nombres ingresados ya esta en uso.");
                        alert.showAndWait();
                    }
                    if (valida) {
                        Variable variable1 = new Variable();
                        Variable variable2 = new Variable();
                        Variable variable3 = new Variable();
                        variable1.setNombre(nombre1);
                        variable1.setTexto(der1);
                        variable2.setNombre(nombre2);
                        variable2.setTexto(der2);
                        variable3.setNombre(nombre3);
                        variable3.setTexto(der3);
                        variables.add(variable1);
                        variables.add(variable2);
                        variables.add(variable3);
                        cantidad = 3;
                    }
                } else if (cantidadComas == 3) {
                    int lastComa = entrada.getTextoFigura().lastIndexOf(",");
                    int lastEqual = entrada.getTextoFigura().lastIndexOf("=");
                    int firstEqual = entrada.getTextoFigura().indexOf("=");
                    String nombre4 = entrada.getTextoFigura().substring(lastComa + 1, lastEqual);
                    String der4 = entrada.getTextoFigura().substring(lastEqual + 1);
                    entrada.setTextoFigura(entrada.getTextoFigura().substring(0, lastComa));
                    lastComa = entrada.getTextoFigura().lastIndexOf(",");
                    lastEqual = entrada.getTextoFigura().lastIndexOf("=");
                    firstEqual = entrada.getTextoFigura().indexOf("=");
                    String nombre3 = entrada.getTextoFigura().substring(lastComa + 1, lastEqual);
                    String der3 = entrada.getTextoFigura().substring(lastEqual + 1);
                    entrada.setTextoFigura(entrada.getTextoFigura().substring(0, lastComa));
                    lastComa = entrada.getTextoFigura().lastIndexOf(",");
                    lastEqual = entrada.getTextoFigura().lastIndexOf("=");
                    firstEqual = entrada.getTextoFigura().indexOf("=");
                    String nombre2 = entrada.getTextoFigura().substring(lastComa + 1, lastEqual);
                    String der2 = entrada.getTextoFigura().substring(lastEqual + 1);
                    String nombre1 = entrada.getTextoFigura().substring(0, firstEqual);
                    String der1 = entrada.getTextoFigura().substring(firstEqual + 1, lastComa);
                    System.out.println("la var1 nombre: " + nombre1);
                    System.out.println("la var1 texto: " + der1);
                    System.out.println("la var2 nombre: " + nombre2);
                    System.out.println("la var2 texto: " + der2);
                    System.out.println("la var3 nombre: " + nombre3);
                    System.out.println("la var3 texto: " + der3);
                    System.out.println("la var4 nombre: " + nombre4);
                    System.out.println("la var4 texto: " + der4);
                    if (nombre1.equals(nombre2) || nombre1.equals(nombre3) || nombre1.equals(nombre4)) {
                        click = false;
                        valida = false;
                        Alert alert = new Alert(AlertType.INFORMATION);
                        Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                        ImageView imageVie = new ImageView(images);
                        alert.setGraphic(imageVie);
                        alert.setTitle("Nombre.");
                        alert.setHeaderText("Ocurrio un error.");
                        alert.setContentText("Los nombres ingresados no pueden ser iguales.");
                        alert.showAndWait();
                    } else if (nombre2.equals(nombre3) || nombre2.equals(nombre4)) {
                        click = false;
                        valida = false;
                        Alert alert = new Alert(AlertType.INFORMATION);
                        Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                        ImageView imageVie = new ImageView(images);
                        alert.setGraphic(imageVie);
                        alert.setTitle("Nombre.");
                        alert.setHeaderText("Ocurrio un error.");
                        alert.setContentText("Los nombres ingresados no pueden ser iguales.");
                        alert.showAndWait();
                    } else if (nombre3.equals(nombre4)) {
                        click = false;
                        valida = false;
                        Alert alert = new Alert(AlertType.INFORMATION);
                        Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                        ImageView imageVie = new ImageView(images);
                        alert.setGraphic(imageVie);
                        alert.setTitle("Nombre.");
                        alert.setHeaderText("Ocurrio un error.");
                        alert.setContentText("Los nombres ingresados no pueden ser iguales.");
                        alert.showAndWait();
                    }
                    if (!variables.isEmpty()) {
                        for (int i = 0; i < variables.size(); i++) {
                            if (nombre1.equals(variables.get(i).getNombre())) {
                                click = false;
                                valida = false;
                            }
                        }
                        for (int i = 0; i < variables.size(); i++) {
                            if (nombre2.equals(variables.get(i).getNombre())) {
                                click = false;
                                valida = false;
                            }
                        }
                        for (int i = 0; i < variables.size(); i++) {
                            if (nombre3.equals(variables.get(i).getNombre())) {
                                click = false;
                                valida = false;
                            }
                        }
                        for (int i = 0; i < variables.size(); i++) {
                            if (nombre4.equals(variables.get(i).getNombre())) {
                                click = false;
                                valida = false;
                            }
                        }
                    }
                    if (valida == false) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                        ImageView imageVie = new ImageView(images);
                        alert.setGraphic(imageVie);
                        alert.setTitle("Nombre.");
                        alert.setHeaderText("Ocurrio un error.");
                        alert.setContentText("Alguno de los nombres ingresados ya esta en uso.");
                        alert.showAndWait();
                    }
                    if (valida) {
                        Variable variable1 = new Variable();
                        Variable variable2 = new Variable();
                        Variable variable3 = new Variable();
                        Variable variable4 = new Variable();
                        variable1.setNombre(nombre1);
                        variable1.setTexto(der1);
                        variable2.setNombre(nombre2);
                        variable2.setTexto(der2);
                        variable3.setNombre(nombre3);
                        variable3.setTexto(der3);
                        variable4.setNombre(nombre4);
                        variable4.setTexto(der4);
                        variables.add(variable1);
                        variables.add(variable2);
                        variables.add(variable3);
                        variables.add(variable4);
                        cantidad = 4;
                    }
                }
            } else {
                click = false;
                Alert alert = new Alert(AlertType.INFORMATION);
                Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                ImageView imageVie = new ImageView(images);
                alert.setGraphic(imageVie);
                alert.setTitle("Formato.");
                alert.setHeaderText("Ocurrio un error.");
                alert.setContentText("El formato ingresado es incorrecto.");
                alert.showAndWait();
            }

            if (click == true) {
                Variable variable = new Variable();
                System.out.println("Figura antes: " + entrada.getTextoFigura());
                entrada.setTextoFigura(aux);
                separarFlujo(entrada, cantidad);

            }
        }

    }

    @FXML
    private void dibujarDecision(ActionEvent event) {
        int cantidad = 0;
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Decision decision = new Decision();
        click = ingresarTexto(decision, "Decision");
        if (click == true) {
            separarFlujo(decision, cantidad);

        }
    }

    @FXML
    private void dibujarDocumento(ActionEvent event) {
        Variable variable = new Variable();
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Documento documento = new Documento();
        click = ingresarTexto(documento, "documento");
        int cantidad = 0;
        if (click == true) {
            separarFlujo(documento, cantidad);

        }
    }

    /**
     * Metodo que se encarga de dibujar la figura ciclo
     *
     * @param event
     */
    @FXML
    private void dibujarCiclo(ActionEvent event) {
        Variable variable = new Variable();
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Ciclo ciclo = new Ciclo();
        int cantidad = 0;
        click = ingresarTexto(ciclo, "ciclo");
        if (click == true) {
            separarFlujo(ciclo, cantidad);
        }
    }

    /**
     * Metodo que se encarga de dibujar una salida
     *
     * @param event
     */
    @FXML
    private void dibujarSalida(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Variable variable = new Variable();
        Salida salida = new Salida();
        int cantidad = 0;
        click = ingresarTexto(salida, "salida");
        if (click == true) {

            separarFlujo(salida, cantidad);
        }
    }

    /**
     * Metodo que se encarga de unir dos flujos luego de que una figura ha sido
     * eliminada y asi no afecte drasticamente al diagrama
     *
     * @param eliminar
     */
    public void reConectarFlujo(Figura eliminar) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el lienzo
        if (formas.size() == 2) {// se pregunta si solo hay dos objetos 
            Figura inicio = formas.get(0); // Cuando solo hay dos objetos estos son inicioFin
            Figura fin = formas.get(1);
            enlaces.clear();// se limpian la lista de enlaces
            Flujo nuevo = new Flujo();// se crea un nuevo flujo y se conecta al inicioFin
            nuevo.dibujar(inicio.getMedioX(), inicio.getMedioY() + 70, fin.getMedioX(), fin.getMedioY(), cuadro);
            enlaces.add(nuevo);// se agrega a la lista
        } else {
            if (eliminar instanceof InicioFin == false) {// se pregunta si es distinto a InicioFin
                Flujo superior = new Flujo();// Se crean dos objetos flujos.
                Flujo inferior = new Flujo();
                for (int i = 0; i < enlaces.size(); i++) {// se recorren la lista de enlaces
                    Flujo fAux = enlaces.get(i);// se guarda en un enlace auxiliar
                    if (fAux.getId() == eliminar.getFlujoSuperior()) {// se pregunta si el ID del Auxiliar es igual al flujo superior de la figura
                        superior = enlaces.get(i);// se guarda el enlace superior
                    }
                    if (fAux.getId() == eliminar.getFlujoInferior()) {// se pregunta si el Id del Aux es igual al Flujo inferior del eliminar
                        inferior = enlaces.get(i);// Se guarda el enlace inferior
                    }
                }
                // se actualiza el enlace inferior combinando las coordenadas del superior e inferior
                inferior.dibujar(superior.getX(), superior.getY(), inferior.getX1(), inferior.getY2(), cuadro);

                // se recorre la lista de formas
                for (int i = 0; i < formas.size(); i++) {
                    if (formas.get(i).getFlujoInferior() == superior.getId()) {// se pregunta si la id del flujo inferior es igual a la del flujo superior eliminado
                        formas.get(i).setFlujoInferior(inferior.getId());// se actualiza el el flujo inferior por el nuevo flujo combinado
                    }
                }
                if (inferior != null && superior != null) {// se pregunta si se encontraron dos enlaces
                    enlaces.remove(superior);// si entra a la condicion y se elimina el enlace de la lista.
                }
            }

        }
    }

    /**
     * Metodo que se encarga de detectar la figura a eliminar y reconectar los
     * flujos de estas
     *
     * @param x
     * @param y
     */
    public void detectarBorrar(int x, int y) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el lienzo
        Figura eliminar = detectarFigura1(x, y);// se usa el metodo dectectarFigura 1 para encontrar la figura
        if (eliminar != null) {// se entra en la condicion solo si se logra detectar una figura
            if (eliminar instanceof InicioFin == false) {// solo se puede eliminar una figura distinta a un InicioFin
                reConectarFlujo(eliminar);// Se llama al metodo reconectarFlujo
                formas.remove(eliminar);// se elimina la figura detectada
                repintar(cuadro);// se vuelve a pintar todos los elementos en la pantalla
            }
        } else {
            System.out.println("no hay nada para eliminar");

        }
    }

    @FXML
    Button cut;
    boolean borrar = false;

    /**
     * Metodo que se encarga de borrar una figura
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void borrarFigura(ActionEvent event) throws Exception {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el lienzo
        borrar = false;//el boolean borrar se convierte en false para activar el boton
        if (borrar == false) {//La condicion solo funciona si el borrar es igual a false
            lienzo.setOnMouseClicked(e -> {// se usa una funcion lambda junto al evento setOnMouseClicked
                detectarBorrar((int) e.getX(), (int) e.getY());// se llama al metodo detectarBorrar y se le ingresa un x e y
                borrar = true;// el borrar se hace true
                lienzo.setOnMouseClicked(null);// se termina el evento setOnMouseClicked
            });
        }
    }

    /**
     * Metodo que se encarga de borrar el diagrama actual y comenzar uno
     * completamente nuevo
     *
     * @param event
     * @throws Exception
     */
    @FXML
    private void borrarAll(ActionEvent event) throws Exception {
        //Se borran todos los elementos de todas las listas
        formas.clear();
        enlaces.clear();
        variables.clear();
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// se declara el lienzo
        ini();// se llama al metodo ini
        repintar(cuadro);// se vuelven a dibujar todos los objetos
    }

    /**
     * Metodo que se encarga de dibujar una figura dentro de una linea de flujo
     * y separarla en dos.
     *
     * @param n - figura a dibujar.
     */
    public void separarFlujo(Figura n, int variable) {//Metodo para dentro de un flujo
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el cuadro del canvas
        lienzo.setOnMouseClicked(e -> {// se usa una funcion lambda para poder detectar XY de un click
            Figura mover = detectarFigura1((int) e.getX(), (int) e.getY());
            System.out.println(mover);
            if (mover == null) {
                for (int i = 0; i < enlaces.size(); i++) {// se recorre el arreglo de lineas de flujo
                    Flujo aux = enlaces.get(i);// Se guarda el enlace i en una variable auxiliar
                    if ((aux.getX() == aux.getX1()) || aux.getX1() >= aux.getX() - 40 || aux.getX1() <= aux.getX() + 40) {
                        System.out.println("son iguales");
                        if ((int) e.getX() <= aux.getX() + 30 && (int) e.getX() >= aux.getX() - 30) {
                            if ((int) e.getY() >= aux.getY() && (int) e.getY() <= aux.getY2()) {// se pregunta si el xy del Click esta dentro de un enlace

                                // se guardan el X e Y en una variable individual
                                int f = (int) e.getY();
                                int o = (int) e.getX();

                                cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());// se limpia el canvas

                                Flujo nuevo = new Flujo();
                                nuevo.setId(idFlujos);
                                int opcion = 1;
                                int diferenciaY = f - aux.getY();
                                if (diferenciaY < 60) {
                                    nuevo.dibujar(aux.getX(), aux.getY(), o, aux.getY() + 60, cuadro);
                                    aux.dibujar(o, aux.getY() + 130, aux.getX1(), aux.getY2(), cuadro);
                                    n.dibujar(cuadro, o, nuevo.getY2());
                                    opcion = 2;
                                } else {
                                    nuevo.dibujar(aux.getX(), aux.getY(), o, f, cuadro);
                                    aux.dibujar(o, f + 70, aux.getX1(), aux.getY2(), cuadro);
                                    n.dibujar(cuadro, o, f);

                                }
                                n.setFlujoSuperior(nuevo.getId());

                                idFlujos++;
                                n.setFlujoInferior(aux.getId());

                                for (int d = 0; d < formas.size(); d++) {
                                    if (formas.get(d).getFlujoInferior() == aux.getId()) {
                                        formas.get(d).setFlujoInferior(nuevo.getId());
                                    }
                                }
                                n.setID(ids);

                                for (int j = 0; j < formas.size(); j++) {
                                    if (formas.get(j).getFlujoInferior() == nuevo.getId()) {
                                        formas.get(j).setSiguiente(ids);
                                        n.setAnterior(formas.get(j).getID());
                                    }
                                    if (formas.get(j).getFlujoSuperior() == aux.getId()) {
                                        formas.get(j).setAnterior(ids);
                                        n.setSiguiente(formas.get(j).getID());
                                    }

                                }
                                ids++;

                                formas.add(n);
                                bajarFiguras(n, opcion);

                                //Funcion que ordena la lista con las nuevas figuras
                                enlaces.set(i, aux);
                                enlaces.add(nuevo);
                                // Se vuelven a dibujar todas las figuras y los enlaces de flujos
                                repintar(cuadro);
                                // se anula la posibilidad de seguir presionando el canvas
                                lienzo.setOnMouseClicked(null);
                                // se detiene el metodo para que no entre a un ciclo infinito.
                                if (n instanceof Entrada && variable != 0) {
                                    int size = variables.size();
                                    size = size - variable;
                                    for (int j = size; j < variables.size(); j++) {
                                        Variable variable2 = variables.get(j);
                                        consola.setText(consola.getText() + "\n" + variable2.getNombre() + " ← " + variable2.getTexto());

                                    }

                                }
                                break;

                            }
                        } else {
                            if (n instanceof Entrada && variable != 0) {
                                System.out.println("Me caigoooo");
                                variables.remove(variable);
                            }
                            lienzo.setOnMouseClicked(null);

                        }
                    }
                }
            }
        });

    }

    public void bajarFiguras(Figura bajar, int opcion) {
        Figura inicio2 = formas.get(0);

        for (int i = 0; i < formas.size(); i++) {
            if (formas.get(i) == bajar) {
                inicio2 = formas.get(i);
            }
        }
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el cuadro del canvas
        for (int i = 0; i < formas.size(); i++) {
            System.out.println("IDS: " + formas.get(i) + "ID: " + formas.get(i).getID());
        }
        System.out.println("Todas las figuras");
        System.out.println("Nombre: " + inicio2);
        for (int i = 0; inicio2.getSiguiente() != -1; i++) {
            for (int j = 0; j < formas.size(); j++) {
                if (formas.get(j).getID() == inicio2.getSiguiente()) {

                    System.out.println("Nombre:" + formas.get(j));
                    inicio2 = formas.get(j);

                    for (int k = 0; k < enlaces.size(); k++) {
                        if (enlaces.get(k).getId() == inicio2.getFlujoSuperior()) {
                            if (opcion == 2) {
                                enlaces.get(k).dibujar(enlaces.get(k).getX(), enlaces.get(k).getY(), enlaces.get(k).getX1(), enlaces.get(k).getY2() + 130, cuadro);
                            } else {
                                enlaces.get(k).dibujar(enlaces.get(k).getX(), enlaces.get(k).getY(), enlaces.get(k).getX1(), enlaces.get(k).getY2() + 40, cuadro);

                            }
                        }
                        if (enlaces.get(k).getId() == inicio2.getFlujoInferior()) {
                            if (opcion == 2) {
                                enlaces.get(k).dibujar(enlaces.get(k).getX(), enlaces.get(k).getY() + 130, enlaces.get(k).getX1(), enlaces.get(k).getY2(), cuadro);
                            } else {
                                enlaces.get(k).dibujar(enlaces.get(k).getX(), enlaces.get(k).getY() + 40, enlaces.get(k).getX1(), enlaces.get(k).getY2(), cuadro);

                            }
                        }
                    }
                    if (opcion == 2) {
                        inicio2.dibujar(cuadro, inicio2.getMedioX(), inicio2.getMedioY() + 130);
                    } else {
                        inicio2.dibujar(cuadro, inicio2.getMedioX(), inicio2.getMedioY() + 40);

                    }
                    if (inicio2.getMedioY() + 120 > lienzo.getHeight()) {
                        lienzo.setHeight(lienzo.getHeight() + 100);
                    }
                    repintar(cuadro);
                    System.out.println("Siguiente: " + inicio2.getID());
                }
            }
        }
    }

    int ids = 0;

    public void ini() {
        idFlujos = 0;
        ids = 0;
        consola.setText("");
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Flujo crear = new Flujo();
        crear.setId(idFlujos);
        boolean validacion = false;
        InicioFin inicio = new InicioFin();
        String respuesta = "";
        while (validacion == false) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Texto de inicio.");
            Image image = new Image(getClass().getResource("/Clases_Figura/Estilos/Inicio.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            dialog.setGraphic(imageView);
            dialog.setHeaderText("");
            dialog.setContentText("Ingrese el texto que va en el inicio:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                respuesta = result.get();
            }

            validacion = true;
            if (respuesta == null || respuesta.replaceAll(" ", "").equals("")) {
                respuesta = "Inicio";
            } else if (respuesta.length() > 15) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Cantidad de caracteres.");
                alert.setHeaderText("Ocurrio un error.");
                alert.setContentText("La cantidad de caracteres no puede ser mayor a 15!.");
                validacion = false;
                alert.showAndWait();
            }
            Pattern p = Pattern.compile("[a-zA-Z0-9]{1,15}$");
            Matcher matcher = p.matcher(respuesta);
            boolean cadenaValida = matcher.matches();
            if (cadenaValida) {
                validacion = true;
            } else {
                validacion = false;
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Formato.");
                alert.setHeaderText("Ocurrio un error.");
                alert.setContentText("El formato ingresado es incorrecto.");

                alert.showAndWait();
            }
        }
        inicio.textoFigura = respuesta;
        inicio.dibujar(cuadro, 400, 41);

        lienzo.setWidth(933);
        lienzo.setHeight(589);
        InicioFin fin = new InicioFin();
        fin.setFlujoSuperior(idFlujos);
        fin.setFlujoInferior(-1);
        fin.setTextoFigura("         Fin");
        int g = inicio.getX1();
        int d = inicio.getX2();
        int f = (int) ((g + d) / 2);
        crear.dibujar(400, 111, 400, 400, cuadro);
        fin.dibujar(cuadro, 400, 400);
        inicio.dibujar(cuadro, 400, 41);
        inicio.setFlujoInferior(idFlujos);
        inicio.setFlujoSuperior(-2);
        enlaces.add(crear);
        inicio.setID(ids);
        ids++;
        fin.setID(ids);
        ids++;

        inicio.setAnterior(-2);
        inicio.setSiguiente(fin.getID());
        fin.setAnterior(inicio.getID());
        fin.setSiguiente(-1);
        formas.add(inicio);
        formas.add(fin);
        idFlujos++;
        moverFigura(cuadro, lienzo);
        consola.setText("*****Consola Iniciada*****");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ini();
    }

}
