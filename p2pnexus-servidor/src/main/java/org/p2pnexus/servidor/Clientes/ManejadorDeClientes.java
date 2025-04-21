package org.p2pnexus.servidor.Clientes;

import com.p2pnexus.comun.SocketConexion;

import java.io.BufferedReader;

public class ManejadorDeClientes implements Runnable {

    private final SocketConexion cliente;
    private BufferedReader input;
    private RecibirMensajes manejadorPeticiones;

    public ManejadorDeClientes(SocketConexion cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        System.out.println("Cliente conectado: " + cliente.getSocket().getInetAddress().getHostAddress());
        manejadorPeticiones = new RecibirMensajes(cliente);
        new Thread(manejadorPeticiones).start();
    }

}
