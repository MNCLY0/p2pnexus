package org.p2pnexus.cliente.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.p2pnexus.comun.Peticion;
import com.p2pnexus.comun.exepciones.ConectarExeption;

import java.io.BufferedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Conexion {

    public static int PUERTO = 5055; // Puerto del servidor
    public static String HOST = "localhost"; // Direccion del servidor
    public static Socket CONEXION = null; // Socket del servidor

    public static BufferedOutputStream OUT = null;


    public static void iniciarConexion() throws ConectarExeption
    {
        try {
            Socket socket = new Socket(HOST, PUERTO);
            System.out.println("Conectado al servidor en " + HOST + ":" + PUERTO);
            // Hacemos que el socket siempre mantenga la conexión activa (nos encargaremos de cerrarla cuando sea necesario)
            socket.setKeepAlive(true);
            // Creamos un BufferedOutputStream para enviar datos al servidor
            OUT = new BufferedOutputStream(socket.getOutputStream());
            CONEXION = socket;

        } catch (Exception e) {
            System.err.println("Error al conectar al servidor: " + e.getMessage());
            throw new ConectarExeption("Error al conectar al servidor", e);
        }

    }

    public static void enviarPeticion(Peticion peticion) {
        try {
            if (CONEXION == null || CONEXION.isClosed()) {
                System.err.println("Error: La conexión está cerrada");
                return;
            }

            // Convertimos la petición a JSON y añadimos un delimitador
            String json = new Gson().toJson(peticion) + "\n";
            // Enviamos la petición al servidor
            OUT.write(json.getBytes());
            OUT.flush();

            System.out.println("Petición enviada: " + json);
        } catch (Exception e) {
            System.err.println("Error al enviar la petición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            iniciarConexion();
        } catch (ConectarExeption e) {
            e.printStackTrace();
        }

    }


}
