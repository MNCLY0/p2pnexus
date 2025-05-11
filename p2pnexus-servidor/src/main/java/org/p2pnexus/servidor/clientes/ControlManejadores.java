package org.p2pnexus.servidor.clientes;

import com.p2pnexus.comun.comunicacion.ManejadorDeMensajes;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.TipoMensaje;
import org.p2pnexus.servidor.clientes.manejadores.ManejarLogin;
import org.p2pnexus.servidor.clientes.manejadores.ManejarRegistro;
import org.p2pnexus.servidor.clientes.manejadores.consultas.*;
import org.p2pnexus.servidor.clientes.manejadores.consultas.p2p.ManejarEnviarICEp2p;
import org.p2pnexus.servidor.clientes.manejadores.consultas.p2p.ManejarOfertaSDPp2p;
import org.p2pnexus.servidor.clientes.manejadores.consultas.p2p.ManejarRespuestaSDPp2p;

public class ControlManejadores extends ManejadorDeMensajes implements Runnable {

    public ControlManejadores(SocketConexion socketConexion) {
        // como la conexion viene de parte de un cliente, podemos notificar
        super(socketConexion,true);
    }

    public void inicializarManejadores()
    {
        manejadoresPeticiones.put(TipoMensaje.S_LOGIN, new ManejarLogin());
        manejadoresPeticiones.put(TipoMensaje.S_REGISTRO, new ManejarRegistro());
        manejadoresPeticiones.put(TipoMensaje.S_BUSCAR_USUARIOS_POR_NOMBRE, new ManejarConsultaUsuariosPorNombre());
        manejadoresPeticiones.put(TipoMensaje.S_SOLICITUDES_POR_ID, new ManejarConsultaSolicitudesPorId());
        manejadoresPeticiones.put(TipoMensaje.S_CREAR_SOLICITUD, new ManejarNuevaSolicitud());
        manejadoresPeticiones.put(TipoMensaje.S_ACEPTAR_SOLICITUD, new ManejarConsultaAceptarSolicitud());
        manejadoresPeticiones.put(TipoMensaje.S_DENEGAR_SOLICITUD, new ManejarConsultaRechazarSolicitud());
        manejadoresPeticiones.put(TipoMensaje.S_LISTA_CONTACTOS, new ManejarConsultaListaContactos());
        manejadoresPeticiones.put(TipoMensaje.S_ACTUALIZAR_CHAT, new ManejarConsultaActualizarChat());
        manejadoresPeticiones.put(TipoMensaje.S_ENVIAR_MENSAJE_CHAT, new ManejarConsultaEnviarMensaje());
        manejadoresPeticiones.put(TipoMensaje.S_CONVERSACION_CON_USUARIO, new ManejarConsultaSolicitudConversacionConUsuario());
        manejadoresPeticiones.put(TipoMensaje.S_CREAR_ESPACIO, new ManejarCrearEspacio());
        manejadoresPeticiones.put(TipoMensaje.S_ESPACIOS_POR_ID, new ManejarConsultaEspaciosPorId());
        manejadoresPeticiones.put(TipoMensaje.S_ELIMINAR_ESPACIO, new ManejarConsultaEliminarEspacio());
        manejadoresPeticiones.put(TipoMensaje.S_EDITAR_ESPACIO, new ManejarConsultaEditarEspacio());
        manejadoresPeticiones.put(TipoMensaje.S_COMPARTIR_ESPACIO, new ManejarConsultaCompartirEspacio());
        manejadoresPeticiones.put(TipoMensaje.S_DEJAR_DE_COMPARTIR_ESPACIO, new ManejarConsultaEliminarAccesoAEspacio());
        manejadoresPeticiones.put(TipoMensaje.S_P2P_SDP_OFERTA, new ManejarOfertaSDPp2p());
        manejadoresPeticiones.put(TipoMensaje.S_P2P_SDP_RESPUESTA, new ManejarRespuestaSDPp2p());
        manejadoresPeticiones.put(TipoMensaje.S_P2P_ICE, new ManejarEnviarICEp2p());
    }

    @Override
    public void desconectar()
    {
        //si el socket tiene un cliente asociado eliminamos la refenrencia de la sesion
        if (getSocketConexion().getIdUsuario() != -1)
        {
            ControladorSesiones.eliminarSesion(getSocketConexion().getIdUsuario());
        }
    }

}
