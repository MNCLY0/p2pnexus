package org.p2pnexus.cliente.server.acciones;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IAccionMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeption;
import org.p2pnexus.cliente.ventanas.Notificaciones;

public class ManejarNotificaciones implements IAccionMensaje {
    @Override
    public void manejarDatos(JsonObject datos, SocketConexion socketConexion) throws ManejarPeticionesExeption {
        TipoNotificacion tipo = TipoNotificacion.valueOf(datos.get("tipo").getAsString().toUpperCase());
        String mensaje = datos.get("mensaje").getAsString();
        System.out.println("Notificacion recibida: " + mensaje);
        Notificaciones.MostrarNotificacion(mensaje, tipo);
    }
}
