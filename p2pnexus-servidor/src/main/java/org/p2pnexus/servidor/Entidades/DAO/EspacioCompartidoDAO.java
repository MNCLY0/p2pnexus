package org.p2pnexus.servidor.Entidades.DAO;

import org.hibernate.Session;
import org.p2pnexus.servidor.ControladorHibernate;
import org.p2pnexus.servidor.Entidades.Conversacion;
import org.p2pnexus.servidor.Entidades.EspacioCompartido;
import org.p2pnexus.servidor.Entidades.PermisoAcceso;
import org.p2pnexus.servidor.Entidades.Usuario;

import java.util.List;

public class EspacioCompartidoDAO extends DAO{

    public EspacioCompartido crearEspacioCompartido(EspacioCompartido espacioCompartido) {
        try(Session session = getSession()) {
            session.beginTransaction();
            session.persist(espacioCompartido);
            session.getTransaction().commit();
            return espacioCompartido;
        }
    }

    public void editarEspacioCompartido(EspacioCompartido modificado) {
        try(Session session = getSession()) {
            session.beginTransaction();

            EspacioCompartido entidadOriginal = session.get(EspacioCompartido.class, modificado.getId_espacio());
            if (entidadOriginal != null) {
                entidadOriginal.setNombre(modificado.getNombre());
                entidadOriginal.setRuta_directorio(modificado.getRuta_directorio());
                session.merge(entidadOriginal);
            }

            session.getTransaction().commit();
        }

    }

    public void eliminarEspacioCompartido(int idEspacioCompartido) {
        try(Session session = getSession()) {
            session.beginTransaction();
            EspacioCompartido espacio = session.get(EspacioCompartido.class, idEspacioCompartido);
            if (espacio != null) {
                session.remove(espacio);
            }
            session.getTransaction().commit();
        }


    }

    public List<EspacioCompartido> espaciosCompartidosPorPropietario(int id) {
        try(Session session = getSession()) {
            session.beginTransaction();
            List<EspacioCompartido> espaciosCompartidos = session.createQuery("FROM EspacioCompartido WHERE propietario.id_usuario = :id", EspacioCompartido.class)
                    .setParameter("id", id)
                    .getResultList();
            session.getTransaction().commit();
            System.out.println("Espacios compartidos por propietario: " + espaciosCompartidos);
            return espaciosCompartidos;
        }
    }

    // Esto ahora mismo no tiene mucho sentido porque las conversaciones ahora mismo solo son entre dos usuarios, pero si lo hacemos de esta manera
    // luego cuando hagamos los grupos no habría que cambiar nada de este punto
    public List<Usuario> crearPermisosATodosLosUsuariosDeConversacion(EspacioCompartido espacio, Conversacion conversacion) {
        ConversacionDAO conversacionDAO = new ConversacionDAO();
        List<Usuario> usuarios = conversacionDAO.obtenerUsuariosParticipantesConversacion(conversacion.getId_conversacion());
        for (Usuario usuario : usuarios) {
            // No tiene sentido crear un permiso de acceso a un espacio que ya es de su propiedad así que nos lo saltamos
            if (usuario.getId_usuario().equals(espacio.getPropietario().getId_usuario())) continue;
            crearPermisoDeAccesoAUsuario(espacio, usuario);
        }
        return usuarios;
    }

    public List<Usuario> eliminarAccesoAEspacioCompartido(EspacioCompartido espacio, Conversacion conversacion) {
        ConversacionDAO conversacionDAO = new ConversacionDAO();
        List<Usuario> usuarios = conversacionDAO.obtenerUsuariosParticipantesConversacion(conversacion.getId_conversacion());
        for (Usuario usuario : usuarios) {
            //Lo mismo de antes, nos saltamos al propietario
            if (usuario.getId_usuario().equals(espacio.getPropietario().getId_usuario())) continue;
            eliminarPermisoDeAccesoAUsuario(espacio, usuario);
        }
        return usuarios;
    }

    public void eliminarPermisoDeAccesoAUsuario(EspacioCompartido espacio, Usuario usuario) {
        try(Session session = getSession()) {
            session.beginTransaction();
            PermisoAcceso permisoAcceso = session.createQuery("FROM PermisoAcceso WHERE espacioCompartido.id_espacio = :idEspacio AND usuario.id_usuario = :idUsuario", PermisoAcceso.class)
                    .setParameter("idEspacio", espacio.getId_espacio())
                    .setParameter("idUsuario", usuario.getId_usuario())
                    .uniqueResult();
            if (permisoAcceso != null) {
                session.remove(permisoAcceso);
            }
            session.getTransaction().commit();
        }
    }

    public void crearPermisoDeAccesoAUsuario(EspacioCompartido espacio, Usuario usuario) {
        try(Session session = getSession()) {
            session.beginTransaction();
            PermisoAcceso permisoAcceso = new PermisoAcceso();
            permisoAcceso.setEspacioCompartido(espacio);
            permisoAcceso.setUsuario(usuario);
            session.persist(permisoAcceso);
            session.getTransaction().commit();
        }

    }

    public List<EspacioCompartido> espaciosCompartidosPorUsuarioConOtroUsuario(int idUsuario1, int idUsuario2) {
        try(Session session = getSession()) {
            session.beginTransaction();
            List<EspacioCompartido> espaciosCompartidos = session.createQuery("SELECT p.espacioCompartido FROM PermisoAcceso p WHERE p.usuario.id_usuario = :idUsuario2 AND p.espacioCompartido.propietario.id_usuario = :idUsuario1", EspacioCompartido.class)
                    .setParameter("idUsuario1", idUsuario1)
                    .setParameter("idUsuario2", idUsuario2)
                    .getResultList();
            session.getTransaction().commit();
            return espaciosCompartidos;
        }
    }

    public List<Usuario> usuariosConAccesoAEspacio(EspacioCompartido espacio) {
        try(Session session = getSession()) {
            session.beginTransaction();
            List<Usuario> usuarios = session.createQuery("SELECT p.usuario FROM PermisoAcceso p WHERE p.espacioCompartido.id_espacio = :idEspacio", Usuario.class)
                    .setParameter("idEspacio", espacio.getId_espacio())
                    .getResultList();
            session.getTransaction().commit();
            return usuarios;
        }
    }
}
