package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.DAO.SolicitudContactoDAO;

public class ManejarNuevaSolicitud implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        int idUsuarioOrigen = mensaje.getData().get("id_usuario_origen").getAsInt();
        int idUsuarioDestino = mensaje.getData().get("id_usuario_destino").getAsInt();

        SolicitudContactoDAO dao = new SolicitudContactoDAO();
        try {
            if (!dao.crearSolicitud(idUsuarioOrigen, idUsuarioDestino))
            {
                return new ResultadoMensaje("No se puede enviar la solicitud ¿Ya has enviado una?", TipoNotificacion.AVISO);
            };
        }catch (Exception e) {
            throw new ManejarPeticionesExeptionError("Error al enviar la solicitud");
        }

        return new ResultadoMensaje("Solicitud enviada correctamente", TipoNotificacion.EXITO);
    }
}
