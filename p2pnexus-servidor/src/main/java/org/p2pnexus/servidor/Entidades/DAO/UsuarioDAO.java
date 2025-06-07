package org.p2pnexus.servidor.Entidades.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.p2pnexus.servidor.Entidades.Usuario;
import org.p2pnexus.servidor.clientes.ControladorSesiones;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends DAO{

    public Integer crearUsuario(String nombre, String contrasena) {
        try(Session session = getSession()) {
            Transaction transaction = null;
            transaction = session.beginTransaction();

            // Creamos nuevo usuario con los datos proporcionados
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setContrasena(contrasena);

            // Guardamos el usuario en la base de datos
            session.persist(usuario);
            transaction.commit();

            return usuario.getId_usuario();
        }
    }

    // Este metodo permite buscar una lista de usuarios por su nombre, buscando coincidencias parciales (para que la busqueda sea mas flexible)
    public ArrayList<Usuario> buscarUsuariosPorNombre(String nombre) {
        try(Session session = getSession()) {
            // no queremos que el usuario pueda buscar por % porque eso en un where devuelve todo
            if (nombre.equals("%")) throw new RuntimeException("El nombre no es valido");

            return (ArrayList<Usuario>) session.createQuery("FROM Usuario WHERE nombre LIKE :nombre", Usuario.class)
                    .setParameter("nombre", "%" + nombre + "%")
                    .setMaxResults(10) // Limitamos a 10 resultados, no queremos que se devuelvan demasiados sería una locura que algunos casos
                    .list();
        }


    }

    // Este metodo permite buscar un usuario por su nombre
    public Usuario buscarPorNombre(String nombre) {
        try(Session session = getSession()) {
            return session.createQuery("FROM Usuario WHERE nombre = :nombre", Usuario.class)
                    .setParameter("nombre", nombre)
                    .uniqueResult();
        }
    }



    // Nos devuelve true o false dependiendo si el usuario ya existe o no
    public boolean yaExiste(String nombre) {
        try(Session session = getSession()) {
            return session.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.nombre = :nombre", Long.class)
                    .setParameter("nombre", nombre)
                    .uniqueResult() > 0;
        }

    }

    // Este metodo valida las credenciales del usuario
    public Usuario validarCredenciales(String nombre, String contrasena) {
        Usuario usuario = buscarPorNombre(nombre);
        if (usuario == null) {
            return null;
        }
        if (!usuario.getContrasena().equals(contrasena)) {
            return null;
        }
        return usuario;
    }

    // Listamos todos los contactos de un usuario, como la relacion es bidireccional, no importa si el usuario1 es el que busca o el usuario2
    // siempre devolvemos el contacto diferente al usuario que busca
    public List<Usuario> listarContactos(int id_usuario) {
        try(Session session = getSession()) {
            List<Usuario> contactos = session.createQuery(
                            "SELECT u FROM Usuario u WHERE u.id_usuario IN " +
                                    "(SELECT CASE " +
                                    "WHEN c.usuario1.id_usuario = :id_usuario THEN c.usuario2.id_usuario " +
                                    "ELSE c.usuario1.id_usuario END " +
                                    "FROM Contacto c " +
                                    "WHERE c.usuario1.id_usuario = :id_usuario OR c.usuario2.id_usuario = :id_usuario)", Usuario.class)
                    .setParameter("id_usuario", id_usuario)
                    .list();
            // Establecemos el estado de cada contacto
            for (Usuario usuario : contactos) {
                boolean estado = ControladorSesiones.getSesion(usuario.getId_usuario()) != null;
                usuario.establecerConectado(estado);
            }
            return contactos;
        }

    }

    public boolean sonContactos(int id_usuario1, int id_usuario2) {
        try(Session session = getSession()) {
            return session.createQuery("SELECT COUNT(c) FROM Contacto c WHERE " +
                            "(c.usuario1.id_usuario = :id_usuario1 AND c.usuario2.id_usuario = :id_usuario2) OR " +
                            "(c.usuario1.id_usuario = :id_usuario2 AND c.usuario2.id_usuario = :id_usuario1)", Long.class)
                    .setParameter("id_usuario1", id_usuario1)
                    .setParameter("id_usuario2", id_usuario2)
                    .uniqueResult() > 0;
        }

    }

    public void actualizar(Usuario usuario) {
        try(Session session = getSession()) {
            Transaction transaction = null;
            transaction = session.beginTransaction();
            session.merge(usuario);
            transaction.commit();
        }
    }

    public void guardar(Usuario usuario) {
        try(Session session = getSession()) {
            Transaction transaction = null;
            transaction = session.beginTransaction();
            session.persist(usuario);
            transaction.commit();
        }
    }

    public void eliminar(Usuario usuario) {
        try(Session session = getSession()) {
            Transaction transaction = null;
            transaction = session.beginTransaction();
            session.remove(usuario);
            transaction.commit();
        }
    }

    public Usuario buscarPorId(int id_usuario) {
        try(Session session = getSession()) {
            return session.get(Usuario.class, id_usuario);
        }


    }
}