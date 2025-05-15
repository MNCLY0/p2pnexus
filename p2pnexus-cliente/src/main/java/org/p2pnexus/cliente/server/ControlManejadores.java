package org.p2pnexus.cliente.server;

import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.ManejadorDeMensajes;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import org.p2pnexus.cliente.server.manejadores.*;
import org.p2pnexus.cliente.server.manejadores.p2p.ManejarRecibirIceP2P;
import org.p2pnexus.cliente.server.manejadores.p2p.ManejarRecibirOfertaP2P;
import org.p2pnexus.cliente.server.manejadores.p2p.ManejarRecibirRespuestaP2P;

public class ControlManejadores extends ManejadorDeMensajes {

    public ControlManejadores(SocketConexion socketConexion) {
        super(socketConexion);
    }

    @Override
    public void inicializarManejadores() {
        manejadoresPeticiones.put(TipoMensaje.NOTIFICACION, new ManejarNotificaciones());
        manejadoresPeticiones.put(TipoMensaje.R_LOGIN_OK, new ManejarLogin());
        manejadoresPeticiones.put(TipoMensaje.R_CERRAR_SESION_CLIENTE, new ManejarCerrarSesion());
        manejadoresPeticiones.put(TipoMensaje.R_BUSCAR_USUARIOS_POR_NOMBRE, new ManejarRespuestaBuscarUsuariosPorNombre());
        manejadoresPeticiones.put(TipoMensaje.R_SOLICITUDES_POR_ID, new ManejarRespuestaSolicitudesPorId());
        manejadoresPeticiones.put(TipoMensaje.R_LISTA_CONTACTOS, new MenejarRespuestaListaContactos());
        manejadoresPeticiones.put(TipoMensaje.R_ACTUALIZAR_CHAT, new ManejarRespuestaActualizarChat());
        manejadoresPeticiones.put(TipoMensaje.R_NUEVO_MENSAJE_CHAT, new ManejarRespuestaNuevoMensajeChat());
        manejadoresPeticiones.put(TipoMensaje.R_CONVERSACION_CON_USUARIO, new ManejarRespuestaSolicitudConversacionConUsuario());
        manejadoresPeticiones.put(TipoMensaje.R_ESPACIOS_POR_ID, new ManejarRespuestaEspaciosPorId());
        manejadoresPeticiones.put(TipoMensaje.R_CREAR_ESPACIO_OK, new ManejarRespuestaCrearEspacioOk());
        manejadoresPeticiones.put(TipoMensaje.R_COMPARTIR_ESPACIO_OK, new ManejarRespuestaCompartirEspacioOk());
        manejadoresPeticiones.put(TipoMensaje.R_NUEVO_ESPACIO_RECIBIDO, new ManejarNuevoEspacioRecibido());
        manejadoresPeticiones.put(TipoMensaje.R_ACTUALIZAR_ESPACIO_RECIBIDO, new ManejarActualizarEspacioRecibido());
        manejadoresPeticiones.put(TipoMensaje.R_ELIMINAR_ESPACIO_RECIBIDO, new ManejarEliminarEspacioRecibido());
        manejadoresPeticiones.put(TipoMensaje.R_ESTADO_SESION_CONTACTO, new ManejarRespuestaEstadoSesionContacto());
        manejadoresPeticiones.put(TipoMensaje.R_P2P_SDP_OFERTA, new ManejarRecibirOfertaP2P());
        manejadoresPeticiones.put(TipoMensaje.R_P2P_SDP_RESPUESTA, new ManejarRecibirRespuestaP2P());
        manejadoresPeticiones.put(TipoMensaje.R_P2P_ICE, new ManejarRecibirIceP2P());
    }

    @Override
    public void desconectar() {
        System.out.println("Desconectando el cliente del servidor...");
        Conexion.cerrarConexion();
    }
}
