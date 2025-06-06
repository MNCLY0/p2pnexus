package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.controladores.vistas.ControladorMenuPrincipal;
import org.p2pnexus.cliente.server.entitades.Usuario;

import java.util.List;

public class MenejarRespuestaListaContactos implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        List<Usuario> lista = JsonHerramientas.obtenerListaDeJsonObject(mensaje.getData(), Usuario.class);
        ControladorMenuPrincipal.instancia.actualizarListaContactos(lista);

        return null;
    }
}
