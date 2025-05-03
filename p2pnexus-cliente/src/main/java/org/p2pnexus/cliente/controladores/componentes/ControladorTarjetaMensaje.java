package org.p2pnexus.cliente.controladores.componentes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.p2pnexus.cliente.server.entitades.MensajeChat;


public class ControladorTarjetaMensaje {
    @FXML
    public Label labelNombre;

    @FXML
    public Text textContenidoMensaje;

    public void establecerMensaje(MensajeChat mensaje)
    {
        labelNombre.setText(mensaje.getEmisor().getNombre());
        textContenidoMensaje.setText(mensaje.getContenido());
    }
}
