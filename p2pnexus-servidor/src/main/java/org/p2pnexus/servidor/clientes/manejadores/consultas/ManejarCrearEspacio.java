package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.DAO.EspacioCompartidoDAO;
import org.p2pnexus.servidor.Entidades.DAO.UsuarioDAO;
import org.p2pnexus.servidor.Entidades.EspacioCompartido;
import org.p2pnexus.servidor.Entidades.Usuario;
import org.p2pnexus.servidor.clientes.FabricaMensajes;

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
        espacioCompartido.setPropietario(usuario);
        espacioCompartido.setRuta_directorio(ruta);

        EspacioCompartidoDAO espacioCompartidoDAO = new EspacioCompartidoDAO();

        try {
            espacioCompartidoDAO.crearEspacioCompartido(espacioCompartido);
            JsonObject json = JsonHerramientas.convertirObjetoAJson(espacioCompartido);
            socketConexion.enviarMensaje(FabricaMensajes.crearNotificacion("Espacio creado correctamente", TipoNotificacion.EXITO));
            return new ResultadoMensaje(new Mensaje(TipoMensaje.R_CREAR_ESPACIO_OK, json));
        } catch (Exception e) {
            throw new ManejarPeticionesExeptionError("Ya existe un espacio con ese nombre");
        }
    }
}
