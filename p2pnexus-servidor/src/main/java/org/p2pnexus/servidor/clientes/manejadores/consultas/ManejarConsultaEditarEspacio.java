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

        EspacioCompartido espacio = JsonHerramientas.convertirJsonAObjeto(mensaje.getData(), EspacioCompartido.class);

        EspacioCompartidoDAO espacioCompartidoDAO = new EspacioCompartidoDAO();
        espacioCompartidoDAO.editarEspacioCompartido(espacio);

        return new ResultadoMensaje(new Mensaje(TipoMensaje.R_CREAR_ESPACIO_OK, mensaje.getData()));
    }
}
