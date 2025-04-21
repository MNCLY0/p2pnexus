package org.p2pnexus.cliente.controllers;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.p2pnexus.cliente.server.Conexion;

public class LoginController {

    public Button btnLogin;
    public Button btnRegistro;
    public TextField txtUsuario;
    public PasswordField txtPassword;


    public void initialize() {
        // Inicializar el controlador
        btnLogin.setOnAction(event -> {
            System.out.println("Botón de inicio de sesión presionado");
            // Lógica para el botón de inicio de sesión

            JsonObject json = new JsonObject();
            json.addProperty("usuario", txtUsuario.getText());
            json.addProperty("pass", txtPassword.getText());
            Conexion.enviarMensaje(new Mensaje(TipoMensaje.P_LOGIN,json));
        });

        btnRegistro.setOnAction(event -> {
            // Lógica para el botón de registro
            System.out.println("Botón de registro presionado");
        });
    }



}
