package org.p2pnexus.cliente.p2p.conexion;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import org.p2pnexus.cliente.server.Conexion;

public class MensajesP2P {

    public static void enviarOferta(JsonObject json) {
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_P2P_SDP_OFERTA, json));
    }

    public static void enviarRespuesta(JsonObject json) {
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_P2P_SDP_RESPUESTA, json));
    }

    public static void enviarIce(JsonObject json) {
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_P2P_ICE, json));
    }
}
