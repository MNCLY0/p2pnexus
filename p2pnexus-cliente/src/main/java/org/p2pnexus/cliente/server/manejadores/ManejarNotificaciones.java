package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.ventanas.Notificaciones;

public class ManejarNotificaciones implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        TipoNotificacion tipo = TipoNotificacion.valueOf(mensaje.getData().get("tipo").getAsString().toUpperCase());
        String mensajeNotificacion = mensaje.getData().get("mensaje").getAsString();
        System.out.println("Notificacion recibida: " + mensaje);
        Notificaciones.MostrarNotificacion(mensajeNotificacion, tipo);
        // No es necesario devolver un resultado
        return null;
    }
}
