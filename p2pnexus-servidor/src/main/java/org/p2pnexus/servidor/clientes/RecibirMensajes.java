package org.p2pnexus.servidor.clientes;

import com.p2pnexus.comun.comunicacion.ManejadorDeMensajes;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.TipoMensaje;
import org.p2pnexus.servidor.clientes.acciones.ManejarLogin;
import org.p2pnexus.servidor.clientes.acciones.ManejarRegistro;

public class RecibirMensajes extends ManejadorDeMensajes implements Runnable {

    public RecibirMensajes(SocketConexion socketConexion) {
        // como la conexion viene de parte de un cliente, podemos notificar
        super(socketConexion,true);
    }

    public void inicializarManejadores()
    {
        manejadoresPeticiones.put(TipoMensaje.P_LOGIN, new ManejarLogin());
        manejadoresPeticiones.put(TipoMensaje.P_REGISTRO, new ManejarRegistro());
    }

}
