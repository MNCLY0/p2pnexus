package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.controladores.vistas.ControladorEspacios;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;

import java.util.List;

public class ManejarRespuestaEspaciosPorId implements IManejadorMensaje {

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        try {
            List<EspacioCompartido> espacioCompartidos = JsonHerramientas.obtenerListaDeJsonObject(mensaje.getData(), EspacioCompartido.class);
            ControladorEspacios.instancia.inicializarEspacios(espacioCompartidos);
        }catch (Exception e) {
            throw new ManejarPeticionesExeptionError("Error al manejar la respuesta", e);
        }

        return null;
    }
}
