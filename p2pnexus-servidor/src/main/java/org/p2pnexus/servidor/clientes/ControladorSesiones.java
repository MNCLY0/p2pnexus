package org.p2pnexus.servidor.clientes;

import com.p2pnexus.comun.comunicacion.SocketConexion;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControladorSesiones {
    public static Map<String, SesionCliente> sesiones = new ConcurrentHashMap<>();

    public static void agregarSesion(SocketConexion socketConexion, String nombreUsuario) {

        if (sesiones.containsKey(nombreUsuario)) {
            System.out.println("El usuario " + nombreUsuario + " ya tiene una sesión activa, desconectando la sesión anterior");
            SesionCliente sesionExistente = sesiones.get(nombreUsuario);
            sesionExistente.desconectar("El usuario ha iniciado sesión desde otro dispositivo");
            sesiones.remove(nombreUsuario);
        }

        SesionCliente sesion = new SesionCliente(socketConexion, nombreUsuario);
        sesiones.put(nombreUsuario, sesion);
    }




}
