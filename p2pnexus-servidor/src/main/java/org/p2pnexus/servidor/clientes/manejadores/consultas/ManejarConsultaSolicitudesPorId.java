package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.DAO.SolicitudContactoDAO;
import org.p2pnexus.servidor.Entidades.SolicitudContacto;

import java.util.ArrayList;

public class ManejarConsultaSolicitudesPorId implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        int idUsuario = mensaje.getData().get("id_usuario").getAsInt();
        boolean notificable = mensaje.getData().get("notificable").getAsBoolean();

        SolicitudContactoDAO dao = new SolicitudContactoDAO();
        ArrayList<SolicitudContacto> solicitudes = dao.obtenerSolicitudesPendientesDeUsuario(idUsuario);

        if (solicitudes.isEmpty()) {
            if (!notificable) return null;
            return new ResultadoMensaje("No se han encontrado solicitudes pendientes", TipoNotificacion.AVISO);
        }

        Mensaje respuesta = Mensaje.empaquetarListaEnMensaje(solicitudes, TipoMensaje.R_SOLICITUDES_POR_ID);

        return new ResultadoMensaje(respuesta);
    }
}
