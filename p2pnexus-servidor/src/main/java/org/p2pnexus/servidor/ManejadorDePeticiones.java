package org.p2pnexus.servidor;

import com.google.gson.Gson;
import com.p2pnexus.comun.Peticion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ManejadorDePeticiones implements Runnable {

    Socket cliente;

    public ManejadorDePeticiones(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        // Aquí se manejaría la petición del cliente

        while (true) {
            try {
                // Aquí se recibiría la petición del cliente
                 BufferedReader input = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                 System.out.println("Esperando petición del cliente...");
                 String json = input.readLine();
                 System.out.println("Petición recibida: " + json);
                 Peticion peticion = new Gson().fromJson(json, Peticion.class);
                 System.out.println("Petición recibida: " + peticion.getTipo());

                // Aquí se procesaría la petición y se enviaría la respuesta al cliente
                // OutputStream output = cliente.getOutputStream();
                // output.write(new Gson().toJson(respuesta).getBytes());

            } catch (Exception e) {
                System.err.println("Error al manejar la petición: " + e.getMessage());
            }
        }
    }
}
