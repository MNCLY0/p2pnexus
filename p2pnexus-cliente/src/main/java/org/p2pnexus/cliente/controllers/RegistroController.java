package org.p2pnexus.cliente.controllers;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Hasheador;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.exepciones.GestorDeVentanasExeption;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.ventanas.GestorVentanas;

public class RegistroController {

    @FXML
    public AnchorPane root;
    @FXML
    public Button btnCrearCuenta;
    @FXML
    public Button btnVolver;
    @FXML
    public TextField txtUsuario;
    @FXML
    public PasswordField txtPassword;

    @FXML
    public StackPane sp;

    @FXML
    public void initialize() {
        
        Platform.runLater(() ->
        {
            GestorVentanas.configurarStackPane(sp);
        });

        // Inicializar el controlador
        btnCrearCuenta.setOnAction(event -> {
            System.out.println("Botón de creacion de cuenta presionado");
            // Lógica para el botón de inicio de sesión
            JsonObject json = new JsonObject();
            json.addProperty("usuario", txtUsuario.getText());
            json.addProperty("pass", Hasheador.hashear(txtPassword.getText()));
            Conexion.enviarMensaje(new Mensaje(TipoMensaje.P_REGISTRO,json));
        });

        btnVolver.setOnAction(event -> {
            try {
                GestorVentanas.retrocederVentana(root.getScene());
            } catch (GestorDeVentanasExeption e) {
                System.err.println(e.getMessage());
            }
        });


    }



}
