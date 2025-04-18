package org.p2pnexus.cliente.server;

import java.net.ServerSocket;
import java.net.Socket;

public class Conexion {

    public static int PUERTO = 5055; // Puerto del servidor
    public static String HOST = "localhost"; // Direccion del servidor

    public static void iniciarConexion()
    {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            System.out.println("Conectado al servidor en " + HOST + ":" + PUERTO);
            // Hacemos que el socket siempre mantenga la conexión activa (nos encargaremos de cerrarla cuando sea necesario)
            socket.setKeepAlive(true);
            socket.getOutputStream().write("PUERTO".getBytes());


        } catch (Exception e) {
            System.err.println("Error al conectar al servidor: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        iniciarConexion();
    }


}
