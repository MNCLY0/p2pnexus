package org.p2pnexus.servidor;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.p2pnexus.servidor.Configuracion.Configuracion;

public class ControladorHibernate {


    private static SessionFactory sessionFactory;

    public static boolean abrirSesion() {
        try {

            if (sessionFactory != null && !sessionFactory.isClosed())
            {
                System.out.println("La sesión ya está abierta");
                return true;
            }

            Configuration config = establecerCFGdesdeConfiguracion();

            // Cargar la configuración desde el archivo de propiedades, en caso de que el nombre o la contraseña estén vacios
            // se avisará por consola que hay que configurarlo desde el archivo de propiedades

            if (config == null) {
                System.err.println("Asegurate de que el archivo de configuración existe y contiene los valores necesarios.");
                return false;
            }

            sessionFactory = config.buildSessionFactory();
            System.out.println("SessionFactory creada correctamente.");
            return true;

        } catch (Exception e) {
            System.out.println("Error al abrir SessionFactory: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void cerrarSesionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    public static Session getSession() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            throw new IllegalStateException("SessionFactory no inicializada. Llama a abrirSesion() primero.");
        }
        return sessionFactory.openSession(); // creamos una sesion cada vez que se solicita
    }



    static Configuration establecerCFGdesdeConfiguracion()
    {
        // Cargar la configuración desde el archivo de propiedades

        Configuracion configuracion = new Configuracion();

        String servidor = configuracion.getServidor();
        String puerto = String.valueOf(configuracion.getPuerto());
        String usuario = configuracion.getUsuario().trim();
        String password = configuracion.getPassword().trim();

        if (usuario.isEmpty() || password.isEmpty())
        {
            System.err.println("Error: Usuario o contraseña vacíos. Por favor, configúralos en el archivo de propiedades.");
            System.exit(0);
            return null;
        }
        // Establecer la configuración de Hibernate desde el archivo de propiedades

        Configuration config = new Configuration().configure();
        config.setProperty("hibernate.connection.url",  "jdbc:mysql://" + servidor + ":" + puerto + "/p2pnexus");
        config.setProperty("hibernate.connection.username", usuario);
        config.setProperty("hibernate.connection.password", password);
        return config;

    }

    // Verificamos si las sesiones se crean correctamente y la conexión funciona
    public static boolean verificarSesion() {
        try (Session session = getSession()) {
            session.createNativeQuery("SELECT 1", Integer.class).getSingleResult();
            System.out.println("Las sesiones están funcionando correctamente.");
            return true;
        } catch (Exception e) {
            System.out.println("Error al verificar la sesión: " + e.getMessage());
            System.exit(0);
            return false;
        }
    }


}