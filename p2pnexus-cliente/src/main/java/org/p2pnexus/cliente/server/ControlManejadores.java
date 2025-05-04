package org.p2pnexus.cliente.server;

import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.ManejadorDeMensajes;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import org.p2pnexus.cliente.server.manejadores.*;

public class ControlManejadores extends ManejadorDeMensajes {

    public ControlManejadores(SocketConexion socketConexion) {
        super(socketConexion);
    }

    @Override
    public void inicializarManejadores() {
        manejadoresPeticiones.put(TipoMensaje.NOTIFICACION, new ManejarNotificaciones());
        manejadoresPeticiones.put(TipoMensaje.R_LOGIN_OK, new ManejarLogin());
        manejadoresPeticiones.put(TipoMensaje.C_CERRAR_SESION_CLIENTE, new ManejarCerrarSesion());
        manejadoresPeticiones.put(TipoMensaje.R_BUSCAR_USUARIOS_POR_NOMBRE, new ManejarRespuestaBuscarUsuariosPorNombre());
        manejadoresPeticiones.put(TipoMensaje.R_SOLICITUDES_POR_ID, new ManejarRespuestaSolicitudesPorId());
        manejadoresPeticiones.put(TipoMensaje.R_LISTA_CONTACTOS, new MenejarRespuestaListaContactos());
        manejadoresPeticiones.put(TipoMensaje.R_MENSAJES_CHAT, new ManejarRespuestaMensajesChat());
        manejadoresPeticiones.put(TipoMensaje.R_NUEVO_MENSAJE_CHAT, new ManejarRespuestaNuevoMensajeChat());
        manejadoresPeticiones.put(TipoMensaje.R_CONVERSACION_CON_USUARIO, new ManejarRespuestaSolicitudConversacionConUsuario());
        manejadoresPeticiones.put(TipoMensaje.R_ESPACIOS_POR_ID, new ManejarRespuestaEspaciosPorId());
        manejadoresPeticiones.put(TipoMensaje.R_CREAR_ESPACIO_OK, new ManejarRespuestaCrearEspacioOk());
    }
}
