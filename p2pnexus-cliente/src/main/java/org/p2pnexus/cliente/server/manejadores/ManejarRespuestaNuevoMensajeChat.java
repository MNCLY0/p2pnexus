package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import javafx.application.Platform;
import org.p2pnexus.cliente.controladores.vistas.controladorChat.ControladorChat;
import org.p2pnexus.cliente.server.entitades.MensajeChat;

public class ManejarRespuestaNuevoMensajeChat implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        System.out.println("Recibiendo nuevo mensaje de chat");

        MensajeChat nuevoMensaje = JsonHerramientas.convertirJsonAObjeto(mensaje.getData(), MensajeChat.class);
        Platform.runLater(() ->{
            if (ControladorChat.instancia == null) {
                return;
            }
            ControladorChat.instancia.gestorChat.nuevoMensaje(nuevoMensaje);
        });

        return null;
    }
}
