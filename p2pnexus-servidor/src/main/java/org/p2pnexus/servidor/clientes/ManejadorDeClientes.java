package org.p2pnexus.servidor.clientes;

import com.p2pnexus.comun.comunicacion.SocketConexion;
import org.p2pnexus.servidor.Clientes.RecibirMensajes;

import java.io.BufferedReader;

public class ManejadorDeClientes implements Runnable {

    private final SocketConexion cliente;
    private BufferedReader input;
    private RecibirMensajes manejadorMensajes;

    public ManejadorDeClientes(SocketConexion cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        System.out.println("Cliente conectado: " + cliente.getSocket().getInetAddress().getHostAddress());
        manejadorMensajes = new RecibirMensajes(cliente);
        new Thread(manejadorMensajes).start();
    }

}
