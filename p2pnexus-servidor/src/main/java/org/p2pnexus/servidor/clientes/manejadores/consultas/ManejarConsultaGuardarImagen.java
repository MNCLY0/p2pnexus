package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.DAO.UsuarioDAO;
import org.p2pnexus.servidor.Entidades.Usuario;

public class ManejarConsultaGuardarImagen implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        String enlace = mensaje.getData().get("enlace").getAsString();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.buscarPorId(socketConexion.getIdUsuario());
        usuario.setImagen_perfil(enlace);
        usuarioDAO.actualizar(usuario);
        return null;
    }
}
