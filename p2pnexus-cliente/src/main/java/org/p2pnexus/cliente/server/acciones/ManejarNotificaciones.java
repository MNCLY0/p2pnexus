package org.p2pnexus.cliente.server.acciones;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IAccionMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.ventanas.Notificaciones;

public class ManejarNotificaciones implements IAccionMensaje {
    @Override
    public ResultadoMensaje manejarDatos(JsonObject datos, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        TipoNotificacion tipo = TipoNotificacion.valueOf(datos.get("tipo").getAsString().toUpperCase());
        String mensaje = datos.get("mensaje").getAsString();
        System.out.println("Notificacion recibida: " + mensaje);
        Notificaciones.MostrarNotificacion(mensaje, tipo);
        // No es necesario devolver un resultado
        return null;
    }
}
