package org.p2pnexus.cliente.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class RecibirMensajes implements Runnable {

    Socket socket;
    BufferedReader input = null;

    RecibirMensajes(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (Exception e) {
            System.err.println("Error al inicializar el BufferedReader: " + e.getMessage());
            return;
        }
        while (true) {

        }
    }
}
