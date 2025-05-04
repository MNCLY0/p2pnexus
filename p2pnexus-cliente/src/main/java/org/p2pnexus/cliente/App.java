package org.p2pnexus.cliente;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.p2pnexus.cliente.ventanas.Ventanas;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
            Parent root = FXMLLoader.load(Ventanas.MAIN.getRuta());
            primaryStage.setTitle("p2pnexus");
            primaryStage.setScene(new Scene(root));
            // Limitamos el tamaño mínimo de la ventana
//            primaryStage.setMinWidth(1200);
//            primaryStage.setMinHeight(800);
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error al cargar la ventana de inicio de sesión.");
        }
    }
}
