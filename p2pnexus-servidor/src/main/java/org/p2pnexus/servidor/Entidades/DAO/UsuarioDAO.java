package org.p2pnexus.servidor.Entidades.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.p2pnexus.servidor.Entidades.Usuario;

import java.util.ArrayList;

public class UsuarioDAO extends DAO{

    public Integer crearUsuario(String nombre, String contrasena) {
        Transaction transaction = null;

        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Creamos nuevo usuario con los datos proporcionados
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setContrasena(contrasena);

            // Guardamos el usuario en la base de datos
            session.persist(usuario);
            transaction.commit();

            return usuario.getId_usuario();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException();
        }
    }

    // Este metodo permite buscar una lista de usuarios por su nombre, buscando coincidencias parciales (para que la busqueda sea mas flexible)
    public ArrayList<Usuario> buscarUsuariosPorNombre(String nombre) {
        try (Session session = getSessionFactory().openSession()) {
            return (ArrayList<Usuario>) session.createQuery("FROM Usuario WHERE nombre LIKE :nombre", Usuario.class)
                    .setParameter("nombre", "%" + nombre + "%")
                    .setMaxResults(10) // Limitamos a 10 resultados, no queremos que se devuelvan demasiados sería una locura que algunos casos
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar usuarios: " + e.getMessage(), e);
        }
    }

    // Este metodo permite buscar un usuario por su nombre
    public Usuario buscarPorNombre(String nombre) {
        try (Session session = getSessionFactory().openSession()) {
            return session.createQuery("FROM Usuario WHERE nombre = :nombre", Usuario.class)
                    .setParameter("nombre", nombre)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
    }

    // Nos devuelve true o false dependiendo si el usuario ya existe o no
    public boolean yaExiste(String nombre) {
        try (Session session = getSessionFactory().openSession()) {
            return session.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.nombre = :nombre", Long.class)
                    .setParameter("nombre", nombre)
                    .uniqueResult() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia de usuario: " + e.getMessage(), e);
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

}