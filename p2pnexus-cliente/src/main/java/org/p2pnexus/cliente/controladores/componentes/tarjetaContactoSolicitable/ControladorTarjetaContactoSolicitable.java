package org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.p2pnexus.cliente.server.entitades.Usuario;

public class ControladorTarjetaContactoSolicitable {

    @FXML
    public Button botonEnviarSolicitud;

    @FXML
    public Label labelNombre;

    Usuario usuario;

    public void inicializarConUsuario(Usuario usuario)
    {
        this.usuario = usuario;
        labelNombre.setText(usuario.getNombre());
    }

    @FXML
    public void enviarSolicitud()
    {

    }
}
