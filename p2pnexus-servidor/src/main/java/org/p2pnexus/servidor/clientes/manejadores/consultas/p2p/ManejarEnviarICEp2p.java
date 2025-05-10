package org.p2pnexus.servidor.clientes.manejadores.consultas.p2p;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.Usuario;
import org.p2pnexus.servidor.clientes.ControladorSesiones;
import org.p2pnexus.servidor.clientes.SesionCliente;

public class ManejarEnviarICEp2p implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        System.out.println("Datos del mensaje de ice: " + mensaje.getData());

        Usuario usuarioRemoto = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("usuario_remoto").getAsJsonObject(), Usuario.class); // obtenemos el usuario al que se envia la oferta
        Usuario usuarioLocal = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("usuario_local").getAsJsonObject(), Usuario.class); // obtenemos el usuario que envia la oferta

        System.out.println("Usuario remoto: " + usuarioRemoto);
        System.out.println("Usuario local: " + usuarioLocal);

        // Obtenemos la sesion del usuario

        SesionCliente sesion = ControladorSesiones.obenerUsuarioEnLinea(usuarioRemoto);

        if (sesion == null) {
            throw new ManejarPeticionesExeptionError("El usuario no esta conectado");
        }

        JsonObject json = new JsonObject();
        json.addProperty("sdpMid", mensaje.getData().get("sdpMid").getAsString());
        json.addProperty("sdpMLineIndex", mensaje.getData().get("sdpMLineIndex").getAsInt());
        json.addProperty("candidate", mensaje.getData().get("candidate").getAsString());
        json.add("usuario_remoto", JsonHerramientas.convertirObjetoAJson(usuarioLocal));
        System.out.println("Enviando ICE: " + json);

        sesion.getCliente().enviarMensaje(new Mensaje(TipoMensaje.R_P2P_ICE, json));

        return null;
    }
}
