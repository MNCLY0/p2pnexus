package org.p2pnexus.servidor.clientes;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;

public class FabricaMensajes {
    public static Mensaje crearNotificacion(String mensaje, TipoNotificacion tipo) {
        JsonObject json = new JsonObject();
        json.addProperty("tipo", tipo.name());
        json.addProperty("mensaje", mensaje);
        return new Mensaje(TipoMensaje.NOTIFICACION, json);
    }
}
