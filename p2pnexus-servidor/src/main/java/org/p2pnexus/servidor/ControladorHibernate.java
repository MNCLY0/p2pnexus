package org.p2pnexus.servidor;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

public class ControladorHibernate {

    // Variable de sesión de Hibernate, al ser estática se comparte en toda la aplicación
    static Session session = null;



    public static void hiloAbrirSesion(Double tiempoIntento) {

        Thread hiloConexion = new Thread(() -> {
            while (true)
            {
//                Log.i("Intentando abrir sesión...");
                boolean estado = abrirSesion();
                if (estado)
                {
                    break;
                }
                try {
                    System.out.println("Esperando " + tiempoIntento  + " milisegundos para intentar abrir sesión de nuevo.");
//                    Log.e("Error al abrir sesión. Intentando de nuevo...");
//                    Log.i("Esperando " + tiempoIntento + " milisegundos para intentar abrir sesión de nuevo.");
                    Thread.sleep(tiempoIntento.longValue() * 1000);
                } catch (InterruptedException e) {
//                    Log.e("Error en hilo de apertura de sesión: " + e.getMessage());
                }
            }
        });

        // hacemos que el hilo sea un daemon para que se cierre cuando se cierra la aplicacion
        hiloConexion.setDaemon(true);
        hiloConexion.start();
    }

    public static boolean abrirSesion() {
        try {
            Configuration config = new Configuration().configure();
            System.out.println("Intentando conectar a: " + config.getProperty("connection.url"));
            System.out.println("Con usuario: " + config.getProperty("connection.username"));

            session = config.buildSessionFactory().openSession();

            if (session != null) {
                System.out.println("Sesión abierta con éxito");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error al abrir sesión: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static void cerrarSesion() {
        if (session != null && session.isOpen()) {
            session.close();
//            Log.i("Sesión cerrada.");
        }
    }

    //Con este metodo verificamos si la sesion esta abierta y si la conexion funciona correctamente
    public static boolean verificarSesion() {
        if (session == null) {
            System.out.println("La sesión no existe");
            return false;
        }

        if (!session.isOpen()) {
            System.out.println("La sesión está cerrada");
            return false;
        }

        try {
            // Ejecutar una consulta simple para verificar la conexión
            session.createNativeQuery("SELECT 1", Integer.class).getSingleResult();
            System.out.println("La sesión está activa y la conexión funciona correctamente");
            return true;
        } catch (Exception e) {
            System.out.println("Error al verificar la sesión: " + e.getMessage());
            return false;
        }
    }


    public static Session getSession()
    {
        return session;
    }

}