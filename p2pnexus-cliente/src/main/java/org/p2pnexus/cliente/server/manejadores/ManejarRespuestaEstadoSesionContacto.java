package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.controladores.vistas.ControladorMenuPrincipal;
import org.p2pnexus.cliente.server.entitades.Usuario;

public class ManejarRespuestaEstadoSesionContacto implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        System.out.print("Actualizando estado de contacto");
        Usuario usuario = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("usuario").getAsJsonObject(), Usuario.class);
        System.out.printf("Usuario: %s", usuario.getNombre());
        System.out.printf("Estado de contacto: %s", usuario.getConectado());

        ControladorMenuPrincipal.instancia.actualizarEstadoTarjetaContacto(usuario);
        return null;
    }
}
