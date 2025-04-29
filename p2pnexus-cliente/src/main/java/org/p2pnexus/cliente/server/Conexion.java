package org.p2pnexus.cliente.server;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ConectarExeption;

import java.io.BufferedOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;

public class Conexion {

    public static int PUERTO = 5055; // Puerto del servidor
    public static String HOST = "localhost"; // Direccion del servidor
    private static SocketConexion CONEXION = null; // Socket del servidor

    public static BufferedOutputStream OUT = null;

    public static RecibirMensajes recibirMensajes = null;

    public static String ipGenerada = null;

    public static void iniciarConexion(boolean test) throws ConectarExeption
    {
        try {

            Socket socket = new Socket();

            if (test && ipGenerada == null) {
                Random rand = new Random();
                ipGenerada = "127.0.0." + rand.nextInt(0,255);
                System.out.println("Ip generada: " + socket.getLocalAddress().getHostAddress());
            }else
            {
              ipGenerada = socket.getLocalAddress().getHostAddress();
            }
            socket.bind(new InetSocketAddress(ipGenerada, 0));
            socket.connect(new InetSocketAddress(HOST, PUERTO));

            System.out.println("Conectado al servidor en " + HOST + ":" + PUERTO);
            // Hacemos que el socket siempre mantenga la conexión activa (nos encargaremos de cerrarla cuando sea necesario)
            socket.setKeepAlive(true);

            CONEXION = new SocketConexion(socket, "Servidor " + socket.getInetAddress().getHostAddress());

            recibirMensajes = new RecibirMensajes(CONEXION);
            new Thread(recibirMensajes).start();

        } catch (Exception e) {
            throw new ConectarExeption("Error al conectar al servidor", e);
        }

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
        if (CONEXION != null) {
            CONEXION.enviarMensaje(mensaje);
        } else {
            System.err.println("No hay conexión establecida.");
        }
    }


    public static Mensaje recibirMensaje() {
        if (CONEXION != null) {
            return CONEXION.recibirMensaje();
        } else {
            System.err.println("No hay conexión establecida.");
            return null;
        }
    }


}
