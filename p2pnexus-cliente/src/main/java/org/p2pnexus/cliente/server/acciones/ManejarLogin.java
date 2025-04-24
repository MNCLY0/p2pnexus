package org.p2pnexus.cliente.server.acciones;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IAccionMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

public class ManejarLogin implements IAccionMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        GestorVentanas.transicionarVentana(Ventanas.MENU_PRINCIPAL);
        return null;
    }
}
