package org.p2pnexus.cliente;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hello world!
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
            Parent root = FXMLLoader.load(getClass().getResource("fxml/CargandoConexion.fxml"));
            primaryStage.setTitle("p2pnexus");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error al cargar la ventana de inicio de sesión.");
        }
    }
}
