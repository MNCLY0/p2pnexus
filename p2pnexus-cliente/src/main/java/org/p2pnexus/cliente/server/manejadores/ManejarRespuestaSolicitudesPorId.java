package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.controladores.vistas.ControladorSolicitudes;
import org.p2pnexus.cliente.server.entitades.SolicitudContacto;

import java.util.List;

public class ManejarRespuestaSolicitudesPorId implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        List<SolicitudContacto> respuesta = JsonHerramientas.obtenerListaDeJsonObject(mensaje.getData(), SolicitudContacto.class);
        if (respuesta.isEmpty()) {
            return null;
        }

        ControladorSolicitudes.controladorSolicitudesActual.agregarSolicitudes(respuesta);

        return null;
    }
}
