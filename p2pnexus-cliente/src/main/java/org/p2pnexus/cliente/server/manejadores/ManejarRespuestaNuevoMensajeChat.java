package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import javafx.application.Platform;
import org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable.ControladorTarjetaContacto;
import org.p2pnexus.cliente.controladores.vistas.ControladorMenuPrincipal;
import org.p2pnexus.cliente.controladores.vistas.controladorChat.ControladorChat;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.MensajeChat;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.ventanas.Notificaciones;

import java.awt.*;


public class ManejarRespuestaNuevoMensajeChat implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        MensajeChat nuevoMensaje = JsonHerramientas.convertirJsonAObjeto(mensaje.getData(), MensajeChat.class);
        intentarNotificar(nuevoMensaje);
        Platform.runLater(() ->{
            if (ControladorChat.instancia == null) return;
            ControladorChat.instancia.gestorChat.nuevoMensaje(nuevoMensaje);
        });

        return null;
    }

    public void intentarNotificar(MensajeChat mensaje)
    {
        ControladorChat controladorChat = ControladorChat.instancia;
        Usuario emisor = mensaje.getEmisor();

        if (controladorChat == null)
        {
            notificarNuevoMensaje(emisor);
            return;
        }
        Usuario usuarioCliente = Sesion.getUsuario();
        if (emisor.getId_usuario() != usuarioCliente.getId_usuario())
        {
            // aprovechamos que el mensaje trae informacion actualizada del usuario para actualizar la imagen del usuario, esto
            // habria que hacerlo de otra manera pero por ahora es suficiente

            ControladorTarjetaContacto controladorTarjetaContacto = ControladorMenuPrincipal.instancia.getControladoresTarjetaContacto().get(emisor);
            controladorTarjetaContacto.getUsuario().setImagen_perfil(emisor.getImagen_perfil());
            controladorTarjetaContacto.actualizarImagen();

            notificarNuevoMensaje(emisor);
        }

    }

    public void notificarNuevoMensaje(Usuario usuario)
    {
        Notificaciones.mostrarNotificacion("Nuevo mensaje de " + usuario.getNombre(), TipoNotificacion.MENSAJE);
    }
}
