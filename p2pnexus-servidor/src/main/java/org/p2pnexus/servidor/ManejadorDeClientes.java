package org.p2pnexus.servidor;

import java.net.Socket;

public class ManejadorDeClientes implements Runnable {

    private final Socket cliente;

    public ManejadorDeClientes(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
    }
}
