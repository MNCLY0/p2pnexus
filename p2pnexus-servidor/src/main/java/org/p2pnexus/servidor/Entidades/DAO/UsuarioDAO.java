package org.p2pnexus.servidor.Entidades.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.p2pnexus.servidor.Entidades.Usuario;

public class UsuarioDAO {

    private final SessionFactory sessionFactory;

    public UsuarioDAO() {
        try {
            // Configuración de Hibernate
            Configuration configuration = new Configuration();
            configuration.configure();
            this.sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar la sesión de Hibernate: " + e.getMessage(), e);
        }
    }


    public Integer crearUsuario(String nombre, String contrasena) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
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


    public Usuario buscarPorNombre(String nombre) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Usuario WHERE nombre = :nombre", Usuario.class)
                    .setParameter("nombre", nombre)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
    }

    public boolean yaExiste(String nombre) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.nombre = :nombre", Long.class)
                    .setParameter("nombre", nombre)
                    .uniqueResult() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia de usuario: " + e.getMessage(), e);
        }
    }

    public String validarCredenciales(String nombre, String contrasena) {
        Usuario usuario = buscarPorNombre(nombre);
        if (usuario == null) {
            return null;
        }
        if (!usuario.getContrasena().equals(contrasena)) {
            return null;
        }
        return usuario.getNombre();
    }

    public void cerrar() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}