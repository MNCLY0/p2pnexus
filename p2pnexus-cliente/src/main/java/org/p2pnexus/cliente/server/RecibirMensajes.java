package org.p2pnexus.cliente.server;

import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.ManejadorDeMensajes;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import org.p2pnexus.cliente.server.manejadores.ManejarCerrarSesion;
import org.p2pnexus.cliente.server.manejadores.ManejarLogin;
import org.p2pnexus.cliente.server.manejadores.ManejarNotificaciones;
import org.p2pnexus.cliente.server.manejadores.ManejarRespuestaBuscarUsuariosPorNombre;

public class RecibirMensajes extends ManejadorDeMensajes {

    public RecibirMensajes(SocketConexion socketConexion) {
        super(socketConexion);
    }

    @Override
    public void inicializarManejadores() {
        manejadoresPeticiones.put(TipoMensaje.NOTIFICACION, new ManejarNotificaciones());
        manejadoresPeticiones.put(TipoMensaje.R_LOGIN_OK, new ManejarLogin());
        manejadoresPeticiones.put(TipoMensaje.C_CERRAR_SESION_CLIENTE, new ManejarCerrarSesion());
        manejadoresPeticiones.put(TipoMensaje.R_BUSCAR_USUARIOS_POR_NOMBRE, new ManejarRespuestaBuscarUsuariosPorNombre());
    }
}
