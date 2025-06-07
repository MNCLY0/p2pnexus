package org.p2pnexus.cliente.controladores.vistas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Hasheador;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.p2pnexus.cliente.configuracion.Configuracion;
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
    public TextField txtPasswordVisible;
    @FXML
    public ImageView imagenLogo;
    @FXML
    public CheckBox chkVisibilidad;


    @FXML
    public void initialize() {

        Platform.runLater(()->
        {
            imagenLogo.setImage(new Configuracion().logoTema());
        });

        // Inicializar el controlador
        btnLogin.setOnAction(event -> {
            intentarLogin();
        });

        btnRegistro.setOnAction(event -> {
            System.out.println("Intentando pasar a registro");
            GestorVentanas.transicionarVentana(Ventanas.REGISTRO);
        });

        // si el usuario acaba de crear una cuenta, se le autocompleta el campo de usuario al mostrar la ventana de login
        if (!ControladorRegistro.ultimoUsuarioCreado.isEmpty())
        {
            txtUsuario.setText(ControladorRegistro.ultimoUsuarioCreado);
            ControladorRegistro.ultimoUsuarioCreado = "";
            txtPassword.requestFocus();
        }

        txtPassword.visibleProperty().bind(chkVisibilidad.selectedProperty().not());
        txtPassword.managedProperty().bind(chkVisibilidad.selectedProperty().not());

        txtPasswordVisible.visibleProperty().bind(chkVisibilidad.selectedProperty());
        txtPasswordVisible.managedProperty().bind(chkVisibilidad.selectedProperty());

        txtPassword.textProperty().bindBidirectional(txtPasswordVisible.textProperty());

    }

    @FXML
    public void intentarLogin(){
        // Validamos campos para que no esten vacíos
        if (txtUsuario.getText().trim().isEmpty() || txtPassword.getText().trim().isEmpty()) {
            Notificaciones.mostrarNotificacion("Usuario o contraseña vacíos.", TipoNotificacion.ERROR);
            return;
        }
        System.out.println("Intenti de nicio de sesión");

        // Lógica para el botón de inicio de sesión
        JsonObject json = new JsonObject();
        json.addProperty("usuario",txtUsuario.getText().trim());
        json.addProperty("pass", Hasheador.hashear(txtPassword.getText().trim()));
        Mensaje mensaje = new Mensaje(TipoMensaje.S_LOGIN,json);
        mensaje.getData();
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.S_LOGIN,json));
    }





}
