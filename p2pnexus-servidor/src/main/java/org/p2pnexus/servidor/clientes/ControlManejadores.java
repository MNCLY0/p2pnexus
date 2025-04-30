package org.p2pnexus.servidor.clientes;

import com.p2pnexus.comun.comunicacion.ManejadorDeMensajes;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.TipoMensaje;
import org.p2pnexus.servidor.clientes.manejadores.ManejarLogin;
import org.p2pnexus.servidor.clientes.manejadores.ManejarRegistro;
import org.p2pnexus.servidor.clientes.manejadores.consultas.ManejarConsultaSolicitudesPorId;
import org.p2pnexus.servidor.clientes.manejadores.consultas.ManejarConsultaUsuariosPorNombre;
import org.p2pnexus.servidor.clientes.manejadores.consultas.ManejarNuevaSolicitud;

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
    }

}
