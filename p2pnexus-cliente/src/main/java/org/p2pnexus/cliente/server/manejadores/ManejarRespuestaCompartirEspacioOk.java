package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.server.entitades.Usuario;

import java.util.List;

public class ManejarRespuestaCompartirEspacioOk implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        EspacioCompartido espacioCompartido = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("espacio").getAsJsonObject(), EspacioCompartido.class);
        List<Usuario> usuarios = JsonHerramientas.obtenerListaDeJsonObject(mensaje.getData().get("usuarios").getAsJsonObject(), Usuario.class);

        System.out.println("El espacio ha sido compartido correctamente con los usuarios: " + usuarios);


        return null;
    }
}
