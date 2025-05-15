package org.p2pnexus.cliente.controladores.componentes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.p2pnexus.cliente.server.entitades.MensajeChat;
import org.p2pnexus.cliente.sesion.Sesion;


public class ControladorTarjetaMensaje {
    @FXML
    public Label labelNombre;

    @FXML
    public Text textContenidoMensaje;
    @FXML
    public ImageView imageViewUsuario;

    public void establecerMensaje(MensajeChat mensaje, Image imagen)
    {
        imageViewUsuario.setImage(imagen);
        labelNombre.setText(mensaje.getEmisor().getNombre());
        textContenidoMensaje.setText(mensaje.getContenido());
    }
}
