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

    private static StackPane stackPaneActual = null;

    public static void transicionarVentana(Scene scene, VENTANAS ventanaDestino)
    {
        try {
            // Guardamos la ventana actual
            if (ventanaActual != null)
            {
                ventanaAnterior = ventanaActual;
            }
            // Cambiamos la ventana actual
            ventanaActual = ventanaDestino;

            Stage stage = (Stage) scene.getWindow();

            // Guardamos las dimensiones actuales
            double width = stage.getWidth();
            double height = stage.getHeight();

            Parent destino = FXMLLoader.load(ventanaDestino.ruta);

            // Creamos la nueva escena manteniendo las dimensiones
            Scene nuevaEscena = new Scene(destino);
            stage.setScene(nuevaEscena);

            // Restauramos las dimensiones
            stage.setWidth(width);
            stage.setHeight(height);

            // Se debe de configurar manualmente el stackPane cuando se cambia de ventana
            stackPaneActual = null;

        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error al cargar la ventana: " + ventanaDestino.ruta);
            throw new GestorDeVentanasExeption("Error al cargar la ventana: " + e);
        }
    }
    // Necesitamos el stackPane para mostrar las notificaciones
    public static void configurarStackPane(StackPane stackPane)
    {
        stackPaneActual = stackPane;
    }

    public static StackPane getStackPane()
    {
        return stackPaneActual;
    }

    public static void retrocederVentana(Scene scene) throws GestorDeVentanasExeption
    {
        if (ventanaAnterior == null) throw new GestorDeVentanasExeption("No hay ventana a la que volver");
        transicionarVentana(scene, ventanaAnterior);
    }

}
