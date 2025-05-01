package org.p2pnexus.servidor.Entidades.DAO;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DAO {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            return configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar SessionFactory: " + e.getMessage(), e);
        }
    }

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void cerrar() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}

