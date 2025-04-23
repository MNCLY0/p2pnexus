package org.p2pnexus.cliente.controllers;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Notificaciones;
import com.p2pnexus.comun.TipoNotificacion;
import org.p2pnexus.cliente.ventanas.VENTANAS;

public class LoginController {

    @FXML
    public AnchorPane root;
    @FXML
    public Button btnLogin;
    @FXML
    public Button btnRegistro;
    @FXML
    public TextField txtUsuario;
    @FXML
    public PasswordField txtPassword;

    public StackPane sp;

    @FXML
    public void initialize() {

        Platform.runLater(() ->
        {
            GestorVentanas.configurarStackPane(sp);
        });


        // Inicializar el controlador
        btnLogin.setOnAction(event -> {
            System.out.println("Botón de inicio de sesión presionado");
            // Lógica para el botón de inicio de sesión

            JsonObject json = new JsonObject();
            json.addProperty("usuario",txtUsuario.getText().trim());
            json.addProperty("pass",txtPassword.getText().trim());
            Conexion.enviarMensaje(new Mensaje(TipoMensaje.P_LOGIN,json));
        });

        btnRegistro.setOnAction(event -> {
            GestorVentanas.transicionarVentana(root.getScene(), VENTANAS.REGISTRO);
        });


    }



}
