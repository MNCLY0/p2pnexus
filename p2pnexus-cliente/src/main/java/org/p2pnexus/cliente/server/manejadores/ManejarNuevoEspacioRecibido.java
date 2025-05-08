package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.controladores.vistas.controladorChat.ControladorChat;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;

public class ManejarNuevoEspacioRecibido implements IManejadorMensaje {

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        EspacioCompartido espacio = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("espacio").getAsJsonObject(), EspacioCompartido.class);
        Conversacion conversacion = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("conversacion").getAsJsonObject(), Conversacion.class);
        espacio.inializarPropiedades();

        ControladorChat.instancia.gestorEspacios.nuevoEspacioRecibido(espacio, conversacion);

        return null;
    }
}
