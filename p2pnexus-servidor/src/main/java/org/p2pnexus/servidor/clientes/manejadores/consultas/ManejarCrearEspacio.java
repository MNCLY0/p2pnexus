package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.DAO.EspacioCompartidoDAO;
import org.p2pnexus.servidor.Entidades.DAO.UsuarioDAO;
import org.p2pnexus.servidor.Entidades.EspacioCompartido;
import org.p2pnexus.servidor.Entidades.Usuario;

public class ManejarCrearEspacio implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        String nombre = mensaje.getData().get("nombre").getAsString();
        String ruta = mensaje.getData().get("ruta").getAsString();
        int id = mensaje.getData().get("propietario_id").getAsInt();

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        Usuario usuario = usuarioDAO.buscarPorId(id);

        EspacioCompartido espacioCompartido = new EspacioCompartido();
        espacioCompartido.setNombre(nombre);
        espacioCompartido.setRuta_directorio(ruta);
        espacioCompartido.setPropietario(usuario);

        EspacioCompartidoDAO espacioCompartidoDAO = new EspacioCompartidoDAO();

        try {
            espacioCompartidoDAO.crearEspacioCompartido(espacioCompartido);
            return new ResultadoMensaje("Espacio creado correctamente", com.p2pnexus.comun.TipoNotificacion.EXITO);
        } catch (Exception e) {
            throw new ManejarPeticionesExeptionError("Error al crear el espacio compartido: " + e.getMessage());
        }

    }
}
