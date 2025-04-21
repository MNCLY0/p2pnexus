package com.p2pnexus.comun;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;


public class SocketConexion {
    private BufferedReader input;
    private PrintWriter output;
    private Socket socket;
    private String nombre;

    public SocketConexion(Socket socket, String nombre) throws IOException {

        // Inicializar los búferes automáticamente
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        System.out.println("Búferes inicializados : " + socket.getInetAddress().getHostAddress() + " (" + nombre + ")");
        this.nombre = nombre;
    }

    public void enviarMensaje(Mensaje mensaje) {
        Gson gson = new Gson();
        String json = gson.toJson(mensaje);
        output.println(json);
        output.flush();
    }

    public Mensaje recibirMensaje() {
        try {
            String json = input.readLine();
            if (json != null) {
                Gson gson = new Gson();
                return gson.fromJson(json, Mensaje.class);
            }
        }catch (IOException e) {
            System.err.println("Error al recibir el mensaje: " + e.getMessage() + " cerrando socket ");
            try {
                cerrar();
            } catch (IOException ex) {
                System.err.println("Error al cerrar el socket: " + ex.getMessage());
            }
        }
        return null;
    }

    public void cerrar() throws IOException {
        if (input != null) input.close();
        if (output != null) output.close();
        if (socket != null) socket.close();
    }

    public Socket getSocket() {
        return socket;
    }

    public String getNombre() {
        return nombre;
    }


}
