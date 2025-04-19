package org.p2pnexus.servidor;

import java.io.BufferedReader;
import java.net.Socket;

public class ManejadorDeClientes implements Runnable {

    private final Socket cliente;
    private BufferedReader input;
    private RecibirPeticiones manejadorPeticiones;

    public ManejadorDeClientes(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
        manejadorPeticiones = new RecibirPeticiones(cliente);
        new Thread(manejadorPeticiones).start();
    }

}
