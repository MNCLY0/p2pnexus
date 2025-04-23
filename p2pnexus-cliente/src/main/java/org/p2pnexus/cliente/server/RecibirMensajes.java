package org.p2pnexus.cliente.server;

import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.ManejadorDeMensajes;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import org.p2pnexus.cliente.server.acciones.ManejarNotificaciones;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class RecibirMensajes extends ManejadorDeMensajes {

    public RecibirMensajes(SocketConexion socketConexion) {
        super(socketConexion);
    }

    @Override
    public void inicializarManejadores() {
        manejadoresPeticiones.put(TipoMensaje.NOTIFICACION, new ManejarNotificaciones());
    }
}
