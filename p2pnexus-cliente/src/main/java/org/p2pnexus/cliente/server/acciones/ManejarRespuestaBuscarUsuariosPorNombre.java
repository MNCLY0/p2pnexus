package org.p2pnexus.cliente.server.acciones;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IAccionMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.controllers.ControladorSolicitudes;

public class ManejarRespuestaBuscarUsuariosPorNombre implements IAccionMensaje {


    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        ControladorSolicitudes.controladorSolicitudesActual.agregarResultado(mensaje.getData());
        return null;
    }

}
