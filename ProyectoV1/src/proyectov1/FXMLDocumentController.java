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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
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

        public void correrDecision(Figura aux, Figura corriendo) throws ScriptException, InterruptedException {
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
                if (infinity == 0) {
                    primerA = a1;
                    primerB = b1;
                }
                if (a1.equals(primerA)) {
                    infinity++;
                }

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

                Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_azul.png"));
                cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());

                Thread.sleep(2000);
                cuadro.clearRect(corriendo.getMedioX() - 230, corriendo.getMedioY(), 60, 60);

                if (c.isVerdadero()) {
                    Figura inicio2 = null;
                    System.out.println(">> C size: " + c.getVerdaderas().size());
                    for (int w = 0; w < c.getVerdaderas().size(); w++) {
                        if (c.getVerdaderas().get(w).getAnterior() == -8) {
                            inicio2 = c.getVerdaderas().get(w);

                        }
                    }
                    if (inicio2 != null) {

                        Figura corriendo2 = inicio2;
                        if (inicio2 instanceof Etapa) {
                            correrEtapa(corriendo2);
                            System.out.println(">> Entre a la Etapa");
                        }
                        if (inicio2 instanceof Salida) {
                            correrSalida((Salida) inicio2);
                        }
                        if (inicio2 instanceof Entrada) {
                            correrEntrada(corriendo2);
                        }
                        if (inicio2 instanceof Decision) {
                            correrDecision(inicio2, corriendo2);

                        }
                        Image image2 = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_azul.png"));
                        cuadro.drawImage(image2, inicio2.getMedioX() - 230, inicio2.getMedioY());
                        Thread.sleep(2000);
                        cuadro.clearRect(inicio2.getMedioX() - 230, inicio2.getMedioY(), 60, 60);
                        for (int k = 0; inicio2.getSiguiente() != -9; k++) {
                            for (int l = 0; l < c.getVerdaderas().size(); l++) {
                                if (c.getVerdaderas().get(l).getID() == inicio2.getSiguiente()) {
                                    inicio2 = c.getVerdaderas().get(l);
                                    Figura corriendo3 = inicio2;
                                    if (inicio2 instanceof Etapa) {
                                        correrEtapa(corriendo3);
                                        System.out.println(">> Entre a la Etapa");
                                    }
                                    if (inicio2 instanceof Salida) {
                                        correrSalida((Salida) inicio2);
                                    }
                                    if (inicio2 instanceof Entrada) {
                                        correrEntrada(corriendo3);
                                    }

                                    if (inicio2 instanceof Decision) {
                                        correrDecision(inicio2, corriendo2);
                                    }
                                    image2 = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_azul.png"));
                                    cuadro.drawImage(image2, inicio2.getMedioX() - 230, inicio2.getMedioY());

                                    Thread.sleep(2000);
                                    cuadro.clearRect(inicio2.getMedioX() - 230, inicio2.getMedioY(), 60, 60);
                                }
                            }
                        }

                    }

                } else {

                    Figura inicio2 = null;
                    System.out.println(">> C size: " + c.getVerdaderas().size());
                    for (int w = 0; w < c.getFalsas().size(); w++) {
                        if (c.getFalsas().get(w).getAnterior() == -8) {
                            inicio2 = c.getFalsas().get(w);

                        }
                    }
                    if (inicio2 != null) {

                        Figura corriendo2 = inicio2;
                        if (inicio2 instanceof Etapa) {
                            correrEtapa(corriendo2);
                            System.out.println(">> Entre a la Etapa");
                        }
                        if (inicio2 instanceof Salida) {
                            correrSalida((Salida) inicio2);
                        }
                        if (inicio2 instanceof Entrada) {
                            correrEntrada(corriendo2);
                        }
                        if (inicio2 instanceof Decision) {
                            correrDecision(inicio2, corriendo2);

                        }
                        Image image2 = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_azul.png"));
                        cuadro.drawImage(image2, inicio2.getMedioX() - 230, inicio2.getMedioY());
                        Thread.sleep(2000);
                        cuadro.clearRect(inicio2.getMedioX() - 230, inicio2.getMedioY(), 60, 60);
                        for (int k = 0; inicio2.getSiguiente() != -9; k++) {
                            for (int l = 0; l < c.getFalsas().size(); l++) {
                                if (c.getFalsas().get(l).getID() == inicio2.getSiguiente()) {
                                    inicio2 = c.getFalsas().get(l);
                                    Figura corriendo3 = inicio2;
                                    if (inicio2 instanceof Etapa) {
                                        correrEtapa(corriendo3);
                                        System.out.println(">> Entre a la Etapa");
                                    }
                                    if (inicio2 instanceof Salida) {
                                        correrSalida((Salida) inicio2);
                                    }
                                    if (inicio2 instanceof Entrada) {
                                        correrEntrada(corriendo3);
                                    }

                                    if (inicio2 instanceof Decision) {
                                        correrDecision(inicio2, corriendo2);

                                    }

                                    image2 = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_azul.png"));
                                    cuadro.drawImage(image2, inicio2.getMedioX() - 230, inicio2.getMedioY());

                                    Thread.sleep(2000);
                                    cuadro.clearRect(inicio2.getMedioX() - 230, inicio2.getMedioY(), 60, 60);
                                }
                            }
                        }

                    }
                }

            } else {
                corriendo = formas.get(1);
                aux = formas.get(1);
                System.out.println(">> La decision no es 1-1");

            }

        }
        int infinity = 0;

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

        public void correrEntrada(Figura corriendo) {
            System.out.println("Soy una entrada");
            String expression = corriendo.getTextoFigura();
            expression = expression.replaceAll("=", ")");
            expression = expression.replaceAll(",", "(");
            System.out.println("expression= " + expression);
            String[] tokens = expression.replaceAll("\\s+", "").split("(?<=[-+*/()])|(?=[-+*/()])");

            ArrayList<String> nombresVar = new ArrayList();
            for (int k = 0; k < tokens.length; k++) {
                if (tokens[k].equals(")")) {
                    nombresVar.add(tokens[k - 1]);
                }

            }

            String var = tokens[0];
            for (String token : tokens) {
                System.out.println(token);
            }
            System.out.println("Var: " + var);
            ArrayList<Variable> vars = new ArrayList();
            Variable mostrar = null;
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

        public void correrEtapa(Figura corriendo) throws ScriptException, ScriptException {
            System.out.println("Soy una etapa");
            String expression = corriendo.getTextoFigura();
            expression = expression.replaceAll("=", "_");
            System.out.println("expression= " + expression);
            String[] tokens = expression.replaceAll("\\s+", "").split("(?<=[-+*/()_])|(?=[-+*/()_])");

            Variable m = null;
            String var = tokens[0];
            for (int k = 0; k < variables.size(); k++) {
                if (variables.get(k).getNombre().equals(var)) {
                    m = variables.get(k);
                }
            }

            String ladoDerecho = "";
            ArrayList<String> vars = new ArrayList();
            for (int k = 2; k < tokens.length; k++) {
                ladoDerecho = ladoDerecho + tokens[k];
                vars.add(tokens[k]);

            }

            for (int k = 0; k < vars.size(); k++) {
                boolean exist = false;
                if (vars.get(k).matches("([A-Za-z0-9]+)")) {
                    boolean resultado1;
                    try {
                        Integer.parseInt(vars.get(k));
                        resultado1 = true;
                    } catch (NumberFormatException excepcion) {
                        resultado1 = false;
                    }

                    if (resultado1 != true) {
                        for (int l = 0; l < variables.size(); l++) {
                            if (variables.get(l).getNombre().equals(vars.get(k))) {
                                vars.set(k, variables.get(l).getTexto());
                                exist = true;
                            }
                        }
                        if (exist == false) {
                            vars.set(k, "0");

                        } else {
                            exist = true;

                        }

                    }

                }

            }

            String ladoDer = "";
            for (int l = 0; l < vars.size(); l++) {
                ladoDer = ladoDer + vars.get(l);
            }
            System.out.println(">>Lado Derecho despues: " + ladoDerecho);

            boolean existeString = false;

            for (int k = 0; k < vars.size(); k++) {
                System.out.println(">>>y: " + vars.get(k));
            }
            for (int k = 0; k < vars.size(); k++) {
                if (vars.get(k).matches("([A-Za-z0-9]+)")) {
                    boolean resultado1;
                    System.out.println(">>> K: " + vars.get(k));

                    try {
                        Integer.parseInt(vars.get(k));
                        resultado1 = true;
                    } catch (NumberFormatException excepcion) {
                        resultado1 = false;
                    }

                    System.out.println(">> Resultado: " + resultado1);
                    if (resultado1 != true) {
                        existeString = true;
                    }

                }
            }

            System.out.println(">> antes de evaluar Llegue");
            if (existeString == false && m != null) {
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                System.out.println(">>Lado Derecho: " + ladoDer);
                m.setTexto(engine.eval(ladoDer).toString());
                for (int k = 0; k < variables.size(); k++) {
                    if (m.getNombre().equals(variables.get(k).getNombre())) {
                        variables.set(k, m);
                    }
                }

            } else if (m != null) {
                ladoDer = ladoDer.replaceAll("([\\/\\+\\-\\(\\)\\*])", "");
                m.setTexto(ladoDer);
                for (int k = 0; k < variables.size(); k++) {
                    if (m.getNombre().equals(variables.get(k).getNombre())) {
                        variables.set(k, m);
                    }
                }

            }

            if (m != null) {
                consola.setText(consola.getText() + "\n" + m.getNombre() + " ← " + m.getTexto());
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

                            if (aux instanceof Etapa) {
                                consola.setStyle(" -fx-background-color: #0032FF;  -fx-text-fill:white; -fx-control-inner-background:#0032FF;");
                                correrEtapa(corriendo);
                            }
                            if (aux instanceof Salida) {
                                consola.setStyle(" -fx-background-color: #FF8B00;  -fx-text-fill:white; -fx-control-inner-background:#FF8B00;");

                                correrSalida((Salida) aux);
                            }
                            if (aux instanceof Entrada) {
                                consola.setStyle(" -fx-background-color: #00FF27;  -fx-text-fill:#FF0000; -fx-control-inner-background:#00FF27;");

                                correrEntrada(corriendo);
                            }
                            if (aux instanceof Decision) {
                                consola.setStyle(" -fx-background-color: #8A01BA;  -fx-text-fill:white; -fx-control-inner-background:#8A01BA;");

                                correrDecision(aux, corriendo);

                            }

                            if (aux instanceof Documento) {
                                consola.setStyle(" -fx-background-color: #E51616;  -fx-text-fill:white; -fx-control-inner-background:#E51616;");

                            }
                            if (aux instanceof Ciclo) {
                                Ciclo c = (Ciclo) aux;
                                consola.setStyle(" -fx-background-color: #E516D6;  -fx-text-fill:white; -fx-control-inner-background:#E516D6;");
                                System.out.println("Is Verdadero: " + c.isVerdadero());
                                if (c.isVerdadero() == false) {
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
                                    if (infinity == 0) {
                                        primerA = a1;
                                        primerB = b1;
                                    }
                                    if (a1.equals(primerA)) {
                                        infinity++;
                                    }

                                    boolean FinEjecucion = false;

                                    System.out.println(">>>a: " + a1);
                                    System.out.println(">>>b: " + b1);
                                    // ver si el simbolo es >
                                    if (simbolo.equals(">")) {
                                        if (resultado == true && resultadoB == true) {
                                            if (Integer.parseInt(a1) > Integer.parseInt(b1)) {
                                                c.setVerdadero(false);
                                            }
                                            if (Integer.parseInt(a1) < Integer.parseInt(b1)) {
                                                c.setVerdadero(true);
                                            }

                                        } else {

                                            FinEjecucion = true;
                                        }

                                    } else if (simbolo.equals("<")) {

                                        if (resultado == true && resultadoB == true) {
                                            if (Integer.parseInt(a1) < Integer.parseInt(b1)) {
                                                c.setVerdadero(false);
                                                System.out.println(">> Se volvio verdadero");

                                            } else if (Integer.parseInt(a1) > Integer.parseInt(b1)) {
                                                c.setVerdadero(true);

                                            }

                                        } else {
                                            System.out.println(">> Sigue siendo Falso");
                                            FinEjecucion = true;
                                        }

                                    } else if (simbolo.equals("=")) {
                                        if (a1.equals(b1)) {
                                            c.setVerdadero(false);
                                        }
                                        if (!a1.equals(b1)) {
                                            c.setVerdadero(true);
                                        }

                                    } else if (simbolo.equals("!=")) {
                                        if (a1.equals(b1) == false) {
                                            c.setVerdadero(true);
                                        }

                                    } else if (simbolo.equals("<=")) {

                                        if (resultado == true && resultadoB == true) {
                                            if (Integer.parseInt(a1) <= Integer.parseInt(b1)) {
                                                c.setVerdadero(false);

                                            } else if (Integer.parseInt(a1) >= Integer.parseInt(b1)) {
                                                c.setVerdadero(true);

                                            }

                                        } else {

                                            FinEjecucion = true;
                                        }
                                    } else if (simbolo.equals(">=")) {

                                        if (resultado == true && resultadoB == true) {
                                            if (Integer.parseInt(a1) >= Integer.parseInt(b1)) {
                                                c.setVerdadero(false);

                                            } else if (Integer.parseInt(a1) >= Integer.parseInt(b1)) {
                                                c.setVerdadero(true);

                                            }

                                        } else {

                                            FinEjecucion = true;
                                        }
                                    }

                                    for (int k = 0; k < c.getIdsFiguras().size(); k++) {
                                        System.out.println("Indice Ciclo: " + c.getIdsFiguras().get(k));
                                    }

                                    System.out.println(">> Entre al if del Ciclo");
                                    System.out.println("Corriendo text: " + corriendo.getTextoFigura());
                                    if (FinEjecucion == true) {
                                        aux = formas.get(1);
                                        corriendo = formas.get(1);
                                    }
                                    if (c.isVerdadero() == false && FinEjecucion != true) {
                                        aux = formas.get(c.getIdsFiguras().get(0));
                                        for (int k = 0; k < formas.size(); k++) {
                                            if (formas.get(k).getID() == aux.getAnterior()) {
                                                aux = formas.get(k);
                                            }
                                        }

                                    }
                                }

                            }

                            if (aux instanceof Decision) {

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

    int manual = 0;
    int indiceactual = 0;

    @FXML
    private void correrManual(ActionEvent event) throws ScriptException, InterruptedException {
        hilo corre = new hilo();

        if (manual >= 0 && manual < formas.size()) {
            GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el Lienzo del programa
            int contador = 0;
            Figura aux = null;
            if (indiceactual == 0) {
                aux = formas.get(0);
                CorrerAutomatico.setDisable(true);
            } else {
                aux = formas.get(indiceactual);

            }
            repintar(cuadro);
            Figura corriendo = aux;

            if (aux.getSiguiente() != -1) {
                for (int j = 0; j < formas.size(); j++) {
                    if (formas.get(j).getID() == aux.getSiguiente()) {
                        indiceactual = j;
                        if (aux instanceof Etapa) {
                            corre.correrEtapa(corriendo);
                            System.out.println(">>Entre a etapa");
                        }

                        if (aux instanceof Salida) {
                            corre.correrSalida((Salida) aux);

                            System.out.println(">>> Entre a la salida");
                        }

                        if (aux instanceof Entrada) {
                            corre.correrEntrada(corriendo);
                            System.out.println(">>> entre a la entrada");
                        }
                        if (aux instanceof Decision) {
                            correrDecision(aux, corriendo);

                        }
                        Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha_naranja.png"));
                        cuadro.drawImage(image, corriendo.getMedioX() - 230, corriendo.getMedioY());
                    }

                }

            } else {
                indiceactual = 0;

            }

        }
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
            formas.get(i).dibujar(cuadro, formas.get(i).getMedioX(), formas.get(i).getY1());
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
        TextInputDialog dialog = new TextInputDialog();
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
        String parentesisAbierto = "";
        int parentesisAbiertos = 0;
        String parentesisCerrado = "";
        int parentesisCerrados = 0;
        String ladoIzquierdo = "";
        String ladoDerecho = "";
        if (click) {
            boolean valida = true;
            Pattern p = Pattern.compile("([A-Za-z0-9]{1,7}\\=)((\\(|\\))|(\\+|\\-|\\/|\\*)|([A-Za-z0-9])){3,40}");
            Matcher matcher = p.matcher(etapa.getTextoFigura());
            boolean cadenaValida = matcher.matches();
            if (cadenaValida) {
                parentesisAbierto = etapa.getTextoFigura().replaceAll("[A-Za-z0-9\\+\\-\\*\\/\\=\\)]", "");
                parentesisCerrado = etapa.getTextoFigura().replaceAll("[A-Za-z0-9\\+\\-\\*\\/\\=\\(]", "");
                parentesisAbiertos = parentesisAbierto.length();
                parentesisCerrados = parentesisCerrado.length();
                if (parentesisAbiertos == parentesisCerrados) {
                    int posIgual;
                    int posVariableIgual = 0;
                    posIgual = etapa.getTextoFigura().indexOf("=");
                    ladoIzquierdo = etapa.getTextoFigura().substring(0, posIgual);
                    ladoDerecho = etapa.getTextoFigura().substring(posIgual + 1, etapa.getTextoFigura().length());
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
                    System.out.println("imprimos el array de tokens antes de evaluar el lado derecho");
                    for (int i = 0; i < arrayTokens.size(); i++) {
                        System.out.println(arrayTokens.get(i).toString());
                    }
                    //validaciones del lado derecho
                    //ver que no empieze el lado derecho con un )
                    //variable=)
                    if (validaLadoDerecho) {
                        System.out.println("Llegue a validacion 1");
                        if (arrayTokens.get(0).equals(")")) {
                            validaLadoDerecho = false;
                        }
                    }
                    //vemos que el lado derecho no sean puras letras
                    //a=popeye
                    if (validaLadoDerecho) {
                        System.out.println("Llegue a validacion 1");
                        if (ladoDerecho.replaceAll("[A-Za-z]", "").equals("")) {
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
                    //k
                    if (validaLadoDerecho) {
                        //hora de dejar la caga
                        //tipo = 0 trabajo solo con numeros
                        //tipo = 1 trabajo solo con strings(solo suma)
                        //tipo = 2 trabajo con string y numeros(solo multiplicacion)
                        int tipo = 0;
                        ArrayList<String> variablesTokensValidar = new ArrayList<>();
                        ArrayList<Integer> posicionesVariablesEnArrayTokensValidar = new ArrayList<>();
                        for (int i = 0; i < arrayTokens.size(); i++) {
                            if (!(arrayTokens.get(i).replaceAll("[0-9\\+\\-\\*\\/\\(\\)]", "").equals(""))) {
                                variablesTokensValidar.add(arrayTokens.get(i));
                                posicionesVariablesEnArrayTokensValidar.add(i);
                            }
                        }
                        int cantidadDeVariablesAValidar = variablesTokensValidar.size();
                        int cantidadDeVariablesStringEncontradas = 0;
                        if (!variablesTokensValidar.isEmpty()) {
                            for (int i = 0; i < variablesTokensValidar.size(); i++) {
                                for (int j = 0; j < variables.size(); j++) {
                                    if (variablesTokensValidar.get(i).equals(variables.get(j).getNombre())) {
                                        System.out.println(variables.get(j).getTipo());
                                        if (variables.get(j).getTipo().equals("texto")) {
                                            cantidadDeVariablesStringEncontradas++;
                                        }
                                    }
                                }
                            }
                        }
                        variablesTokensValidar.clear();
                        posicionesVariablesEnArrayTokensValidar.clear();
                        System.out.println("cantidad de variables tipo string encontradas");
                        System.out.println(cantidadDeVariablesStringEncontradas);
                        System.out.println("cantidad de variables a validar");
                        System.out.println(cantidadDeVariablesAValidar);
                        if (cantidadDeVariablesStringEncontradas == cantidadDeVariablesAValidar) {
                            tipo = 1;
                        }
                        //solo numeros
                        if (tipo == 0) {
                            ArrayList<String> variablesEnTokens = new ArrayList<>();
                            ArrayList<Integer> posicionesVariablesEnArrayTokens = new ArrayList<>();
                            ArrayList<String> valoresVariables = new ArrayList<>();
                            System.out.println("hora de imprimir todos los tokens en el arreglo para asi reemplazar");
                            for (int i = 0; i < arrayTokens.size(); i++) {
                                System.out.println("token original: " + arrayTokens.get(i).toString());
                                System.out.println("token sin num o simbolos: " + arrayTokens.get(i).replaceAll("[0-9\\+\\-\\*\\/\\(\\)]", ""));
                                if (!(arrayTokens.get(i).replaceAll("[0-9\\+\\-\\*\\/\\(\\)]", "").equals(""))) {
                                    variablesEnTokens.add(arrayTokens.get(i));
                                    posicionesVariablesEnArrayTokens.add(i);
                                }
                            }
                            //print
                            System.out.println("variables en tokens: ");
                            for (int i = 0; i < variablesEnTokens.size(); i++) {
                                System.out.println(variablesEnTokens.get(i));
                            }
                            System.out.println("posiciones variables en array tokens original: ");
                            for (int i = 0; i < posicionesVariablesEnArrayTokens.size(); i++) {
                                System.out.println(posicionesVariablesEnArrayTokens.get(i));
                            }
                            //
                            boolean existe = false;
                            if (!variablesEnTokens.isEmpty()) {
                                System.out.println("variablesEnTokens no esta vacio");
                                for (int i = 0; i < variablesEnTokens.size(); i++) {
                                    for (int j = 0; j < variables.size(); j++) {
                                        //System.out.println(variablesEnTokens.get(i));
                                        //System.out.println("coma");
                                        //System.out.println(variables.get(j).getNombre());
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
                                for (int i = 0; i < posicionesVariablesEnArrayTokens.size(); i++) {
                                    arrayTokens.set(posicionesVariablesEnArrayTokens.get(i), valoresVariables.get(i));
                                }
                            }
                            variablesEnTokens.clear();
                            posicionesVariablesEnArrayTokens.clear();
                            valoresVariables.clear();
                            System.out.println("el lado derecho esta validado");
                            boolean iguales = false;
                            for (int i = 0; i < variables.size(); i++) {
                                if (variables.get(i).getNombre().equals(ladoIzquierdo)) {
                                    iguales = true;
                                    posVariableIgual = i;
                                }
                            }
                            System.out.println("termine ese ciclo");
                            if (iguales) {
                                System.out.println("ya hay variable con ese nombre");
                                //cuando la variable ya esta en el arreglo
                                ScriptEngineManager mgr = new ScriptEngineManager();
                                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                                //vamos a unir el string
                                ladoDerecho = String.join("", arrayTokens);
                                String ecuacion = ladoDerecho;

                                if (Double.isNaN(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                    variables.get(posVariableIgual).setTexto("No es un numero.");
                                    variables.get(posVariableIgual).setTipo("texto");
                                } else if (Double.isInfinite(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                    variables.get(posVariableIgual).setTexto("Infinito.");
                                    variables.get(posVariableIgual).setTipo("texto");
                                } else {
                                    variables.get(posVariableIgual).setTexto(engine.eval(ecuacion).toString());
                                    variables.get(posVariableIgual).setTipo("numero");
                                }
                                System.out.println(engine.eval(ecuacion));
                                variables.get(posVariableIgual).setTexto(engine.eval(ecuacion).toString());
                            } else {
                                //cuando la variable no esta en el arreglo
                                System.out.println("no hay variable con ese nombre");
                                Variable variableNueva = new Variable();
                                variableNueva.setNombre(ladoIzquierdo);
                                ScriptEngineManager mgr = new ScriptEngineManager();
                                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                                //vamos a unir el string
                                ladoDerecho = String.join("", arrayTokens);
                                String ecuacion = ladoDerecho;

                                System.out.println("voy a validar si la ecuacion no da numero como resultado");
                                if (Double.isNaN(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                    variableNueva.setTexto("No es un numero.");
                                    variableNueva.setTipo("texto");
                                    variables.add(variableNueva);
                                } else {
                                    System.out.println("voy a validar que esto no sea infinito");
                                }
                                if (Double.isInfinite(Double.parseDouble(engine.eval(ecuacion).toString()))) {
                                    variableNueva.setTexto("Infinito.");
                                    variableNueva.setTipo("texto");
                                    variables.add(variableNueva);
                                } else {
                                    System.out.println("voy a validar que esto este normal");
                                }
                                {
                                    variableNueva.setTexto(engine.eval(ecuacion).toString());
                                    variableNueva.setTipo("numero");
                                    variables.add(variableNueva);
                                }
                                System.out.println(engine.eval(ecuacion));
                            }
                        } else //solo strings
                        //hola+chao
                        if (tipo == 1) {
                            int cantidadDeSumas = 0;
                            int candidadPalabras = 0;
                            for (int i = 0; i < arrayTokens.size(); i++) {
                                if (arrayTokens.get(i).equals("+")) {
                                    cantidadDeSumas++;
                                }
                            }
                            for (int i = 0; i < arrayTokens.size(); i++) {
                                if (!arrayTokens.get(i).replaceAll("[\\+]", "").equals("")) {
                                    candidadPalabras++;
                                }
                            }
                            if (!ladoDerecho.replaceAll("[A-Za-z0-9\\+]", "").equals("")) {
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
                                ArrayList<String> tokensVariables = new ArrayList<>();
                                ArrayList<String> palabrasTokensVariables = new ArrayList<>();
                                for (int i = 0; i < arrayTokens.size(); i++) {
                                    if (arrayTokens.get(i).matches("[A-Za-z0-9]+")) {
                                        tokensVariables.add(arrayTokens.get(i));
                                    }
                                }
                                for (int i = 0; i < tokensVariables.size(); i++) {
                                    for (int j = 0; j < variables.size(); j++) {
                                        if (tokensVariables.get(i).equals(variables.get(j).getNombre())) {
                                            palabrasTokensVariables.add(variables.get(j).getTexto());
                                        }
                                    }
                                }
                                Variable variableNueva = new Variable();
                                variableNueva.setNombre(ladoIzquierdo);
                                variableNueva.setTexto(String.join("", palabrasTokensVariables));
                                variableNueva.setTipo("texto");
                                variables.add(variableNueva);
                                System.out.println(String.join("", palabrasTokensVariables));
                            }
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
            for (int i = 0; i < variables.size(); i++) {
                System.out.println("variable " + (i + 1) + ". nombre: " + variables.get(i).getNombre() + ". lado derecho: " + variables.get(i).getTexto() + ". tipo: " + variables.get(i).getTipo());
            }
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
                        if (variable.getTexto().replaceAll("[0-9]", "").equals("")) {
                            variable.setTipo("numero");
                        } else {
                            variable.setTipo("texto");
                        }
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
                        if (variable1.getTexto().replaceAll("[0-9]", "").equals("")) {
                            variable1.setTipo("numero");
                        } else {
                            variable1.setTipo("texto");
                        }
                        if (variable2.getTexto().replaceAll("[0-9]", "").equals("")) {
                            variable2.setTipo("numero");
                        } else {
                            variable2.setTipo("texto");
                        }
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
                        if (variable1.getTexto().replaceAll("[0-9]", "").equals("")) {
                            variable1.setTipo("numero");
                        } else {
                            variable1.setTipo("texto");
                        }
                        if (variable2.getTexto().replaceAll("[0-9]", "").equals("")) {
                            variable2.setTipo("numero");
                        } else {
                            variable2.setTipo("texto");
                        }
                        if (variable3.getTexto().replaceAll("[0-9]", "").equals("")) {
                            variable3.setTipo("numero");
                        } else {
                            variable3.setTipo("texto");
                        }
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
                        if (variable1.getTexto().replaceAll("[0-9]", "").equals("")) {
                            variable1.setTipo("numero");
                        } else {
                            variable1.setTipo("texto");
                        }
                        if (variable2.getTexto().replaceAll("[0-9]", "").equals("")) {
                            variable2.setTipo("numero");
                        } else {
                            variable2.setTipo("texto");
                        }
                        if (variable3.getTexto().replaceAll("[0-9]", "").equals("")) {
                            variable3.setTipo("numero");
                        } else {
                            variable3.setTipo("texto");
                        }
                        if (variable4.getTexto().replaceAll("[0-9]", "").equals("")) {
                            variable4.setTipo("numero");
                        } else {
                            variable4.setTipo("texto");
                        }
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
        if (click) {
            boolean valida = true;
            Pattern p = Pattern.compile("[A-Za-z0-9]{1,10}([|>\\|<\\=]|[\\>|\\<|\\=|\\!]{2})[A-Za-z0-9]{1,10}");
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
        if (click) {
            boolean valida = true;
            Pattern p = Pattern.compile("[A-Za-z0-9]{1,10}([|>\\|<\\=]|[\\>|\\<|\\=|\\!]{2})[A-Za-z0-9]{1,10}");
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
        if (click) {
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

            separarFlujo(salida, cantidad);
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

    boolean edit = true;
    int xEdit;
    int yEdit;

    @FXML
    public void editFigura(ActionEvent event) throws Exception {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();// Se declara el lienzo
        edit = false;//el boolean edit se convierte en false para activar el boton
        if (edit == false) {//La condicion solo funciona si el borrar es igual a false
            lienzo.setOnMouseClicked(e -> {// se usa una funcion lambda junto al evento setOnMouseClicked
                Figura figura = detectarFigura1((int) e.getX(), (int) e.getY());// se llama al metodo detectarBorrar y se le ingresa un x e y
                if (figura != null) {
                    xEdit = (int) e.getX();
                    yEdit = (int) e.getY();
                    System.out.println("Se detecto la figura" + figura.getClass());
                    Decision.setDisable(true);
                    Ciclo.setDisable(true);
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
            int X = 0;
            int Y = 0;
            if (mover == null || edit == false) {
                if (edit == true) {
                    System.out.println("continua dibujando");
                    X = (int) e.getX();
                    Y = (int) e.getY();
                }
                if (edit == false) {
                    System.out.println("Estamos editando");
                    X = xEdit;
                    Y = yEdit;
                    detectarBorrar(xEdit, yEdit);
                }
                for (int i = 0; i < enlaces.size(); i++) {// se recorre el arreglo de lineas de flujo
                    Flujo aux = enlaces.get(i);// Se guarda el enlace i en una variable auxiliar
                    System.out.println("recorro el flujo");
                    if (X <= aux.getX() + 40 && X >= aux.getX() - 40 && Y >= aux.getY() && Y <= aux.getY2() || edit == false) {// se pregunta si el xy del Click esta dentro de un enlace
                        System.out.println("Entre");
                        Decision condicional = new Decision();
                        cuadro.clearRect(0, 0, lienzo.getWidth(), lienzo.getHeight());// se limpia el canvas
                        // se guardan el X e Y en una variable individual
                        int f = Y;
                        int o = X;
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
                                derecho.dibujar(o + 180, f + 30, o + 180, f + 200, cuadro);
                                izquierdo.dibujar(o - 180, f + 30, o - 180, f + 200, cuadro);
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
                                condicional.getFinalIzquierdo().dibujar(condicional.getFinalIzquierdo().getX(), condicional.getFinalIzquierdo().getY(), condicional.getFinalIzquierdo().getX1(), condicional.getFinalIzquierdo().getY2() + 70, cuadro);
                                nuevo.dibujar(aux.getX(), aux.getY(), o, f, cuadro);
                                aux.dibujar(o, f + 200, aux.getX1(), aux.getY2() + 140, cuadro);
                                n.dibujar(cuadro, o, f);
                                for (int j = 0; j < enlaces.size(); j++) {
                                    if (enlaces.get(j).getId() == condicional.getFlujoInferior()) {
                                        enlaces.get(j).dibujar(enlaces.get(j).getX(), aux.getY2(), enlaces.get(j).getX1(), enlaces.get(j).getY2(), cuadro);
                                    }
                                }

                            } else {
                                nuevo.dibujar(aux.getX(), aux.getY(), o, f, cuadro);
                                aux.dibujar(o, f + 70, aux.getX1(), aux.getY2() + 70, cuadro);
                                if (n instanceof Ciclo) {
                                    ((Ciclo) n).setConexionH(nuevo);
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
                                derecho.dibujar(o + 180, f + 30, o + 180, f + 300, cuadro);
                                izquierdo.dibujar(o - 180, f + 30, o - 180, f + 300, cuadro);
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
                                aux.dibujar(o, f + 300, aux.getX1(), aux.getY2(), cuadro);
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
                        if (edit == false) {
                            Decision.setDisable(false);
                            Ciclo.setDisable(false);
                            edit = true;
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
        System.out.println("*****Formas dentro del ciclo " + iteracion.getTextoFigura() + "*******");
        Flujo flujoCiclo = iteracion.getConexionH();
        Figura i2 = null;
        for (int i = 0; i < formas.size(); i++) {
            if (formas.get(i).getFlujoSuperior() == flujoCiclo.getId() && formas.get(i) instanceof Ciclo == false) {
                i2 = formas.get(i);
                iteracion.setIdsFiguras(i);
                iteracion.setIdsFiguras(i);
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

    @FXML
    public void ZoomPlus(ActionEvent event) {
        if (zoomP <= 2.1) {
            zoomP = zoomP + 0.1;
            lienzo.setScaleX(zoomP);
            lienzo.setScaleY(zoomP);

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

        reiniciarHilo = true;
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

}
