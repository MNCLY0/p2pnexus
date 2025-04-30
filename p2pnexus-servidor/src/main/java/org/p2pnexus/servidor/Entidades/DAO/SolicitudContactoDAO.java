package org.p2pnexus.servidor.Entidades.DAO;

import org.hibernate.Session;
import org.p2pnexus.servidor.Entidades.EstadoSolicitud;
import org.p2pnexus.servidor.Entidades.SolicitudContacto;
import org.p2pnexus.servidor.Entidades.Usuario;

import java.util.ArrayList;

public class SolicitudContactoDAO extends DAO{

    public ArrayList<SolicitudContacto> obtenerSolicitudesPendientesDeUsuario(int id_usuario) {
        try(Session session = getSessionFactory().openSession()) {
            return (ArrayList<SolicitudContacto>) session.createQuery("FROM SolicitudContacto WHERE usuarioDestino.id_usuario = :id_usuario", SolicitudContacto.class)
                    .setParameter("id_usuario", id_usuario)
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

    public boolean puedeMandarSolicitud(int idUsuarioOrigen, int idUsuarioDestino) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            // La logica que seguimos para saber si puede mandar la solicitud es que no exista una solicitud pendiente (en ambos sentidos)
            // o aceptada entre los dos usuarios
            SolicitudContacto solicitud = session.createQuery(
                            "FROM SolicitudContacto " +
                                    "WHERE ((usuarioOrigen.id_usuario = :idUsuarioOrigen AND usuarioDestino.id_usuario = :idUsuarioDestino) " +
                                    "   OR (usuarioOrigen.id_usuario = :idUsuarioDestino AND usuarioDestino.id_usuario = :idUsuarioOrigen)) " +
                                    "AND (estado = :pendiente OR estado = :aceptada)", SolicitudContacto.class)
                    .setParameter("idUsuarioOrigen", idUsuarioOrigen)
                    .setParameter("idUsuarioDestino", idUsuarioDestino)
                    .setParameter("pendiente", EstadoSolicitud.PENDIENTE)
                    .setParameter("aceptada", EstadoSolicitud.ACEPTADA)
                    .uniqueResult();

            return solicitud == null;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar la existencia de la solicitud: " + e.getMessage(), e);
        }
    }

}
