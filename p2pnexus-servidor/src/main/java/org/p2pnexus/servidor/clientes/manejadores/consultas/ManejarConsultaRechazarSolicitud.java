package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.DAO.SolicitudContactoDAO;
import org.p2pnexus.servidor.Entidades.EstadoSolicitud;

public class ManejarConsultaRechazarSolicitud implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        int idSolicitud = mensaje.getData().get("idSolicitud").getAsInt();

        SolicitudContactoDAO dao = new SolicitudContactoDAO();
        dao.actualizarEstadoSolicitud(idSolicitud, EstadoSolicitud.RECHAZADA);

        return new ResultadoMensaje("Solicitud rechazada con éxito", TipoNotificacion.EXITO);
    }
}
