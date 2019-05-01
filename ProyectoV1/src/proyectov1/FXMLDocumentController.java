package proyectov1;

import Clases_Figura.Documento;
import Clases_Figura.EntradaSalida;
import Clases_Figura.Etapa;
import Clases_Figura.Figura;
import Clases_Figura.Flujo;
import Clases_Figura.InicioFin;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    @FXML
    Button borrarAll;

    @FXML
    AnchorPane root;

    @FXML
    Canvas lienzo;

    @FXML
    Button Etapa;

    @FXML
    Button EntradaSalida;

    @FXML
    Button Correr;

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
                    Image image = new Image(getClass().getResourceAsStream("/Clases_Figura/Estilos/flecha.png"));
                    cuadro.drawImage(image, corriendo.getMedioX() - 280, corriendo.getMedioY());
                    Thread.sleep(2000);
                    cuadro.clearRect(corriendo.getMedioX() - 280, corriendo.getMedioY(), 120, 65);
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
            } else {
                return null;
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
                }
                if (Aux != null) {
                    System.out.println("Entreee");
                    lienzo.setOnMouseDragged(en -> {
                        if (Aux != null && reiniciarHilo == true) {
                            for (int i = 0; i < enlaces.size(); i++) {
                                Flujo link = enlaces.get(i);
                                if (link.getX() == Aux.getMedioX() && link.getY() == Aux.getMedioY()) {
                                    link.dibujar((int) en.getX(), (int) en.getY(), link.getX1(), link.getY2(), cuadro);
                                    System.out.println("Entre");
                                } else if (link.getX1() == Aux.getMedioX() && link.getY2() == Aux.getMedioY()) {
                                    link.dibujar(link.getX(), link.getY(), (int) en.getX(), (int) en.getY(), cuadro);
                                    System.out.println("Entre");
                                } else if (link.getX() == Aux.getMedioX() && link.getY() == Aux.getMedioY() + 70) {
                                    link.dibujar((int) en.getX(), (int) en.getY() + 70, link.getX1(), link.getY2(), cuadro);
                                    System.out.println("Entre");
                                } else if (link.getX1() == Aux.getMedioX() && link.getY2() == Aux.getMedioY() + 70) {
                                    link.dibujar(link.getX(), link.getY(), (int) en.getX(), (int) en.getY() + 70, cuadro);
                                    System.out.println("Entre");
                                }

                                enlaces.set(i, link);
                            }

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

                        Figura b = detectarFigura1((int) p.getX(), (int) p.getY());
                        int px = (int) p.getX();
                        int py = (int) p.getY();
                        if (b != null) {
                            System.out.println("Superposicion");
                        }
                        if (Aux != null && b != null) {
                            if (py >= b.getY1() && py <= b.getY3() && px >= b.getX1() && px <= b.getX2()) {
                                System.out.println("Sobre mi mismo");
                                Aux = null;
                            } else {
                                System.out.println("Dibujar");
                                Aux = null;
                            }

                        }

                    });

                }
            } else {
                System.out.println("No hay nada");

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
    private void dibujarEntradaSalida(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        EntradaSalida entrada = new EntradaSalida();
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
        if (click == true) {
            cut(entrada);
        }

    }

    @FXML
    private void dibujarInicioFin(ActionEvent event) {
        GraphicsContext cuadro = lienzo.getGraphicsContext2D();
        InicioFin inicioFin = new InicioFin();
        //String respuesta = JOptionPane.showInputDialog("Ingrese texto: ");
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Text de inicio.");
        dialog.setHeaderText("");
        dialog.setContentText("Ingrese el texto que va en el inicio:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            inicioFin.setTextoFigura(result.get());
        }
        //inicioFin.setTextoFigura(respuesta);
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
                    }
                } else if ((e.getY() + 70) > lienzo.getHeight()) {
                    lienzo.setHeight(lienzo.getHeight() + 80);
                    inicioFin.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    inicioFin.setNombre("Inicio " + numero);
                    numero++;
                    formas.add(inicioFin);
                    click = false;
                } else if ((e.getX() + 70) > lienzo.getWidth()) {
                    lienzo.setWidth(lienzo.getWidth() + 250);
                    inicioFin.dibujar(cuadro, (int) e.getX(), (int) e.getY());
                    inicioFin.setNombre("Inicio " + numero);
                    numero++;
                    formas.add(inicioFin);
                    click = false;
                }
            });
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

    int d = 1;
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
                 if((aux.getX()== aux.getX1())||aux.getX1()>=aux.getX()-20 || aux.getX1()<=aux.getX()+20){
                     System.out.println("son iguales");
            if((int)e.getX()<= aux.getX()+30&&(int)e.getX()>=aux.getX()-30){
                if ((int) e.getY() >= aux.getY() && (int) e.getY() <= aux.getY2()) {// se pregunta si el xy del Click esta dentro de un enlace
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
        formas.add(inicio);
        formas.add(fin);

        moverFigura(cuadro, lienzo);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ini();
    }

}
