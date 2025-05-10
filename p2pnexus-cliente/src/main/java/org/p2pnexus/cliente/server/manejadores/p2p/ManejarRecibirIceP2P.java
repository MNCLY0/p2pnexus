package org.p2pnexus.cliente.server.manejadores.p2p;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.p2p.conexion.GestorP2P;
import org.p2pnexus.cliente.server.entitades.Usuario;

public class ManejarRecibirIceP2P implements IManejadorMensaje {

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        System.out.println("Datos del mensaje de ice: " + mensaje.getData());

        Usuario usuarioRemoto = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("usuario_remoto").getAsJsonObject(), Usuario.class);

        GestorP2P gestor = GestorP2P.conexiones.get(usuarioRemoto.getId_usuario());

        if (gestor == null) {
            System.out.println("No se ha realizado ninguna oferta con este usuario");
            return null;
        }

        gestor.recibirIce(mensaje.getData());

        return null;
    }
}
