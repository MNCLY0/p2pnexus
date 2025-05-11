package org.p2pnexus.cliente.controladores.vistasModales;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.ventanas.Animaciones;

import java.io.File;

public class ControladorCreacionEspacio {
    @FXML
    TextField textFieldNombre;
    @FXML
    TextField textFieldRuta;
    @FXML
    Button botonSeleccionarRuta;

    @FXML
    GridPane gridPanePrincipal;

    @FXML
    StackPane stackPanePrincipal;




    @FXML
    public void seleccionarRuta()
    {
       DirectoryChooser directoryChooser = new DirectoryChooser();
       directoryChooser.setTitle("Seleccionar ruta");
       File rutaSeleccionada = directoryChooser.showDialog(botonSeleccionarRuta.getScene().getWindow());
         if (rutaSeleccionada != null) {
              textFieldRuta.setText(rutaSeleccionada.getAbsolutePath());
         }
    }

    @FXML
    public void crearEspacio()
    {
        String nombre = textFieldNombre.getText();
        String ruta = textFieldRuta.getText();

        if (nombre.isEmpty()) {
            textFieldNombre.setPromptText("Introduce un nombre antes de crear el espacio");
            Animaciones.animarError(textFieldNombre);
            return;
        }

        if (ruta.isEmpty()) {
            textFieldRuta.setPromptText("Selecciona una ruta antes de crear el espacio");
            Animaciones.animarError(textFieldRuta);
            return;
        }

        JsonObject json = new JsonObject();
        json.addProperty("nombre", nombre);
        json.addProperty("ruta", ruta);
        json.addProperty("propietario_id", Sesion.getUsuario().getId_usuario());

        Mensaje mensaje = new Mensaje(TipoMensaje.S_CREAR_ESPACIO, json);
        Conexion.enviarMensaje(mensaje);

        Stage stage = (Stage) stackPanePrincipal.getScene().getWindow();
        stage.close();

    }


}
