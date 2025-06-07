package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.ControladorHibernate;
import org.p2pnexus.servidor.Entidades.DAO.SolicitudContactoDAO;
import org.p2pnexus.servidor.Entidades.DAO.UsuarioDAO;
import org.p2pnexus.servidor.Entidades.SolicitudContacto;
import org.p2pnexus.servidor.Entidades.Usuario;
import org.p2pnexus.servidor.clientes.ControladorSesiones;
import org.p2pnexus.servidor.clientes.FabricaMensajes;

import java.util.ArrayList;
import java.util.List;

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
            }
        }catch (Exception e) {
            throw new ManejarPeticionesExeptionError("Error al enviar la solicitud");
        }

        ArrayList<SolicitudContacto> solicitudes = dao.obtenerSolicitudesPendientesDeUsuario(idUsuarioDestino);

        Mensaje respuesta = Mensaje.empaquetarListaEnMensaje(solicitudes, TipoMensaje.R_SOLICITUDES_POR_ID);

        ControladorSesiones.enviarMensajeCliente(idUsuarioDestino, respuesta);
        ControladorSesiones.enviarMensajeCliente(idUsuarioDestino, FabricaMensajes.crearNotificacion(
                "Nueva solicitud de contacto recibida.", TipoNotificacion.AVISO));

        return new ResultadoMensaje("Solicitud enviada correctamente", TipoNotificacion.EXITO);
    }
}
