package org.p2pnexus.servidor.clientes.manejadores.consultas.p2p;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.Usuario;
import org.p2pnexus.servidor.clientes.ControladorSesiones;
import org.p2pnexus.servidor.clientes.SesionCliente;

public class ManejarOfertaSDPp2p implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        ManejarComunP2P p2p = new ManejarComunP2P();
        return p2p.enviarMensaje(mensaje, socketConexion, TipoMensaje.R_P2P_SDP_OFERTA);
    }
}
