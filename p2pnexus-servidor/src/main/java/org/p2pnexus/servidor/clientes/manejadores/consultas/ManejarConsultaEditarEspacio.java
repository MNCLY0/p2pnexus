package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.Conversacion;
import org.p2pnexus.servidor.Entidades.DAO.ConversacionDAO;
import org.p2pnexus.servidor.Entidades.DAO.EspacioCompartidoDAO;
import org.p2pnexus.servidor.Entidades.EspacioCompartido;
import org.p2pnexus.servidor.Entidades.Usuario;
import org.p2pnexus.servidor.clientes.ControladorSesiones;
import org.p2pnexus.servidor.clientes.SesionCliente;

import java.util.List;

public class ManejarConsultaEditarEspacio implements IManejadorMensaje {

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        EspacioCompartido espacioOriginal = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("espacio_original").getAsJsonObject(), EspacioCompartido.class);

        EspacioCompartido espacioModificado = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("espacio_modificado").getAsJsonObject(), EspacioCompartido.class);

        EspacioCompartidoDAO espacioCompartidoDAO = new EspacioCompartidoDAO();

        List<Usuario> usuariosAfectados;

        try {
            espacioCompartidoDAO.editarEspacioCompartido(espacioModificado);
            usuariosAfectados = espacioCompartidoDAO.usuariosConAccesoAEspacio(espacioModificado);
            // Al usuario del espacio no le afecta el cambio, lo hacemos en local
            usuariosAfectados.remove(espacioModificado.getPropietario());
        }catch (Exception e) {
            socketConexion.enviarMensaje(new Mensaje(TipoMensaje.R_CREAR_ESPACIO_OK, JsonHerramientas.convertirObjetoAJson(espacioOriginal)));
            throw new ManejarPeticionesExeptionError("Ya existe un espacio con ese nombre");
        }

        // Si hay usuarios afectados nos encargamos de avisar a los clientes para que actualicen el espacio
        if (usuariosAfectados.isEmpty()) {
            ConversacionDAO conversacionDAO = new ConversacionDAO();
            List<SesionCliente> sesiones = ControladorSesiones.filtrarEnLinea(usuariosAfectados);
            for (SesionCliente sesionCliente : sesiones) {
                Conversacion conversacion = conversacionDAO.obtenerConversacionEntreDos(sesionCliente.getUsuario().getId_usuario(), espacioModificado.getPropietario().getId_usuario());
                JsonObject json = new JsonObject();
                json.add("espacio", JsonHerramientas.convertirObjetoAJson(espacioModificado));
                json.add("conversacion", JsonHerramientas.convertirObjetoAJson(conversacion));
                sesionCliente.getCliente().enviarMensaje(new Mensaje(TipoMensaje.R_ACTUALIZAR_ESPACIO_RECIBIDO, json));
            }
        }

        return new ResultadoMensaje(new Mensaje(TipoMensaje.R_CREAR_ESPACIO_OK, JsonHerramientas.convertirObjetoAJson(espacioModificado)));
    }
}
