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
import org.p2pnexus.servidor.Entidades.DAO.EspacioCompartidoDAO;
import org.p2pnexus.servidor.Entidades.EspacioCompartido;
import org.p2pnexus.servidor.Entidades.Usuario;
import org.p2pnexus.servidor.clientes.FabricaMensajes;

import java.util.List;

public class ManejarConsultaCompartirEspacio implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        EspacioCompartido espacio = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("espacio").getAsJsonObject(), EspacioCompartido.class);
        Conversacion conversacion = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("conversacion").getAsJsonObject(), Conversacion.class);
        EspacioCompartidoDAO espacioCompartidoDAO = new EspacioCompartidoDAO();
        List<Usuario> usuariosAfectados;
        try {
            usuariosAfectados = espacioCompartidoDAO.crearPermisosATodosLosUsuariosDeConversacion(espacio, conversacion);
        }catch (Exception e){
            throw new ManejarPeticionesExeptionError("Ya has compartido este espacio con esta conversacion");
        }

        JsonObject json = new JsonObject();

        json.add("espacio", JsonHerramientas.convertirObjetoAJson(espacio));
        json.add("usuarios", JsonHerramientas.empaquetarListaEnJsonObject(usuariosAfectados));
        socketConexion.enviarMensaje(FabricaMensajes.crearNotificacion("Espacio compartirdo corretamente", TipoNotificacion.EXITO));
        return new ResultadoMensaje(new Mensaje(TipoMensaje.R_COMPARTIR_ESPACIO_OK,json));
    }
}
