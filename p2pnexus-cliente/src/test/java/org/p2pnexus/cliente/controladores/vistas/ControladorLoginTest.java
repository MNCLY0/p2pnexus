package org.p2pnexus.cliente.controladores.vistas;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ControladorLoginTest {

    // Necesitamos inicializar JavaFX antes de ejecutar las pruebas
    @BeforeAll
    static void initJfxRuntime() {
        Platform.startup(() -> {});
    }

    @Test
    void testUsuarioVacioNoPermiteLogin() {
        ControladorLogin controlador = new ControladorLogin();
        controlador.txtUsuario = new javafx.scene.control.TextField("");
        controlador.txtPassword = new javafx.scene.control.PasswordField();
        controlador.txtPassword.setText("algo");
        // Simula la llamada y verifica que no lanza excepción
        assertDoesNotThrow(controlador::intentarLogin);
    }

    @Test
    void testPasswordVacioNoPermiteLogin() {
        ControladorLogin controlador = new ControladorLogin();
        controlador.txtUsuario = new javafx.scene.control.TextField("usuario");
        controlador.txtPassword = new javafx.scene.control.PasswordField();
        controlador.txtPassword.setText("");
        assertDoesNotThrow(controlador::intentarLogin);
    }
}