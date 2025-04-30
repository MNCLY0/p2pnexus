package org.p2pnexus.cliente.ventanas;

import com.p2pnexus.comun.exepciones.GestorDeVentanasExeption;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

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
