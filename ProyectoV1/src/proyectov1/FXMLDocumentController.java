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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
import static javafx.application.Application.launch;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Controlador de las funciones principales dentro del programa
 *
 * @author Enfermos
 */
public class FXMLDocumentController implements Initializable {

    public static ArrayList<Figura> formas = new ArrayList();// Coleccion de las Figuras que se van creando
    private ArrayList<Variable> variables = new ArrayList();// Coleccion de Variables ingresadas por el usuario
    public static ArrayList<Flujo> enlaces = new ArrayList();// Coleccion de enlaces dentro del diagrama
    private boolean click = false; // Booleano que desactiva un boton cuando se hace click
    private boolean reiniciarHilo = true;// Booleano que verifica para 
    private int idFlujos = 0;
    @FXML
    Button toPFD;
    @FXML
    Button toPNG;
    @FXML
    Button toJPG;
    @FXML
    Button editarTexto;
    @FXML
    Button correrManual;
    @FXML
    Button toPseudo;

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

    @FXML
    Button undo;

    @FXML
    Button editar;

    /**
     * Clase interna Hilo que implementa Runnable para crear la funcionalidad
     * "Correr" se implementa la clase Abstracta "Run" para ejecutarlo ademas se
     * usa sleep para que tenga una "Descanso" Durante cada iteracion del ciclo.
     */
    class hilo implements Runnable {

        public Boolean resolverCondicional(String condicional) throws ScriptException {
            ArrayList<String> letras = new ArrayList();
            System.out.println("Condicional: " + condicional);
            if (condicional.contains("|")) {
                condicional = condicional.replaceAll("\\|", "+");
            }
            String[] tokens = condicional.replaceAll("\\s+", "").split("(?<=[\\&-+/()><=!])|(?=[\\&-_+/()><=!])");
            for (String token : tokens) {
                letras.add(token);
            }

            for (int i = 0; i < letras.size(); i++) {
                if (letras.get(i).matches("[\\&-_+/()><=!]") == false) {
                    boolean existe = false;
                    for (int j = 0; j < variables.size(); j++) {
                        if (variables.get(j).getNombre().equals(letras.get(i))) {
                            existe = true;
                            if (variables.get(j).getTexto().matches("[0-9]")) {
                                letras.set(i, variables.get(j).getTexto());
                            } else {
                                letras.set(i, "'" + variables.get(j).getTexto() + "'");
                            }
                        }
                    }
                    if (existe == false) {
                        letras.set(i, "0");

                    }
                }
            }
            String h = "";
            for (int i = 0; i < letras.size(); i++) {

                if (letras.get(i).equals("+")) {
                    h = h + "|";

                } else {
                    h = h + letras.get(i);
                }

            }
            System.out.println("Nuevo Condicional: " + h);
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("JavaScript");

            Boolean cumple = (Boolean) engine.eval(h);
            return cumple;
        }

        public void correrEtapa(String texto) throws ScriptException {
            String[] tokens = texto.replaceAll("\\s+", "").split("(?<=[;])|(?=[;])");
            for (int i = 0; i < tokens.length; i++) {
                if (!";".equals(tokens[i])) {
                    System.out.println(">>" + tokens[i]);
                    String expresion = tokens[i];
                    expresion = expresion.replaceAll("=", "_");
                    String[] token = expresion.replaceAll("\\s+", "").split("(?<=[-+*/()_])|(?=[-+*/()_])");
                    Variable m = null;
                    String var = token[0];
                    for (int k = 0; k < variables.size(); k++) {
                        if (variables.get(k).getNombre().equals(var)) {
                            m = variables.get(k);
                        }
                    }

                    String ladoDerecho = "";
                    ArrayList<String> vars = new ArrayList();
                    for (int k = 2; k < token.length; k++) {
                        ladoDerecho = ladoDerecho + token[k];
                        vars.add(token[k]);
                    }

                    System.out.println("Lado Derecho: " + ladoDerecho);
                    for (int d = 0; d < vars.size(); d++) {
                        if (vars.get(d).matches("[\\&-_+/()><=!]") == false) {
                            boolean existe = false;
                            for (int j = 0; j < variables.size(); j++) {
                                if (variables.get(j).getNombre().equals(vars.get(d))) {
                                    existe = true;
                                    if (variables.get(j).getTexto().matches("[0-9]")) {
                                        vars.set(d, variables.get(j).getTexto());
                                    } else {
                                        vars.set(d, "'" + variables.get(j).getTexto() + "'");
                                    }
                                }
                            }
                            if (existe == false) {
                                vars.set(d, "0");

                            }
                        }
                    }

                    String h = "";
                    for (int t = 0; t < vars.size(); t++) {

                        h = h + vars.get(t);

                    }
                    System.out.println(">>H:" + h);
                    ScriptEngineManager mgr = new ScriptEngineManager();
                    ScriptEngine engine = mgr.getEngineByName("JavaScript");
                    if (m != null) {
                        m.setTexto(engine.eval(h).toString());
                        for (int k = 0; k < variables.size(); k++) {
                            if (m.getNombre().equals(variables.get(k).getNombre())) {
                                variables.set(k, m);
                            }
                        }
                        consola.setText(consola.getText() + "\n" + m.getNombre() + " ← " + m.getTexto());

                    }
                }

            }

        }

        public void correrEntrada(Entrada entrada) {
            String expression = entrada.getTextoFigura();
            expression = expression.replaceAll("=", ")");
            if (expression.contains(",")) {
                expression = expression.replaceAll(",", "(");
            }
            String[] tokens = expression.replaceAll("\\s+", "").split("(?<=[-+*/()])|(?=[-+*/()])");
            ArrayList<String> nombresVar = new ArrayList();
            for (int k = 0; k < tokens.length; k++) {
                if (tokens[k].equals(")")) {
                    nombresVar.add(tokens[k - 1]);
                }
            }
            ArrayList<Variable> vars = new ArrayList();
            for (int t = 0; t < nombresVar.size(); t++) {
                for (int k = 0; k < variables.size(); k++) {
                    if (variables.get(k).getNombre().equals(nombresVar.get(t))) {
                        Variable variable = variables.get(k);
                        vars.add(variable);
                    }

                }
            }
            for (int k = 0; k < vars.size(); k++) {
                consola.setText(consola.getText() + "\n" + vars.get(k).getNombre() + " ← " + vars.get(k).getTexto());

            }

        }

        public void correrSalida(Salida aux) {
            String ex = aux.getTextoFigura();
            ex = ex.replaceAll(",", "_");
            String[] tokens = ex.replaceAll("\\s+", "").split("(?<=[-+*/()_])|(?=[-+*/()_])");
            ArrayList<String> tok = new ArrayList();

            for (int k = 0; k < tokens.length; k++) {
                System.out.println(">>Tokens: " + tokens[k]);
                tok.add(tokens[k]);
            }
            String valor = "0";
            if (tokens.length == 3) {
                String var = tok.get(2);

                for (int k = 0; k < variables.size(); k++) {
                    if (var.equals(variables.get(k).getNombre())) {
                        valor = variables.get(k).getTexto();

                    }
                }

                String comillas = tok.get(0).replaceAll("\"", "");

                consola.setText(consola.getText() + "\n" + comillas + valor);
            }

        }

        public Figura correrCiclo(Ciclo ciclo, Figura aux) throws ScriptException {
            if (ciclo.getIdsFiguras().size() > 0) {
                if (ciclo.isVerdadero() == false) {
                    boolean isverdad = resolverCondicional(ciclo.getTextoFigura());
                    if (isverdad == true) {
                        isverdad = false;
                        ciclo.setVerdadero(isverdad);
                    } else {
                        isverdad = true;
                        ciclo.setVerdadero(isverdad);
                    }
                    if (ciclo.isVerdadero() == false) {
                        aux = formas.get(ciclo.getIdsFiguras().get(0));
                        for (int k = 0; k < formas.size(); k++) {
                            if (formas.get(k).getID() == aux.getAnterior()) {
                                aux = formas.get(k);
                            }
                        }

                    }

                }
            }
            return aux;
        }

        public void CorrerDecision(Decision decision) throws ScriptException {
            if (decision.getFalsas().size() > 0 && decision.getVerdaderas().size() > 0) {
                boolean condicion = resolverCondicional(decision.getTextoFigura());
                decision.setVerdadero(condicion);
                int idciclo = 0;
                if (decision.isVerdadero()) {
                    Figura inicio = null;
                    for (int w = 0; w < decision.getVerdaderas().size(); w++) {
                        if (decision.getVerdaderas().get(w).getAnterior() == -8) {
                            inicio = decision.getVerdaderas().get(w);

                        }
                    }
                    if (inicio != null) {
                        for (int k = 0; inicio.getSiguiente() != -9; k++) {
                            for (int l = 0; l < decision.getVerdaderas().size(); l++) {
                                if (decision.getVerdaderas().get(l).getID() == inicio.getSiguiente()) {
                                    inicio = decision.getVerdaderas().get(l);
                                    Figura corriendo3 = inicio;
                                    if (inicio instanceof Etapa) {
                                        correrEtapa(corriendo3.getTextoFigura());
                                        System.out.println(">> Entre a la Etapa");
                                    }
                                    if (inicio instanceof Entrada) {
                                        correrEntrada((Entrada) corriendo3);
                                    }
                                    if (inicio instanceof Ciclo) {
                                        System.out.println(">>Entre al ciclo: " + idciclo);
                                        idciclo++;
                                        inicio = correrCiclo((Ciclo) inicio, corriendo3);

                                    }
                                    if (inicio instanceof Decision) {
                                        CorrerDecision((Decision) corriendo3);
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }

        @Override
        public void run() {// Se implementa el Metodo Run

            GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el Lienzo del programa
            int contador = 0;
            Figura aux = formas.get(0);

            try {
                Figura corriendo = aux;
                Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_naranja.png"));
                cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());

                Thread.sleep(2000);
                cuadro.clearRect(corriendo.getMedioX() - 230, corriendo.getMedioY(), 60, 60);
            } catch (InterruptedException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("Figura: " + formas.get(0).getTextoFigura());
            for (int i = 0; aux.getSiguiente() != -1; i++) {

                for (int j = 0; j < formas.size(); j++) {
                    if (formas.get(j).getID() == aux.getSiguiente()) {
                        System.out.println("Figura: " + formas.get(j).getTextoFigura());
                        aux = formas.get(j);
                        try {
                            Figura corriendo = aux;
                            if (aux instanceof Ciclo) {
                                aux = correrCiclo((Ciclo) corriendo, aux);

                            }
                            if (aux instanceof Etapa) {

                                correrEtapa(aux.getTextoFigura());

                            }
                            if (aux instanceof Salida) {
                                correrSalida((Salida) aux);

                            }
                            if (aux instanceof Entrada) {
                                correrEntrada((Entrada) aux);

                            }
                            if (aux instanceof Decision) {
                                CorrerDecision((Decision) aux);

                            }
                            if (corriendo instanceof Decision == false) {
                                Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_azul.png"));
                                cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());

                                Thread.sleep(2000);
                                cuadro.clearRect(corriendo.getMedioX() - 230, corriendo.getMedioY(), 60, 60);
                            }

                        } catch (InterruptedException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ScriptException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }

            }
            for (int i = 0;
                    i < formas.size();
                    i++) {
                if (formas.get(i) instanceof Ciclo) {
                    Ciclo ciclo = (Ciclo) formas.get(i);
                    ciclo.setVerdadero(false);
                }
            }

            reiniciarHilo = true;

        }

    }

    @FXML
    Button CorrerManual;
    @FXML
    Button CorrerAutomatico;
    @FXML
    Button DetenerCorrer;
    boolean activarSubMenu = true;
    @FXML
    Rectangle subMenu;

    int automatico = 0;

    public ArrayList<String> coloresFondo = new ArrayList();
    public ArrayList<String> coloresBordes = new ArrayList();
    int manual = 0;
    int indiceactual = 0;

    @FXML
    private void correrManual(ActionEvent event) throws ScriptException, InterruptedException {

    }

    @FXML
    private void correrAutomatico(ActionEvent event) {

        consola.setStyle(" -fx-background-color: #FFFB00;  -fx-text-fill:#FFFB00; -fx-control-inner-background:#000000;");

        Thread a = new Thread(new hilo());// Se crea un Thread con la clase Hilo como argumento
        reiniciarHilo = false;// se convierte el Boolean en false para que se pueda ejecutar
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// se declara el lienzo.
        a.start();// Se "Ejecuta el Hilo"
        automatico = 1;
    }

    /**
     * Metodo que se encarga de crear un crear un nuevo Thread y ejecutar la
     * funcion correr dentro del diagrama
     *
     * @param event
     */
    @FXML
    private void correr(ActionEvent event) {
        if (activarSubMenu == true) {
            subMenu.setVisible(true);
            activarSubMenu = false;
            CorrerAutomatico.setVisible(true);
            CorrerManual.setVisible(true);
            DetenerCorrer.setVisible(true);

        } else {
            subMenu.setVisible(false);
            activarSubMenu = true;
            CorrerAutomatico.setVisible(false);
            CorrerManual.setVisible(false);
            DetenerCorrer.setVisible(false);

        }
    }

    public void moverDiagramaDecision(Figura a, int df2, int df) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// se declara el lienzo.
        if (a.getMedioY() + df2 < 0) {
            df2 = 0;
        }
        if ((a.getMedioX() + df) - 100 < 0) {
            df = 0;

        }
        if (a.getMedioY() + df2 + 70 > lienzo.getHeight() || a.getMedioY() + df2 > lienzo.getHeight()) {
            lienzo.setHeight(lienzo.getHeight() + 80);

        }
        if (a.getMedioX() + df + 200 > lienzo.getWidth() || a.getMedioX() + df > lienzo.getWidth()) {
            lienzo.setWidth(lienzo.getWidth() + 130);

        }

        a.dibujar(cuadro, a.getMedioX() + df, a.getMedioY() + df2);
    }

    boolean inicio = true;
    int in = 0;
    int yn = 0;

    @FXML
    private void moverDiagrama(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// se declara el lienzo.
        lienzo.setOnMouseDragged(e -> {
            if (inicio == true) {
                in = (int) e.getX();
                yn = (int) e.getY();
                inicio = false;

            } else {
                int diferencia = (int) e.getX();
                int df = diferencia - (in);
                df = df * 2;
                int diferencia2 = (int) e.getY();
                int df2 = diferencia2 - (yn);
                df2 = df2 * 2;
                for (int i = 0; i < formas.size(); i++) {
                    Figura a = formas.get(i);

                    if (a.getMedioY() + df2 < 0) {
                        df2 = 0;
                    }
                    if ((a.getMedioX() + df) - 100 < 0) {
                        df = 0;

                    }
                    if (a.getMedioY() + df2 + 70 > lienzo.getHeight() || a.getMedioY() + df2 > lienzo.getHeight()) {
                        lienzo.setHeight(lienzo.getHeight() + 80);

                    }
                    if (a.getMedioX() + df + 200 > lienzo.getWidth() || a.getMedioX() + df > lienzo.getWidth()) {
                        lienzo.setWidth(lienzo.getWidth() + 130);

                    }
                    for (int j = 0; j < enlaces.size(); j++) {
                        Flujo aux = enlaces.get(j);
                        if (aux.getId() == a.getFlujoInferior()) {
                            aux.dibujar(aux.getX() + df, aux.getY() + df2, aux.getX1(), aux.getY2(), cuadro);
                        }
                        if (aux.getId() == a.getFlujoSuperior()) {
                            aux.dibujar(aux.getX(), aux.getY(), aux.getX1() + df, aux.getY2() + df2, cuadro);

                        }
                        if (a instanceof Decision) {
                            if (aux.getId() == ((Decision) a).getLadoDerecho().getId()) {
                                aux.dibujar(a.getMedioX() + 180, a.getMedioY() + 30, aux.getX1() + df, aux.getY2() + df2, cuadro);

                            }
                            if (aux.getId() == ((Decision) a).getLadoIzquierdo().getId()) {
                                aux.dibujar(a.getMedioX() - 180, a.getMedioY() + 30, aux.getX1() + df, aux.getY2() + df2, cuadro);

                            }

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
            if (formas.get(i) instanceof Documento) {
                formas.get(i).setBorde(coloresBordes.get(1));
                formas.get(i).setFondo(coloresFondo.get(1));
                formas.get(i).dibujar(cuadro, formas.get(i).getMedioX(), formas.get(i).getY1());

            }
            if (formas.get(i) instanceof InicioFin) {
                formas.get(i).setBorde(coloresBordes.get(0));
                formas.get(i).setFondo(coloresFondo.get(0));
                formas.get(i).dibujar(cuadro, formas.get(i).getMedioX(), formas.get(i).getY1());

            }
            if (formas.get(i) instanceof Salida) {
                formas.get(i).setBorde(coloresBordes.get(2));
                formas.get(i).setFondo(coloresFondo.get(2));
                formas.get(i).dibujar(cuadro, formas.get(i).getMedioX(), formas.get(i).getY1());

            }
            if (formas.get(i) instanceof Entrada) {
                formas.get(i).setBorde(coloresBordes.get(3));
                formas.get(i).setFondo(coloresFondo.get(3));
                formas.get(i).dibujar(cuadro, formas.get(i).getMedioX(), formas.get(i).getY1());

            }
            if (formas.get(i) instanceof Ciclo) {
                formas.get(i).setBorde(coloresBordes.get(4));
                formas.get(i).setFondo(coloresFondo.get(4));
                formas.get(i).dibujar(cuadro, formas.get(i).getMedioX(), formas.get(i).getY1());

            }
            if (formas.get(i) instanceof Decision) {
                formas.get(i).setBorde(coloresBordes.get(5));
                formas.get(i).setFondo(coloresFondo.get(5));
                formas.get(i).dibujar(cuadro, formas.get(i).getMedioX(), formas.get(i).getY1());

            }
            if (formas.get(i) instanceof Etapa) {
                formas.get(i).setBorde(coloresBordes.get(6));
                formas.get(i).setFondo(coloresFondo.get(6));
                formas.get(i).dibujar(cuadro, formas.get(i).getMedioX(), formas.get(i).getY1());

            }
        }
    }

    public Figura detectarFigura2(int x, int y) {
        for (int i = 0; i < formas.size(); i++) {
            Figura aux = formas.get(i);
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
            if (move instanceof Decision) {// mover conexion verdadera de la decision
                for (int j = 0; j < enlaces.size(); j++) {
                    if (enlaces.get(j).getId() == ((Decision) move).getLadoDerecho().getId()) {
                        enlaces.get(j).dibujar(x + 180, y + 30, enlaces.get(j).getX1(), enlaces.get(j).getY2(), cuadro);
                    }
                    if (enlaces.get(j).getId() == ((Decision) move).getLadoIzquierdo().getId()) {
                        enlaces.get(j).dibujar(x - 180, y + 30, enlaces.get(j).getX1(), enlaces.get(j).getY2(), cuadro);

                    }
                }
            }

            if (fAux.getId() == move.getFlujoSuperior()) {// se pregunta si el ID del Auxiliar es igual al flujo superior de la figura
                fAux.dibujar(fAux.getX(), fAux.getY(), x, y, cuadro);
            }
            if (fAux.getId() == move.getFlujoInferior()) {// se pregunta si el Id del Aux es igual al Flujo inferior del eliminar
                if (Aux instanceof Decision) {
                    Decision a = (Decision) Aux;
                    if (a.getTipo() == 1) {
                        fAux.dibujar(x, y + 300, fAux.getX1(), fAux.getY2(), cuadro);
                    } else if (a.getTipo() == 2) {
                        fAux.dibujar(x, y + 150, fAux.getX1(), fAux.getY2(), cuadro);

                    }
                } else {
                    fAux.dibujar(x, y + 70, fAux.getX1(), fAux.getY2(), cuadro);

                }

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
    /**
     * Metodo que se encarga de mover las figuras arrastrando el mouse.
     *
     * @param cuadro
     * @param lienzo
     */
    public void moverFigura(GraphicsContext cuadro, Canvas lienzo) {
        lienzo.setOnMousePressed(e -> {
            Aux = detectarFigura1((int) e.getX(), (int) e.getY());
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
                                            if (Aux instanceof Decision) {
                                                Decision a = (Decision) Aux;
                                                if (a.getTipo() == 1) {
                                                    link.dibujar(mx, my + 300, link.getX1(), link.getY2(), cuadro);
                                                } else if (a.getTipo() == 2) {
                                                    link.dibujar(mx, my + 140, link.getX1(), link.getY2(), cuadro);

                                                }
                                            } else {
                                                link.dibujar(mx, my + 70, link.getX1(), link.getY2(), cuadro);

                                            }

                                        }

                                        enlaces.set(t, link);
                                        repintar(cuadro);

                                    }
                                    if (Aux instanceof Decision) {
                                        Decision a = (Decision) Aux;
                                        for (int i = 0; i < enlaces.size(); i++) {
                                            if (enlaces.get(i).getId() == a.getLadoDerecho().getId()) {
                                                enlaces.get(i).dibujar(mx + 180, my + 30, enlaces.get(i).getX1(), enlaces.get(i).getY2(), cuadro);
                                            }
                                            if (enlaces.get(i).getId() == a.getLadoIzquierdo().getId()) {
                                                enlaces.get(i).dibujar(mx - 180, my + 30, enlaces.get(i).getX1(), enlaces.get(i).getY2(), cuadro);

                                            }
                                        }

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
        TextInputDialog dialog = new TextInputDialog(crear.getTextoFigura());
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/Clases_Figura/Estilos/Alertas.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
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
        if (click) {
            //ejemplo: se ingreso a=(a+b)/2,d=(5+9)*(98/2)
            Pattern p = Pattern.compile("([A-Za-z0-9]+\\=((\\(+|\\)+|\\+|\\-|\\*|\\/|[A-ZA-z0-9]+|[0-9]+))+\\,?)+");
            int cantidadParentesisAbiertos = 0;
            int cantidadParentesisCerrados = 0;
            Matcher matcher = p.matcher(etapa.getTextoFigura());
            boolean cadenaValida = matcher.matches();
            boolean cantidadParentesis = true;
            boolean cantidadOperaciones = true;
            boolean noRepetidos = true;
            //tokenizar a=(a+b)/2,d=(5+9)*(98/2)
            String[] tokensX = etapa.getTextoFigura().replaceAll("\\s+", "").split("(?<=[=,])|(?=[=,])");
            //se imprimen todos los tokens
            //en este caso
            //[a][=][(a+b)/2][,][d][=][(5+9)*(98/2)]
            System.out.println("Se imprimen todos los tokens de la lista.");
            for (String token : tokensX) {
                System.out.println(token);
            }
            //se introducen en un arreglo para manejo mas facil
            ArrayList<String> tokensOriginal = new ArrayList<>();
            for (String token : tokensX) {
                tokensOriginal.add(token);
            }
            //se imprime tokensOriginal que tiene "a","=","(a+b)/2",",","d","=","(5+9)*(98/2)" 
            System.out.println("imprimos el array de tokens antes de evaluar el lado derecho");
            for (int i = 0; i < tokensOriginal.size(); i++) {
                System.out.print("'" + tokensOriginal.get(i) + "', ");
            }
            System.out.println("");
            //ahora tomaremos todos los nombres de variables
            //y las guardamos en nombreVariables
            //nombreVariables = "a","d"
            ArrayList<String> nombreVariables = new ArrayList<>();
            for (int i = 0; i < tokensOriginal.size(); i++) {
                if (tokensOriginal.get(i).equals("=")) {
                    nombreVariables.add(tokensOriginal.get(i - 1));
                }
            }
            //imprimos todos los nombres
            System.out.println("imprimos todos los nombres de variables");
            for (int i = 0; i < nombreVariables.size(); i++) {
                System.out.println("'" + nombreVariables.get(i) + "', ");
            }
            System.out.println("");
            //vemos que los nombres ingresados no sean iguales entre si
            //cantidadVecesRepetido ve cuantas veces se repite un nombre de variable
            //si se repite mas de una vez entonces esta mal
            int cantidadVecesRepetido = 0;
            for (int i = 0; i < nombreVariables.size(); i++) {
                cantidadVecesRepetido = 0;
                for (int j = 0; j < nombreVariables.size(); j++) {
                    if (nombreVariables.get(i).equals(nombreVariables.get(j))) {
                        cantidadVecesRepetido++;
                    }
                }
                if (cantidadVecesRepetido > 1) {
                    System.out.println("HAY NOMBRES REPETIDOS");
                    noRepetidos = false;
                    click = false;
                    i = nombreVariables.size();
                }
            }
            //ya se valido que no hayan nombres repetidos
            //validemos la cantidad de operaciones
            //osea la relacion entre la cantidad de , y la cantidad de etapas.
            int comasPresentes = 0;
            for (int i = 0; i < tokensOriginal.size(); i++) {
                if (tokensOriginal.get(i).equals(",")) {
                    comasPresentes++;
                }
            }
            if (comasPresentes > nombreVariables.size() - 1) {
                System.out.println("LA CANTIDAD DE COMAS Y OPERACIONES NO CALZA");
                cantidadOperaciones = false;
                click = false;
            }
            //ya validamos que la cantidad de operaciones y comas este bien
            //ahora validamos la cantidad de parentesis abiertos y cerrados
            //debe ser la misma cantidad.
            //debemos tomar los lados derechos
            ArrayList<String> ladosDerechosCompletos = new ArrayList<>();
            for (int i = 0; i < tokensOriginal.size(); i++) {
                if (tokensOriginal.get(i).equals("=")) {
                    ladosDerechosCompletos.add(tokensOriginal.get(i + 1));
                }
            }
            //Ahora en ladosDerechosCompletos estan los lados derechos
            //nombreVaribles = "a","d"
            //ladosDerechosCompletos = "(a+b)/2","(5+9)*(98/2)"
            //debemos tokenizar los lados derechos
            ArrayList<ArrayList> arrayDeDerechos = new ArrayList<>();
            for (int i = 0; i < nombreVariables.size(); i++) {
                String[] tokensDerechos = ladosDerechosCompletos.get(i).replaceAll("\\s+", "").split("(?<=[-+/*()])|(?=[-+/*()])");
                ArrayList<String> ladoDerechoTokenizado = new ArrayList<>();
                for (String token : tokensDerechos) {
                    ladoDerechoTokenizado.add(token);
                }
                arrayDeDerechos.add(ladoDerechoTokenizado);
            }
            //ahora tenemos un array llamado arrayDeDerechos, que dentro tiene arrays que cada uno tiene los lados derechos tokenizados
            //imprimamoslo
            System.out.println("IMPRIMIMOS TODOS LOS LADOS DERECHOS TOKENIZADOS");
            for (int i = 0; i < arrayDeDerechos.size(); i++) {
                for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                    System.out.print("'" + arrayDeDerechos.get(i).get(j) + "', ");
                }
                System.out.println("");
            }
            //ahora podemos evaluar que la cantidad de parentesis abiertos sea igual a los cerrados
            for (int i = 0; i < arrayDeDerechos.size(); i++) {
                for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                    if (arrayDeDerechos.get(i).get(j).equals("(")) {
                        cantidadParentesisAbiertos++;
                    }
                }
            }
            for (int i = 0; i < arrayDeDerechos.size(); i++) {
                for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                    if (arrayDeDerechos.get(i).get(j).equals(")")) {
                        cantidadParentesisCerrados++;
                    }
                }
            }
            //nos aseguramos imprimiendo si la cantidad esta bien
            System.out.println("Cantidad de parentesis abiertos: " + cantidadParentesisAbiertos);
            System.out.println("Cantidad de parentesis cerrados: " + cantidadParentesisCerrados);
            if (cantidadParentesisAbiertos != cantidadParentesisCerrados) {
                cantidadParentesis = false;
                click = false;
            }
            if (click == false) {
                Alert alert = new Alert(AlertType.INFORMATION);
                Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                ImageView imageVie = new ImageView(images);
                alert.setGraphic(imageVie);
                alert.setTitle("Error.");
                alert.setHeaderText("Ocurrio un error.");
                alert.setContentText("Alguna de las etapas ingresadas tenia un formato incorrecto.");
                alert.showAndWait();
                click = false;
            }
            //se valido la cantidad de parentesis
            if (cadenaValida && noRepetidos && cantidadOperaciones && cantidadParentesis) {
                System.out.print("etapa.getTextofigura: ");
                System.out.println(etapa.getTextoFigura());
                //Aca deberia estar todo el texto ingresado validado
                //ahora se deben validar todos los lados derechos
                boolean validaLadoDerecho = true;
                //porte del arreglo con arreglos adentro
                //osea la cantidad de etapas ingresadas
                for (int i = 0; i < arrayDeDerechos.size(); i++) {
                    System.out.println("arrayDeDerechos.get(i)");
                    System.out.println(arrayDeDerechos.get(i));
                    //porte del arreglo dentro del arreglo
                    //osea el texto de cada una de las etapas ingresadas
                    if (validaLadoDerecho) {
                        String texto = "";
                        texto = String.join("", arrayDeDerechos.get(i));
                        System.out.println("texto unido: " + texto);
                        //ver que no empieze el lado derecho deturno con un )
                        //variable=)
                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 1");
                            if (arrayDeDerechos.get(i).get(0).equals(")")) {
                                System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                validaLadoDerecho = false;
                            }
                        }
                        //vemos que el lado derecho no sean puras letras o numeros
                        //a=2452
                        //a=popeye
                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 2");
                            if (texto.replaceAll("[A-Za-z0-9]", "").equals("")) {
                                System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                validaLadoDerecho = false;
                            }
                        }
                        //ver que no haya una letra o numero antes de un (
                        //variable=a+b(
                        //variale=a+8(
                        //que hayan simbolos antes es valido
                        //variable=a+b+(2*9)
                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 3");
                            if (arrayDeDerechos.get(i).get(0).equals("(")) {
                                for (int j = 1; j < arrayDeDerechos.get(i).size(); j++) {
                                    if (arrayDeDerechos.get(i).get(j).equals("(")) {
                                        if (arrayDeDerechos.get(i).get((j) - 1).toString().matches("[A-Za-z0-9]")) {
                                            System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                            validaLadoDerecho = false;
                                        }
                                    }
                                }
                            }
                        }
                        //ver que no haya un simbolo justo antes de )
                        //variable=a+b+)
                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 4");
                            for (int j = 1; j < arrayDeDerechos.get(i).size(); j++) {
                                if (arrayDeDerechos.get(i).get(j).equals(")")) {
                                    if (arrayDeDerechos.get(i).get((j) - 1).toString().matches("[\\+\\-\\*\\/]")) {
                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                        validaLadoDerecho = false;
                                    }
                                }
                            }
                        }
                        //valida que no haya ()
                        //variable=()
                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 5");
                            for (int j = 1; j < arrayDeDerechos.get(i).size(); j++) {
                                if (arrayDeDerechos.get(i).get(j).equals(")")) {
                                    if (arrayDeDerechos.get(i).get(j - 1).toString().matches("[\\(]")) {
                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                        validaLadoDerecho = false;
                                    }
                                }
                            }
                        }
                        //valida que no empieze con un simbolo
                        //variable=+
                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 6");
                            if (arrayDeDerechos.get(i).get(0).toString().matches("[\\+\\-\\*\\/]")) {
                                System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                validaLadoDerecho = false;
                            }
                        }
                        //valida que no haya simbolo justo despues de (
                        //variable=(+
                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 7");
                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                if (arrayDeDerechos.get(i).get(j).equals("(")) {
                                    if ((j + 1) < arrayDeDerechos.get(i).size() && arrayDeDerechos.get(i).get(j + 1).toString().matches("[\\+\\-\\*\\/]")) {
                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                        validaLadoDerecho = false;
                                    }
                                }
                            }
                        }
                        //valida que no haya )(
                        //variable=)(
                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 8");
                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                if (arrayDeDerechos.get(i).get(j).equals(")")) {
                                    if ((j + 1) < arrayDeDerechos.get(i).size() && arrayDeDerechos.get(i).get(j + 1).toString().matches("[\\(]")) {
                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                        validaLadoDerecho = false;
                                    }
                                }
                            }
                        }
                        //si el lado derecho empieza con un ( avanzo a la siguiente posicion
                        //si no, veo si la posicion actual es ( luego
                        //pregunto si la anterior era una letra, numero(0-9) o simbolo, si lo era es un error.
                        //si no, transformor el token anterior a numero y pregunto si es mayor a 9
                        //si lo es tiro error.
                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 9");
                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                if (arrayDeDerechos.get(i).get(0).equals("(")) {
                                    j++;
                                } else if (arrayDeDerechos.get(i).get(j).equals("(")) {
                                    if (arrayDeDerechos.get(i).get(j - 1).toString().matches("[A-Za-z0-9\\+\\-\\*\\/]")) {
                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                        validaLadoDerecho = false;
                                    } else if (Integer.parseInt(arrayDeDerechos.get(i).get(j - 1).toString()) > 9) {
                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                        validaLadoDerecho = false;
                                    }
                                }
                            }
                        }
                        //valida que no hayan 2 simbolos seguidos
                        //variable=a+-b
                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 10");
                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                if (arrayDeDerechos.get(i).get(j).toString().matches("[\\+\\-\\*\\/]")) {
                                    if ((j + 1) < arrayDeDerechos.get(i).size() && arrayDeDerechos.get(i).get(j + 1).toString().matches("[\\+\\-\\*\\/]")) {
                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                        validaLadoDerecho = false;
                                    }
                                }
                            }
                        }
                        //valida que la posicion final no sea (
                        //variable=a+b(
                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 11");
                            if (arrayDeDerechos.get(i).get(arrayDeDerechos.get(i).size() - 1).equals("(")) {
                                System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                validaLadoDerecho = false;
                            }
                        }
                        //valida que la posicion final no sea un simbolo
                        //variable=a+b+
                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 12");
                            if (arrayDeDerechos.get(i).get(arrayDeDerechos.get(i).size() - 1).toString().matches("[\\+\\-\\*\\/]")) {
                                System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                validaLadoDerecho = false;
                            }
                        }
                        //aca termine de validar el lado derecho en esta iteracion
                    }
                }
                ArrayList<Integer> posicionesVariablesEnArrayTokens = new ArrayList<>();
                if (validaLadoDerecho) {
                    System.out.println("TODAS LAS VALIDACIONES FUERON CORRECTAS");
                    //aca creo una copia de las variables
                    //para modificar ese
                    //en caso de salir todo bien, limpio las variables y le pego el copiado
                    //en caso de salir algo mal, limpio la copia y no hago nada.
                    ArrayList<Variable> variablesCopia = new ArrayList<>();
                    for (int i = 0; i < variables.size(); i++) {
                        variablesCopia.add(variables.get(i));
                    }
                    //aca ya esta listo, debemos comenzar a crear las variables
                    //pero tenemos que ver el tipo, si son solo numeros, strings o ambos(error).
                    //operaciones fallidas nos dice si debemos modificar el array de variables o no
                    int operacionesFallidas = 0;
                    //creamos arreglos para ayudarnos
                    //en variablesTokensValidar guardamos todas las letras o "variables"
                    ArrayList<String> variablesTokensValidar = new ArrayList<>();
                    //en posicionesVariablesEnArrayTokensValidar guardamos las posiciones
                    // que toman aquellas letras o "variables" en el original
                    ArrayList<Integer> posicionesVariablesEnArrayTokensValidar = new ArrayList<>();
                    ArrayList<String> variablesEnTokens = new ArrayList<>();
                    ArrayList<String> valoresVariables = new ArrayList<>();
                    ArrayList<String> tokensVariables = new ArrayList<>();
                    ArrayList<String> palabrasTokensVariables = new ArrayList<>();
                    for (int i = 0; i < arrayDeDerechos.size(); i++) {
                        //Limpiamos los arreglos al principio de la iteracion
                        //donde se cambia de etapa, para no tomar valores erroneos
                        variablesEnTokens.clear();
                        posicionesVariablesEnArrayTokens.clear();
                        variablesTokensValidar.clear();
                        posicionesVariablesEnArrayTokensValidar.clear();
                        valoresVariables.clear();
                        tokensVariables.clear();
                        palabrasTokensVariables.clear();
                        //IMPRIMOS LOS TOKENS DEL LADO IZQUIERDO PARA ASEGURARSE
                        System.out.println("VEZ " + (i + 1));
                        System.out.println("Tokens derechos: ");
                        for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                            System.out.println(arrayDeDerechos.get(i).get(j));
                        }
                        //imprimos los tokens unidos
                        String unido = String.join("", arrayDeDerechos.get(i));
                        System.out.println("El texto unido: " + unido);
                        int tipo = 0;
                        //ACA PASA ALGO
                        //tipo 1 solo numeros
                        //tipo 2 solo strings
                        //cualquier otra cosa es error.
                        //Vemos cuales de todos los tokens son letras o "variables"
                        for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                            if (!(arrayDeDerechos.get(i).get(j).toString().replaceAll("[0-9\\+\\-\\*\\/\\(\\)]", "").equals(""))) {
                                variablesTokensValidar.add(arrayDeDerechos.get(i).get(j).toString());
                                posicionesVariablesEnArrayTokensValidar.add(j);
                            }
                        }
                        //cantidadDeVariablesAValidar me dice cuantas variables encontre en los tokens
                        //en la etapa que esta de turno
                        int cantidadDeVariablesAValidar = variablesTokensValidar.size();
                        //variable que se usa para ver cuantas variables de tipo string se encontraron
                        int cantidadDeVariablesStringEncontradas = 0;
                        //este ciclo se ejecuta si se encontraron letras o "variables" en la etapa
                        if (!variablesTokensValidar.isEmpty()) {
                            for (int j = 0; j < variablesTokensValidar.size(); j++) {
                                for (int k = 0; k < variablesCopia.size(); k++) {
                                    if (variablesTokensValidar.get(j).equals(variablesCopia.get(k).getNombre())) {
                                        System.out.println(variablesCopia.get(k).getTipo());
                                        if (variablesCopia.get(k).getTipo().equals("texto")) {
                                            cantidadDeVariablesStringEncontradas++;
                                        }
                                    }
                                }
                            }
                        }
                        //
                        variablesTokensValidar.clear();
                        posicionesVariablesEnArrayTokensValidar.clear();
                        System.out.println("cantidad de variables tipo string encontradas");
                        System.out.println(cantidadDeVariablesStringEncontradas);
                        System.out.println("cantidad de variables a validar");
                        System.out.println(cantidadDeVariablesAValidar);
                        if (cantidadDeVariablesStringEncontradas > 0 && cantidadDeVariablesStringEncontradas == cantidadDeVariablesAValidar) {
                            tipo = 1;
                        } else if (cantidadDeVariablesStringEncontradas == 0) {
                            tipo = 0;
                        } else {
                            tipo = 3;
                        }
                        //ACA TERMINA ESE ALGO
                        System.out.println("TIPO = " + tipo);
                        if (tipo == 0) {
                            //solo numeros
                            System.out.println("hora de imprimir todos los tokens en el arreglo para asi reemplazar");
                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                System.out.println("token original: " + arrayDeDerechos.get(i).get(j).toString());
                                System.out.println("token sin num o simbolos: " + arrayDeDerechos.get(i).get(j).toString().replaceAll("[0-9\\+\\-\\*\\/\\(\\)]", ""));
                                if (!(arrayDeDerechos.get(i).get(j).toString().replaceAll("[0-9\\+\\-\\*\\/\\(\\)]", "").equals(""))) {
                                    variablesEnTokens.add(arrayDeDerechos.get(i).get(j).toString());
                                    posicionesVariablesEnArrayTokens.add(j);
                                }
                            }
                            boolean existe = false;
                            if (!variablesEnTokens.isEmpty()) {
                                System.out.println("variablesEnTokens no esta vacio");
                                for (int j = 0; j < variablesEnTokens.size(); j++) {
                                    for (int k = 0; k < variablesCopia.size(); k++) {
                                        //System.out.println(variablesEnTokens.get(i));
                                        //System.out.println("coma");
                                        //System.out.println(variables.get(j).getNombre());
                                        if (variablesEnTokens.get(j).equals(variablesCopia.get(k).getNombre())) {
                                            valoresVariables.add(variablesCopia.get(k).getTexto());
                                            existe = true;
                                        }
                                    }
                                    if (existe == false) {
                                        valoresVariables.add("0");
                                    } else {
                                        existe = false;
                                    }
                                }
                                for (int j = 0; j < posicionesVariablesEnArrayTokens.size(); j++) {
                                    arrayDeDerechos.get(i).set(posicionesVariablesEnArrayTokens.get(j), valoresVariables.get(j));
                                }
                            }
                            variablesEnTokens.clear();
                            posicionesVariablesEnArrayTokens.clear();
                            valoresVariables.clear();
                            int posVariableIgual = 0;
                            System.out.println("el lado derecho esta validado");
                            boolean iguales = false;
                            for (int j = 0; j < variablesCopia.size(); j++) {
                                if (variablesCopia.get(j).getNombre().equals(nombreVariables.get(i))) {
                                    iguales = true;
                                    posVariableIgual = j;
                                }
                            }
                            if (iguales) {
                                System.out.println("ya hay variable con ese nombre");
                                //cuando la variable ya esta en el arreglo
                                ScriptEngineManager mgr = new ScriptEngineManager();
                                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                                //vamos a unir el string
                                unido = String.join("", arrayDeDerechos.get(i));
                                String ecuacion = unido;

                                if (Double.isNaN(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                    variablesCopia.get(posVariableIgual).setTexto("No es un numero.");
                                    variablesCopia.get(posVariableIgual).setTipo("texto");
                                } else if (Double.isInfinite(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                    variablesCopia.get(posVariableIgual).setTexto("Infinito.");
                                    variablesCopia.get(posVariableIgual).setTipo("texto");
                                } else {
                                    variablesCopia.get(posVariableIgual).setTexto(engine.eval(ecuacion).toString());
                                    variablesCopia.get(posVariableIgual).setTipo("numero");
                                }
                                System.out.println(engine.eval(ecuacion));
                                variablesCopia.get(posVariableIgual).setTexto(engine.eval(ecuacion).toString());
                            } else {
                                //cuando la variable no esta en el arreglo
                                System.out.println("no hay variable con ese nombre");
                                Variable variableNueva = new Variable();
                                variableNueva.setNombre(nombreVariables.get(i));
                                ScriptEngineManager mgr = new ScriptEngineManager();
                                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                                //vamos a unir el string
                                unido = String.join("", arrayDeDerechos.get(i));
                                String ecuacion = unido;

                                System.out.println("voy a validar si la ecuacion no da numero como resultado");
                                if (Double.isNaN(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                    variableNueva.setTexto("No es un numero.");
                                    variableNueva.setTipo("texto");
                                    variablesCopia.add(variableNueva);
                                } else {
                                    System.out.println("voy a validar que esto no sea infinito");
                                }
                                if (Double.isInfinite(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                    variableNueva.setTexto("Infinito.");
                                    variableNueva.setTipo("texto");
                                    variablesCopia.add(variableNueva);
                                } else {
                                    System.out.println("voy a validar que esto este normal");
                                }
                                {
                                    variableNueva.setTexto(engine.eval(ecuacion).toString());
                                    variableNueva.setTipo("numero");
                                    variablesCopia.add(variableNueva);
                                }
                                System.out.println(engine.eval(ecuacion));
                            }
                        } else if (tipo == 1) {
                            //solo strings
                            int cantidadDeSumas = 0;
                            int candidadPalabras = 0;
                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                if (arrayDeDerechos.get(i).get(j).equals("+")) {
                                    cantidadDeSumas++;
                                }
                            }
                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                if (!arrayDeDerechos.get(i).get(j).toString().replaceAll("[\\+]", "").equals("")) {
                                    candidadPalabras++;
                                }
                            }
                            if (!unido.replaceAll("[A-Za-z0-9\\+]", "").equals("")) {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                                ImageView imageVie = new ImageView(images);
                                alert.setGraphic(imageVie);
                                alert.setTitle("Error.");
                                alert.setHeaderText("Ocurrio un error.");
                                alert.setContentText("No se puede concatenar strings si hay algun otro simbolo ademas de +.");
                                alert.showAndWait();
                                click = false;
                            } else if (candidadPalabras != (cantidadDeSumas + 1)) {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                                ImageView imageVie = new ImageView(images);
                                alert.setGraphic(imageVie);
                                alert.setTitle("Error.");
                                alert.setHeaderText("Ocurrio un error.");
                                alert.setContentText("Error en el formato.");
                                alert.showAndWait();
                                click = false;
                            } else {
                                //hora de reemplazar y concatenar
                                for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                    if (arrayDeDerechos.get(i).get(j).toString().matches("[A-Za-z0-9]+")) {
                                        tokensVariables.add(arrayDeDerechos.get(i).get(j).toString());
                                    }
                                }
                                for (int j = 0; j < tokensVariables.size(); j++) {
                                    for (int k = 0; k < variablesCopia.size(); k++) {
                                        if (tokensVariables.get(j).equals(variablesCopia.get(k).getNombre())) {
                                            palabrasTokensVariables.add(variablesCopia.get(k).getTexto());
                                        }
                                    }
                                }
                                Variable variableNueva = new Variable();
                                variableNueva.setNombre(nombreVariables.get(i));
                                variableNueva.setTexto(String.join("", palabrasTokensVariables));
                                variableNueva.setTipo("texto");
                                variablesCopia.add(variableNueva);
                                System.out.println(String.join("", palabrasTokensVariables));
                            }
                        } else {
                            //alguna otra cosa(error)
                            operacionesFallidas++;
                            i = arrayDeDerechos.size();
                        }
                    }
                    if (operacionesFallidas > 0) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                        ImageView imageVie = new ImageView(images);
                        alert.setGraphic(imageVie);
                        alert.setTitle("Error.");
                        alert.setHeaderText("Ocurrio un error.");
                        alert.setContentText("Alguna de las etapas ingresadas estaba incorrecta ya que no se estaba trabajando solo con numeros o solo con strings.");
                        alert.showAndWait();
                        click = false;
                        variablesCopia.clear();
                    } else {
                        variables.clear();
                        for (int i = 0; i < variablesCopia.size(); i++) {
                            variables.add(variablesCopia.get(i));
                        }
                        variablesCopia.clear();
                    }
                    //aca deberia terminar la ejecucion de una etapa.
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                    ImageView imageVie = new ImageView(images);
                    alert.setGraphic(imageVie);
                    alert.setTitle("Error.");
                    alert.setHeaderText("Ocurrio un error.");
                    alert.setContentText("Alguna de las etapas ingresadas tenia un formato incorrecto.");
                    alert.showAndWait();
                    click = false;
                }
            }
        }
        if (click == true&& edit==true) {
            System.out.println("imprime todas las variables");
            for (int i = 0; i < variables.size(); i++) {
                System.out.println("variable " + (i + 1) + ". nombre: " + variables.get(i).getNombre() + ". lado derecho: " + variables.get(i).getTexto() + ". tipo: " + variables.get(i).getTipo());
            }
            separarFlujo(etapa, cantidad);

        } else if (edit == false && click==true) {
            System.out.println(">> Entre al editar documento");
            for (int i = 0; i < formas.size(); i++) {
                if (formas.get(i).getID() == idEdit) {
                    formas.remove(i);

                }
            }
            repintar(cuadro);
            etapa.setAnterior(anterior);
            etapa.setSiguiente(siguiente);
            etapa.setID(idEdit);
            etapa.setFlujoInferior(fsiguiente);
            etapa.setFlujoSuperior(fanterios);
            etapa.dibujar(cuadro, xEdit, yEdit);
            edit = true;
            Decision.setDisable(false);
            Ciclo.setDisable(false);
            formas.add(etapa);
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
            Pattern p = Pattern.compile("([A-Za-z0-9]+\\=([0-9]+|[A-Za-z0-9]+)\\,?)+");
            Matcher matcher = p.matcher(entrada.getTextoFigura());
            boolean cadenavalida = matcher.matches();
            String expression = entrada.getTextoFigura();
            String[] tokens = expression.replaceAll("\\s+", "").split("(?<=[=,])|(?=[=,])");
            ArrayList<String> arrayTokens = new ArrayList<>();
            for (String token : tokens) {
                arrayTokens.add(token);
            }
            //imprimos todos los tokens
            System.out.println("tamano: " + arrayTokens.size());
            for (int i = 0; i < arrayTokens.size(); i++) {
                System.out.println(arrayTokens.get(i));
            }
            ArrayList<String> nombresVariables = new ArrayList<>();
            //intentemos tomar todos los nombres de variables
            for (int i = 0; i < arrayTokens.size(); i++) {
                if (arrayTokens.get(i).matches("=")) {
                    nombresVariables.add(arrayTokens.get(i - 1));
                }
            }
            //imprimimos todos los nombres de variables
            for (int i = 0; i < nombresVariables.size(); i++) {
                System.out.println("nombre " + (i + 1) + " " + nombresVariables.get(i));
            }
            ArrayList<Integer> comasTokens = new ArrayList<>();
            //intentamos tomar todas las comas
            for (int i = 0; i < arrayTokens.size(); i++) {
                if (arrayTokens.get(i).matches(",")) {
                    comasTokens.add(i);
                }
            }
            for (int i = 0; i < comasTokens.size(); i++) {
                System.out.println("hay una coma en los tokens en la posicion " + comasTokens.get(i));
            }
            //validamos que la cant de comas sea igual a los nombres de variables -1
            if (comasTokens.size() == (nombresVariables.size() - 1)) {
                cadenavalida = true;
            } else {
                cadenavalida = false;
            }
            //intentamos todos los valores a la derecha
            ArrayList<String> valoresVariablesTokens = new ArrayList<>();
            if (cadenavalida) {
                for (int i = 0; i < arrayTokens.size(); i++) {
                    if (arrayTokens.get(i).matches("=")) {
                        valoresVariablesTokens.add(arrayTokens.get(i + 1));
                    }
                }
            }
            //validamos que no hayan nombres repetidos
            int cantidadRepetida = 0;
            if (cadenavalida) {
                for (int i = 0; i < nombresVariables.size(); i++) {
                    cantidadRepetida = 0;
                    for (int j = 0; j < nombresVariables.size(); j++) {
                        if (nombresVariables.get(i).equals(nombresVariables.get(j))) {
                            cantidadRepetida++;
                        }
                        if (cantidadRepetida > 1) {
                            cadenavalida = false;
                            System.out.println("hay nombres repetidos.");
                        }
                    }
                }
            }
            //aca deberia estar todo bien y listo
            if (cadenavalida) {
                boolean validarNombres;
                for (int i = 0; i < nombresVariables.size(); i++) {
                    validarNombres = false;
                    Variable variableNueva = new Variable();
                    variableNueva.setNombre(nombresVariables.get(i));
                    variableNueva.setTexto(valoresVariablesTokens.get(i));
                    if (variableNueva.getTexto().matches("[0-9]+")) {
                        variableNueva.setTipo("numero");
                    } else {
                        variableNueva.setTipo("texto");
                    }
                    for (int j = 0; j < variables.size(); j++) {
                        if (variableNueva.getNombre().equals(variables.get(j).getNombre())) {
                            validarNombres = true;
                            j = variables.size();
                        }
                    }
                    //
                    if (validarNombres) {
                        System.out.println("encontre variables iguales");
                    }
                    //
                    if (validarNombres) {
                        for (int k = 0; k < variables.size(); k++) {
                            if (variableNueva.getNombre().equals(variables.get(k).getNombre())) {
                                variables.get(k).setNombre(variableNueva.getNombre());
                                variables.get(k).setTexto(variableNueva.getTexto());
                                variables.get(k).setTipo(variableNueva.getTipo());
                                k = variables.size();
                            }
                        }
                    } else {
                        variables.add(variableNueva);
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
            if (click == true && edit == true) {
                Variable variable = new Variable();
                System.out.println("Figura antes: " + entrada.getTextoFigura());
                entrada.setTextoFigura(aux);
                separarFlujo(entrada, cantidad);

            } else if (edit == false && click==true) {
                System.out.println(">> Entre al editar documento");
                for (int i = 0; i < formas.size(); i++) {
                    if (formas.get(i).getID() == idEdit) {
                        formas.remove(i);

                    }
                }
                repintar(cuadro);
                entrada.setAnterior(anterior);
                entrada.setSiguiente(siguiente);
                entrada.setID(idEdit);
                entrada.setFlujoInferior(fsiguiente);
                entrada.setFlujoSuperior(fanterios);
                entrada.dibujar(cuadro, xEdit, yEdit);
                edit = true;
                Decision.setDisable(false);
                Ciclo.setDisable(false);
                formas.add(entrada);
            }
            System.out.println("IMPRIMOS TODAS LAS VARIABLES EXISTENTES");
            for (int i = 0; i < variables.size(); i++) {
                System.out.println("variable " + (i + 1) + " " + variables.get(i).getNombre() + ", " + variables.get(i).getTexto() + ", " + variables.get(i).getTipo());
            }
        }
    }

    @FXML
    private void dibujarDecision(ActionEvent event) {
        int cantidad = 0;
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Decision decision = new Decision();
        click = ingresarTexto(decision, "Decision");
        if (click) {
            boolean valida = true;
            Pattern p = Pattern.compile("([A-Za-z0-9]{1,30}([\\<|\\>]|[\\>|\\<|\\=|\\!]{2})([A-Za-z0-9]|([\\\"true\\\"]+|[\\\"true\\\"])){1,30}([\\<|\\>|\\!|\\=|\\&|\\|]{2})?)+");
            Matcher matcher = p.matcher(decision.getTextoFigura());
            boolean cadenaValida = matcher.matches();
            if (cadenaValida) {
                String[] tokens = decision.getTextoFigura().replaceAll("\\s+", "").split("(?<=[-+/()><=!])|(?=[-+/()><=!])");
                for (String token : tokens) {
                    System.out.println(token);
                }
                ArrayList<String> arrayTokens = new ArrayList<>();
                for (String token : tokens) {
                    arrayTokens.add(token);
                }
                System.out.println("imprimir tokens ciclo");
                for (int i = 0; i < arrayTokens.size(); i++) {
                    System.out.println(arrayTokens.get(i));
                }
                if (arrayTokens.size() == 3) {
                    //valido que el simbolo entremedio no sea un !
                    if (arrayTokens.get(1).equals("!")) {
                        valida = false;
                    }
                } else if (arrayTokens.size() == 4) {
                    //valido que no este presente !>
                    if (arrayTokens.get(1).equals("!")) {
                        if (arrayTokens.get(2).equals(">")) {
                            valida = false;
                        }
                    }
                    //valido que no este presente !<
                    if (arrayTokens.get(1).equals("!")) {
                        if (arrayTokens.get(2).equals("<")) {
                            valida = false;
                        }
                    }
                    //valido que no este 2 simbolos iguales seguidos
                    if (arrayTokens.get(1).equals(arrayTokens.get(2))) {
                        valida = false;
                    }
                    //valido que no este >!
                    if (arrayTokens.get(1).equals(">")) {
                        if (arrayTokens.get(2).equals("!")) {
                            valida = false;
                        }
                    }
                    //valido que no este <!
                    if (arrayTokens.get(1).equals("<")) {
                        if (arrayTokens.get(2).equals("!")) {
                            valida = false;
                        }
                    }
                    //valido que no este ><
                    if (arrayTokens.get(1).equals(">")) {
                        if (arrayTokens.get(2).equals("<")) {
                            valida = false;
                        }
                    }
                    //valido que no este <>
                    if (arrayTokens.get(1).equals("!")) {
                        if (arrayTokens.get(2).equals("<")) {
                            valida = false;
                        }
                    }
                    //valido que no este =!
                    if (arrayTokens.get(1).equals("<")) {
                        if (arrayTokens.get(2).equals(">")) {
                            valida = false;
                        }
                    }
                }
                if (valida) {
                    //falta hacer todo lo que hace el ciclo y esta validado el formato ingresado.
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
        }
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
        if (click == true && edit == true) {
            separarFlujo(documento, cantidad);

        } else if (edit == false) {
            System.out.println(">> Entre al editar documento");
            for (int i = 0; i < formas.size(); i++) {
                if (formas.get(i).getID() == idEdit) {
                    formas.remove(i);

                }
            }
            repintar(cuadro);
            documento.setAnterior(anterior);
            documento.setSiguiente(siguiente);
            documento.setID(idEdit);
            documento.setFlujoInferior(fsiguiente);
            documento.setFlujoSuperior(fanterios);
            documento.dibujar(cuadro, xEdit, yEdit);
            edit = true;
            Decision.setDisable(false);
            Ciclo.setDisable(false);
            formas.add(documento);
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
        if (click) {
            boolean valida = true;
            Pattern p = Pattern.compile("([A-Za-z0-9]{1,30}([\\<|\\>]|[\\>|\\<|\\=|\\!]{2})([A-Za-z0-9]|([\\\"true\\\"]+|[\\\"true\\\"])){1,30}([\\<|\\>|\\!|\\=|\\&|\\|]{2})?)+");
            Matcher matcher = p.matcher(ciclo.getTextoFigura());
            boolean cadenaValida = matcher.matches();
            if (cadenaValida) {
                String[] tokens = ciclo.getTextoFigura().replaceAll("\\s+", "").split("(?<=[-+/()><=!])|(?=[-+/()><=!])");
                for (String token : tokens) {
                    System.out.println(token);
                }
                ArrayList<String> arrayTokens = new ArrayList<>();
                for (String token : tokens) {
                    arrayTokens.add(token);
                }
                System.out.println("imprimir tokens ciclo");
                for (int i = 0; i < arrayTokens.size(); i++) {
                    System.out.println(arrayTokens.get(i));
                }
                if (arrayTokens.size() == 3) {
                    //valido que el simbolo entremedio no sea un !
                    if (arrayTokens.get(1).equals("!")) {
                        valida = false;
                    }
                } else if (arrayTokens.size() == 4) {
                    //valido que no este presente !>
                    if (arrayTokens.get(1).equals("!")) {
                        if (arrayTokens.get(2).equals(">")) {
                            valida = false;
                        }
                    }
                    //valido que no este presente !<
                    if (arrayTokens.get(1).equals("!")) {
                        if (arrayTokens.get(2).equals("<")) {
                            valida = false;
                        }
                    }
                    //valido que no este 2 simbolos iguales seguidos
                    if (arrayTokens.get(1).equals(arrayTokens.get(2))) {
                        valida = false;
                    }
                    //valido que no este >!
                    if (arrayTokens.get(1).equals(">")) {
                        if (arrayTokens.get(2).equals("!")) {
                            valida = false;
                        }
                    }
                    //valido que no este <!
                    if (arrayTokens.get(1).equals("<")) {
                        if (arrayTokens.get(2).equals("!")) {
                            valida = false;
                        }
                    }
                    //valido que no este ><
                    if (arrayTokens.get(1).equals(">")) {
                        if (arrayTokens.get(2).equals("<")) {
                            valida = false;
                        }
                    }
                    //valido que no este <>
                    if (arrayTokens.get(1).equals("!")) {
                        if (arrayTokens.get(2).equals("<")) {
                            valida = false;
                        }
                    }
                    //valido que no este =!
                    if (arrayTokens.get(1).equals("<")) {
                        if (arrayTokens.get(2).equals(">")) {
                            valida = false;
                        }
                    }
                }
                if (valida) {
                    //falta hacer todo lo que hace el ciclo y esta validado el formato ingresado.
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
        }
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
    private void dibujarSalida(ActionEvent event) throws ScriptException {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Variable variable = new Variable();
        Salida salida = new Salida();
        int cantidad = 0;
        click = ingresarTexto(salida, "salida");
        String parentesisAbierto = "";
        int parentesisAbiertos = 0;
        String parentesisCerrado = "";
        int parentesisCerrados = 0;
        String ladoIzquierdo = "";
        String ladoDerecho = "";
        if (click == true) {
            boolean valida = true;
            Pattern p = Pattern.compile("(\\\"[A-Za-z0-9\\ \\+\\-\\*\\/]{1,25}\\\"\\,\\ )((((\\(|\\))|(\\+|\\-|\\/|\\*)|([A-Za-z0-9])){3,40})|([A-Za-z0-9]{1,40}))");
            Matcher matcher = p.matcher(salida.getTextoFigura());
            boolean cadenaValida = matcher.matches();
            if (cadenaValida) {
                parentesisAbierto = salida.getTextoFigura().replaceAll("[\\\"\\,A-Za-z0-9\\ \\+\\-\\*\\/]", "");
                parentesisCerrado = salida.getTextoFigura().replaceAll("[\\\"\\,A-Za-z0-9\\ \\+\\-\\*\\/]", "");
                parentesisAbiertos = parentesisAbierto.length();
                parentesisCerrados = parentesisCerrado.length();
                if (parentesisAbiertos == parentesisCerrados) {
                    int posComa;
                    int posEspacio;
                    posComa = salida.getTextoFigura().indexOf(",");
                    posEspacio = salida.getTextoFigura().lastIndexOf(" ");
                    ladoIzquierdo = salida.getTextoFigura().substring(0, posComa);
                    ladoDerecho = salida.getTextoFigura().substring(posEspacio + 1, salida.getTextoFigura().length());
                    boolean validaLadoDerecho = true;
                    String expression = ladoDerecho;
                    String[] tokens = expression.replaceAll("\\s+", "").split("(?<=[-+/()])|(?=[-+/()])");
                    for (String token : tokens) {
                        System.out.println(token);
                    }
                    ArrayList<String> arrayTokens = new ArrayList<>();
                    for (String token : tokens) {
                        arrayTokens.add(token);
                    }
                    if (arrayTokens.size() == 1) {
                        //trabajo en solo una variable
                        //"el valor de a es",a

                        //vemos si la variable existe o no en el arreglo
                        boolean existeEnLasVariables = false;
                        int posicionVariableIgual = 0;
                        for (int i = 0; i < variables.size(); i++) {
                            if (arrayTokens.get(0).equals(variables.get(i).getNombre())) {
                                posicionVariableIgual = i;
                                i = variables.size();
                                existeEnLasVariables = true;
                            }
                        }
                        if (existeEnLasVariables) {
                            //reemplazar el texto por el valor de la variable
                            ladoDerecho = variables.get(posicionVariableIgual).getTexto();
                            System.out.println("existe en las variables");
                            System.out.println(ladoIzquierdo + " " + ladoDerecho);
                        } else {
                            //reemplazar por 0 el valor de la variable
                            ladoDerecho = Integer.toString(0);
                            System.out.println("no existe en las variables");
                            System.out.println(ladoIzquierdo + " " + ladoDerecho);
                        }
                    } else {
                        //validaciones del lado derecho
                        //ver que no empieze el lado derecho con un )
                        //variable=)
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 1");
                            if (arrayTokens.get(0).equals(")")) {
                                validaLadoDerecho = false;
                            }
                        }
                        //ver que no haya una letra o simbolo antes de un (
                        //variable=a+b(
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 2");
                            if (arrayTokens.get(0).equals("(")) {
                                for (int i = 1; i < arrayTokens.size(); i++) {
                                    if (arrayTokens.get(i).equals("(")) {
                                        if (arrayTokens.get((i) - 1).matches("[A-Za-z0-9]")) {
                                            validaLadoDerecho = false;
                                        }
                                    }
                                }
                            }
                        }
                        //ver que no haya un simbolo justo antes de )
                        //variable=a+b+)
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 3");
                            for (int i = 1; i < arrayTokens.size(); i++) {
                                if (arrayTokens.get(i).equals(")")) {
                                    if (arrayTokens.get((i) - 1).matches("[\\+\\-\\*\\/]")) {
                                        validaLadoDerecho = false;
                                    }
                                }
                            }
                        }
                        //valida que no haya ()
                        //variable=()
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 4");
                            for (int i = 0; i < arrayTokens.size(); i++) {
                                if (arrayTokens.get(i).equals(")")) {
                                    if ((i + 1) < arrayTokens.size() && arrayTokens.get(i + 1).matches("[\\(]")) {
                                        validaLadoDerecho = false;
                                    }
                                }
                            }
                        }
                        //valida que no empieze con un simbolo
                        //variable=+
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 5");
                            if (arrayTokens.get(0).matches("[\\+\\-\\*\\/]")) {
                                validaLadoDerecho = false;
                            }
                        }
                        //valida que no haya simbolo justo despues de (
                        //variable=(+
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 6");
                            for (int i = 0; i < arrayTokens.size(); i++) {
                                if (arrayTokens.get(i).equals("(")) {
                                    if ((i + 1) < arrayTokens.size() && arrayTokens.get(i + 1).matches("[\\+\\-\\*\\/]")) {
                                        validaLadoDerecho = false;
                                    }
                                }
                            }
                        }
                        //valida que no haya )(
                        //variable=)(
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 7");
                            for (int i = 0; i < arrayTokens.size(); i++) {
                                if (arrayTokens.get(i).equals(")")) {
                                    if ((i + 1) < arrayTokens.size() && arrayTokens.get(i + 1).matches("[\\(]")) {
                                        validaLadoDerecho = false;
                                    }
                                }
                            }
                        }
                        //si el lado derecho empieza con un ( avanzo a la siguiente posicion
                        //si no, veo si la posicion actual es ( luego
                        //pregunto si la anterior era una letra, numero(0-9) o simbolo, si lo era es un error.
                        //si no, transformor el token anterior a numero y pregunto si es mayor a 9
                        //si lo es tiro error.
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 8");
                            for (int i = 0; i < arrayTokens.size(); i++) {
                                if (arrayTokens.get(0).equals("(")) {
                                    i++;
                                } else if (arrayTokens.get(i).equals("(")) {
                                    if (arrayTokens.get(i - 1).matches("[A-Za-z0-9\\+\\-\\*\\/]")) {
                                        validaLadoDerecho = false;
                                    } else if (Integer.parseInt(arrayTokens.get(i - 1)) > 9) {
                                        validaLadoDerecho = false;
                                    }
                                }
                            }
                        }
                        //valida que no hayan 2 simbolos seguidos
                        //variable=a+-b
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 9");
                            for (int i = 0; i < arrayTokens.size(); i++) {
                                if (arrayTokens.get(i).matches("[\\+\\-\\*\\/]")) {
                                    if ((i + 1) < arrayTokens.size() && arrayTokens.get(i + 1).matches("[\\+\\-\\*\\/]")) {
                                        validaLadoDerecho = false;
                                    }
                                }
                            }
                        }
                        //valida que la posicion final no sea (
                        //variable=a+b(
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 10");
                            if (arrayTokens.get(arrayTokens.size() - 1).equals("(")) {
                                validaLadoDerecho = false;
                            }
                        }
                        //valida que la posicion final no sea un simbolo
                        //variable=a+b+
                        if (validaLadoDerecho) {
                            System.out.println("Llegue a validacion 11");
                            if (arrayTokens.get(arrayTokens.size() - 1).matches("[\\+\\-\\*\\/]")) {
                                validaLadoDerecho = false;
                            }
                        }
                        if (validaLadoDerecho) {
                            ArrayList<String> variablesEnTokens = new ArrayList<>();
                            ArrayList<Integer> posicionesVariablesEnArrayTokens = new ArrayList<>();
                            ArrayList<String> valoresVariables = new ArrayList<>();
                            for (int i = 0; i < arrayTokens.size(); i++) {
                                System.out.println(arrayTokens.get(i).replaceAll("[0-9\\+\\-\\*\\/\\(\\)]", ""));
                                if (!(arrayTokens.get(i).replaceAll("[0-9\\+\\-\\*\\/\\(\\)]", "").equals(""))) {
                                    variablesEnTokens.add(arrayTokens.get(i));
                                    posicionesVariablesEnArrayTokens.add(i);
                                }
                            }
                            //print
                            System.out.println("variables en tokens");
                            for (int i = 0; i < variablesEnTokens.size(); i++) {
                                System.out.println(variablesEnTokens.get(i));
                            }
                            System.out.println("posiciones variables en array tokens original");
                            for (int i = 0; i < posicionesVariablesEnArrayTokens.size(); i++) {
                                System.out.println(posicionesVariablesEnArrayTokens.get(i));
                            }
                            //
                            boolean existe = false;
                            if (!variablesEnTokens.isEmpty()) {
                                System.out.println("variablesEnTokens no esta vacio");
                                for (int i = 0; i < variablesEnTokens.size(); i++) {
                                    for (int j = 0; j < variables.size(); j++) {
                                        System.out.println(variablesEnTokens.get(i));
                                        System.out.println("coma");
                                        System.out.println(variables.get(j).getNombre());
                                        if (variablesEnTokens.get(i).equals(variables.get(j).getNombre())) {
                                            valoresVariables.add(variables.get(j).getTexto());
                                            existe = true;
                                        }
                                    }
                                    if (existe == false) {
                                        valoresVariables.add("0");
                                    } else {
                                        existe = false;
                                    }
                                }

                                System.out.println("valores de variables en arraytokens");
                                for (int i = 0; i < valoresVariables.size(); i++) {
                                    System.out.println(valoresVariables.get(i));
                                }
                                System.out.println("imprimir los tokens originales");
                                for (int i = 0; i < arrayTokens.size(); i++) {
                                    System.out.println(arrayTokens.get(i));
                                }
                                System.out.println("llege al segundo if");
                                for (int i = 0; i < posicionesVariablesEnArrayTokens.size(); i++) {
                                    arrayTokens.set(posicionesVariablesEnArrayTokens.get(i), valoresVariables.get(i));
                                }
                                System.out.println("imprimir los tokens despues: ");
                                for (int i = 0; i < arrayTokens.size(); i++) {
                                    System.out.println(arrayTokens.get(i));
                                }
                            }
                            variablesEnTokens.clear();
                            posicionesVariablesEnArrayTokens.clear();
                            valoresVariables.clear();
                        }
                        if (validaLadoDerecho) {
                            Variable variableNueva = new Variable();
                            variableNueva.setNombre(ladoIzquierdo);
                            ScriptEngineManager mgr = new ScriptEngineManager();
                            ScriptEngine engine = mgr.getEngineByName("JavaScript");
                            //vamos a unir el string
                            ladoDerecho = String.join("", arrayTokens);
                            String ecuacion = ladoDerecho;
                            if (Double.isNaN(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                variableNueva.setTexto("No es un numero.");
                            } else if (Double.isInfinite(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                variableNueva.setTexto("Infinito.");
                            } else {
                                variableNueva.setTexto(engine.eval(ecuacion).toString());
                            }
                            System.out.println(variableNueva.getNombre() + variableNueva.getTexto());
                        } else {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                            ImageView imageVie = new ImageView(images);
                            alert.setGraphic(imageVie);
                            alert.setTitle("Error.");
                            alert.setHeaderText("Ocurrio un error.");
                            alert.setContentText("El formato ingresado es incorrecto o se usaron variables no declaradas en la operacion.");
                            alert.showAndWait();
                            click = false;
                        }
                    }
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                    ImageView imageVie = new ImageView(images);
                    alert.setGraphic(imageVie);
                    alert.setTitle("Parentesis.");
                    alert.setHeaderText("Ocurrio un error.");
                    alert.setContentText("La cantidad de parentesis abiertos y cerrados no concuerda.");
                    alert.showAndWait();
                    click = false;
                }
            } else {
                p = Pattern.compile("\\\"[A-Za-z0-9]+\\\"");
                matcher = p.matcher(salida.getTextoFigura());
                cadenaValida = matcher.matches();
                if (cadenaValida) {
                    Variable variableNueva = new Variable();
                    variableNueva.setTipo("string");
                    variableNueva.setTexto(salida.getTextoFigura());
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
            }
        }
        if (click == true && edit == true) {
            separarFlujo(salida, cantidad);

        } else if (edit == false && click==true) {
            System.out.println(">> Entre al editar documento");
            for (int i = 0; i < formas.size(); i++) {
                if (formas.get(i).getID() == idEdit) {
                    formas.remove(i);

                }
            }
            repintar(cuadro);
            salida.setAnterior(anterior);
            salida.setSiguiente(siguiente);
            salida.setID(idEdit);
            salida.setFlujoInferior(fsiguiente);
            salida.setFlujoSuperior(fanterios);
            salida.dibujar(cuadro, xEdit, yEdit);
            edit = true;
            Decision.setDisable(false);
            Ciclo.setDisable(false);
            formas.add(salida);
        }
    }

    public void borrarDecision(Decision eliminar) {

        for (int i = 0; i < ((Decision) eliminar).getVerdaderas().size(); i++) {

            if (eliminar.getVerdaderas().get(i) instanceof Decision) {
                borrarDecision((Decision) eliminar.getVerdaderas().get(i));

            }
            reConectarFlujo(((Decision) eliminar).getVerdaderas().get(i));// Se llama al metodo reconectarFlujo
            formas.remove(((Decision) eliminar).getVerdaderas().get(i));// se elimina la figura detectada

        }

        for (int i = 0; i < ((Decision) eliminar).getFalsas().size(); i++) {

            if (eliminar.getFalsas().get(i) instanceof Decision) {
                borrarDecision((Decision) eliminar.getFalsas().get(i));

            }
            reConectarFlujo(((Decision) eliminar).getFalsas().get(i));// Se llama al metodo reconectarFlujo
            formas.remove(((Decision) eliminar).getFalsas().get(i));// se elimina la figura detectada

        }
        for (int i = 0; i < enlaces.size(); i++) {
            if (enlaces.get(i).getDecision() == eliminar.getID()) {
                if (enlaces.get(i).isDerecho()) {
                    enlaces.remove(enlaces.get(i));
                    i = enlaces.size();

                }

            }
        }

        for (int i = 0; i < enlaces.size(); i++) {
            if (enlaces.get(i).getDecision() == eliminar.getID()) {
                if (enlaces.get(i).isDerecho() == false) {
                    enlaces.remove(enlaces.get(i));
                    i = enlaces.size();

                }

            }
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
                if (eliminar instanceof Decision) {
                    borrarDecision((Decision) eliminar);
                }
                if (eliminar instanceof Ciclo) {
                    for (int i = 0; i < ((Ciclo) eliminar).getIdsFiguras().size(); i++) {
                        for (int j = 0; j < formas.size(); j++) {
                            if (formas.get(j).getID() == ((Ciclo) eliminar).getIdsFiguras().get(i)) {
                                if (eliminar instanceof Decision) {
                                    borrarDecision((Decision) eliminar);
                                } else {
                                    reConectarFlujo(formas.get(j));// Se llama al metodo reconectarFlujo
                                    formas.remove(formas.get(j));// 

                                }
                            }
                        }
                    }

                }
                if (eliminar instanceof Entrada) {
                    String expression = eliminar.getTextoFigura();
                    String[] tokens = expression.replaceAll("\\s+", "").split("(?<=[=,])|(?=[=,])");
                    ArrayList<String> arrayTokens = new ArrayList<>();
                    for (String token : tokens) {
                        arrayTokens.add(token);
                    }
                    ArrayList<String> nombresVariables = new ArrayList<>();
                    //intentemos tomar todos los nombres de variables
                    for (int i = 0; i < arrayTokens.size(); i++) {
                        if (arrayTokens.get(i).matches("=")) {
                            nombresVariables.add(arrayTokens.get(i - 1));
                        }
                    }
                    //ciclo para borrar todas las variables que se crearon en la entrada
                    for (int i = 0; i < nombresVariables.size(); i++) {
                        for (int j = 0; j < variables.size(); j++) {
                            if (nombresVariables.get(i).equals(variables.get(j).getNombre())) {
                                variables.remove(j);
                                j = variables.size();
                            }
                        }
                    }
                }
                if (eliminar instanceof Etapa) {
                    String expression = eliminar.getTextoFigura();
                    String[] tokensX = expression.replaceAll("\\s+", "").split("(?<=[=,])|(?=[=,])");
                    ArrayList<String> arrayTokens = new ArrayList<>();
                    for (String token : tokensX) {
                        arrayTokens.add(token);
                    }
                    ArrayList<String> nombresVariables = new ArrayList<>();
                    //intentemos tomar todos los nombres de variables
                    for (int i = 0; i < arrayTokens.size(); i++) {
                        if (arrayTokens.get(i).matches("=")) {
                            nombresVariables.add(arrayTokens.get(i - 1));
                        }
                    }
                    //ciclo para borrar todas las variables que se crearon en la etapa
                    for (int i = 0; i < nombresVariables.size(); i++) {
                        for (int j = 0; j < variables.size(); j++) {
                            if (nombresVariables.get(i).equals(variables.get(j).getNombre())) {
                                variables.remove(j);
                                j = variables.size();
                            }
                        }
                    }
                }
                reConectarFlujo(eliminar);// Se llama al metodo reconectarFlujo
                formas.remove(eliminar);// se elimina la figura detectada
                for (int i = 0; i < formas.size(); i++) {
                    if (formas.get(i) instanceof Decision) {
                        Decision aux = (Decision) formas.get(i);
                        aux.actualizarFlujos();
                        formas.set(i, aux);
                    }
                }

                for (int i = 0; i < formas.size(); i++) {
                    if (formas.get(i) instanceof Ciclo) {
                        Ciclo aux = (Ciclo) formas.get(i);
                        aux.actualizarBloque();
                        formas.set(i, aux);
                    }
                }
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
        cut.setOnMousePressed(t -> {
            GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el lienzo
            borrar = false;//el boolean borrar se convierte en false para activar el boton
            if (borrar == false) {//La condicion solo funciona si el borrar es igual a false
                lienzo.setOnMouseClicked(e -> {// se usa una funcion lambda junto al evento setOnMouseClicked
                    detectarBorrar((int) e.getX(), (int) e.getY());// se llama al metodo detectarBorrar y se le ingresa un x e y
                    borrar = true;// el borrar se hace true
                });
            }

        });

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
        //se activan los botones 
        Decision.setDisable(false);
        Ciclo.setDisable(false);
        edit = true;
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// se declara el lienzo
        ini();// se llama al metodo ini
        repintar(cuadro);// se vuelven a dibujar todos los objetos
    }
    boolean edit = true;
    int xEdit;
    int yEdit;
    int idEdit;
    int anterior;
    int siguiente;
    int fanterios;
    int fsiguiente;

    @FXML
    public void editFigura(ActionEvent event) throws Exception {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el lienzo
        edit = false;//el boolean edit se convierte en false para activar el boton
        if (edit == false) {//La condicion solo funciona si el borrar es igual a false
            lienzo.setOnMouseClicked(e -> {// se usa una funcion lambda junto al evento setOnMouseClicked
                Figura figura = detectarFigura1((int) e.getX(), (int) e.getY());// se llama al metodo detectarFiguray se le ingresa un x e y
                if (figura != null && figura instanceof InicioFin == false && figura instanceof Decision == false && figura instanceof Ciclo == false) {
                    figura.isPressed(cuadro);
                    xEdit = figura.getMedioX();
                    yEdit = figura.getMedioY();
                    System.out.println("Se detecto la figura" + figura.getClass());
                    Decision.setDisable(true);
                    Ciclo.setDisable(true);
                    idEdit = figura.getID();
                    anterior = figura.getAnterior();
                    siguiente = figura.getSiguiente();
                    fanterios = figura.getFlujoSuperior();
                    fsiguiente = figura.getFlujoInferior();

                    edit = false;
                }
                if (figura == null) {
                    System.out.println("No se detecto figura");
                    edit = true;
                }
                lienzo.setOnMouseClicked(null);// se termina el evento setOnMouseClicked
            });
        }
    }

    /**
     * Metodo que se encarga de dibujar una figura dentro de una linea de flujo
     * y separarla en dos.
     *
     * @param n - figura a dibujar.
     * @param variable
     */
    public void separarFlujo(Figura n, int variable) {//Metodo para dentro de un flujo
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el cuadro del canvas
        lienzo.setOnMouseClicked(e -> {// se usa una funcion lambda para poder detectar XY de un click
            Figura mover = detectarFigura1((int) e.getX(), (int) e.getY());
            if (mover == null) {
                for (int i = 0; i < enlaces.size(); i++) {// se recorre el arreglo de lineas de flujo
                    Flujo aux = enlaces.get(i);// Se guarda el enlace i en una variable auxiliar
                    if ((int) e.getX() <= aux.getX() + 40 && (int) e.getX() >= aux.getX() - 40 && (int) e.getY() >= aux.getY() && (int) e.getY() <= aux.getY2()) {// se pregunta si el xy del Click esta dentro de un enlace
                        Decision condicional = new Decision();
                        cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());// se limpia el canvas
                        // se guardan el X e Y en una variable individual
                        int f = (int) e.getY();
                        int o = (int) e.getX();
                        //Preguntar si el flujo es parte de una decision
                        if (aux.getDecision() >= 0) {
                            //Buscar la figura decision enlazada al flujo;
                            for (int j = 0; j < formas.size(); j++) {
                                if (formas.get(j).getID() == aux.getDecision()) {
                                    condicional = (Decision) formas.get(j);//Asignar condicional
                                    System.out.println(">> Encontre una decision");
                                }
                            }

                            Flujo nuevo = new Flujo();
                            nuevo.setColor(n.getColor());

                            nuevo.setId(idFlujos);
                            if (n instanceof Decision) {
                                Flujo derecho = new Flujo();
                                Flujo izquierdo = new Flujo();
                                derecho.setColor("#01be9b");
                                izquierdo.setDecision(ids);
                                izquierdo.setColor("#ff0025");
                                derecho.dibujar(o + 180, f + 30, o + 180, f + 150, cuadro);
                                izquierdo.dibujar(o - 180, f + 30, o - 180, f + 150, cuadro);
                                idFlujos++;
                                ((Decision) n).setTipo(2);
                                derecho.setId(idFlujos);
                                idFlujos++;
                                izquierdo.setId(idFlujos);
                                idFlujos++;

                                derecho.setDerecho(true);
                                derecho.setDecision(ids);
                                izquierdo.setDerecho(false);
                                enlaces.add(derecho);
                                enlaces.add(izquierdo);
                                ((Decision) n).setLadoIzquierdo(izquierdo);
                                ((Decision) n).setLadoDerecho(derecho);
                                ((Decision) n).setFinalDerecho(derecho);
                                ((Decision) n).setFinalIzquierdo(izquierdo);
                            }

                            nuevo.setDecision(condicional.getID());
                            int opcion = 1;
                            int diferenciaY = f - aux.getY();
                            int diferenciaX = (aux.getY() + aux.getY2()) / 2;
                            boolean enCiclo = false;
                            if (f > diferenciaX) {
                                enCiclo = true;
                                System.out.println("En el ciclo");
                            } else if (f < diferenciaX) {
                                enCiclo = false;
                                System.out.println("Sobre el ciclo");

                            }
                            if (n instanceof Decision) {
                                nuevo.dibujar(aux.getX(), aux.getY(), o, f, cuadro);
                                aux.dibujar(o, f + 200, aux.getX1(), aux.getY2() + 140, cuadro);
                                n.dibujar(cuadro, o, f);
                                for (int j = 0; j < enlaces.size(); j++) {
                                    if (enlaces.get(j).getId() == condicional.getFlujoInferior()) {
                                        enlaces.get(j).dibujar(enlaces.get(j).getX(), aux.getY2(), enlaces.get(j).getX1(), enlaces.get(j).getY2(), cuadro);
                                    }
                                }
                                if (aux.isDerecho()) {
                                    condicional.getFinalIzquierdo().dibujar(condicional.getFinalIzquierdo().getX(), condicional.getFinalIzquierdo().getY(), condicional.getFinalIzquierdo().getX1(), condicional.getFinalIzquierdo().getY2() + 140, cuadro);

                                } else {
                                    condicional.getFinalDerecho().dibujar(condicional.getFinalDerecho().getX(), condicional.getFinalDerecho().getY(), condicional.getFinalDerecho().getX1(), condicional.getFinalDerecho().getY2() + 140, cuadro);

                                }

                            } else {
                                nuevo.dibujar(aux.getX(), aux.getY(), o, f, cuadro);
                                aux.dibujar(o, f + 70, aux.getX1(), aux.getY2() + 70, cuadro);
                                if (n instanceof Ciclo) {
                                    ((Ciclo) n).setConexionH(nuevo);
                                }
                                for (int j = 0; j < enlaces.size(); j++) {
                                    if (enlaces.get(j).getId() == condicional.getFlujoInferior()) {
                                        enlaces.get(j).dibujar(enlaces.get(j).getX(), aux.getY2(), enlaces.get(j).getX1(), enlaces.get(j).getY2(), cuadro);
                                    }
                                }
                                if (aux.isDerecho()) {
                                    condicional.getFinalIzquierdo().dibujar(condicional.getFinalIzquierdo().getX(), condicional.getFinalIzquierdo().getY(), condicional.getFinalIzquierdo().getX1(), condicional.getFinalIzquierdo().getY2() + 70, cuadro);

                                } else {
                                    condicional.getFinalDerecho().dibujar(condicional.getFinalDerecho().getX(), condicional.getFinalDerecho().getY(), condicional.getFinalDerecho().getX1(), condicional.getFinalDerecho().getY2() + 70, cuadro);

                                }
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
                            if (n instanceof Ciclo) {
                                nuevo.setCiclo(ids);
                            } else {
                                nuevo.setCiclo(-7);
                            }
                            ids++;
                            int diferenciay = (nuevo.getY() + nuevo.getY2()) / 2;
                            formas.add(n);
                            if (aux.isDerecho()) {
                                condicional.setVerdaderas(n);
                                nuevo.setDerecho(true);
                            } else {
                                condicional.setFalsas(n);
                                nuevo.setDerecho(false);

                            }

                            System.out.println("Ciclo Aux: " + aux.getCiclo());

                            if (aux.getCiclo() >= 0) {
                                for (int j = 0; j < formas.size(); j++) {
                                    if (formas.get(j).getID() == aux.getCiclo()) {
                                        Figura ciclo = formas.get(j);
                                        if (ciclo instanceof Ciclo) {
                                            ((Ciclo) ciclo).setConexionH(nuevo);
                                            aux.setCiclo(-7);
                                            System.out.println("Encontre un Ciclo");

                                        }
                                    }
                                }
                            }
                            System.out.println(">> Entre al dibujar dentro de la decision");
                            //bajarFiguras(n, opcion);//

                            nuevo.setDecision(condicional.getID());

                            if (enCiclo == true && !(n instanceof Ciclo)) {

                                for (int j = 0; j < formas.size(); j++) {
                                    if (formas.get(j) instanceof Ciclo) {
                                        Ciclo iteracion = (Ciclo) formas.get(j);
                                        //prints
                                        for (int k = 0; k < iteracion.getIdsFiguras().size(); k++) {
                                            System.out.println("ID " + i + ": " + iteracion.getIdsFiguras().get(k));

                                        }

                                        if (iteracion.getConexionH() == aux) {
                                            iteracion.setConexionH(nuevo);

                                        }
                                    }
                                }
                            }
                            //Funcion que ordena la lista con las nuevas figuras
                            enlaces.set(i, aux);
                            enlaces.add(nuevo);
                            condicional.nodo();

                            //Actualizar Condicional
                            for (int j = 0; j < formas.size(); j++) {
                                if (aux.getDecision() == formas.get(j).getID()) {
                                    formas.set(j, condicional);

                                }
                            }

                            for (int j = 0; j < formas.size(); j++) {
                                if (formas.get(j) instanceof Ciclo) {
                                    Ciclo iteracion = (Ciclo) formas.get(j);
                                    bloqueCiclo(iteracion);

                                }
                            }
                            //condicional.Bajar(n, opcion, lienzo);

                        } else {

                            Flujo nuevo = new Flujo();
                            nuevo.setColor(n.getColor());
                            nuevo.setId(idFlujos);
                            if (n instanceof Decision) {
                                Flujo derecho = new Flujo();
                                Flujo izquierdo = new Flujo();
                                derecho.setColor("#01be9b");
                                izquierdo.setColor("#ff0025");
                                derecho.dibujar(o + 180, f + 30, o + 180, f + 200, cuadro);
                                izquierdo.dibujar(o - 180, f + 30, o - 180, f + 200, cuadro);
                                idFlujos++;
                                derecho.setId(idFlujos);
                                idFlujos++;
                                izquierdo.setId(idFlujos);
                                idFlujos++;
                                derecho.setDerecho(true);
                                derecho.setDecision(ids);
                                izquierdo.setDerecho(false);
                                izquierdo.setDecision(ids);

                                enlaces.add(derecho);
                                enlaces.add(izquierdo);
                                ((Decision) n).setLadoIzquierdo(izquierdo);
                                ((Decision) n).setLadoDerecho(derecho);
                                ((Decision) n).setFinalDerecho(derecho);
                                ((Decision) n).setFinalIzquierdo(izquierdo);
                            }
                            boolean enCiclo = false;
                            int opcion = 1;
                            int diferenciaY = f - aux.getY();
                            int diferenciaX = (aux.getY() + aux.getY2()) / 2;
                            enCiclo = false;
                            if (f > diferenciaX) {
                                enCiclo = true;
                                System.out.println("En el ciclo");
                            } else if (f < diferenciaX) {
                                enCiclo = false;
                                System.out.println("Sobre el ciclo");

                            }
                            if (n instanceof Decision) {
                                nuevo.dibujar(aux.getX(), aux.getY(), o, f, cuadro);
                                aux.dibujar(o, f + 200, aux.getX1(), aux.getY2(), cuadro);
                                n.dibujar(cuadro, o, f);
                            } else {
                                if (diferenciaY < 60) {
                                    nuevo.dibujar(aux.getX(), aux.getY(), o, aux.getY() + 60, cuadro);
                                    aux.dibujar(o, aux.getY() + 130, aux.getX1(), aux.getY2(), cuadro);
                                    if (n instanceof Ciclo) {
                                        ((Ciclo) n).setConexionH(nuevo);
                                    }
                                    n.dibujar(cuadro, o, nuevo.getY2());
                                    opcion = 2;
                                } else {
                                    nuevo.dibujar(aux.getX(), aux.getY(), o, f, cuadro);
                                    aux.dibujar(o, f + 70, aux.getX1(), aux.getY2(), cuadro);
                                    if (n instanceof Ciclo) {
                                        ((Ciclo) n).setConexionH(nuevo);
                                    }
                                    n.dibujar(cuadro, o, f);
                                }
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
                            if (n instanceof Ciclo) {
                                nuevo.setCiclo(ids);
                            } else {
                                nuevo.setCiclo(-7);
                            }
                            ids++;
                            int diferenciay = (nuevo.getY() + nuevo.getY2()) / 2;
                            formas.add(n);
                            System.out.println("Ciclo Aux: " + aux.getCiclo());

                            if (enCiclo == true && !(n instanceof Ciclo)) {

                                for (int j = 0; j < formas.size(); j++) {
                                    if (formas.get(j) instanceof Ciclo) {
                                        Ciclo iteracion = (Ciclo) formas.get(j);
                                        //prints
                                        for (int k = 0; k < iteracion.getIdsFiguras().size(); k++) {
                                            System.out.println("ID " + i + ": " + iteracion.getIdsFiguras().get(k));

                                        }

                                        if (iteracion.getConexionH() == aux) {
                                            iteracion.setConexionH(nuevo);

                                        }
                                    }
                                }
                            }
                            if (n instanceof Decision == false) {
                                bajarFiguras(n, opcion, 150, 60);
                            } else {
                                bajarFiguras(n, opcion, 300, 150);
                            }

                            //Funcion que ordena la lista con las nuevas figuras
                            enlaces.set(i, aux);
                            enlaces.add(nuevo);

                            for (int j = 0; j < formas.size(); j++) {
                                if (formas.get(j) instanceof Ciclo) {
                                    Ciclo iteracion = (Ciclo) formas.get(j);
                                    bloqueCiclo(iteracion);

                                }
                            }
                        }
                        for (int t = 0; t < 2; t++) {
                            for (int b = 0; b < formas.size(); b++) {
                                if (formas.get(b) instanceof Decision) {
                                    //DetectarDecision2((Decision) formas.get(b));
                                    //DetectarDecision3((Decision) formas.get(b));
                                    DetectarDecisionDer((Decision) formas.get(b));
                                    DetectarDecisionIzq((Decision) formas.get(b));
                                }
                            }
                        }

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
                    } else {
                        lienzo.setOnMouseClicked(null);
                    }

                }
            }
        });

    }

    public void bloqueCiclo(Ciclo iteracion) {
        iteracion.getIdsFiguras().clear();
        iteracion.getIdFormas().clear();

        System.out.println("*****Formas dentro del ciclo " + iteracion.getTextoFigura() + "*******");
        Flujo flujoCiclo = iteracion.getConexionH();
        Figura i2 = null;
        for (int i = 0; i < formas.size(); i++) {
            if (formas.get(i).getFlujoSuperior() == flujoCiclo.getId() && formas.get(i) instanceof Ciclo == false) {
                i2 = formas.get(i);
                iteracion.setIdsFiguras(i);
                iteracion.setIdsFiguras(i);
                iteracion.setIdFormas(formas.get(i).getID());

            }

        }
        System.out.println(">> Flujo ID: " + flujoCiclo.getId());

        if (i2 != null) {

            boolean Ciclico = false;
            System.out.println(">> " + i2.getTextoFigura());

            for (int i = 0; i < formas.size(); i++) {
                for (int j = 0; j < formas.size(); j++) {
                    if (formas.get(j).getID() == i2.getSiguiente() && Ciclico != true) {
                        i2 = formas.get(j);
                        System.out.println(">> " + i2.getTextoFigura());
                        iteracion.setIdsFiguras(j);
                        iteracion.setIdFormas(i2.getID());

                        if (i2.getID() == iteracion.getID()) {
                            Ciclico = true;
                            System.out.println(">> Aqui deberia terminar la busqueda");
                        }

                    }
                }
            }

            for (int i = 0; i < iteracion.getIdsFiguras().size(); i++) {
                System.out.println("Indice; " + i + " " + iteracion.getIdsFiguras().get(i));
            }

        }

    }

    ArrayList<String> FlujosSerializables = new ArrayList();
    ArrayList<String> FormasSerializables = new ArrayList();
    ArrayList<String> VariablesSerializables = new ArrayList();
    int indice = 0;

    /**
     *
     * @param event
     */
    @FXML
    public void undo(ActionEvent event) {

    }

    double zoomP = 1.1;
    public static String nuevoFondo = "";
    public static Figura recolor = null;
    @FXML
    public ColorPicker fondo;
    @FXML
    public ColorPicker border;
    @FXML
    public Button aceptarBorde;
    @FXML
    public Button aceptarFondo;

    @FXML
    public void ZoomPlus(ActionEvent event) throws IOException {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el cuadro del canvas

        lienzo.setOnMouseClicked(e -> {// se usa una funcion lambda junto al evento setOnMouseClicked
            recolor = detectarFigura1((int) e.getX(), (int) e.getY());// se llama al metodo detectarBorrar y se le ingresa un x e y
            if (recolor != null) {
                nuevoFondo = recolor.getFondo();
                recolor.isPressed(cuadro);
                aceptarFondo.setDisable(false);
                aceptarBorde.setDisable(false);
            }
            lienzo.setOnMouseClicked(null);// se termina el evento setOnMouseClicked
        });

    }

    @FXML
    public void AceptarColorFondo(ActionEvent event) {
        if (recolor != null) {
            GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el cuadro del canvas
            Color c = fondo.getValue();
            String fondito = c.toString();
            fondito = fondito.replaceAll("0x", "#");
            String n = "";
            for (int i = 0; i < 7; i++) {
                n = n + fondito.charAt(i);

            }
            recolor.setFondo(n);

            if (recolor instanceof InicioFin) {
                coloresFondo.set(0, n);
            }
            if (recolor instanceof Documento) {
                coloresFondo.set(1, n);
            }
            if (recolor instanceof Salida) {
                coloresFondo.set(2, n);
            }
            if (recolor instanceof Entrada) {
                coloresFondo.set(3, n);
            }
            if (recolor instanceof Ciclo) {
                coloresFondo.set(4, n);
            }
            if (recolor instanceof Decision) {
                coloresFondo.set(5, n);
            }
            if (recolor instanceof Etapa) {
                coloresFondo.set(6, n);
            }
            System.out.println(" >> ColorN: " + n);
            repintar(cuadro);
        }
    }

    @FXML
    public void AceptarColorBorder(ActionEvent event) {
        if (recolor != null) {
            GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el cuadro del canvas
            Color c = border.getValue();
            String fondito = c.toString();
            fondito = fondito.replaceAll("0x", "#");
            String n = "";
            for (int i = 0; i < 7; i++) {
                n = n + fondito.charAt(i);

            }
            recolor.setBorde(n);
            if (recolor instanceof InicioFin) {
                coloresBordes.set(0, n);
            }
            if (recolor instanceof Documento) {
                coloresBordes.set(1, n);
            }
            if (recolor instanceof Salida) {
                coloresBordes.set(2, n);
            }
            if (recolor instanceof Entrada) {
                coloresBordes.set(3, n);
            }
            if (recolor instanceof Ciclo) {
                coloresBordes.set(4, n);
            }
            if (recolor instanceof Decision) {
                coloresBordes.set(5, n);
            }
            if (recolor instanceof Etapa) {
                coloresBordes.set(6, n);
            }
            System.out.println(" >> ColorN: " + n);
            repintar(cuadro);
        }
    }

    @FXML
    public void ZoomMinus(ActionEvent event) {
        if (zoomP > 0.1) {
            zoomP = zoomP - 0.1;
            lienzo.setScaleX(zoomP);
            lienzo.setScaleY(zoomP);
        }

    }

    /**
     *
     *
     */
    public void guardarEstadoActual() {
        Decision d = new Decision();
        serializar(formas, "figuras" + indice);
        serializar(enlaces, "flujos" + indice);
        serializar(variables, "variables" + indice);
        FlujosSerializables.add("flujos" + indice);
        FormasSerializables.add("figuras" + indice);
        VariablesSerializables.add("variables" + indice);
        indice++;
    }

    /**
     * Metodo que se encarga de rellenar objetos con la informacion serializada
     * el metodo recibe un objeto en blanco y un string con la cadena que
     * contiene el nombre del archivo , retorna el archivo con la informacion
     * cargada y en caso distinto retorna el objeto en null;
     *
     * @param o
     * @param nombreArchivo
     * @return
     */
    public static Object getTxt(Object o, String nombreArchivo) {
        try {
            FileInputStream fileIn = new FileInputStream(nombreArchivo + ".txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            o = (Object) in.readObject();
            return o;
        } catch (IOException r) {
            r.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Error");
            c.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo que se encarga de serializar un objeto dentro de un archivo TXT
     * recibe dentro de sus parametos un objeto de cualquier tipo y luego un
     * string con el nombre que se le quiere dar al archivo;
     *
     * @param e
     * @param nombre
     */
    public static void serializar(Object e, String nombre) {
        try {
            FileOutputStream fileOut = new FileOutputStream(nombre + ".txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(e);

        } catch (IOException q) {
            q.printStackTrace();
        }
    }

    public void bajarFiguras(Figura bajar, int opcion, int xPlus, int yPlus) {
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
                                enlaces.get(k).dibujar(enlaces.get(k).getX(), enlaces.get(k).getY(), enlaces.get(k).getX1(), enlaces.get(k).getY2() + yPlus, cuadro);
                            } else {
                                enlaces.get(k).dibujar(enlaces.get(k).getX(), enlaces.get(k).getY(), enlaces.get(k).getX1(), enlaces.get(k).getY2() + xPlus, cuadro);

                            }
                        }
                        if (enlaces.get(k).getId() == inicio2.getFlujoInferior()) {
                            if (opcion == 2) {
                                enlaces.get(k).dibujar(enlaces.get(k).getX(), enlaces.get(k).getY() + yPlus, enlaces.get(k).getX1(), enlaces.get(k).getY2(), cuadro);
                            } else {
                                enlaces.get(k).dibujar(enlaces.get(k).getX(), enlaces.get(k).getY() + xPlus, enlaces.get(k).getX1(), enlaces.get(k).getY2(), cuadro);

                            }
                        }
                    }
                    if (opcion == 2) {
                        inicio2.dibujar(cuadro, inicio2.getMedioX(), inicio2.getMedioY() + yPlus);
                    } else {
                        inicio2.dibujar(cuadro, inicio2.getMedioX(), inicio2.getMedioY() + xPlus);

                    }
                    if (inicio2.getMedioY() + (xPlus * 2) > lienzo.getHeight()) {
                        lienzo.setHeight(lienzo.getHeight() + 250);
                    }
                    repintar(cuadro);
                    System.out.println("Siguiente: " + inicio2.getID());
                }
            }
        }
    }

    int ids = 0;

    public void ini() {

        // asignar colores por defecto//
        coloresFondo.add("#f8f76a");//InicioFin
        coloresFondo.add("#c31c2c");//Documento
        coloresFondo.add("#fec973");//Salida
        coloresFondo.add("#adfaaa");//Entrada
        coloresFondo.add("#ffa8b8");//Ciclo
        coloresFondo.add("#b44cd9");//Decision
        coloresFondo.add("#61bbef");//Proceso

        //Asignar Colores de Bordes por defecto
        coloresBordes.add("#b4b314");//InicioFin
        coloresBordes.add("#c31c2c");//Documento
        coloresBordes.add("#d5700d");//Salida
        coloresBordes.add("#4dc66c");//Entrada
        coloresBordes.add("#e9748a");//Ciclo
        coloresBordes.add("#8a08b8");//Decision
        coloresBordes.add("#3b83ad");//Proceso

        reiniciarHilo = true;
        aceptarFondo.setDisable(true);
        aceptarBorde.setDisable(true);
        idFlujos = 0;
        ids = 0;
        consola.setText("");
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        Flujo crear = new Flujo();
        crear.setId(idFlujos);
        boolean validacion = false;
        InicioFin inicio = new InicioFin();
        String respuesta = "";
        crear.setColor(inicio.getColor());

        while (validacion == false) {

            TextInputDialog dialog = new TextInputDialog();

            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/Clases_Figura/Estilos/Alertas.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
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
        crear.setCiclo(-7);
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

    public void correrDecision(Figura aux, Figura corriendo) throws ScriptException, InterruptedException {

        hilo corre = new hilo();
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// se declara el lienzo

        //Inicio validacion condicional
        Decision c = (Decision) aux;
        if (c.getVerdaderas().size() > 0 && c.getFalsas().size() > 0) {

            System.out.println("Is Verdadero: " + c.isVerdadero());
            String[] tokens = c.getTextoFigura().replaceAll("\\s+", "").split("(?<=[-+/()><=!])|(?=[-+/()><=!])");
            System.out.println("Tokens Ciclo");
            for (String token : tokens) {
                System.out.println(token);
            }
            ArrayList<String> arrayTokens = new ArrayList<>();
            for (String token : tokens) {
                arrayTokens.add(token);
            }
            Variable a = null;
            Variable b = null;
            String a1 = "";
            String b1 = "";
            String simbolo = "";
            if (arrayTokens.size() == 3) {
                simbolo = arrayTokens.get(1);
                a1 = arrayTokens.get(0);
                b1 = arrayTokens.get(2);

            } else if (arrayTokens.size() == 4) {
                simbolo = arrayTokens.get(1) + arrayTokens.get(2);
                a1 = arrayTokens.get(0);
                b1 = arrayTokens.get(3);
            }

            boolean existeA = false;
            boolean existeB = false;
            //BuscarVariables
            for (int k = 0; k < variables.size(); k++) {
                if (variables.get(k).getNombre().equals(a1)) {
                    a1 = variables.get(k).getTexto();
                    existeA = true;

                } else if (variables.get(k).getNombre().equals(b1)) {
                    b1 = variables.get(k).getTexto();
                    existeB = true;
                }
            }

            boolean resultado1;
            try {
                Integer.parseInt(a1);
                resultado1 = true;
            } catch (NumberFormatException excepcion) {
                resultado1 = false;
            }

            System.out.println("B2:" + b1);
            boolean resultado2;
            try {
                Integer.parseInt(b1);
                resultado2 = true;
            } catch (NumberFormatException excepcion) {
                resultado2 = false;
            }
            System.out.println("Llegue a ver si existen");
            System.out.println("ABoleando: " + resultado1);
            System.out.println("BBoleando: " + resultado2);

            System.out.println("ExisteABoleando: " + existeA);
            System.out.println("ExisteBBoleando: " + existeB);
//ver si existen
            if (existeA == false) {
                a1 = "0";

            } else if (resultado2 == true) {

            } else if (existeB == false && resultado2 != true) {
                b1 = "0";
            }

            boolean resultado;
            try {
                Integer.parseInt(a1);
                resultado = true;
            } catch (NumberFormatException excepcion) {
                resultado = false;
            }

            boolean resultadoB;
            try {
                Integer.parseInt(b1);
                resultadoB = true;
            } catch (NumberFormatException excepcion) {
                resultadoB = false;
            }

            String primerA = "";
            String primerB = "";

            boolean FinEjecucion = false;

            System.out.println(">>>a: " + a1);
            System.out.println(">>>b: " + b1);
            // ver si el simbolo es >
            if (simbolo.equals(">")) {
                if (resultado == true && resultadoB == true) {
                    if (Integer.parseInt(a1) > Integer.parseInt(b1)) {
                        c.setVerdadero(true);
                    }
                    if (Integer.parseInt(a1) < Integer.parseInt(b1)) {
                        c.setVerdadero(false);
                    }
                }
            } else if (simbolo.equals("<")) {

                if (resultado == true && resultadoB == true) {
                    if (Integer.parseInt(a1) < Integer.parseInt(b1)) {
                        c.setVerdadero(true);
                        System.out.println(">> Se volvio verdadero");

                    } else if (Integer.parseInt(a1) > Integer.parseInt(b1)) {
                        c.setVerdadero(false);

                    }
                }
            } else if (simbolo.equals("=")) {
                if (a1.equals(b1)) {
                    c.setVerdadero(true);
                }
                if (!a1.equals(b1)) {
                    c.setVerdadero(false);
                }
            } else if (simbolo.equals("!=")) {
                if (a1.equals(b1) == false) {
                    c.setVerdadero(true);
                }

            } else if (simbolo.equals("<=")) {

                if (resultado == true && resultadoB == true) {
                    if (Integer.parseInt(a1) <= Integer.parseInt(b1)) {
                        c.setVerdadero(true);

                    } else if (Integer.parseInt(a1) >= Integer.parseInt(b1)) {
                        c.setVerdadero(false);

                    }
                }
            } else if (simbolo.equals(">=")) {

                if (resultado == true && resultadoB == true) {
                    if (Integer.parseInt(a1) >= Integer.parseInt(b1)) {
                        c.setVerdadero(true);

                    } else if (Integer.parseInt(a1) >= Integer.parseInt(b1)) {
                        c.setVerdadero(false);

                    }

                }
            }

            repintar(cuadro);
            if (c.isVerdadero()) {
                Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_azul.png"));
                cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());

            }

        } else {
            corriendo = formas.get(1);
            aux = formas.get(1);
            System.out.println(">> La decision no es 1-1");

        }

    }
    Stage primaryStage;

    @FXML
    public void guardarPNG(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) lienzo.getWidth(), (int) lienzo.getHeight());
                SnapshotParameters parameters = new SnapshotParameters();
                parameters.setFill(Color.TRANSPARENT);
                lienzo.snapshot(parameters, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
            }
        }
    }

    @FXML
    public void guardarJPG(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) lienzo.getWidth(), (int) lienzo.getHeight());
                lienzo.snapshot(null, writableImage);
                BufferedImage bImage = SwingFXUtils.fromFXImage(writableImage, null);
                BufferedImage bImage2 = new BufferedImage(bImage.getWidth(), bImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                bImage2.getGraphics().drawImage(bImage, 0, 0, null);
                ImageIO.write(bImage2, "jpg", file);

            } catch (IOException ex) {
            }
        }
    }

    @FXML
    Button paso;
    Figura correrActual = null;
    boolean inicio2 = false;
    int index = 0;

    @FXML
    public void PasoAPaso(ActionEvent event) throws ScriptException {
        hilo metodos = new hilo();
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el Lienzo del programa
        repintar(cuadro);
        if (inicio2 == false && index == 0) {
            correrActual = formas.get(0);
            inicio2 = true;
            index++;
            Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_naranja.png"));
            cuadro.drawImage(image, correrActual.getMedioX() - 230, correrActual.getMedioY());

        } else if (correrActual != null && correrActual.getSiguiente() != -1 && index > 0) {
            for (int i = 0; i < formas.size(); i++) {
                index++;
                if (formas.get(i).getID() == correrActual.getSiguiente()) {
                    correrActual = formas.get(i);

                    Figura corriendo = correrActual;
                    System.out.println(">> Veces: " + i);

                    if (correrActual instanceof Etapa) {
                        Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_naranja.png"));
                        cuadro.drawImage(image, correrActual.getMedioX() - 230, correrActual.getMedioY());
                        metodos.correrEtapa(corriendo.getTextoFigura());

                    }
                    if (correrActual instanceof Salida) {
                        Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_naranja.png"));
                        cuadro.drawImage(image, correrActual.getMedioX() - 230, correrActual.getMedioY());
                        metodos.correrSalida((Salida) corriendo);

                    }
                    if (correrActual instanceof Entrada) {
                        Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_naranja.png"));
                        cuadro.drawImage(image, correrActual.getMedioX() - 230, correrActual.getMedioY());
                        metodos.correrEntrada((Entrada) corriendo);

                    }
                    if (correrActual instanceof Decision) {
                        Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_naranja.png"));
                        cuadro.drawImage(image, correrActual.getMedioX() - 230, correrActual.getMedioY());
                        metodos.CorrerDecision((Decision) corriendo);

                    }
                    if (correrActual instanceof Ciclo) {
                        Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_naranja.png"));
                        cuadro.drawImage(image, correrActual.getMedioX() - 230, correrActual.getMedioY());
                        correrActual = metodos.correrCiclo((Ciclo) corriendo, correrActual);
                    }
                    if (correrActual instanceof InicioFin) {
                        Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_naranja.png"));
                        cuadro.drawImage(image, correrActual.getMedioX() - 230, correrActual.getMedioY());

                    }
                    if (correrActual instanceof Documento) {
                        Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_naranja.png"));
                        cuadro.drawImage(image, correrActual.getMedioX() - 230, correrActual.getMedioY());

                    }

                    i = formas.size();
                }
            }
        } else {
            Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_naranja.png"));
            cuadro.drawImage(image, correrActual.getMedioX() - 230, correrActual.getMedioY());
            inicio2 = false;
            index = 0;

        }

    }

    @FXML
    public void editarTexto(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el lienzo
        lienzo.setOnMouseClicked(e -> {// se usa una funcion lambda junto al evento setOnMouseClicked
            Figura figura = detectarFigura1((int) e.getX(), (int) e.getY());
            if (figura != null) {
                //Encontro figura....
                if (figura instanceof Entrada) {
                    String textoQueYaTenia = figura.getTextoFigura();
                    Entrada entrada = new Entrada();
                    entrada.setTextoFigura(figura.getTextoFigura());
                    click = ingresarTexto(entrada, "Entrada");
                    if (click) {
                        Pattern p = Pattern.compile("([A-Za-z0-9]+\\=([0-9]+|[A-Za-z0-9]+)\\,?)+");
                        Matcher matcher = p.matcher(entrada.getTextoFigura());
                        boolean cadenavalida = matcher.matches();
                        if (cadenavalida) {
                            //ahora que valide el texto que se ingreso, borrare las entradas
                            //que estaban en la entrada original.

                            //ya borramos las variables que se crearon
                            //debo crear nuevas variables en base a las variables que se introdujieron
                            //ya tengo la entrada con el texto dentro, tomar con entrada.getTextoFigura()
                            String[] tokens2 = entrada.getTextoFigura().replaceAll("\\s+", "").split("(?<=[=,])|(?=[=,])");
                            ArrayList<String> arrayTokens = new ArrayList<>();
                            for (String token : tokens2) {
                                arrayTokens.add(token);
                            }
                            ArrayList<String> nombresVariables = new ArrayList<>();
                            for (int i = 0; i < arrayTokens.size(); i++) {
                                if (arrayTokens.get(i).matches("=")) {
                                    nombresVariables.add(arrayTokens.get(i - 1));
                                }
                            }
                            ArrayList<Integer> comasTokens = new ArrayList<>();
                            //intentamos tomar todas las comas
                            for (int i = 0; i < arrayTokens.size(); i++) {
                                if (arrayTokens.get(i).matches(",")) {
                                    comasTokens.add(i);
                                }
                            }
                            for (int i = 0; i < comasTokens.size(); i++) {
                                System.out.println("hay una coma en los tokens en la posicion " + comasTokens.get(i));
                            }
                            //validamos que la cant de comas sea igual a los nombres de variables -1
                            if (comasTokens.size() == (nombresVariables.size() - 1)) {
                                cadenavalida = true;
                            } else {
                                cadenavalida = false;
                            }
                            //intentamos todos los valores a la derecha
                            ArrayList<String> valoresVariablesTokens = new ArrayList<>();
                            if (cadenavalida) {
                                for (int i = 0; i < arrayTokens.size(); i++) {
                                    if (arrayTokens.get(i).matches("=")) {
                                        valoresVariablesTokens.add(arrayTokens.get(i + 1));
                                    }
                                }
                            }
                            //validamos que no hayan nombres repetidos
                            int cantidadRepetida = 0;
                            if (cadenavalida) {
                                for (int i = 0; i < nombresVariables.size(); i++) {
                                    cantidadRepetida = 0;
                                    for (int j = 0; j < nombresVariables.size(); j++) {
                                        if (nombresVariables.get(i).equals(nombresVariables.get(j))) {
                                            cantidadRepetida++;
                                        }
                                        if (cantidadRepetida > 1) {
                                            cadenavalida = false;
                                            System.out.println("hay nombres repetidos.");
                                        }
                                    }
                                }
                            }
                            //aca deberia estar todo bien y listo
                            if (cadenavalida) {
                                String expressionX = textoQueYaTenia;
                                String[] tokensX = expressionX.replaceAll("\\s+", "").split("(?<=[=,])|(?=[=,])");
                                ArrayList<String> arrayTokensX = new ArrayList<>();
                                for (String token : tokensX) {
                                    arrayTokensX.add(token);
                                }
                                ArrayList<String> nombresVariablesX = new ArrayList<>();
                                //intentemos tomar todos los nombres de variables
                                for (int i = 0; i < arrayTokensX.size(); i++) {
                                    if (arrayTokensX.get(i).matches("=")) {
                                        nombresVariablesX.add(arrayTokensX.get(i - 1));
                                    }
                                }
                                //ciclo para borrar todas las variables que se crearon en la entrada
                                for (int i = 0; i < nombresVariablesX.size(); i++) {
                                    for (int j = 0; j < variables.size(); j++) {
                                        if (nombresVariablesX.get(i).equals(variables.get(j).getNombre())) {
                                            variables.remove(j);
                                            j = variables.size();
                                        }
                                    }
                                }
                                boolean validarNombres;
                                for (int i = 0; i < nombresVariables.size(); i++) {
                                    validarNombres = false;
                                    Variable variableNueva = new Variable();
                                    variableNueva.setNombre(nombresVariables.get(i));
                                    variableNueva.setTexto(valoresVariablesTokens.get(i));
                                    if (variableNueva.getTexto().matches("[0-9]+")) {
                                        variableNueva.setTipo("numero");
                                    } else {
                                        variableNueva.setTipo("texto");
                                    }
                                    for (int j = 0; j < variables.size(); j++) {
                                        if (variableNueva.getNombre().equals(variables.get(j).getNombre())) {
                                            validarNombres = true;
                                            j = variables.size();
                                        }
                                    }
                                    //
                                    if (validarNombres) {
                                        System.out.println("encontre variables iguales");
                                    }
                                    //
                                    if (validarNombres) {
                                        for (int k = 0; k < variables.size(); k++) {
                                            if (variableNueva.getNombre().equals(variables.get(k).getNombre())) {
                                                variables.get(k).setNombre(variableNueva.getNombre());
                                                variables.get(k).setTexto(variableNueva.getTexto());
                                                variables.get(k).setTipo(variableNueva.getTipo());
                                                k = variables.size();
                                            }
                                        }
                                    } else {
                                        variables.add(variableNueva);
                                    }
                                }
                                //tengo que reemplazar el texto original
                                figura.setTextoFigura(entrada.getTextoFigura());
                                repintar(cuadro);
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
                        }
                    }
                }

                if (figura instanceof Etapa) {
                    Etapa etapa = new Etapa();
                    String textoQueYaTenia = figura.getTextoFigura();
                    etapa.setTextoFigura(figura.getTextoFigura());
                    click = ingresarTexto(etapa, "Etapa");
                    if (click) {
                        Pattern p = Pattern.compile("([A-Za-z0-9]+\\=((\\(+|\\)+|\\+|\\-|\\*|\\/|[A-ZA-z0-9]+|[0-9]+))+\\,?)+");
                        Matcher matcher = p.matcher(etapa.getTextoFigura());
                        boolean cadenaValida = matcher.matches();
                        if (cadenaValida) {
                            //aca se trabaja
                            //ejemplo: se ingreso a=(a+b)/2,d=(5+9)*(98/2)
                            int cantidadParentesisAbiertos = 0;
                            int cantidadParentesisCerrados = 0;
                            cadenaValida = matcher.matches();
                            boolean cantidadParentesis = true;
                            boolean cantidadOperaciones = true;
                            boolean noRepetidos = true;
                            //tokenizar a=(a+b)/2,d=(5+9)*(98/2)
                            String[] tokens2 = etapa.getTextoFigura().replaceAll("\\s+", "").split("(?<=[=,])|(?=[=,])");
                            //se imprimen todos los tokens
                            //en este caso
                            //[a][=][(a+b)/2][,][d][=][(5+9)*(98/2)]
                            System.out.println("Se imprimen todos los tokens de la lista.");
                            for (String token : tokens2) {
                                System.out.println(token);
                            }
                            //se introducen en un arreglo para manejo mas facil
                            ArrayList<String> tokensOriginal = new ArrayList<>();
                            for (String token : tokens2) {
                                tokensOriginal.add(token);
                            }
                            //se imprime tokensOriginal que tiene "a","=","(a+b)/2",",","d","=","(5+9)*(98/2)" 
                            System.out.println("imprimos el array de tokens antes de evaluar el lado derecho");
                            for (int i = 0; i < tokensOriginal.size(); i++) {
                                System.out.print("'" + tokensOriginal.get(i) + "', ");
                            }
                            System.out.println("");
                            //ahora tomaremos todos los nombres de variables
                            //y las guardamos en nombreVariables
                            //nombreVariables = "a","d"
                            ArrayList<String> nombreVariables = new ArrayList<>();
                            for (int i = 0; i < tokensOriginal.size(); i++) {
                                if (tokensOriginal.get(i).equals("=")) {
                                    nombreVariables.add(tokensOriginal.get(i - 1));
                                }
                            }
                            //imprimos todos los nombres
                            System.out.println("imprimos todos los nombres de variables");
                            for (int i = 0; i < nombreVariables.size(); i++) {
                                System.out.println("'" + nombreVariables.get(i) + "', ");
                            }
                            System.out.println("");
                            //vemos que los nombres ingresados no sean iguales entre si
                            //cantidadVecesRepetido ve cuantas veces se repite un nombre de variable
                            //si se repite mas de una vez entonces esta mal
                            int cantidadVecesRepetido = 0;
                            for (int i = 0; i < nombreVariables.size(); i++) {
                                cantidadVecesRepetido = 0;
                                for (int j = 0; j < nombreVariables.size(); j++) {
                                    if (nombreVariables.get(i).equals(nombreVariables.get(j))) {
                                        cantidadVecesRepetido++;
                                    }
                                }
                                if (cantidadVecesRepetido > 1) {
                                    System.out.println("HAY NOMBRES REPETIDOS");
                                    noRepetidos = false;
                                    click = false;
                                    i = nombreVariables.size();
                                }
                            }
                            //ya se valido que no hayan nombres repetidos
                            //validemos la cantidad de operaciones
                            //osea la relacion entre la cantidad de , y la cantidad de etapas.
                            int comasPresentes = 0;
                            for (int i = 0; i < tokensOriginal.size(); i++) {
                                if (tokensOriginal.get(i).equals(",")) {
                                    comasPresentes++;
                                }
                            }
                            if (comasPresentes > nombreVariables.size() - 1) {
                                System.out.println("LA CANTIDAD DE COMAS Y OPERACIONES NO CALZA");
                                cantidadOperaciones = false;
                                click = false;
                            }
                            //ya validamos que la cantidad de operaciones y comas este bien
                            //ahora validamos la cantidad de parentesis abiertos y cerrados
                            //debe ser la misma cantidad.
                            //debemos tomar los lados derechos
                            ArrayList<String> ladosDerechosCompletos = new ArrayList<>();
                            for (int i = 0; i < tokensOriginal.size(); i++) {
                                if (tokensOriginal.get(i).equals("=")) {
                                    ladosDerechosCompletos.add(tokensOriginal.get(i + 1));
                                }
                            }
                            //Ahora en ladosDerechosCompletos estan los lados derechos
                            //nombreVaribles = "a","d"
                            //ladosDerechosCompletos = "(a+b)/2","(5+9)*(98/2)"
                            //debemos tokenizar los lados derechos
                            ArrayList<ArrayList> arrayDeDerechos = new ArrayList<>();
                            for (int i = 0; i < nombreVariables.size(); i++) {
                                String[] tokensDerechos = ladosDerechosCompletos.get(i).replaceAll("\\s+", "").split("(?<=[-+/*()])|(?=[-+/*()])");
                                ArrayList<String> ladoDerechoTokenizado = new ArrayList<>();
                                for (String token : tokensDerechos) {
                                    ladoDerechoTokenizado.add(token);
                                }
                                arrayDeDerechos.add(ladoDerechoTokenizado);
                            }
                            //ahora tenemos un array llamado arrayDeDerechos, que dentro tiene arrays que cada uno tiene los lados derechos tokenizados
                            //imprimamoslo
                            System.out.println("IMPRIMIMOS TODOS LOS LADOS DERECHOS TOKENIZADOS");
                            for (int i = 0; i < arrayDeDerechos.size(); i++) {
                                for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                    System.out.print("'" + arrayDeDerechos.get(i).get(j) + "', ");
                                }
                                System.out.println("");
                            }
                            //ahora podemos evaluar que la cantidad de parentesis abiertos sea igual a los cerrados
                            for (int i = 0; i < arrayDeDerechos.size(); i++) {
                                for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                    if (arrayDeDerechos.get(i).get(j).equals("(")) {
                                        cantidadParentesisAbiertos++;
                                    }
                                }
                            }
                            for (int i = 0; i < arrayDeDerechos.size(); i++) {
                                for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                    if (arrayDeDerechos.get(i).get(j).equals(")")) {
                                        cantidadParentesisCerrados++;
                                    }
                                }
                            }
                            //nos aseguramos imprimiendo si la cantidad esta bien
                            System.out.println("Cantidad de parentesis abiertos: " + cantidadParentesisAbiertos);
                            System.out.println("Cantidad de parentesis cerrados: " + cantidadParentesisCerrados);
                            if (cantidadParentesisAbiertos != cantidadParentesisCerrados) {
                                cantidadParentesis = false;
                                click = false;
                            }
                            if (click == false) {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                                ImageView imageVie = new ImageView(images);
                                alert.setGraphic(imageVie);
                                alert.setTitle("Error.");
                                alert.setHeaderText("Ocurrio un error.");
                                alert.setContentText("Alguna de las etapas ingresadas tenia un formato incorrecto.");
                                alert.showAndWait();
                                click = false;
                            }
                            //se valido la cantidad de parentesis
                            if (cadenaValida && noRepetidos && cantidadOperaciones && cantidadParentesis) {
                                System.out.print("etapa.getTextofigura: ");
                                System.out.println(etapa.getTextoFigura());
                                //Aca deberia estar todo el texto ingresado validado
                                //ahora se deben validar todos los lados derechos
                                boolean validaLadoDerecho = true;
                                //porte del arreglo con arreglos adentro
                                //osea la cantidad de etapas ingresadas
                                for (int i = 0; i < arrayDeDerechos.size(); i++) {
                                    System.out.println("arrayDeDerechos.get(i)");
                                    System.out.println(arrayDeDerechos.get(i));
                                    //porte del arreglo dentro del arreglo
                                    //osea el texto de cada una de las etapas ingresadas
                                    if (validaLadoDerecho) {
                                        String texto = "";
                                        texto = String.join("", arrayDeDerechos.get(i));
                                        System.out.println("texto unido: " + texto);
                                        //ver que no empieze el lado derecho deturno con un )
                                        //variable=)
                                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                                        if (validaLadoDerecho) {
                                            System.out.println("Llegue a validacion 1");
                                            if (arrayDeDerechos.get(i).get(0).equals(")")) {
                                                System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                                validaLadoDerecho = false;
                                            }
                                        }
                                        //vemos que el lado derecho no sean puras letras o numeros
                                        //a=2452
                                        //a=popeye
                                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                                        if (validaLadoDerecho) {
                                            System.out.println("Llegue a validacion 2");
                                            if (texto.replaceAll("[A-Za-z0-9]", "").equals("")) {
                                                System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                                validaLadoDerecho = false;
                                            }
                                        }
                                        //ver que no haya una letra o numero antes de un (
                                        //variable=a+b(
                                        //variale=a+8(
                                        //que hayan simbolos antes es valido
                                        //variable=a+b+(2*9)
                                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                                        if (validaLadoDerecho) {
                                            System.out.println("Llegue a validacion 3");
                                            if (arrayDeDerechos.get(i).get(0).equals("(")) {
                                                for (int j = 1; j < arrayDeDerechos.get(i).size(); j++) {
                                                    if (arrayDeDerechos.get(i).get(j).equals("(")) {
                                                        if (arrayDeDerechos.get(i).get((j) - 1).toString().matches("[A-Za-z0-9]")) {
                                                            System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                                            validaLadoDerecho = false;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        //ver que no haya un simbolo justo antes de )
                                        //variable=a+b+)
                                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                                        if (validaLadoDerecho) {
                                            System.out.println("Llegue a validacion 4");
                                            for (int j = 1; j < arrayDeDerechos.get(i).size(); j++) {
                                                if (arrayDeDerechos.get(i).get(j).equals(")")) {
                                                    if (arrayDeDerechos.get(i).get((j) - 1).toString().matches("[\\+\\-\\*\\/]")) {
                                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                                        validaLadoDerecho = false;
                                                    }
                                                }
                                            }
                                        }
                                        //valida que no haya ()
                                        //variable=()
                                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                                        if (validaLadoDerecho) {
                                            System.out.println("Llegue a validacion 5");
                                            for (int j = 1; j < arrayDeDerechos.get(i).size(); j++) {
                                                if (arrayDeDerechos.get(i).get(j).equals(")")) {
                                                    if (arrayDeDerechos.get(i).get(j - 1).toString().matches("[\\(]")) {
                                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                                        validaLadoDerecho = false;
                                                    }
                                                }
                                            }
                                        }
                                        //valida que no empieze con un simbolo
                                        //variable=+
                                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                                        if (validaLadoDerecho) {
                                            System.out.println("Llegue a validacion 6");
                                            if (arrayDeDerechos.get(i).get(0).toString().matches("[\\+\\-\\*\\/]")) {
                                                System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                                validaLadoDerecho = false;
                                            }
                                        }
                                        //valida que no haya simbolo justo despues de (
                                        //variable=(+
                                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                                        if (validaLadoDerecho) {
                                            System.out.println("Llegue a validacion 7");
                                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                                if (arrayDeDerechos.get(i).get(j).equals("(")) {
                                                    if ((j + 1) < arrayDeDerechos.get(i).size() && arrayDeDerechos.get(i).get(j + 1).toString().matches("[\\+\\-\\*\\/]")) {
                                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                                        validaLadoDerecho = false;
                                                    }
                                                }
                                            }
                                        }
                                        //valida que no haya )(
                                        //variable=)(
                                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                                        if (validaLadoDerecho) {
                                            System.out.println("Llegue a validacion 8");
                                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                                if (arrayDeDerechos.get(i).get(j).equals(")")) {
                                                    if ((j + 1) < arrayDeDerechos.get(i).size() && arrayDeDerechos.get(i).get(j + 1).toString().matches("[\\(]")) {
                                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                                        validaLadoDerecho = false;
                                                    }
                                                }
                                            }
                                        }
                                        //si el lado derecho empieza con un ( avanzo a la siguiente posicion
                                        //si no, veo si la posicion actual es ( luego
                                        //pregunto si la anterior era una letra, numero(0-9) o simbolo, si lo era es un error.
                                        //si no, transformor el token anterior a numero y pregunto si es mayor a 9
                                        //si lo es tiro error.
                                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                                        if (validaLadoDerecho) {
                                            System.out.println("Llegue a validacion 9");
                                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                                if (arrayDeDerechos.get(i).get(0).equals("(")) {
                                                    j++;
                                                } else if (arrayDeDerechos.get(i).get(j).equals("(")) {
                                                    if (arrayDeDerechos.get(i).get(j - 1).toString().matches("[A-Za-z0-9\\+\\-\\*\\/]")) {
                                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                                        validaLadoDerecho = false;
                                                    } else if (Integer.parseInt(arrayDeDerechos.get(i).get(j - 1).toString()) > 9) {
                                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                                        validaLadoDerecho = false;
                                                    }
                                                }
                                            }
                                        }
                                        //valida que no hayan 2 simbolos seguidos
                                        //variable=a+-b
                                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                                        if (validaLadoDerecho) {
                                            System.out.println("Llegue a validacion 10");
                                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                                if (arrayDeDerechos.get(i).get(j).toString().matches("[\\+\\-\\*\\/]")) {
                                                    if ((j + 1) < arrayDeDerechos.get(i).size() && arrayDeDerechos.get(i).get(j + 1).toString().matches("[\\+\\-\\*\\/]")) {
                                                        System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                                        validaLadoDerecho = false;
                                                    }
                                                }
                                            }
                                        }
                                        //valida que la posicion final no sea (
                                        //variable=a+b(
                                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                                        if (validaLadoDerecho) {
                                            System.out.println("Llegue a validacion 11");
                                            if (arrayDeDerechos.get(i).get(arrayDeDerechos.get(i).size() - 1).equals("(")) {
                                                System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                                validaLadoDerecho = false;
                                            }
                                        }
                                        //valida que la posicion final no sea un simbolo
                                        //variable=a+b+
                                        System.out.println("validaLadoDerecho: " + validaLadoDerecho);
                                        if (validaLadoDerecho) {
                                            System.out.println("Llegue a validacion 12");
                                            if (arrayDeDerechos.get(i).get(arrayDeDerechos.get(i).size() - 1).toString().matches("[\\+\\-\\*\\/]")) {
                                                System.out.println("murio al evaluar la etapa " + (i + 1) + " de las ingresadas.");
                                                validaLadoDerecho = false;
                                            }
                                        }
                                        //aca termine de validar el lado derecho en esta iteracion
                                    }
                                }
                                ArrayList<Integer> posicionesVariablesEnArrayTokens = new ArrayList<>();
                                if (validaLadoDerecho) {
                                    System.out.println("TODAS LAS VALIDACIONES FUERON CORRECTAS");
                                    //aca creo una copia de las variables
                                    //para modificar ese
                                    //en caso de salir todo bien, limpio las variables y le pego el copiado
                                    //en caso de salir algo mal, limpio la copia y no hago nada.
                                    ArrayList<Variable> variablesCopia = new ArrayList<>();
                                    for (int i = 0; i < variables.size(); i++) {
                                        variablesCopia.add(variables.get(i));
                                    }
                                    String expression = textoQueYaTenia;
                                    String[] tokensX = expression.replaceAll("\\s+", "").split("(?<=[=,])|(?=[=,])");
                                    ArrayList<String> arrayTokens = new ArrayList<>();
                                    for (String token : tokensX) {
                                        arrayTokens.add(token);
                                    }
                                    ArrayList<String> nombresVariables = new ArrayList<>();
                                    //intentemos tomar todos los nombres de variables
                                    for (int i = 0; i < arrayTokens.size(); i++) {
                                        if (arrayTokens.get(i).matches("=")) {
                                            nombresVariables.add(arrayTokens.get(i - 1));
                                        }
                                    }
                                    //ciclo para borrar todas las variables que se crearon en la etapa
                                    for (int i = 0; i < nombresVariables.size(); i++) {
                                        for (int j = 0; j < variablesCopia.size(); j++) {
                                            if (nombresVariables.get(i).equals(variablesCopia.get(j).getNombre())) {
                                                variablesCopia.remove(j);
                                                j = variablesCopia.size();
                                            }
                                        }
                                    }
                                    //aca ya esta listo, debemos comenzar a crear las variables
                                    //pero tenemos que ver el tipo, si son solo numeros, strings o ambos(error).
                                    //operaciones fallidas nos dice si debemos modificar el array de variables o no
                                    int operacionesFallidas = 0;
                                    //creamos arreglos para ayudarnos
                                    //en variablesTokensValidar guardamos todas las letras o "variables"
                                    ArrayList<String> variablesTokensValidar = new ArrayList<>();
                                    //en posicionesVariablesEnArrayTokensValidar guardamos las posiciones
                                    // que toman aquellas letras o "variables" en el original
                                    ArrayList<Integer> posicionesVariablesEnArrayTokensValidar = new ArrayList<>();
                                    ArrayList<String> variablesEnTokens = new ArrayList<>();
                                    ArrayList<String> valoresVariables = new ArrayList<>();
                                    ArrayList<String> tokensVariables = new ArrayList<>();
                                    ArrayList<String> palabrasTokensVariables = new ArrayList<>();
                                    for (int i = 0; i < arrayDeDerechos.size(); i++) {
                                        //Limpiamos los arreglos al principio de la iteracion
                                        //donde se cambia de etapa, para no tomar valores erroneos
                                        variablesEnTokens.clear();
                                        posicionesVariablesEnArrayTokens.clear();
                                        variablesTokensValidar.clear();
                                        posicionesVariablesEnArrayTokensValidar.clear();
                                        valoresVariables.clear();
                                        tokensVariables.clear();
                                        palabrasTokensVariables.clear();
                                        //IMPRIMOS LOS TOKENS DEL LADO IZQUIERDO PARA ASEGURARSE
                                        System.out.println("VEZ " + (i + 1));
                                        System.out.println("Tokens derechos: ");
                                        for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                            System.out.println(arrayDeDerechos.get(i).get(j));
                                        }
                                        //imprimos los tokens unidos
                                        String unido = String.join("", arrayDeDerechos.get(i));
                                        System.out.println("El texto unido: " + unido);
                                        int tipo = 0;
                                        //ACA PASA ALGO
                                        //tipo 1 solo numeros
                                        //tipo 2 solo strings
                                        //cualquier otra cosa es error.
                                        //Vemos cuales de todos los tokens son letras o "variables"
                                        for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                            if (!(arrayDeDerechos.get(i).get(j).toString().replaceAll("[0-9\\+\\-\\*\\/\\(\\)]", "").equals(""))) {
                                                variablesTokensValidar.add(arrayDeDerechos.get(i).get(j).toString());
                                                posicionesVariablesEnArrayTokensValidar.add(j);
                                            }
                                        }
                                        //cantidadDeVariablesAValidar me dice cuantas variables encontre en los tokens
                                        //en la etapa que esta de turno
                                        int cantidadDeVariablesAValidar = variablesTokensValidar.size();
                                        //variable que se usa para ver cuantas variables de tipo string se encontraron
                                        int cantidadDeVariablesStringEncontradas = 0;
                                        //este ciclo se ejecuta si se encontraron letras o "variables" en la etapa
                                        if (!variablesTokensValidar.isEmpty()) {
                                            for (int j = 0; j < variablesTokensValidar.size(); j++) {
                                                for (int k = 0; k < variablesCopia.size(); k++) {
                                                    if (variablesTokensValidar.get(j).equals(variablesCopia.get(k).getNombre())) {
                                                        System.out.println(variablesCopia.get(k).getTipo());
                                                        if (variablesCopia.get(k).getTipo().equals("texto")) {
                                                            cantidadDeVariablesStringEncontradas++;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        //
                                        variablesTokensValidar.clear();
                                        posicionesVariablesEnArrayTokensValidar.clear();
                                        System.out.println("cantidad de variables tipo string encontradas");
                                        System.out.println(cantidadDeVariablesStringEncontradas);
                                        System.out.println("cantidad de variables a validar");
                                        System.out.println(cantidadDeVariablesAValidar);
                                        if (cantidadDeVariablesStringEncontradas > 0 && cantidadDeVariablesStringEncontradas == cantidadDeVariablesAValidar) {
                                            tipo = 1;
                                        } else if (cantidadDeVariablesStringEncontradas == 0) {
                                            tipo = 0;
                                        } else {
                                            tipo = 3;
                                        }
                                        //ACA TERMINA ESE ALGO
                                        System.out.println("TIPO = " + tipo);
                                        if (tipo == 0) {
                                            //solo numeros
                                            System.out.println("hora de imprimir todos los tokens en el arreglo para asi reemplazar");
                                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                                System.out.println("token original: " + arrayDeDerechos.get(i).get(j).toString());
                                                System.out.println("token sin num o simbolos: " + arrayDeDerechos.get(i).get(j).toString().replaceAll("[0-9\\+\\-\\*\\/\\(\\)]", ""));
                                                if (!(arrayDeDerechos.get(i).get(j).toString().replaceAll("[0-9\\+\\-\\*\\/\\(\\)]", "").equals(""))) {
                                                    variablesEnTokens.add(arrayDeDerechos.get(i).get(j).toString());
                                                    posicionesVariablesEnArrayTokens.add(j);
                                                }
                                            }
                                            boolean existe = false;
                                            if (!variablesEnTokens.isEmpty()) {
                                                System.out.println("variablesEnTokens no esta vacio");
                                                for (int j = 0; j < variablesEnTokens.size(); j++) {
                                                    for (int k = 0; k < variablesCopia.size(); k++) {
                                                        //System.out.println(variablesEnTokens.get(i));
                                                        //System.out.println("coma");
                                                        //System.out.println(variables.get(j).getNombre());
                                                        if (variablesEnTokens.get(j).equals(variablesCopia.get(k).getNombre())) {
                                                            valoresVariables.add(variablesCopia.get(k).getTexto());
                                                            existe = true;
                                                        }
                                                    }
                                                    if (existe == false) {
                                                        valoresVariables.add("0");
                                                    } else {
                                                        existe = false;
                                                    }
                                                }
                                                for (int j = 0; j < posicionesVariablesEnArrayTokens.size(); j++) {
                                                    arrayDeDerechos.get(i).set(posicionesVariablesEnArrayTokens.get(j), valoresVariables.get(j));
                                                }
                                            }
                                            variablesEnTokens.clear();
                                            posicionesVariablesEnArrayTokens.clear();
                                            valoresVariables.clear();
                                            int posVariableIgual = 0;
                                            System.out.println("el lado derecho esta validado");
                                            boolean iguales = false;
                                            for (int j = 0; j < variablesCopia.size(); j++) {
                                                if (variablesCopia.get(j).getNombre().equals(nombreVariables.get(i))) {
                                                    iguales = true;
                                                    posVariableIgual = j;
                                                }
                                            }
                                            if (iguales) {
                                                try {
                                                    System.out.println("ya hay variable con ese nombre");
                                                    //cuando la variable ya esta en el arreglo
                                                    ScriptEngineManager mgr = new ScriptEngineManager();
                                                    ScriptEngine engine = mgr.getEngineByName("JavaScript");
                                                    //vamos a unir el string
                                                    unido = String.join("", arrayDeDerechos.get(i));
                                                    String ecuacion = unido;

                                                    if (Double.isNaN(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                                        variablesCopia.get(posVariableIgual).setTexto("No es un numero.");
                                                        variablesCopia.get(posVariableIgual).setTipo("texto");
                                                    } else if (Double.isInfinite(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                                        variablesCopia.get(posVariableIgual).setTexto("Infinito.");
                                                        variablesCopia.get(posVariableIgual).setTipo("texto");
                                                    } else {
                                                        variablesCopia.get(posVariableIgual).setTexto(engine.eval(ecuacion).toString());
                                                        variablesCopia.get(posVariableIgual).setTipo("numero");
                                                    }
                                                    System.out.println(engine.eval(ecuacion));
                                                    variablesCopia.get(posVariableIgual).setTexto(engine.eval(ecuacion).toString());
                                                } catch (ScriptException ex) {
                                                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            } else {
                                                try {
                                                    //cuando la variable no esta en el arreglo
                                                    System.out.println("no hay variable con ese nombre");
                                                    Variable variableNueva = new Variable();
                                                    variableNueva.setNombre(nombreVariables.get(i));
                                                    ScriptEngineManager mgr = new ScriptEngineManager();
                                                    ScriptEngine engine = mgr.getEngineByName("JavaScript");
                                                    //vamos a unir el string
                                                    unido = String.join("", arrayDeDerechos.get(i));
                                                    String ecuacion = unido;

                                                    System.out.println("voy a validar si la ecuacion no da numero como resultado");
                                                    if (Double.isNaN(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                                        variableNueva.setTexto("No es un numero.");
                                                        variableNueva.setTipo("texto");
                                                        variablesCopia.add(variableNueva);
                                                    } else {
                                                        System.out.println("voy a validar que esto no sea infinito");
                                                    }
                                                    if (Double.isInfinite(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                                        variableNueva.setTexto("Infinito.");
                                                        variableNueva.setTipo("texto");
                                                        variablesCopia.add(variableNueva);
                                                    } else {
                                                        System.out.println("voy a validar que esto este normal");
                                                    }
                                                    {
                                                        variableNueva.setTexto(engine.eval(ecuacion).toString());
                                                        variableNueva.setTipo("numero");
                                                        variablesCopia.add(variableNueva);
                                                    }
                                                    System.out.println(engine.eval(ecuacion));
                                                } catch (ScriptException ex) {
                                                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        } else if (tipo == 1) {
                                            //solo strings
                                            int cantidadDeSumas = 0;
                                            int candidadPalabras = 0;
                                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                                if (arrayDeDerechos.get(i).get(j).equals("+")) {
                                                    cantidadDeSumas++;
                                                }
                                            }
                                            for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                                if (!arrayDeDerechos.get(i).get(j).toString().replaceAll("[\\+]", "").equals("")) {
                                                    candidadPalabras++;
                                                }
                                            }
                                            if (!unido.replaceAll("[A-Za-z0-9\\+]", "").equals("")) {
                                                Alert alert = new Alert(AlertType.INFORMATION);
                                                Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                                                ImageView imageVie = new ImageView(images);
                                                alert.setGraphic(imageVie);
                                                alert.setTitle("Error.");
                                                alert.setHeaderText("Ocurrio un error.");
                                                alert.setContentText("No se puede concatenar strings si hay algun otro simbolo ademas de +.");
                                                alert.showAndWait();
                                                click = false;
                                            } else if (candidadPalabras != (cantidadDeSumas + 1)) {
                                                Alert alert = new Alert(AlertType.INFORMATION);
                                                Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                                                ImageView imageVie = new ImageView(images);
                                                alert.setGraphic(imageVie);
                                                alert.setTitle("Error.");
                                                alert.setHeaderText("Ocurrio un error.");
                                                alert.setContentText("Error en el formato.");
                                                alert.showAndWait();
                                                click = false;
                                            } else {
                                                //hora de reemplazar y concatenar
                                                for (int j = 0; j < arrayDeDerechos.get(i).size(); j++) {
                                                    if (arrayDeDerechos.get(i).get(j).toString().matches("[A-Za-z0-9]+")) {
                                                        tokensVariables.add(arrayDeDerechos.get(i).get(j).toString());
                                                    }
                                                }
                                                for (int j = 0; j < tokensVariables.size(); j++) {
                                                    for (int k = 0; k < variablesCopia.size(); k++) {
                                                        if (tokensVariables.get(j).equals(variablesCopia.get(k).getNombre())) {
                                                            palabrasTokensVariables.add(variablesCopia.get(k).getTexto());
                                                        }
                                                    }
                                                }
                                                Variable variableNueva = new Variable();
                                                variableNueva.setNombre(nombreVariables.get(i));
                                                variableNueva.setTexto(String.join("", palabrasTokensVariables));
                                                variableNueva.setTipo("texto");
                                                variablesCopia.add(variableNueva);
                                                System.out.println(String.join("", palabrasTokensVariables));
                                            }
                                        } else {
                                            //alguna otra cosa(error)
                                            operacionesFallidas++;
                                            i = arrayDeDerechos.size();
                                        }
                                    }
                                    if (operacionesFallidas > 0) {
                                        Alert alert = new Alert(AlertType.INFORMATION);
                                        Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                                        ImageView imageVie = new ImageView(images);
                                        alert.setGraphic(imageVie);
                                        alert.setTitle("Error.");
                                        alert.setHeaderText("Ocurrio un error.");
                                        alert.setContentText("Alguna de las etapas ingresadas estaba incorrecta ya que no se estaba trabajando solo con numeros o solo con strings.");
                                        alert.showAndWait();
                                        click = false;
                                        variablesCopia.clear();
                                    } else {
                                        variables.clear();
                                        for (int i = 0; i < variablesCopia.size(); i++) {
                                            variables.add(variablesCopia.get(i));
                                        }
                                        variablesCopia.clear();
                                    }
                                    figura.setTextoFigura(etapa.getTextoFigura());
                                    repintar(cuadro);
                                    //aca deberia terminar la ejecucion de una etapa.
                                } else {
                                    Alert alert = new Alert(AlertType.INFORMATION);
                                    Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                                    ImageView imageVie = new ImageView(images);
                                    alert.setGraphic(imageVie);
                                    alert.setTitle("Error.");
                                    alert.setHeaderText("Ocurrio un error.");
                                    alert.setContentText("Alguna de las etapas ingresadas tenia un formato incorrecto.");
                                    alert.showAndWait();
                                    click = false;
                                }
                            }
                        }
                    }
                }

                if (figura instanceof Decision) {
                    Decision decision = new Decision();
                    decision.setTextoFigura(figura.getTextoFigura());
                    click = ingresarTexto(decision, "Decision");
                    if (click) {
                        Pattern p = Pattern.compile("([A-Za-z0-9]{1,30}([\\<|\\>]|[\\>|\\<|\\=|\\!]{2})([A-Za-z0-9]|([\\\"true\\\"]+|[\\\"true\\\"])){1,30}([\\<|\\>|\\!|\\=|\\&|\\|]{2})?)+");
                        Matcher matcher = p.matcher(decision.getTextoFigura());
                        boolean cadenavalida = matcher.matches();
                        if (cadenavalida) {
                            figura.setTextoFigura(decision.getTextoFigura());
                            repintar(cuadro);
                        }
                    }
                }
                if (figura instanceof InicioFin) {
                    InicioFin inicioFin = new InicioFin();
                    inicioFin.setTextoFigura(figura.getTextoFigura());
                    click = ingresarTexto(inicioFin, "Documento");
                    if (click) {
                        figura.setTextoFigura(inicioFin.getTextoFigura());
                        repintar(cuadro);
                    }
                }
                if (figura instanceof Ciclo) {
                    Ciclo ciclo = new Ciclo();
                    ciclo.setTextoFigura(figura.getTextoFigura());
                    click = ingresarTexto(ciclo, "Ciclo");
                    if (click) {
                        Pattern p = Pattern.compile("([A-Za-z0-9]{1,30}([\\<|\\>]|[\\>|\\<|\\=|\\!]{2})([A-Za-z0-9]|([\\\"true\\\"]+|[\\\"true\\\"])){1,30}([\\<|\\>|\\!|\\=|\\&|\\|]{2})?)+");
                        Matcher matcher = p.matcher(ciclo.getTextoFigura());
                        boolean cadenavalida = matcher.matches();
                        if (cadenavalida) {
                            figura.setTextoFigura(ciclo.getTextoFigura());
                            repintar(cuadro);
                        }
                    }
                }
                if (figura instanceof Documento) {
                    Documento documento = new Documento();
                    documento.setTextoFigura(figura.getTextoFigura());
                    click = ingresarTexto(documento, "Documento");
                    if (click) {
                        figura.setTextoFigura(documento.getTextoFigura());
                        repintar(cuadro);
                    }
                }
                if (figura instanceof Salida) {
                    Salida salida = new Salida();
                    salida.setTextoFigura(figura.getTextoFigura());
                    click = ingresarTexto(salida, "Salida");
                    if (click) {
                        String parentesisAbierto = "";
                        int parentesisAbiertos = 0;
                        String parentesisCerrado = "";
                        int parentesisCerrados = 0;
                        String ladoIzquierdo = "";
                        String ladoDerecho = "";
                        Pattern p = Pattern.compile("(\\\"[A-Za-z0-9\\ \\+\\-\\*\\/]{1,25}\\\"\\,\\ )((((\\(|\\))|(\\+|\\-|\\/|\\*)|([A-Za-z0-9])){3,40})|([A-Za-z0-9]{1,40}))");
                        Matcher matcher = p.matcher(salida.getTextoFigura());
                        boolean cadenaValida = matcher.matches();
                        if (cadenaValida) {
                            parentesisAbierto = salida.getTextoFigura().replaceAll("[\\\"\\,A-Za-z0-9\\ \\+\\-\\*\\/]", "");
                            parentesisCerrado = salida.getTextoFigura().replaceAll("[\\\"\\,A-Za-z0-9\\ \\+\\-\\*\\/]", "");
                            parentesisAbiertos = parentesisAbierto.length();
                            parentesisCerrados = parentesisCerrado.length();
                            if (parentesisAbiertos == parentesisCerrados) {
                                int posComa;
                                int posEspacio;
                                posComa = salida.getTextoFigura().indexOf(",");
                                posEspacio = salida.getTextoFigura().lastIndexOf(" ");
                                ladoIzquierdo = salida.getTextoFigura().substring(0, posComa);
                                ladoDerecho = salida.getTextoFigura().substring(posEspacio + 1, salida.getTextoFigura().length());
                                boolean validaLadoDerecho = true;
                                String expression = ladoDerecho;
                                String[] tokens = expression.replaceAll("\\s+", "").split("(?<=[-+/()])|(?=[-+/()])");
                                for (String token : tokens) {
                                    System.out.println(token);
                                }
                                ArrayList<String> arrayTokens = new ArrayList<>();
                                for (String token : tokens) {
                                    arrayTokens.add(token);
                                }
                                if (arrayTokens.size() == 1) {
                                    //trabajo en solo una variable
                                    //"el valor de a es",a

                                    //vemos si la variable existe o no en el arreglo
                                    boolean existeEnLasVariables = false;
                                    int posicionVariableIgual = 0;
                                    for (int i = 0; i < variables.size(); i++) {
                                        if (arrayTokens.get(0).equals(variables.get(i).getNombre())) {
                                            posicionVariableIgual = i;
                                            i = variables.size();
                                            existeEnLasVariables = true;
                                        }
                                    }
                                    if (existeEnLasVariables) {
                                        //reemplazar el texto por el valor de la variable
                                        ladoDerecho = variables.get(posicionVariableIgual).getTexto();
                                        System.out.println("existe en las variables");
                                        System.out.println(ladoIzquierdo + " " + ladoDerecho);
                                    } else {
                                        //reemplazar por 0 el valor de la variable
                                        ladoDerecho = Integer.toString(0);
                                        System.out.println("no existe en las variables");
                                        System.out.println(ladoIzquierdo + " " + ladoDerecho);
                                    }
                                } else {
                                    //validaciones del lado derecho
                                    //ver que no empieze el lado derecho con un )
                                    //variable=)
                                    if (validaLadoDerecho) {
                                        System.out.println("Llegue a validacion 1");
                                        if (arrayTokens.get(0).equals(")")) {
                                            validaLadoDerecho = false;
                                        }
                                    }
                                    //ver que no haya una letra o simbolo antes de un (
                                    //variable=a+b(
                                    if (validaLadoDerecho) {
                                        System.out.println("Llegue a validacion 2");
                                        if (arrayTokens.get(0).equals("(")) {
                                            for (int i = 1; i < arrayTokens.size(); i++) {
                                                if (arrayTokens.get(i).equals("(")) {
                                                    if (arrayTokens.get((i) - 1).matches("[A-Za-z0-9]")) {
                                                        validaLadoDerecho = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    //ver que no haya un simbolo justo antes de )
                                    //variable=a+b+)
                                    if (validaLadoDerecho) {
                                        System.out.println("Llegue a validacion 3");
                                        for (int i = 1; i < arrayTokens.size(); i++) {
                                            if (arrayTokens.get(i).equals(")")) {
                                                if (arrayTokens.get((i) - 1).matches("[\\+\\-\\*\\/]")) {
                                                    validaLadoDerecho = false;
                                                }
                                            }
                                        }
                                    }
                                    //valida que no haya ()
                                    //variable=()
                                    if (validaLadoDerecho) {
                                        System.out.println("Llegue a validacion 4");
                                        for (int i = 0; i < arrayTokens.size(); i++) {
                                            if (arrayTokens.get(i).equals(")")) {
                                                if ((i + 1) < arrayTokens.size() && arrayTokens.get(i + 1).matches("[\\(]")) {
                                                    validaLadoDerecho = false;
                                                }
                                            }
                                        }
                                    }
                                    //valida que no empieze con un simbolo
                                    //variable=+
                                    if (validaLadoDerecho) {
                                        System.out.println("Llegue a validacion 5");
                                        if (arrayTokens.get(0).matches("[\\+\\-\\*\\/]")) {
                                            validaLadoDerecho = false;
                                        }
                                    }
                                    //valida que no haya simbolo justo despues de (
                                    //variable=(+
                                    if (validaLadoDerecho) {
                                        System.out.println("Llegue a validacion 6");
                                        for (int i = 0; i < arrayTokens.size(); i++) {
                                            if (arrayTokens.get(i).equals("(")) {
                                                if ((i + 1) < arrayTokens.size() && arrayTokens.get(i + 1).matches("[\\+\\-\\*\\/]")) {
                                                    validaLadoDerecho = false;
                                                }
                                            }
                                        }
                                    }
                                    //valida que no haya )(
                                    //variable=)(
                                    if (validaLadoDerecho) {
                                        System.out.println("Llegue a validacion 7");
                                        for (int i = 0; i < arrayTokens.size(); i++) {
                                            if (arrayTokens.get(i).equals(")")) {
                                                if ((i + 1) < arrayTokens.size() && arrayTokens.get(i + 1).matches("[\\(]")) {
                                                    validaLadoDerecho = false;
                                                }
                                            }
                                        }
                                    }
                                    //si el lado derecho empieza con un ( avanzo a la siguiente posicion
                                    //si no, veo si la posicion actual es ( luego
                                    //pregunto si la anterior era una letra, numero(0-9) o simbolo, si lo era es un error.
                                    //si no, transformor el token anterior a numero y pregunto si es mayor a 9
                                    //si lo es tiro error.
                                    if (validaLadoDerecho) {
                                        System.out.println("Llegue a validacion 8");
                                        for (int i = 0; i < arrayTokens.size(); i++) {
                                            if (arrayTokens.get(0).equals("(")) {
                                                i++;
                                            } else if (arrayTokens.get(i).equals("(")) {
                                                if (arrayTokens.get(i - 1).matches("[A-Za-z0-9\\+\\-\\*\\/]")) {
                                                    validaLadoDerecho = false;
                                                } else if (Integer.parseInt(arrayTokens.get(i - 1)) > 9) {
                                                    validaLadoDerecho = false;
                                                }
                                            }
                                        }
                                    }
                                    //valida que no hayan 2 simbolos seguidos
                                    //variable=a+-b
                                    if (validaLadoDerecho) {
                                        System.out.println("Llegue a validacion 9");
                                        for (int i = 0; i < arrayTokens.size(); i++) {
                                            if (arrayTokens.get(i).matches("[\\+\\-\\*\\/]")) {
                                                if ((i + 1) < arrayTokens.size() && arrayTokens.get(i + 1).matches("[\\+\\-\\*\\/]")) {
                                                    validaLadoDerecho = false;
                                                }
                                            }
                                        }
                                    }
                                    //valida que la posicion final no sea (
                                    //variable=a+b(
                                    if (validaLadoDerecho) {
                                        System.out.println("Llegue a validacion 10");
                                        if (arrayTokens.get(arrayTokens.size() - 1).equals("(")) {
                                            validaLadoDerecho = false;
                                        }
                                    }
                                    //valida que la posicion final no sea un simbolo
                                    //variable=a+b+
                                    if (validaLadoDerecho) {
                                        System.out.println("Llegue a validacion 11");
                                        if (arrayTokens.get(arrayTokens.size() - 1).matches("[\\+\\-\\*\\/]")) {
                                            validaLadoDerecho = false;
                                        }
                                    }
                                    if (validaLadoDerecho) {
                                        ArrayList<String> variablesEnTokens = new ArrayList<>();
                                        ArrayList<Integer> posicionesVariablesEnArrayTokens = new ArrayList<>();
                                        ArrayList<String> valoresVariables = new ArrayList<>();
                                        for (int i = 0; i < arrayTokens.size(); i++) {
                                            System.out.println(arrayTokens.get(i).replaceAll("[0-9\\+\\-\\*\\/\\(\\)]", ""));
                                            if (!(arrayTokens.get(i).replaceAll("[0-9\\+\\-\\*\\/\\(\\)]", "").equals(""))) {
                                                variablesEnTokens.add(arrayTokens.get(i));
                                                posicionesVariablesEnArrayTokens.add(i);
                                            }
                                        }
                                        //print
                                        System.out.println("variables en tokens");
                                        for (int i = 0; i < variablesEnTokens.size(); i++) {
                                            System.out.println(variablesEnTokens.get(i));
                                        }
                                        System.out.println("posiciones variables en array tokens original");
                                        for (int i = 0; i < posicionesVariablesEnArrayTokens.size(); i++) {
                                            System.out.println(posicionesVariablesEnArrayTokens.get(i));
                                        }
                                        //
                                        boolean existe = false;
                                        if (!variablesEnTokens.isEmpty()) {
                                            System.out.println("variablesEnTokens no esta vacio");
                                            for (int i = 0; i < variablesEnTokens.size(); i++) {
                                                for (int j = 0; j < variables.size(); j++) {
                                                    System.out.println(variablesEnTokens.get(i));
                                                    System.out.println("coma");
                                                    System.out.println(variables.get(j).getNombre());
                                                    if (variablesEnTokens.get(i).equals(variables.get(j).getNombre())) {
                                                        valoresVariables.add(variables.get(j).getTexto());
                                                        existe = true;
                                                    }
                                                }
                                                if (existe == false) {
                                                    valoresVariables.add("0");
                                                } else {
                                                    existe = false;
                                                }
                                            }

                                            System.out.println("valores de variables en arraytokens");
                                            for (int i = 0; i < valoresVariables.size(); i++) {
                                                System.out.println(valoresVariables.get(i));
                                            }
                                            System.out.println("imprimir los tokens originales");
                                            for (int i = 0; i < arrayTokens.size(); i++) {
                                                System.out.println(arrayTokens.get(i));
                                            }
                                            System.out.println("llege al segundo if");
                                            for (int i = 0; i < posicionesVariablesEnArrayTokens.size(); i++) {
                                                arrayTokens.set(posicionesVariablesEnArrayTokens.get(i), valoresVariables.get(i));
                                            }
                                            System.out.println("imprimir los tokens despues: ");
                                            for (int i = 0; i < arrayTokens.size(); i++) {
                                                System.out.println(arrayTokens.get(i));
                                            }
                                        }
                                        variablesEnTokens.clear();
                                        posicionesVariablesEnArrayTokens.clear();
                                        valoresVariables.clear();
                                    }
                                    if (validaLadoDerecho) {
                                        try {
                                            Variable variableNueva = new Variable();
                                            variableNueva.setNombre(ladoIzquierdo);
                                            ScriptEngineManager mgr = new ScriptEngineManager();
                                            ScriptEngine engine = mgr.getEngineByName("JavaScript");
                                            //vamos a unir el string
                                            ladoDerecho = String.join("", arrayTokens);
                                            String ecuacion = ladoDerecho;
                                            if (Double.isNaN(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                                variableNueva.setTexto("No es un numero.");
                                            } else if (Double.isInfinite(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                                variableNueva.setTexto("Infinito.");
                                            } else {
                                                variableNueva.setTexto(engine.eval(ecuacion).toString());
                                            }
                                            System.out.println(variableNueva.getNombre() + variableNueva.getTexto());
                                        } catch (ScriptException ex) {
                                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    } else {
                                        Alert alert = new Alert(AlertType.INFORMATION);
                                        Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                                        ImageView imageVie = new ImageView(images);
                                        alert.setGraphic(imageVie);
                                        alert.setTitle("Error.");
                                        alert.setHeaderText("Ocurrio un error.");
                                        alert.setContentText("El formato ingresado es incorrecto o se usaron variables no declaradas en la operacion.");
                                        alert.showAndWait();
                                        click = false;
                                    }
                                }
                            } else {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                Image images = new Image(getClass().getResource("/Clases_Figura/Estilos/Error.png").toExternalForm());
                                ImageView imageVie = new ImageView(images);
                                alert.setGraphic(imageVie);
                                alert.setTitle("Parentesis.");
                                alert.setHeaderText("Ocurrio un error.");
                                alert.setContentText("La cantidad de parentesis abiertos y cerrados no concuerda.");
                                alert.showAndWait();
                                click = false;
                            }
                        }
                        figura.setTextoFigura(salida.getTextoFigura());
                        repintar(cuadro);
                    }
                }
                System.out.println("IMPRIMOS TODAS LAS VARIABLES EXISTENTES");
                for (int i = 0; i < variables.size(); i++) {
                    System.out.println("variable " + (i + 1) + " " + variables.get(i).getNombre() + ", " + variables.get(i).getTexto() + ", " + variables.get(i).getTipo());
                }
            }
            lienzo.setOnMouseClicked(null);// se termina el evento setOnMouseClicked
        });

    }

    @FXML
    private void guardarPDF(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("pdf files (.pdf)", ".pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(primaryStage);
        OutputStream archivo = null;
        try {
            archivo = new FileOutputStream(file);
        } catch (Exception e) {
        }

        WritableImage writableImage = lienzo.snapshot(new SnapshotParameters(), null);
        lienzo.snapshot(null, writableImage);
        File file2 = new File("chart.png");
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        try {
            ImageIO.write(renderedImage, "png", file2);
        } catch (IllegalArgumentException r) {
        }
        try {
            com.itextpdf.text.Rectangle rectangle = new com.itextpdf.text.Rectangle((float) lienzo.getWidth() + 200, (float) lienzo.getHeight() + 200);
            Document doc = new Document(rectangle);
            PdfWriter.getInstance(doc, archivo);
            com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance("chart.png");
            img.setBorderColor(BaseColor.BLACK);
            doc.setMargins(0, (float) lienzo.getWidth() + 200, 0, (float) lienzo.getHeight() + 200);
            doc.open();
            doc.add(img);
            doc.close();
            file2.delete();
        } catch (Exception e) {

        }
    }

    public void DetectarDecisionDer(Decision Condicional) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el cuadro del canvas
        for (int i = formas.size() - 1; i > 0; i--) {
            Figura d = formas.get(i);
            if (d instanceof Decision) {
                for (int j = 0; j < ((Decision) d).getVerdaderas().size(); j++) {
                    Figura d2 = ((Decision) d).getVerdaderas().get(j);
                    if (d2 instanceof Decision) {
                        if (d2.getID() == Condicional.getID()) {
                            System.out.println("El Condicional: " + Condicional.getTextoFigura() + "- Esta anidado en el condi"
                                    + "cional: " + d.getTextoFigura());
                            System.out.println("\n");
                            Flujo der = ((Decision) d).getFinalDerecho();
                            Flujo in = new Flujo();
                            Flujo in2 = new Flujo();
                            for (int k = 0; k < enlaces.size(); k++) {
                                if (enlaces.get(k).getId() == Condicional.getFlujoInferior()) {
                                    in = enlaces.get(k);
                                }
                                if (enlaces.get(k).getId() == d.getFlujoInferior()) {
                                    in2 = enlaces.get(k);
                                }
                            }
                            Flujo iz = ((Decision) d).getFinalIzquierdo();
                            iz.dibujar(iz.getX(), iz.getY(), iz.getX1(), ((Decision) d2).getFinalDerecho().getY2() + 90, cuadro);
                            in.dibujar(in.getX(), ((Decision) d2).getFinalDerecho().getY2(), in.getX1(), ((Decision) d2).getFinalDerecho().getY2() + 90, cuadro);
                            in2.dibujar(in2.getX(), ((Decision) d2).getFinalDerecho().getY2() + 90, in2.getX1(), in2.getY2(), cuadro);
                            repintar(cuadro);
                            DetectarDecisionDer((Decision) d);

                        }
                    }
                }

            }
        }

    }

    public void DetectarDecisionIzq(Decision Condicional) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el cuadro del canvas
        for (int i = formas.size() - 1; i > 0; i--) {
            Figura d = formas.get(i);
            if (d instanceof Decision) {
                for (int j = 0; j < ((Decision) d).getFalsas().size(); j++) {
                    Figura d2 = ((Decision) d).getFalsas().get(j);
                    if (d2 instanceof Decision) {
                        if (d2.getID() == Condicional.getID()) {
                            System.out.println("El Condicional: " + Condicional.getTextoFigura() + "- Esta anidado en el condi"
                                    + "cional: " + d.getTextoFigura());
                            System.out.println("\n");
                            Flujo der = ((Decision) d).getFinalDerecho();
                            Flujo in = new Flujo();
                            Flujo in2 = new Flujo();
                            for (int k = 0; k < enlaces.size(); k++) {
                                if (enlaces.get(k).getId() == Condicional.getFlujoInferior()) {
                                    in = enlaces.get(k);
                                }
                                if (enlaces.get(k).getId() == d.getFlujoInferior()) {
                                    in2 = enlaces.get(k);
                                }
                            }
                            Flujo iz = ((Decision) d).getFinalIzquierdo();
                            der.dibujar(der.getX(), der.getY(), der.getX1(), ((Decision) d2).getFinalDerecho().getY2() + 90, cuadro);
                            in.dibujar(in.getX(), ((Decision) d2).getFinalDerecho().getY2(), in.getX1(), ((Decision) d2).getFinalDerecho().getY2() + 90, cuadro);
                            in2.dibujar(in2.getX(), ((Decision) d2).getFinalDerecho().getY2() + 90, in2.getX1(), in2.getY2(), cuadro);
                            repintar(cuadro);
                            DetectarDecisionIzq((Decision) d);

                        }
                    }
                }

            }
        }
    }

    public ArrayList<String> pseudocodigo = new ArrayList();

    public void crearPseudocodigo() {

        Figura aux = formas.get(0);
        System.out.println(">>PseudoCodigo");
        System.out.println(">> Algoritmo");
        pseudocodigo.add("Algoritmo " + aux.getTextoFigura());
        for (int i = 0; aux.getSiguiente() != -1; i++) {
            for (int j = 0; j < formas.size(); j++) {
                if (formas.get(j).getID() == aux.getSiguiente()) {
                    aux = formas.get(j);
                    if (identarCiclo(aux)) {
                    } else {
                        if (aux instanceof Etapa) {
                            pseudocodigo.add("    " + aux.getTextoFigura());

                        }
                        if (aux instanceof Entrada) {

                            pseudocodigo.add("    Entrada " + aux.getTextoFigura());

                        }
                        if (aux instanceof Salida) {
                            pseudocodigo.add("    Escribir " + aux.getTextoFigura());

                        }
                        if (aux instanceof Decision) {
                            imprimirDecision((Decision) aux, "    ");

                        }
                        if (aux instanceof Ciclo) {
                            //System.out.println(">>    Repetir");
                            //System.out.println(">>    Hasta que "+aux.getTextoFigura());
                            imprimirCiclos((Ciclo) aux, "    ");
                        }

                    }
                }
            }
        }

        pseudocodigo.add("Fin Algoritmo");
        pasarATXT();
    }

    public void codigoDecision(Figura aux, String identacion) {
        if (identarCiclo(aux)) {
        } else {
            if (aux instanceof Etapa) {
                pseudocodigo.add(identacion + aux.getTextoFigura());
                System.out.println(">>" + identacion + aux.getTextoFigura());
            }
            if (aux instanceof Entrada) {
                System.out.println(">>" + identacion + "Entrada " + aux.getTextoFigura());
                pseudocodigo.add(identacion + "Entrada " + aux.getTextoFigura());

            }
            if (aux instanceof Salida) {
                System.out.println(">>" + identacion + "Escribir " + aux.getTextoFigura());
                pseudocodigo.add(identacion + "Escribir " + aux.getTextoFigura());

            }
            if (aux instanceof Decision) {
                imprimirDecision((Decision) aux, identacion + "  ");

            }
            if (aux instanceof Ciclo) {
                imprimirCiclos((Ciclo) aux, identacion);

            }
        }

    }

    public void imprimirDecision(Decision decision, String identacion) {
        pseudocodigo.add(identacion + "Si " + decision.getTextoFigura() + " Entonces");
        Figura inicio = null;
        for (int w = 0; w < decision.getVerdaderas().size(); w++) {
            if (decision.getVerdaderas().get(w).getAnterior() == -8) {
                inicio = decision.getVerdaderas().get(w);
                codigoDecision(inicio, identacion + "  ");
            }
        }

        if (inicio != null) {
            for (int k = 0; inicio.getSiguiente() != -9; k++) {

                for (int l = 0; l < decision.getVerdaderas().size(); l++) {
                    if (decision.getVerdaderas().get(l).getID() == inicio.getSiguiente()) {
                        inicio = decision.getVerdaderas().get(l);
                        codigoDecision(inicio, identacion + "  ");
                    }
                }
            }
        }

        pseudocodigo.add(identacion + "SiNO ");

        Figura inicioF = null;
        for (int w = 0; w < decision.getFalsas().size(); w++) {
            if (decision.getFalsas().get(w).getAnterior() == -8) {
                inicioF = decision.getFalsas().get(w);
                codigoDecision(inicioF, identacion + "  ");
            }
        }

        if (inicioF != null) {
            for (int k = 0; inicioF.getSiguiente() != -9; k++) {
                for (int l = 0; l < decision.getFalsas().size(); l++) {
                    if (decision.getFalsas().get(l).getID() == inicioF.getSiguiente()) {
                        inicioF = decision.getFalsas().get(l);
                        codigoDecision(inicioF, identacion + "  ");
                    }
                }
            }
        }
        pseudocodigo.add(identacion + "FinSi ");

    }

    public boolean identarCiclo(Figura aux) {
        boolean algunCiclo = false;
        for (int i = 0; i < formas.size(); i++) {
            Figura ciclo = formas.get(i);
            if (ciclo instanceof Ciclo) {
                for (int j = 0; j < ((Ciclo) ciclo).getIdFormas().size() - 1; j++) {
                    if (aux.getID() == ((Ciclo) ciclo).getIdFormas().get(j)) {
                        algunCiclo = true;

                    }
                }

            }
        }
        return algunCiclo;

    }

    public void imprimirCiclos(Ciclo ciclo, String identacion) {
        pseudocodigo.add(identacion + "Repetir ");
        for (int i = 0; i < ciclo.getIdFormas().size() - 1; i++) {
            int id = ciclo.getIdFormas().get(i);
            for (int j = 0; j < formas.size(); j++) {

                Figura aux = formas.get(j);
                if (aux.getID() == id) {
                    if (aux instanceof Etapa) {
                        pseudocodigo.add(identacion + "  " + aux.getTextoFigura());
                        System.out.println(">>" + identacion + "  " + aux.getTextoFigura());
                    }
                    if (aux instanceof Entrada) {
                        System.out.println(">>" + identacion + "  " + "Entrada " + aux.getTextoFigura());
                        pseudocodigo.add(identacion + "  " + "Entrada " + aux.getTextoFigura());

                    }
                    if (aux instanceof Salida) {
                        System.out.println(">>" + identacion + "  " + "Escribir " + aux.getTextoFigura());
                        pseudocodigo.add(identacion + "  " + "Escribir " + aux.getTextoFigura());

                    }
                    if (aux instanceof Ciclo) {
                        imprimirCiclos((Ciclo) aux, identacion + "  ");

                    }

                }
            }

        }
        pseudocodigo.add(identacion + "Hasta que  " + ciclo.getTextoFigura());

    }

    public void pasarATXT() {
        try {

            File archivo = new File("pseudocodigo.txt");// Se crea el Archivo
            FileWriter escribir = new FileWriter(archivo, true);
            BufferedWriter bw = new BufferedWriter(escribir);
            bw.newLine();
            for (int i = 0; i < pseudocodigo.size(); i++) {
                escribir.write(pseudocodigo.get(i) + "\r\n");
            }

            pseudocodigo.clear();
            escribir.close();
        } catch (Exception e) {
            System.out.println("Error al escribir");
        }

    }
}
