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
import org.p2pnexus.servidor.Entidades.Conversacion;
import org.p2pnexus.servidor.Entidades.DAO.ConversacionDAO;
import org.p2pnexus.servidor.Entidades.DAO.EspacioCompartidoDAO;
import org.p2pnexus.servidor.Entidades.EspacioCompartido;
import org.p2pnexus.servidor.Entidades.Usuario;
import org.p2pnexus.servidor.clientes.ControladorSesiones;
import org.p2pnexus.servidor.clientes.FabricaMensajes;
import org.p2pnexus.servidor.clientes.SesionCliente;

import java.util.List;

public class ManejarConsultaEliminarEspacio implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        EspacioCompartido espacio = JsonHerramientas.convertirJsonAObjeto(mensaje.getData(), EspacioCompartido.class);
        EspacioCompartidoDAO espacioCompartidoDAO = new EspacioCompartidoDAO();
        List<Usuario> usuariosAfectados;

        try {
            usuariosAfectados = espacioCompartidoDAO.usuariosConAccesoAEspacio(espacio);
            espacioCompartidoDAO.eliminarEspacioCompartido(espacio.getId_espacio());
            socketConexion.enviarMensaje(FabricaMensajes.crearNotificacion("Espacio eliminado correctamente", TipoNotificacion.EXITO));
        } catch (RuntimeException e) {
            throw new ManejarPeticionesExeptionError("Error al eliminar el espacio compartido");
        }

        if (usuariosAfectados != null) {
            ConversacionDAO conversacionDAO = new ConversacionDAO();
            List<SesionCliente> sesiones = ControladorSesiones.filtrarEnLinea(usuariosAfectados);
            for (SesionCliente sesionCliente : sesiones) {
                Conversacion conversacion = conversacionDAO.obtenerConversacionEntreDos(sesionCliente.getUsuario().getId_usuario(), espacio.getPropietario().getId_usuario());
                JsonObject json = new JsonObject();
                json.add("espacio", JsonHerramientas.convertirObjetoAJson(espacio));
                json.add("conversacion", JsonHerramientas.convertirObjetoAJson(conversacion));
                sesionCliente.getCliente().enviarMensaje(new Mensaje(TipoMensaje.R_ELIMINAR_ESPACIO_RECIBIDO, json));
            }
        }

        return null;
    }
}
