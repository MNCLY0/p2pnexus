package org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.p2pnexus.cliente.controladores.vistas.ControladorSolicitudes;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;

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
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario_origen", Sesion.getUsuario().getId_usuario());
        json.addProperty("id_usuario_destino", usuario.getId_usuario());

        Conexion.enviarMensaje(new Mensaje(TipoMensaje.S_CREAR_SOLICITUD,json));

        ControladorSolicitudes.instancia.vboxResultadosUsuarios.getChildren().remove(labelNombre.getParent());

        ControladorSolicitudes.instancia.solicitarActualizacionSolicitudes();

    }
}
