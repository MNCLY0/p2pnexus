package org.p2pnexus.cliente.ventanas;

import com.p2pnexus.comun.exepciones.GestorDeVentanasExeption;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GestorVentanas {

    private static VENTANAS ventanaActual = null;
    private static VENTANAS ventanaAnterior = null;

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

    public static void transicionarVentana(VENTANAS ventanaDestino) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ventanaDestino.ruta);
            Parent vistaDestino = fxmlLoader.load();

            // Ahora podemos usar setAll() en la capa de contenido sin afectar las notificaciones
            capaContenido.getChildren().setAll(vistaDestino);

            ventanaAnterior = ventanaActual;
            ventanaActual = ventanaDestino;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static StackPane getCapaNotificaciones() {
        return capaNotificaciones;
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

}
