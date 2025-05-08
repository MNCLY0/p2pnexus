package org.p2pnexus.cliente.ventanas;

import com.p2pnexus.comun.exepciones.GestorDeVentanasExeption;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GestorVentanas {

    private static Ventanas ventanaActual = null;
    private static Ventanas ventanaAnterior = null;

    private static StackPane contenedorPrincipal = null;
    private static StackPane capaContenido = null;
    private static StackPane capaNotificaciones = null;

    private static boolean inicializado = false;


    public static void inicializar(StackPane stackPane) {
        if (inicializado) {
            return;
        }
        contenedorPrincipal = stackPane;

        // Crear capas separadas para contenido y notificaciones
        capaContenido = new StackPane();
        capaNotificaciones = new StackPane();

        // Configurar propiedades de las capas
        capaContenido.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
        capaContenido.prefHeightProperty().bind(contenedorPrincipal.heightProperty());

        capaNotificaciones.prefWidthProperty().bind(contenedorPrincipal.widthProperty());
        capaNotificaciones.prefHeightProperty().bind(contenedorPrincipal.heightProperty());

        // Hacer que la capa de notificaciones sea transparente a los eventos del ratón
        capaNotificaciones.setPickOnBounds(false);

        contenedorPrincipal.getChildren().addAll(capaContenido, capaNotificaciones);

        inicializado = true;
    }

    public static void transicionarVentana(Ventanas ventanaDestino) {
        System.out.println("Transicionando a ventana: " + ventanaDestino);
        try {
            Parent vistaDestino = crearVentana(ventanaDestino);
            if (vistaDestino == null) {
                System.err.println("La vista destino es nula para " + ventanaDestino);
                return;
            }
            // Ahora podemos usar setAll() en la capa de contenido sin afectar las notificaciones
            Platform.runLater(() -> {
                capaContenido.getChildren().setAll(vistaDestino);
                ventanaAnterior = ventanaActual;
                ventanaActual = ventanaDestino;
            });

        } catch (IOException e) {
            System.err.println("Error al cargar la ventana: " + e);
            throw new RuntimeException(e);
        }
    }

    public static void abrirModal(Parent root, String titulo, Boolean escalable)
    {
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setResizable(escalable);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public static Parent crearVentana(Ventanas ventanaDestino) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(ventanaDestino.ruta);
            return fxmlLoader.load();
    }

    public static FXMLLoader crearFXMLoader(IEnumVistaCargable ventanaDestino) throws IOException {
        return new FXMLLoader(ventanaDestino.getRuta());
    }


    public static StackPane getCapaNotificaciones() {
        return capaNotificaciones;
    }

    public static void setCapaNotificaciones(StackPane capaNotificaciones) {
        GestorVentanas.capaNotificaciones = capaNotificaciones;
    }


    public static StackPane getContenedorPrincipal()
    {
        return contenedorPrincipal;
    }

    public static void retrocederVentana() throws GestorDeVentanasExeption
    {
        if (ventanaAnterior == null) throw new GestorDeVentanasExeption("No hay ventana a la que volver");
        transicionarVentana(ventanaAnterior);
    }

    public static String transformarDriveURL(String enlace) {
        if (enlace.contains("drive.google.com/file/d/")) {
            String id = enlace.split("/d/")[1].split("/")[0];
            return "https://drive.google.com/uc?export=view&id=" + id;
        }
        return enlace;
    }

    public static boolean pedirConfirmacion(String titulo, String mensaje, Alert.AlertType tipo, Scene owner) {
        Alert ventanaConfirmacion = new Alert(tipo);
        ventanaConfirmacion.setTitle("Confirmación");
        ventanaConfirmacion.setHeaderText(titulo);
        ventanaConfirmacion.setContentText(mensaje);

        ventanaConfirmacion.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        ventanaConfirmacion.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);


        ButtonType si = new ButtonType("Si", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);

        ventanaConfirmacion.getButtonTypes().setAll(si, no);
        ventanaConfirmacion.initOwner(owner.getWindow());
        ventanaConfirmacion.showAndWait();

        return ventanaConfirmacion.getResult() == si;
    }

    public static Image rutaAImagen(String ruta) {
        try {
            URL url = new URL(ruta);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestProperty("User-Agent", "Mozilla/5.0");
            InputStream inputStream = conexion.getInputStream();
            return new Image(inputStream);
        } catch (IOException e) {
            System.err.println("No se pudo cargar la imagen: " + e.getMessage());
            return null;
        }
    }

}
