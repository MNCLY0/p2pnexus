package org.p2pnexus.servidor.clientes;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.SocketConexion;

public class SesionCliente {
    SocketConexion cliente;
    String nombreUsuario;
    String ip;
    int puerto;

    public SesionCliente(SocketConexion cliente, String nombreUsuario) {
        this.cliente = cliente;
        this.nombreUsuario = nombreUsuario;
        this.ip = cliente.getSocket().getInetAddress().getHostAddress();
        this.puerto = cliente.getSocket().getPort();
    }

    public SocketConexion getCliente() {
        return cliente;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getIp() {
        return ip;
    }

    public void desconectar(String motivo) {
        try {
            cliente.enviarMensaje(FabricaMensajes.crearNotificacion("Desconectando del servidor " + motivo, TipoNotificacion.ERROR));
            cliente.enviarMensaje(new Mensaje(TipoMensaje.C_CERRAR_SESION_CLIENTE));
            // TODO implementar el cierre de la conexión, no podemos cerrar el socket porque el cliente debe seguir conectado
        } catch (Exception e) {
            System.err.println("Error al cerrar la conexión del cliente: " + e.getMessage());
        }
    }
    public void desconectar() {
        desconectar("");
    }
}
