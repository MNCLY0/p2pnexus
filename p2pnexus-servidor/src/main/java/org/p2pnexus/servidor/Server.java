package org.p2pnexus.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server extends ServerSocket {

    public Server(int port) throws IOException {
        super(port);
    }

    public static void main(String[] args) {
        int port = 5055; // Puerto del servidor
        try {
            Server server = new Server(port);
            System.out.println("Servidor iniciado en el puerto " + port);

            while (true) {
                Socket socket = server.accept();
                // Crear un nuevo hilo para manejar al cliente, el hilo será el encargado de gestionar la conexión
                // y la comunicación con el cliente de manera independiente.
                 new Thread(new ManejadorDeClientes(socket)).start();
            }

        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}
