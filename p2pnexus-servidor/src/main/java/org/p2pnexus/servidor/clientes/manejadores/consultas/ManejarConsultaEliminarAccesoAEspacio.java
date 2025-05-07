package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.Conversacion;
import org.p2pnexus.servidor.Entidades.DAO.EspacioCompartidoDAO;
import org.p2pnexus.servidor.Entidades.EspacioCompartido;
import org.p2pnexus.servidor.clientes.FabricaMensajes;

public class ManejarConsultaEliminarAccesoAEspacio implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        Conversacion conversacion = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("conversacion").getAsJsonObject(), Conversacion.class);
        EspacioCompartido espacio = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("espacio").getAsJsonObject(), EspacioCompartido.class);

        try {
            EspacioCompartidoDAO dao = new EspacioCompartidoDAO();
            dao.eliminarAccesoAEspacioCompartido(espacio, conversacion);
            socketConexion.enviarMensaje(FabricaMensajes.crearNotificacion("Acceso eliminado correctamente", TipoNotificacion.EXITO));
        }catch (Exception e) {
            socketConexion.enviarMensaje(FabricaMensajes.crearNotificacion("Error al eliminar acceso al espacio compartido", TipoNotificacion.ERROR));

            // Si hay algun problema aprovechamos la logica de la respuesta de compartir espacio para enviar el mensaje de error
            // y hacer que el cliente vuelva a agregarlo mandandole de vuelta el espacio y la conversacion
            socketConexion.enviarMensaje(new Mensaje(TipoMensaje.R_COMPARTIR_ESPACIO_OK, mensaje.getData()));

            throw new ManejarPeticionesExeptionError("Error al eliminar acceso al espacio compartido");
        }

        return null;
    }
}
