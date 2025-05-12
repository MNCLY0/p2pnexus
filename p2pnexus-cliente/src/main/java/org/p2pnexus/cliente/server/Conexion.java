package org.p2pnexus.cliente.server;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ConectarExeption;
import javafx.application.Platform;
import org.p2pnexus.cliente.configuracion.Configuracion;
import org.p2pnexus.cliente.p2p.conexion.GestorP2P;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Notificaciones;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.io.BufferedOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;


public class Conexion {

    public static int puerto = 0;
    public static String host = "";

    private static SocketConexion conexion = null; // Socket del servidor

    public static String[] servidores = new String[] {
            "localhost",
            ""
    };

    public static BufferedOutputStream OUT = null;

    public static ControlManejadores controlManejadores = null;

    public static String ipGenerada = null;

    public static void iniciarConexion(boolean test) throws ConectarExeption {

        Configuracion configuracion = new Configuracion();
        // establecemos la ruta del servidor según la configuración (localhost siempre se prioriza)
        servidores[1] = configuracion.getServidor();
        puerto = configuracion.getPuerto();


        for (String servidor : servidores) {
            try {
                Socket socket = new Socket();

                if (test && ipGenerada == null) {
                    Random rand = new Random();
                    ipGenerada = "127.0.0." + rand.nextInt(0, 255);
                    System.out.println("IP generada: " + ipGenerada);
                } else {
                    ipGenerada = socket.getLocalAddress().getHostAddress();
                }

                socket.bind(new InetSocketAddress(ipGenerada, 0));
                socket.connect(new InetSocketAddress(servidor, puerto), 3000); // timeout 3s por intento

                System.out.println("Conectado al servidor en " + servidor + ":" + puerto);
                socket.setKeepAlive(true);

                conexion = new SocketConexion(socket, "Servidor " + socket.getInetAddress().getHostAddress());

                controlManejadores = new ControlManejadores(conexion);
                new Thread(controlManejadores).start();

                host = servidor;
                return;

            } catch (Exception e) {
                System.err.println("Error al conectar con " + servidor + ": " + e.getMessage());
            }
        }

        throw new ConectarExeption("No se pudo conectar a ningún servidor");
    }

    public static void iniciarConexion() throws ConectarExeption
    {
        iniciarConexion(false);
    }



    public static void main(String[] args) {

        try {
            iniciarConexion();
        } catch (ConectarExeption e) {
            e.printStackTrace();
        }
    }

    public static void enviarMensaje(Mensaje mensaje) {
        if (conexion != null) {
            conexion.enviarMensaje(mensaje);
        } else {
            System.err.println("No hay conexión establecida.");
        }
    }


    public static Mensaje recibirMensaje() {
        if (conexion != null) {
            return conexion.recibirMensaje();
        } else {
            System.err.println("No hay conexión establecida.");
            return null;
        }
    }

    public static void cerrarConexion() {
        Notificaciones.mostrarNotificacion("Se ha perdido la conexión con el servidor", TipoNotificacion.ERROR);
        OUT = null;
        controlManejadores = null;
        conexion = null;

        // Cerrar todas las conexiones P2P que puedan estar abiertas
        GestorP2P.conexiones.forEach((integer, gestorP2P) -> {
            gestorP2P.cerrarConexion();
        });

        Platform.runLater(() ->
        {
            GestorVentanas.transicionarVentana(Ventanas.CARGANDO_CONEXION);
        });
    }


}
