package org.p2pnexus.cliente.controladores.componentes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.p2pnexus.cliente.server.entitades.MensajeChat;


public class ControladorTarjetaMensaje {
    @FXML
    public Label labelNombre;

    @FXML
    public Label labelContenidoMensaje;

    public void establecerMensaje(MensajeChat mensaje)
    {
        labelNombre.setText(mensaje.getEmisor().getNombre());
        labelContenidoMensaje.setText(mensaje.getContenido());
    }
}
