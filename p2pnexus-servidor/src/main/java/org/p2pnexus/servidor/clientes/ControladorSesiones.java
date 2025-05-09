package org.p2pnexus.servidor.clientes;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import org.p2pnexus.servidor.Entidades.DAO.ContactoDAO;
import org.p2pnexus.servidor.Entidades.DAO.UsuarioDAO;
import org.p2pnexus.servidor.Entidades.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControladorSesiones {
    public static Map<Integer, SesionCliente> sesiones = new ConcurrentHashMap<>();

    public static void agregarSesion(SocketConexion socketConexion, Usuario usuario) {

        if (sesiones.containsKey(usuario.getId_usuario())) {
            System.out.println("El usuario " + usuario.getNombre() + " ya tiene una sesión activa, desconectando la sesión anterior");
            SesionCliente sesionExistente = sesiones.get(usuario.getId_usuario());
            sesionExistente.desconectar("El usuario ha iniciado sesión desde otro dispositivo");
            sesiones.remove(usuario.getId_usuario());
        }

        SesionCliente sesion = new SesionCliente(socketConexion, usuario);
        sesiones.put(usuario.getId_usuario(), sesion);
        usuario.establecerConectado(true);
        notificarEstadoSesionAClientes(usuario);
    }

    public static void enviarMensajeClientes(List<Usuario> usuarios, Mensaje mensaje )
    {
        for (Usuario usuario : usuarios) {
            SesionCliente sesion = sesiones.get(usuario.getId_usuario());
            if (sesion != null) {
                sesion.getCliente().enviarMensaje(mensaje);
            }
        }
    }

    public static void enviarMensajeCliente(Integer idUsuario, Mensaje mensaje) {
        SesionCliente sesion = sesiones.get(idUsuario);
        if (sesion != null) {
            sesion.getCliente().enviarMensaje(mensaje);
        }
    }

    public static void eliminarSesion(Integer idUsuario) {
        SesionCliente sesion = sesiones.get(idUsuario);
        if (sesion != null) {
            sesion.desconectar("");
            sesiones.remove(idUsuario);
            sesion.getUsuario().establecerConectado(false);
            notificarEstadoSesionAClientes(sesion.usuario);
        }
    }

    public static void enviarMensajeAUsuariosEnLinea(Mensaje mensaje, List<Usuario> usuarios)
    {
        enviarMensajeAUsuariosEnLinea(mensaje, usuarios, null);
    }

    public static void enviarMensajeAUsuariosEnLinea(Mensaje mensaje, List<Usuario> usuarios, Usuario excluido)
    {
        List<SesionCliente> sesionesFiltradas = filtrarEnLinea(usuarios);
        // Eliminamos la sesion del usuario excluido
        if (excluido != null) {
            SesionCliente sesionExcluida = sesiones.get(excluido.getId_usuario());
            if (sesionExcluida != null) {
                sesionesFiltradas.remove(sesionExcluida);
            }
        }
        for (SesionCliente sesion : sesionesFiltradas) {
            sesion.getCliente().enviarMensaje(mensaje);
        }
    }

    public static List<SesionCliente> filtrarEnLinea(List<Usuario> usuarios)
    {
        List<SesionCliente> sesionesFiltradas = new ArrayList<>();
        // Recorremos la lista de usuarios y vemos cuales tienen sesion activa
        for (Usuario usuario : usuarios) {
            SesionCliente sesion = sesiones.get(usuario.getId_usuario());
            if (sesion != null) {
                sesionesFiltradas.add(sesion);
            }
        }
        return sesionesFiltradas;
    }

    public static SesionCliente getSesion(Integer id) {
        return sesiones.get(id);
    }

    public static void notificarEstadoSesionAClientes(Usuario usuario) {
        boolean estado;
        if (sesiones.get(usuario.getId_usuario()) == null) {
            estado = false;
        } else {
            estado = sesiones.get(usuario.getId_usuario()).usuario.conectado;
        }
        System.out.printf("Notificando estado de sesion a contactos de %s, estado: %s", usuario.getNombre(), estado);
        usuario.establecerConectado(estado);
        System.out.printf("Estado de sesion de %s: %s", usuario.getNombre(), usuario.getConectado());
        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> contactos = dao.listarContactos(usuario.getId_usuario());
        System.out.printf("Notificando estado de sesion a %s contactos", contactos.size());
        JsonObject json = new JsonObject();
        json.add("usuario", JsonHerramientas.convertirObjetoAJson(usuario));
        System.out.printf("Json: %s", json);

        enviarMensajeAUsuariosEnLinea(new Mensaje(TipoMensaje.R_ESTADO_SESION_CONTACTO,json),contactos);
    }
}
