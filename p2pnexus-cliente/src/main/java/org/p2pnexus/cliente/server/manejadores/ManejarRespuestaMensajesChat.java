package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.controladores.vistas.ControladorChat;
import org.p2pnexus.cliente.server.entitades.MensajeChat;

import java.util.List;

public class ManejarRespuestaMensajesChat implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        List<MensajeChat> mensajes = JsonHerramientas.obtenerListaDeJsonObject(mensaje.getData(), MensajeChat.class);
        System.out.println("Estableciendo mensajes en el chat");
        ControladorChat.controladorChatActual.establecerMensajes(mensajes);
        return null;
    }
}
