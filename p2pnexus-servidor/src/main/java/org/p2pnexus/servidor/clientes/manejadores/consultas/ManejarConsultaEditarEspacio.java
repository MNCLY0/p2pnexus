package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.DAO.EspacioCompartidoDAO;
import org.p2pnexus.servidor.Entidades.EspacioCompartido;

public class ManejarConsultaEditarEspacio implements IManejadorMensaje {

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        EspacioCompartido espacioOriginal = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("espacio_original").getAsJsonObject(), EspacioCompartido.class);
        System.out.printf("\n\n\n\n\n\n\nEspacio original: %s\n", espacioOriginal.getId_espacio());
        EspacioCompartido espacioModificado = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("espacio_modificado").getAsJsonObject(), EspacioCompartido.class);
        EspacioCompartidoDAO espacioCompartidoDAO = new EspacioCompartidoDAO();
        try {
            espacioCompartidoDAO.editarEspacioCompartido(espacioModificado);
        }catch (Exception e) {
            socketConexion.enviarMensaje(new Mensaje(TipoMensaje.R_CREAR_ESPACIO_OK, JsonHerramientas.convertirObjetoAJson(espacioOriginal)));
            throw new ManejarPeticionesExeptionError("Ya existe un espacio con ese nombre");
        }


        return new ResultadoMensaje(new Mensaje(TipoMensaje.R_CREAR_ESPACIO_OK, JsonHerramientas.convertirObjetoAJson(espacioModificado)));
    }
}
