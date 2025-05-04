package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.controladores.vistas.ControladorEspacios;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;

public class ManejarRespuestaCrearEspacioOk implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        EspacioCompartido espacioCreado = JsonHerramientas.convertirJsonAObjeto(mensaje.getData(), EspacioCompartido.class);
        ControladorEspacios.instancia.inicializarTarjetaEspacio(espacioCreado);
        return null;
    }
}
