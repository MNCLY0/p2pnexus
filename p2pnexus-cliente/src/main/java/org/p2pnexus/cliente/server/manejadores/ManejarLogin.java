package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

public class ManejarLogin implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
//        String nombre = mensaje.getData().get("nombre").getAsString();
//        int id = mensaje.getData().get("id").getAsInt();


        GestorVentanas.transicionarVentana(Ventanas.MENU_PRINCIPAL);

        return null;
    }
}
