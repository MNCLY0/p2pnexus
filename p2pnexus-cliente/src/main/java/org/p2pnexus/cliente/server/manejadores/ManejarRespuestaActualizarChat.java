package org.p2pnexus.cliente.server.manejadores;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.controladores.vistas.ControladorChat;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.MensajeChat;

import java.util.List;

public class ManejarRespuestaActualizarChat implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        Conversacion conversacion = new Conversacion(mensaje.getData().get("id_conversacion").getAsInt());
        JsonObject mensajesJson = mensaje.getData().get("mensajes").getAsJsonObject();
        List<MensajeChat> mensajes = JsonHerramientas.obtenerListaDeJsonObject(mensajesJson, MensajeChat.class);
        System.out.println("Estableciendo mensajes en el chat");
        ControladorChat.instancia.establecerMensajes(mensajes, conversacion);
        return null;
    }
}
