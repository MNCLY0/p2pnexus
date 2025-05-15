package org.p2pnexus.servidor.clientes;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import org.p2pnexus.servidor.Entidades.Usuario;

public class SesionCliente {
    SocketConexion cliente;
    Usuario usuario;
    String ip;
    int puerto;

    public SesionCliente(SocketConexion cliente, Usuario usuario) {
        this.cliente = cliente;
        this.usuario = usuario;
        this.ip = cliente.getSocket().getInetAddress().getHostAddress();
        this.puerto = cliente.getSocket().getPort();
        cliente.setIdUsuario(usuario.getId_usuario());
    }

    public SocketConexion getCliente() {
        return cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getIp() {
        return ip;
    }

    public void desconectar(String motivo) {
        try {
            if (cliente.getSocket().isClosed()) return; // ya está desconectado
            // El motivo es opcional si no se pasa nada se cierra la conexión sin notificar
            if (!motivo.isEmpty()) cliente.enviarMensaje(FabricaMensajes.crearNotificacion("Desconectando del servidor " + motivo, TipoNotificacion.ERROR));
            cliente.enviarMensaje(new Mensaje(TipoMensaje.R_CERRAR_SESION_CLIENTE));
        } catch (Exception e) {
            System.err.println("Error al cerrar la conexión del cliente: " + e.getMessage());
        }
    }
    public void desconectar() {
        desconectar("");
    }
}
