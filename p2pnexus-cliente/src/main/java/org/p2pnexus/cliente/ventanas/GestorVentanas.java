package org.p2pnexus.cliente.ventanas;

import com.p2pnexus.comun.exepciones.GestorDeVentanasExeption;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GestorVentanas {

    public static VENTANAS ventanaActual = null;
    public static VENTANAS ventanaAnterior = null;

    public static StackPane ventanaPrincipal;

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

            // Buscamos el StackPane de la nueva escena, si no lo hay, simplemente se pone en null
            ventanaPrincipal = (StackPane) scene.lookup("#sp");

        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error al cargar la ventana: " + ventanaDestino.ruta);
            throw new GestorDeVentanasExeption("Error al cargar la ventana: " + e);
        }
    }
    public static void retrocederVentana(Scene scene) throws GestorDeVentanasExeption
    {
        if (ventanaAnterior == null) throw new GestorDeVentanasExeption("No hay ventana a la que volver");
        transicionarVentana(scene, ventanaAnterior);
    }

}
