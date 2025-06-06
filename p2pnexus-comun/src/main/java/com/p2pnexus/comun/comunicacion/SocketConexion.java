package com.p2pnexus.comun.comunicacion;

import com.google.gson.Gson;
import com.p2pnexus.comun.Mensaje;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class SocketConexion {
    private final BufferedReader input;
    private final PrintWriter output;
    private final Socket socket;
    private final String nombre;
    private int idUsuario;
    private boolean errorConexion = false;

    public SocketConexion(Socket socket, String nombre) throws IOException {

        // Inicializar los búferes automáticamente
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        System.out.println("Búferes inicializados : " + socket.getInetAddress().getHostAddress() + " (" + nombre + ")");
        this.nombre = nombre;
        this.idUsuario = -1; // valor por defecto cuando no hay ningún usuario asociado
    }


    public void enviarMensaje(Mensaje mensaje) {
        System.out.println("Enviando mensaje: " + mensaje.getTipo() + " " + mensaje.getData());
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
        } catch (IOException e) {
            System.err.println("Error al recibir el mensaje: " + e.getMessage() + " cerrando socket ");
            errorConexion = true;
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public boolean hayErrorConexion() {
        return errorConexion;
    }
}
