package org.p2pnexus.servidor;

import com.google.gson.Gson;
import com.p2pnexus.comun.Peticion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ManejadorDeClientes implements Runnable {

    private final Socket cliente;
    private BufferedReader input;


    public ManejadorDeClientes(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
        try {
            input = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            Gson gson = new Gson();
            Peticion peticion = gson.fromJson(input.readLine(), Peticion.class);
            System.out.println("Petición recibida: " + peticion.getTipo());

        } catch (IOException e) {
            System.err.println("Error al leer la petición del cliente: " + e.getMessage());
            throw new RuntimeException(e);
        }


    }
}
