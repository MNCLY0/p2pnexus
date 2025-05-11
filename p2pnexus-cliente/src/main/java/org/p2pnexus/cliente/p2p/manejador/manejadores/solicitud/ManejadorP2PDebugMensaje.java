package org.p2pnexus.cliente.p2p.manejador.manejadores.solicitud;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;

public class ManejadorP2PDebugMensaje implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        System.out.println("Mensaje recibido: " + mensaje.getTipo() + " " + mensaje.getData().get("mensajePrueba"));
        return null;
    }
}
