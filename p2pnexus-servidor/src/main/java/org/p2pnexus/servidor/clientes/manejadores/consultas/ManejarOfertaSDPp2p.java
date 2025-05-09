package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.hibernate.Session;
import org.p2pnexus.servidor.Entidades.Usuario;
import org.p2pnexus.servidor.clientes.ControladorSesiones;
import org.p2pnexus.servidor.clientes.SesionCliente;

public class ManejarOfertaSDPp2p implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        Usuario usuarioRemoto = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("usuario_remoto").getAsJsonObject(), Usuario.class); // obtenemos el usuario al que se envia la oferta

        // Obtenemos la sesion del usuario
        SesionCliente sesion = ControladorSesiones.obenerUsuarioEnLinea(usuarioRemoto);

        if (sesion == null) {
            throw new ManejarPeticionesExeptionError("El usuario no esta conectado");
        }

        JsonObject sdp = new JsonObject();
        sdp.addProperty("sdp", mensaje.getData().get("sdp").getAsString());
        sdp.add("usuario_remoto", mensaje.getData().get("usuario_local").getAsJsonObject());

        sesion.getCliente().enviarMensaje(new Mensaje(TipoMensaje.R_P2P_SDP_OFERTA, sdp));


        return null;
    }
}
