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
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class FXMLDocumentController implements Initializable {

    ArrayList<Figura> formas = new ArrayList();
    ArrayList<Variable> variables = new ArrayList();
    @FXML
    Button borrarAll;

    @FXML
    AnchorPane root;

    @FXML
    Canvas lienzo;

    @FXML
    Button Etapa;
    @FXML
    Button Decision;

    @FXML
    Button Entrada;

    @FXML
    Button Correr;

    @FXML
    Button Salida;

    @FXML
    Button Ciclo;
    @FXML
    Button Documento;

    ArrayList<Flujo> enlaces = new ArrayList();

    boolean click = false;
    @FXML

    int numero = 0;

    boolean reiniciarHilo = true;

    class hilo implements Runnable {

        @Override
        public void run() {
            GraphicsContext cuadro = lienzo.getGraphicsContext2D();
            for (int i = 0; i < formas.size(); i++) {
                try {
                    Figura corriendo = formas.get(i);
                    if (corriendo instanceof InicioFin) {
                        Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha.png"));
                        cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());
                    }
                    if (corriendo instanceof Etapa) {
                        Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_azul.png"));
                        cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());
                    }
                    if (corriendo instanceof Entrada) {
                        Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_verde.png"));
                        cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());
                    }
                    if (corriendo instanceof Documento) {
                        Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_roja.png"));
                        cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());

                    }
                    if (corriendo instanceof Decision) {
                        Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_morado.png"));
                        cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());
                    }
                    Thread.sleep(2000);
                    cuadro.clearRect(corriendo.getMedioX() - 230, corriendo.getMedioY(), 60, 60);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            reiniciarHilo = true;
        }

    }

    @FXML
    public void correr(ActionEvent event) {
        reiniciarHilo = false;
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Thread a = new Thread(new hilo());
        a.start();

    }

    /**
     * Metodo que se encarga de dibujar todos los objetos en la pantalla del
     * canvas
     *
     * @param cuadro
     */
    public void repintar(GraphicsContext cuadro) {
        cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());
        for (int i = 0; i < enlaces.size(); i++) {
            Flujo enlace = enlaces.get(i);
            enlace.dibujar(enlace.getX(), enlace.getY(), enlace.getX1(), enlace.getY2(), cuadro);
        }
        for (int i = 0; i < formas.size(); i++) {
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

    public Figura detectarFigura1(int x, int y) {
        for (int i = 0; i < formas.size(); i++) {
            Figura aux = formas.get(i);
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
    Figura Aux;

    public void moverEnlaces(Figura move, int x, int y) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        for (int i = 0; i < enlaces.size(); i++) {
            Flujo aux = enlaces.get(i);
            // mover enlace superior
            if (aux.getX1() == move.getMedioX() && aux.getY2() == move.getMedioY()) {
                aux.dibujar(aux.getX(), aux.getY(), x, y, cuadro);
            }//mover enlace inferior.
            if (aux.getX() == move.getMedioX() && aux.getY() == move.getMedioY() + 70) {
                aux.dibujar(x, y + 70, aux.getX1(), aux.getY2(), cuadro);
            }
            enlaces.set(i, aux);
        }
        repintar(cuadro);
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
                        if (p.getY() + 60 >= lienzo.getHeight()) {
                            lienzo.setHeight(lienzo.getHeight() + 70);
                            repintar(cuadro);
                        }
                        if (p.getX() + 200 >= lienzo.getWidth()) {
                            lienzo.setWidth(lienzo.getWidth() + 210);
                            repintar(cuadro);
                        }

                        System.out.println("Me detuve");
                        if (Aux != null) {
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
                            if (Aux != null && b != null && b != Aux) {
                                System.out.println("Entrennnn");
                                if (enlaces.size() != 0) {
                                    for (int t = 0; t < enlaces.size(); t++) {
                                        Flujo link = enlaces.get(t);
                                        // mover enlace superior
                                        if (link.getX1() == (int) p.getX() && link.getY2() == (int) p.getY()) {
                                            link.dibujar(link.getX(), link.getY(), mx, my, cuadro);
                                        }//mover enlace inferior.
                                        if (link.getX() == (int) p.getX() && link.getY() == (int) p.getY() + 70) {
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

    @FXML
    private void dibujarEtapa(ActionEvent event) throws Exception {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Etapa etapa = new Etapa();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Text de etapa.");
        dialog.setHeaderText("");
        dialog.setContentText("Ingrese el texto que va en la etapa:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            etapa.setTextoFigura(result.get());
        }
        //etapa.setTextoFigura(respuestaEtapa);
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
        if (click == true) {
            cut(etapa);
        }
    }

    @FXML
    private void dibujarEntrada(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Entrada entrada = new Entrada();
        //String respuesta = JOptionPane.showInputDialog("Ingrese texto: ");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Text de entrada.");
        dialog.setHeaderText("");
        dialog.setContentText("Ingrese el texto que va en la entrada:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            entrada.setTextoFigura(result.get());
        }
        //entrada.setTextoFigura(respuesta);
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
        Pattern p = Pattern.compile("[A-Za-z]{1,7}\\=[A-Za-z0-9|0-9]{1,7}$");
        Matcher matcher = p.matcher(entrada.getTextoFigura());
        boolean cadenaValida = matcher.matches();
        if (cadenaValida) {
            click = true;
            Variable variable = new Variable();
            int posicion = entrada.getTextoFigura().indexOf("=");
            variable.setNombre(entrada.getTextoFigura().substring(0, posicion));
            variable.setTexto(entrada.getTextoFigura().substring(entrada.getTextoFigura().indexOf("=") + 1));
            variables.add(variable);
        } else {
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Formato.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("El formato ingresado es incorrecto.");

            alert.showAndWait();
        }
        if (click == true) {
            cut(entrada);
        }
        for (int i = 0; i < variables.size(); i++) {
            System.out.println("el nombre de la variable " + (i + 1) + " es: " + variables.get(i).getNombre());
            System.out.println("el texto de la variable " + (i + 1) + " es: " + variables.get(i).getTexto());
        }
    }

    @FXML
    private void dibujarDecision(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Decision decision = new Decision();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Text de etapa.");
        dialog.setHeaderText("");
        dialog.setContentText("Ingrese el texto que va en la decision:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            decision.setTextoFigura(result.get());
        }
        //etapa.setTextoFigura(respuestaEtapa);
        System.out.println("el texto en esta etapa es: " + decision.getTextoFigura());
        //texto = "";
        click = true;

        if (decision.getTextoFigura() == null || decision.getTextoFigura().replaceAll(" ", "").equals("")) {
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("El objeto no puede no tener texto o ser blanco!.");

            alert.showAndWait();

        } else if (decision.getTextoFigura().length() > 15) {
            System.out.println("soy muy grande");
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("La cantidad de caracteres no puede ser mayor a 15!.");

            alert.showAndWait();
        }
        if (click == true) {
            cut(decision);
        }
    }

    @FXML
    private void dibujarDocumento(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Documento documento = new Documento();
        //String respuesta = JOptionPane.showInputDialog("Ingrese texto: ");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Text de documento.");
        dialog.setHeaderText("");
        dialog.setContentText("Ingrese el texto que va en el documento:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            documento.setTextoFigura(result.get());
        }
        //documento.setTextoFigura(respuesta);
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
        if (click == true) {
            cut(documento);
        }
    }

    @FXML
    private void dibujarCiclo(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Ciclo ciclo = new Ciclo();
        //String respuesta = JOptionPane.showInputDialog("Ingrese texto: ");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Text de documento.");
        dialog.setHeaderText("");
        dialog.setContentText("Ingrese el texto que va en el ciclo:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            ciclo.setTextoFigura(result.get());
        }
        //documento.setTextoFigura(respuesta);
        System.out.println("el texto en este documento es: " + ciclo.getTextoFigura());
        click = true;

        if (ciclo.getTextoFigura() == null || ciclo.getTextoFigura().replaceAll(" ", "").equals("")) {
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("El objeto no puede no tener texto o ser blanco!.");

            alert.showAndWait();

        } else if (ciclo.getTextoFigura().length() > 15) {
            System.out.println("soy muy grande");
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("La cantidad de caracteres no puede ser mayor a 15!.");

            alert.showAndWait();
        }
        if (click == true) {
            cut(ciclo);
        }
    }

    @FXML
    private void dibujarSalida(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Salida salida = new Salida();
        //String respuesta = JOptionPane.showInputDialog("Ingrese texto: ");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Text de documento.");
        dialog.setHeaderText("");
        dialog.setContentText("Ingrese el texto que va en la salida:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            salida.setTextoFigura(result.get());
        }
        //documento.setTextoFigura(respuesta);
        System.out.println("el texto en este documento es: " + salida.getTextoFigura());
        click = true;

        if (salida.getTextoFigura() == null || salida.getTextoFigura().replaceAll(" ", "").equals("")) {
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("El objeto no puede no tener texto o ser blanco!.");

            alert.showAndWait();

        } else if (salida.getTextoFigura().length() > 15) {
            System.out.println("soy muy grande");
            click = false;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cantidad de caracteres.");
            alert.setHeaderText("Ocurrio un error.");
            alert.setContentText("La cantidad de caracteres no puede ser mayor a 15!.");

            alert.showAndWait();
        }
        if (click == true) {
            cut(salida);
        }
    }

    public void reConectarFlujo(Figura eliminar) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        int indiceFigura = 0;
        if (formas.size() == 2) {
            Figura inicio = formas.get(0);
            Figura fin = formas.get(1);
            enlaces.clear();
            Flujo nuevo = new Flujo();
            nuevo.dibujar(inicio.getMedioX(), inicio.getMedioY() + 70, fin.getMedioX(), fin.getMedioY(), cuadro);
            enlaces.add(nuevo);
        } else {
            if (eliminar instanceof InicioFin == false) {
                for (int i = 0; i < formas.size(); i++) {
                    if (formas.get(i) == eliminar) {
                        indiceFigura = i;
                        System.out.println("Encontre forma");
                    }
                }
                Figura anterior = formas.get(indiceFigura - 1);
                Figura siguiente = formas.get(indiceFigura + 1);
                Flujo combinacion = new Flujo();
                combinacion.dibujar(anterior.getMedioX(), anterior.getMedioY() + 70, siguiente.getMedioX(), siguiente.getMedioY(), cuadro);
                enlaces.add(combinacion);

            }

        }
    }

    public void borrarFlujos(Figura eliminar) {
        Flujo enlaceSuperior = new Flujo();
        Flujo enlaceInferior = new Flujo();
        if (formas.size() > 2) {
            for (int i = 0; i < enlaces.size(); i++) {
                Flujo fAux = enlaces.get(i);
                if (fAux.getX1() == eliminar.getMedioX() && fAux.getY2() == eliminar.getMedioY()) {
                    enlaceSuperior = enlaces.get(i);
                }
                if (fAux.getX() == eliminar.getMedioX() && fAux.getY() == eliminar.getMedioY() + 70) {
                    enlaceInferior = enlaces.get(i);
                }
            }
            if (enlaceInferior != null && enlaceSuperior != null) {
                enlaces.remove(enlaceSuperior);
                enlaces.remove(enlaceInferior);
            }
        }

    }

    public void detectarBorrar(int x, int y) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Figura eliminar = detectarFigura1(x, y);
        if (eliminar != null) {
            if (eliminar instanceof InicioFin == false) {
                borrarFlujos(eliminar);
                reConectarFlujo(eliminar);
                formas.remove(eliminar);
                repintar(cuadro);
                System.out.println("Borrar");
            }
        } else {
            System.out.println("no hay nada para eliminar");

        }
    }

    @FXML
    Button cut;
    boolean borrar = false;

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
                lienzo.setOnMouseClicked(null);

            });
        }
    }

    @FXML
    private void borrarAll(ActionEvent event) throws Exception {
        formas.clear();
        enlaces.clear();
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        ini();
        repintar(cuadro);
    }

    /**
     * Metodo que se encarga de dibujar una figura dentro de una linea de flujo
     * y separarla en dos.
     *
     * @param n - figura a dibujar.
     */
    public void cut(Figura n) {//Metodo para dentro de un flujo
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
                                nuevo.dibujar(aux.getX(), aux.getY(), o, f, cuadro);
                                aux.dibujar(o, f + 70, aux.getX1(), aux.getY2(), cuadro);
                                n.dibujar(cuadro, o, f);// se dibuja la figura 
                                formas.add(n);
                                //Funcion que ordena la lista con las nuevas figuras
                                Collections.sort(formas, new Comparator<Figura>() {
                                    @Override
                                    public int compare(Figura t, Figura t1) {
                                        return new Integer(t.getY3()).compareTo(t1.getY3());
                                    }
                                });
                                enlaces.set(i, aux);
                                enlaces.add(nuevo);
                                // Se vuelven a dibujar todas las figuras y los enlaces de flujos
                                repintar(cuadro);
                                // se anula la posibilidad de seguir presionando el canvas
                                lienzo.setOnMouseClicked(null);
                                // se detiene el metodo para que no entre a un ciclo infinito.

                                break;

                            }
                        } else {
                            lienzo.setOnMouseClicked(null);

                        }
                    }
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
                if (a.getMedioY() + 70 > n.getMedioY() + 70) {
                    a.dibujar(cuadro, a.getMedioX(), a.getY1() + 70);// se dibuja la figura actual con nuevas coordenadas
                }
            }
        }

    }

    public void bajarFin(Figura fin) {
        for (int i = 0; i < enlaces.size(); i++) {
            Flujo link = enlaces.get(i);
            if (link.getX1() == fin.getMedioX() && link.getY2() == fin.getMedioY()) {

            }
        }

    }

    public void ini() {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Flujo crear = new Flujo();
        boolean validacion = false;
        InicioFin inicio = new InicioFin();
        String respuesta = "";
        while (validacion == false) {
            //respuesta = JOptionPane.showInputDialog("Ingrese texto que va en el inicio: ");
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Text de inicio.");
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
        inicio.dibujar(cuadro, 351, 41);

        InicioFin fin = new InicioFin();
        fin.setTextoFigura("         Fin");
        int g = inicio.getX1();
        int d = inicio.getX2();
        int f = (int) ((g + d) / 2);
        crear.dibujar(351, 111, 351, 400, cuadro);
        fin.dibujar(cuadro, 351, 400);
        inicio.dibujar(cuadro, 351, 41);
        enlaces.add(crear);
        formas.add(inicio);
        formas.add(fin);

        moverFigura(cuadro, lienzo);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ini();
    }

}
