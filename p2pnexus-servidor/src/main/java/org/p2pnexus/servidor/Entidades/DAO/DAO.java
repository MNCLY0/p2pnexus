package org.p2pnexus.servidor.Entidades.DAO;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DAO {
    private final SessionFactory sessionFactory;

    public DAO() {
        try {
            // Configuración de Hibernate
            Configuration configuration = new Configuration();
            configuration.configure();
            this.sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar la sesión de Hibernate: " + e.getMessage(), e);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void cerrar() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
