package org.p2pnexus.cliente.ventanas;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GestorVentanas {

    public static void transicionarVentana(Scene scene, VENTANAS ventanaDestino)
    {
        try {
            Stage stage = (Stage) scene.getWindow();
            Parent destino = FXMLLoader.load(VENTANAS.LOGIN.ruta);
            stage.setScene(new Scene(destino));
        }catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error al cargar la ventana: " + ventanaDestino.ruta);
        }


    }

}
