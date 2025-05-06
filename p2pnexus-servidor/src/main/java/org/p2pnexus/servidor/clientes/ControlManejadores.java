package org.p2pnexus.servidor.clientes;

import com.p2pnexus.comun.comunicacion.ManejadorDeMensajes;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.TipoMensaje;
import org.p2pnexus.servidor.clientes.manejadores.ManejarLogin;
import org.p2pnexus.servidor.clientes.manejadores.ManejarRegistro;
import org.p2pnexus.servidor.clientes.manejadores.consultas.*;

public class ControlManejadores extends ManejadorDeMensajes implements Runnable {

    public ControlManejadores(SocketConexion socketConexion) {
        // como la conexion viene de parte de un cliente, podemos notificar
        super(socketConexion,true);
    }

    public void inicializarManejadores()
    {
        manejadoresPeticiones.put(TipoMensaje.C_LOGIN, new ManejarLogin());
        manejadoresPeticiones.put(TipoMensaje.C_REGISTRO, new ManejarRegistro());
        manejadoresPeticiones.put(TipoMensaje.C_BUSCAR_USUARIOS_POR_NOMBRE, new ManejarConsultaUsuariosPorNombre());
        manejadoresPeticiones.put(TipoMensaje.C_SOLICITUDES_POR_ID, new ManejarConsultaSolicitudesPorId());
        manejadoresPeticiones.put(TipoMensaje.C_CREAR_SOLICITUD, new ManejarNuevaSolicitud());
        manejadoresPeticiones.put(TipoMensaje.C_ACEPTAR_SOLICITUD, new ManejarConsultaAceptarSolicitud());
        manejadoresPeticiones.put(TipoMensaje.C_DENEGAR_SOLICITUD, new ManejarConsultaRechazarSolicitud());
        manejadoresPeticiones.put(TipoMensaje.C_LISTA_CONTACTOS, new ManejarConsultaListaContactos());
        manejadoresPeticiones.put(TipoMensaje.C_ACTUALIZAR_CHAT, new ManejarConsultaActualizarChat());
        manejadoresPeticiones.put(TipoMensaje.C_ENVIAR_MENSAJE_CHAT, new ManejarConsultaEnviarMensaje());
        manejadoresPeticiones.put(TipoMensaje.C_CONVERSACION_CON_USUARIO, new ManejarConsultaSolicitudConversacionConUsuario());
        manejadoresPeticiones.put(TipoMensaje.C_CREAR_ESPACIO, new ManejarCrearEspacio());
        manejadoresPeticiones.put(TipoMensaje.C_ESPACIOS_POR_ID, new ManejarConsultaEspaciosPorId());
        manejadoresPeticiones.put(TipoMensaje.C_ELIMINAR_ESPACIO, new ManejarConsultaEliminarEspacio());
        manejadoresPeticiones.put(TipoMensaje.C_EDITAR_ESPACIO, new ManejarConsultaEditarEspacio());
        manejadoresPeticiones.put(TipoMensaje.C_COMPARTIR_ESPACIO, new ManejarConsultaCompartirEspacio());
    }

}
