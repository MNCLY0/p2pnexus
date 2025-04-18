package org.p2pnexus.cliente.controllers;

import javafx.scene.control.Button;

public class LoginController {

    public Button btnLogin;
    public Button btnRegistro;

    public void initialize() {
        // Inicializar el controlador
        btnLogin.setOnAction(event -> {
            // Lógica para el botón de inicio de sesión
            System.out.println("Botón de inicio de sesión presionado");
        });

        btnRegistro.setOnAction(event -> {
            // Lógica para el botón de registro
            System.out.println("Botón de registro presionado");
        });
    }



}
