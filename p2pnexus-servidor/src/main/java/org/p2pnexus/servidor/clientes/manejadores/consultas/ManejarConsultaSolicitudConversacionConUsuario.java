package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.DAO.ConversacionDAO;

public class ManejarConsultaSolicitudConversacionConUsuario implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        int usuarioOrigen = mensaje.getData().get("id_usuario_origen").getAsInt();
        int usuarioBusqueda = mensaje.getData().get("id_usuario_busqueda").getAsInt();

        ConversacionDAO conversacionDAO = new ConversacionDAO();
        int id_conversacion = conversacionDAO.obtenerConversacionEntreDos(usuarioOrigen, usuarioBusqueda).getId_conversacion();
        JsonObject json = new JsonObject();
        json.addProperty("id_conversacion", id_conversacion);
        json.addProperty("id_usuario", usuarioBusqueda);
        Mensaje respuesta = new Mensaje(TipoMensaje.R_CONVERSACION_CON_USUARIO, json);

        return new ResultadoMensaje(respuesta);
    }
}
