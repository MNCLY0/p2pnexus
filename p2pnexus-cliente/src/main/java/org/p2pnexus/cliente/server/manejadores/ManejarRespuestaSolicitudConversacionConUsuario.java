package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import javafx.application.Platform;
import org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable.ControladorTarjetaContacto;
import org.p2pnexus.cliente.controladores.vistas.ControladorMenuPrincipal;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.Usuario;

public class ManejarRespuestaSolicitudConversacionConUsuario implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        Usuario usuario = new Usuario("", mensaje.getData().get("id_usuario").getAsInt());
        Conversacion conversacion = new Conversacion(mensaje.getData().get("id_conversacion").getAsInt());
        ControladorTarjetaContacto controladorTarjetaContacto = ControladorMenuPrincipal.controladorMenuPrincipalActual.getControladoresTarjetaContacto().get(usuario);
        Platform.runLater(() -> {
            controladorTarjetaContacto.establecerConversacion(conversacion);
        });
        return null;
    }
}
