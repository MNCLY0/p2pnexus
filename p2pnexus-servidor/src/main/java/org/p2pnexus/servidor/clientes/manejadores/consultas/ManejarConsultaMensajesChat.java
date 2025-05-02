package org.p2pnexus.servidor.clientes.manejadores.consultas;

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

import java.util.List;

public class ManejarConsultaMensajesChat implements IManejadorMensaje {

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        int id_usuario_solicitante = mensaje.getData().get("id_usuario_solicitante").getAsInt();
        int id_usuario = mensaje.getData().get("id_usuario").getAsInt();


        ConversacionDAO conversacionDAO = new ConversacionDAO();
        Conversacion conversacion = conversacionDAO.obtenerConversacionEntreDos(id_usuario_solicitante, id_usuario);

        List<org.p2pnexus.servidor.Entidades.Mensaje> mensajes = conversacionDAO.obtenerUltimosMensajesDeConversacion(conversacion.getId_conversacion());
        JsonObject mensajesPauqete = JsonHerramientas.empaquetarListaEnJsonObject(mensajes);
        mensajesPauqete.addProperty("id_conversacion", conversacion.getId_conversacion());
        return new ResultadoMensaje(new Mensaje(TipoMensaje.R_MENSAJES_CHAT, mensajesPauqete));
    }
}
