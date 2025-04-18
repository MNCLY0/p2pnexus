package org.p2pnexus.cliente.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.p2pnexus.comun.Peticion;

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
            // Enviamos la petición de saludo al servidor
            JsonObject json = new JsonObject();
            json.addProperty("nombre", "Cliente");
            json.addProperty("test", "2131231231245235");

            Peticion peticion = new Peticion("SALUDO", json);

            socket.getOutputStream().write(new Gson().toJson(peticion).getBytes());

        } catch (Exception e) {
            System.err.println("Error al conectar al servidor: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        iniciarConexion();
    }


}
