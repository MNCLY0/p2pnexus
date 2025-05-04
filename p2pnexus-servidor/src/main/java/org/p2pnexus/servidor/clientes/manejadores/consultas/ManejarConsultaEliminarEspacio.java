package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.DAO.EspacioCompartidoDAO;
import org.p2pnexus.servidor.Entidades.EspacioCompartido;
import org.p2pnexus.servidor.clientes.FabricaMensajes;

public class ManejarConsultaEliminarEspacio implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        EspacioCompartido espacio = JsonHerramientas.convertirJsonAObjeto(mensaje.getData(), EspacioCompartido.class);
        EspacioCompartidoDAO espacioCompartidoDAO = new EspacioCompartidoDAO();

        try {
            espacioCompartidoDAO.eliminarEspacioCompartido(espacio.getId_espacio());
            socketConexion.enviarMensaje(FabricaMensajes.crearNotificacion("Espacio eliminado correctamente", TipoNotificacion.EXITO));

        } catch (RuntimeException e) {
            throw new ManejarPeticionesExeptionError("Error al eliminar el espacio compartido");
        }

        return null;
    }
}
