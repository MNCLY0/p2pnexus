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
import org.p2pnexus.servidor.Entidades.DAO.EspacioCompartidoDAO;
import org.p2pnexus.servidor.Entidades.EspacioCompartido;

import java.util.List;

public class ManejarConsultaActualizarChat implements IManejadorMensaje {

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        int idUsuarioCliente = mensaje.getData().get("id_usuario_cliente").getAsInt();
        int idUsuarioSolicitado = mensaje.getData().get("id_usuario_solicitado").getAsInt();


        ConversacionDAO conversacionDAO = new ConversacionDAO();
        Conversacion conversacion = conversacionDAO.obtenerConversacionEntreDos(idUsuarioCliente, idUsuarioSolicitado);

        List<org.p2pnexus.servidor.Entidades.Mensaje> mensajes = conversacionDAO.obtenerUltimosMensajesDeConversacion(conversacion.getId_conversacion());
        JsonObject json = new JsonObject();
        json.add("mensajes", JsonHerramientas.empaquetarListaEnJsonObject(mensajes));
        json.addProperty("id_conversacion", conversacion.getId_conversacion());

        EspacioCompartidoDAO espacioDAO = new EspacioCompartidoDAO();
        System.out.println("Se procede a deserializar los espacios compartidos");
        List<EspacioCompartido> espaciosCompartidosPorSolicitante = espacioDAO.espaciosCompartidosPorUsuarioConOtroUsuario(idUsuarioCliente, idUsuarioSolicitado);
        List<EspacioCompartido> espaciosCompartidosPorUsuario = espacioDAO.espaciosCompartidosPorUsuarioConOtroUsuario(idUsuarioSolicitado, idUsuarioCliente);
        System.out.println("Espacios compartidos por solicitante: " + espaciosCompartidosPorSolicitante);
        System.out.println("Espacios compartidos por usuario: " + espaciosCompartidosPorUsuario);
        json.add("espacios_compartidos_enviados", JsonHerramientas.empaquetarListaEnJsonObject(espaciosCompartidosPorSolicitante));
        json.add("espacios_compartidos_recibidos", JsonHerramientas.empaquetarListaEnJsonObject(espaciosCompartidosPorUsuario));

        return new ResultadoMensaje(new Mensaje(TipoMensaje.R_ACTUALIZAR_CHAT, json));
    }
}
