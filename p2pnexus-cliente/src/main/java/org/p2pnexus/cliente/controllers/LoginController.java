package org.p2pnexus.cliente.controllers;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Peticion;
import javafx.scene.control.Button;
import org.p2pnexus.cliente.server.Conexion;

public class LoginController {

    public Button btnLogin;
    public Button btnRegistro;

    public void initialize() {
        // Inicializar el controlador
        btnLogin.setOnAction(event -> {
            System.out.println("Botón de inicio de sesión presionado");
            // Lógica para el botón de inicio de sesión

            JsonObject json = new JsonObject();
            json.addProperty("data", "El usuario ha pulsado el botón de inicio de sesión");
            Conexion.enviarPeticion(new Peticion("TEST",json));
        });

        btnRegistro.setOnAction(event -> {
            // Lógica para el botón de registro
            System.out.println("Botón de registro presionado");
        });
    }



}
