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

import java.util.List;

public class ManejarConsultaEspaciosPorId implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        int idUsuario = mensaje.getData().get("id_usuario").getAsInt();

        EspacioCompartidoDAO dao = new EspacioCompartidoDAO();

        List<EspacioCompartido> espaciosCompartidos = dao.espaciosCompartidosPorPropietario(idUsuario);

        JsonObject json = JsonHerramientas.empaquetarListaEnJsonObject(espaciosCompartidos);

        return new ResultadoMensaje(new Mensaje(TipoMensaje.R_ESPACIOS_POR_ID, json));
    }
}
