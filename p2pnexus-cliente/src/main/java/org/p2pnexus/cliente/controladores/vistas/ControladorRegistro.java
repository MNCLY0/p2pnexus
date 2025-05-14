package org.p2pnexus.cliente.controladores.vistas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Hasheador;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.exepciones.GestorDeVentanasExeption;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.p2pnexus.cliente.configuracion.Configuracion;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Notificaciones;
import org.p2pnexus.cliente.ventanas.Ventanas;

public class ControladorRegistro {


    @FXML
    public Button btnCrearCuenta;
    @FXML
    public Button btnVolver;
    @FXML
    public TextField txtUsuario;
    @FXML
    public PasswordField passfieldPassword;
    @FXML
    public PasswordField passfieldPasswordConfirmar;
    @FXML
    public TextField txtPassword;
    @FXML
    public TextField txtPasswordConfirmar;

    @FXML
    public CheckBox chkVisibilidad;

    public VBox iconoInfoPassword;

    public ImageView imagenLogo;

    public static String ultimoUsuarioCreado = "";

    @FXML
    public void initialize() {

        Platform.runLater(()->
        {
            imagenLogo.setImage(new Configuracion().logoTema());
        });

        crearInfo();

        // Inicializar el controlador
        btnCrearCuenta.setOnAction(event -> {
            System.out.println("Botón de creacion de cuenta presionado");
            // Lógica para el botón de inicio de sesión
            crearCuenta();
        });


        btnVolver.setOnAction(event -> {
            try {
                GestorVentanas.retrocederVentana();
            } catch (GestorDeVentanasExeption e) {
                System.err.println(e.getMessage());
            }
        });

        passfieldPassword.managedProperty().bind(chkVisibilidad.selectedProperty().not());
        passfieldPassword.visibleProperty().bind(chkVisibilidad.selectedProperty().not());

        txtPassword.managedProperty().bind(chkVisibilidad.selectedProperty());
        txtPassword.visibleProperty().bind(chkVisibilidad.selectedProperty());

        passfieldPasswordConfirmar.managedProperty().bind(chkVisibilidad.selectedProperty().not());
        passfieldPasswordConfirmar.visibleProperty().bind(chkVisibilidad.selectedProperty().not());

        txtPasswordConfirmar.managedProperty().bind(chkVisibilidad.selectedProperty());
        txtPasswordConfirmar.visibleProperty().bind(chkVisibilidad.selectedProperty());

        passfieldPasswordConfirmar.textProperty().bindBidirectional(txtPasswordConfirmar.textProperty());
        passfieldPassword.textProperty().bindBidirectional(txtPassword.textProperty());



    }

    @FXML
    public void crearCuenta() {
        if (!validarUsuario(txtUsuario.getText().trim())) {
            return;
        }

        if (validarPass(passfieldPassword.getText().trim())) {
            JsonObject json = new JsonObject();
            json.addProperty("usuario", txtUsuario.getText().trim());
            json.addProperty("pass", Hasheador.hashear(passfieldPassword.getText().trim()));
            Conexion.enviarMensaje(new Mensaje(TipoMensaje.S_REGISTRO,json));
            // Guardar el último usuario creado
            ultimoUsuarioCreado = txtUsuario.getText().trim();
            GestorVentanas.retrocederVentana();
        }
    }

    public void crearInfo()
    {
        // Crear el icono de información
        FontIcon icono = new FontIcon(Material2AL.INFO);
        var tooltip = new Tooltip("La contraseña debe tener al menos 8 caracteres, una letra mayúscula, una letra minúscula y un número");
        tooltip.setShowDelay(javafx.util.Duration.millis(100));
        tooltip.setHideOnEscape(true);
        iconoInfoPassword.getChildren().add(icono);
        Tooltip.install(iconoInfoPassword, tooltip);

    }

    public boolean validarUsuario(String usuario)
    {
        if (usuario.isEmpty()) {
            Notificaciones.mostrarNotificacion("El usuario no puede estar vacío", TipoNotificacion.AVISO);
            return false;
        }

        if (usuario.length() < 5) {
            Notificaciones.mostrarNotificacion("El usuario debe tener al menos 5 caracteres", TipoNotificacion.AVISO);
            return false;
        }
        return true;
    }

    public boolean validarPass(String pass) {
        // Validar la contraseña con todas las reglas
        boolean valida = true;
        String razon = "";

        if (pass.length() < 8) {
            razon = "La contraseña debe tener al menos 8 caracteres";
            valida = false;
        }
        if (!pass.matches(".*[a-z].*") && valida) {
            razon = "La contraseña debe tener al menos una letra minúscula";
            valida = false;
        }
        if (!pass.matches(".*[A-Z].*") && valida) {
            razon = "La contraseña debe tener al menos una letra mayúscula";
            valida = false;
        }
        if (!pass.matches(".*\\d.*") && valida) {
            razon = "La contraseña debe tener al menos un número";
            valida = false;
        }

        if (!pass.equals(passfieldPasswordConfirmar.getText().trim())) {
            razon = "Las contraseñas no coinciden";
            valida = false;
        }

        if (valida) return valida;
        // Si no es valida, mostramos en una notificacion de aviso
        Notificaciones.mostrarNotificacion(razon, TipoNotificacion.AVISO);
        return false;
    }
}
