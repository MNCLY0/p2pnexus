package org.p2pnexus.cliente.controllers;

import com.p2pnexus.comun.exepciones.ConectarExeption;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.VENTANAS;

public class CargandoConexionController {

    public AnchorPane root;

    public void initialize() {
        Platform.runLater(this::conectar);
    }

    void conectar()
    {
        new Thread(() -> {
            // Intentamos conectar al servidor
            System.out.println("Iniciando conexión con el servidor...");
            boolean conectado = false;
            while (!conectado)
            {
                try {
                    // Intentamos conectar al servidor
                    Conexion.iniciarConexion();
                    conectado = true;
                    System.out.println("Conexión establecida con el servidor.");
                } catch (ConectarExeption e) {
                    // Si no se puede conectar, esperamos 5 segundos y lo intentamos de nuevo
                    System.out.println(e.getMessage());
                    System.out.println("No se pudo conectar al servidor. Reintentando en 5 segundos...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
            //Cerramos el thread cuando se conecta
            //Pasamos a la ventana de login
            Platform.runLater(this::pasarALogin);
            Thread.currentThread().interrupt();
        }).start();
    }

    void pasarALogin(){
        GestorVentanas.transicionarVentana(VENTANAS.LOGIN);
    }
}
