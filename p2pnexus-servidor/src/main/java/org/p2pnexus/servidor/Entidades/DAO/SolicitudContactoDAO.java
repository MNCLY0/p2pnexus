package org.p2pnexus.servidor.Entidades.DAO;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import org.hibernate.Session;
import org.p2pnexus.servidor.Entidades.EstadoSolicitud;
import org.p2pnexus.servidor.Entidades.SolicitudContacto;
import org.p2pnexus.servidor.Entidades.Usuario;
import org.p2pnexus.servidor.clientes.ControladorSesiones;
import org.p2pnexus.servidor.clientes.SesionCliente;

import java.util.ArrayList;
import java.util.List;

public class SolicitudContactoDAO extends DAO{

    // Entendemos como pendientes las solicitudes que no han sido aceptadas ni rechazadas
    public ArrayList<SolicitudContacto> obtenerSolicitudesPendientesDeUsuario(int id_usuario) {
        try(Session session = getSessionFactory().openSession()) {
            return (ArrayList<SolicitudContacto>) session.createQuery("FROM SolicitudContacto WHERE usuarioDestino.id_usuario = :id_usuario AND estado NOT IN :estado", SolicitudContacto.class)
                    .setParameter("id_usuario", id_usuario)
                    .setParameterList("estado", new EstadoSolicitud[]{EstadoSolicitud.ACEPTADA, EstadoSolicitud.RECHAZADA})
                    .list();
        }
    }

    public boolean crearSolicitud(int idUsuarioOrigen, int idUsuarioDestino)
    {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();

            if (!puedeMandarSolicitud(idUsuarioOrigen, idUsuarioDestino))
            {
                return false;
            }

            SolicitudContacto solicitud = new SolicitudContacto();
            Usuario usuarioOrigen = session.get(Usuario.class, idUsuarioOrigen);
            Usuario usuarioDestino = session.get(Usuario.class, idUsuarioDestino);
            solicitud.setUsuarioOrigen(usuarioOrigen);
            solicitud.setUsuarioDestino(usuarioDestino);
            solicitud.setEstado(EstadoSolicitud.PENDIENTE);

            session.persist(solicitud);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la solicitud: " + e.getMessage(), e);
        }
    }

    public ArrayList<Usuario> buscarUsuariosSolicitables(String nombre, int idUsuario_origen){
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ArrayList<Usuario> usuarios = usuarioDAO.buscarUsuariosPorNombre(nombre);

        // Filtramos los usuarios para que no se devuelvan los que ya son amigos o tienen una solicitud pendiente, y al proprio usuario
        ArrayList<Usuario> usuariosFiltrados = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getId_usuario() != idUsuario_origen && puedeMandarSolicitud(idUsuario_origen, usuario.getId_usuario())) {
                usuariosFiltrados.add(usuario);
            }
        }
        return usuariosFiltrados;
    }

    // Solo devuelve true si la solicitud se ha aceptado
    public boolean actualizarEstadoSolicitud(int idSolicitud, EstadoSolicitud estado) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            SolicitudContacto solicitud = session.get(SolicitudContacto.class, idSolicitud);
            if (solicitud != null) {
                solicitud.setEstado(estado);
                session.persist(solicitud);
                session.getTransaction().commit();
                if (estado == EstadoSolicitud.ACEPTADA) {
                    // Si la solicitud es aceptada, se añade el contacto a la lista de contactos de ambos usuarios y se les actualiza la lista de contactos
                    ContactoDAO contactoDAO = new ContactoDAO();
                    contactoDAO.crearContacto(solicitud.getUsuarioOrigen().getId_usuario(), solicitud.getUsuarioDestino().getId_usuario());
                    UsuarioDAO usuarioDAO = new UsuarioDAO();

                    // Esta parte no me gusta mucho, si me da tiempo hay que refactorizar
                    // Basicamente lo que estamos haciendo es obtener las sesiones de ambos usuarios y enviarles un mensaje para que actualicen su lista de contactos
                    List<SesionCliente> sesionClientes = List.of(
                            ControladorSesiones.getSesion(solicitud.getUsuarioOrigen().getNombre()),
                            ControladorSesiones.getSesion(solicitud.getUsuarioDestino().getNombre())
                    );
                    for (SesionCliente sesionCliente : sesionClientes) {
                        // Si la sesion es nula, significa que el usuario no esta conectado
                        if (sesionCliente != null) {
                            // Actualizamos la lista de contactos del usuario
                            List<Usuario> contactos = usuarioDAO.listarContactos(sesionCliente.getUsuario().getId_usuario());
                            sesionCliente.getCliente().enviarMensaje(new Mensaje(TipoMensaje.R_LISTA_CONTACTOS, JsonHerramientas.empaquetarListaEnJsonObject(contactos)));
                        }
                    }
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al aceptar la solicitud: " + e.getMessage(), e);
        }
    }


    // Entendemos que una solicitud se puede mandar si no hay una solicitud pendiente o aceptada entre los dos usuarios y estos siguen siendo contcatos
    public boolean puedeMandarSolicitud(int idUsuarioOrigen, int idUsuarioDestino) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();

            // Buscar si hay una solicitud pendiente o aceptada entre los dos usuarios
            SolicitudContacto solicitud = session.createQuery(
                            "FROM SolicitudContacto " +
                                    "WHERE ((usuarioOrigen.id_usuario = :idUsuarioOrigen AND usuarioDestino.id_usuario = :idUsuarioDestino) " +
                                    "OR (usuarioOrigen.id_usuario = :idUsuarioDestino AND usuarioDestino.id_usuario = :idUsuarioOrigen)) " +
                                    "AND (estado = :pendiente OR estado = :aceptada)", SolicitudContacto.class)
                    .setParameter("idUsuarioOrigen", idUsuarioOrigen)
                    .setParameter("idUsuarioDestino", idUsuarioDestino)
                    .setParameter("pendiente", EstadoSolicitud.PENDIENTE)
                    .setParameter("aceptada", EstadoSolicitud.ACEPTADA)
                    .uniqueResult();

            // Si no hay solicitud previa se puede mandar
            if (solicitud == null) return true;

            // Si la solicitud está pendiente no se puede mandar (hasta que se acepte o se rechace)
            if (solicitud.getEstado() == EstadoSolicitud.PENDIENTE) return false;

            // Si la solicitud ha sido aceptada comprobar si siguen siendo contactos
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            return !usuarioDAO.sonContactos(idUsuarioOrigen, idUsuarioDestino);

        } catch (Exception e) {
            throw new RuntimeException("Error al verificar la posibilidad de enviar solicitud: " + e.getMessage(), e);
        }
    }

}
