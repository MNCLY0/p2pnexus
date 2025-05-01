package org.p2pnexus.servidor.clientes;

import com.p2pnexus.comun.comunicacion.SocketConexion;
import org.p2pnexus.servidor.Entidades.Usuario;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControladorSesiones {
    public static Map<String, SesionCliente> sesiones = new ConcurrentHashMap<>();

    public static void agregarSesion(SocketConexion socketConexion, Usuario usuario) {

        if (sesiones.containsKey(usuario.getNombre())) {
            System.out.println("El usuario " + usuario.getNombre() + " ya tiene una sesión activa, desconectando la sesión anterior");
            SesionCliente sesionExistente = sesiones.get(usuario.getNombre());
            sesionExistente.desconectar("El usuario ha iniciado sesión desde otro dispositivo");
            sesiones.remove(usuario.getNombre());
        }

        SesionCliente sesion = new SesionCliente(socketConexion, usuario);
        sesiones.put(usuario.getNombre(), sesion);
    }

    public static SesionCliente getSesion(String nombreUsuario) {
        return sesiones.get(nombreUsuario);
    }
}
