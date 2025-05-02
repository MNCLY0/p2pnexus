package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.Conversacion;
import org.p2pnexus.servidor.Entidades.DAO.ConversacionDAO;
import org.p2pnexus.servidor.clientes.ControladorSesiones;

import java.util.List;

public class ManejarConsultaEnviarMensaje implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        int idUsuarioEmisor = mensaje.getData().get("id_usuario_emisor").getAsInt();
        int idConversacion = mensaje.getData().get("id_conversacion").getAsInt();
        String contenido = mensaje.getData().get("contenido").getAsString();

        System.out.println("Intentando enviar mensaje a la conversación " + idConversacion + " de " + idUsuarioEmisor);

        ConversacionDAO conversacionDAO = new ConversacionDAO();

        org.p2pnexus.servidor.Entidades.Mensaje mensajeChat =  conversacionDAO.enviarMensajeAConversacion(idConversacion,idUsuarioEmisor,contenido);

        JsonObject json = JsonHerramientas.convertirObjetoAJson(mensajeChat);

        Mensaje mensajeRespuesta = new Mensaje(TipoMensaje.R_NUEVO_MENSAJE_CHAT, json);

        // Enviar el mensaje a los usuarios que participan en la conversación (esto está también pensado para cuando montenmos los grupos)
        ControladorSesiones.enviarMensajeClientes(conversacionDAO.obtenerUsuariosParticipantesConversacion(idConversacion), mensajeRespuesta);

        return null;
    }
}
