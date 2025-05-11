package org.p2pnexus.cliente;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.p2pnexus.cliente.p2p.conexion.GestorP2P;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.lang.management.ManagementFactory;
import java.lang.management.PlatformLoggingMXBean;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
            Parent root = FXMLLoader.load(Ventanas.MAIN.getRuta());
            primaryStage.setTitle("p2pnexus");
            primaryStage.setScene(new Scene(root));
            primaryStage.setMaximized(true);

            // Nos aseguramos de que al terminar el programa se cierren todas las conexiones p2p que haya abiertas
            primaryStage.setOnCloseRequest(e -> {
                GestorP2P.cerrarConexiones();
                Platform.exit();
                System.exit(0);
            });

            primaryStage.show();

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error al cargar la ventana de inicio de sesión.");
        }
    }
}
