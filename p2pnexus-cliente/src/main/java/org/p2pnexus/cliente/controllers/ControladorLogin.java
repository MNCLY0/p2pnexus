package org.p2pnexus.cliente.controllers;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Hasheador;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Notificaciones;
import com.p2pnexus.comun.TipoNotificacion;
import org.p2pnexus.cliente.ventanas.Ventanas;

public class ControladorLogin {

    @FXML
    public Button btnLogin;
    @FXML
    public Button btnRegistro;
    @FXML
    public TextField txtUsuario;
    @FXML
    public PasswordField txtPassword;


    @FXML
    public void initialize() {

        // Inicializar el controlador
        btnLogin.setOnAction(event -> {

            // Validamos campos para que no esten vacíos
            if (txtUsuario.getText().trim().isEmpty() || txtPassword.getText().trim().isEmpty()) {
                Notificaciones.MostrarNotificacion("Usuario o contraseña vacíos.", TipoNotificacion.ERROR);
                return;
            }

            System.out.println("Botón de inicio de sesión presionado");
            // Lógica para el botón de inicio de sesión
            JsonObject json = new JsonObject();
            json.addProperty("usuario",txtUsuario.getText().trim());
            json.addProperty("pass", Hasheador.hashear(txtPassword.getText().trim()));
            Conexion.enviarMensaje(new Mensaje(TipoMensaje.P_LOGIN,json));

        });

        btnRegistro.setOnAction(event -> {
            System.out.println("Intentando pasar a registro");
            GestorVentanas.transicionarVentana(Ventanas.REGISTRO);
        });


    }



}
