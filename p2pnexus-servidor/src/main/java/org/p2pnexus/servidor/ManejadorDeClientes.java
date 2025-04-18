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
    private ManejadorDePeticiones manejadorPeticiones;

    public ManejadorDeClientes(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
        manejadorPeticiones = new ManejadorDePeticiones(cliente);
        new Thread(manejadorPeticiones).start();



    }
}
