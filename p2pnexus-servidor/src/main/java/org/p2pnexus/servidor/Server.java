package org.p2pnexus.servidor;

import org.p2pnexus.servidor.clientes.ManejadorDeClientes;
import com.p2pnexus.comun.comunicacion.SocketConexion;

import java.io.IOException;
import java.net.ServerSocket;

public class Server extends ServerSocket {

    static Server server;
    public Server(int port) throws IOException {
        super(port);
    }

    public static void main(String[] args) {
        int port = 5055; // Puerto del servidor
        try {
            server = new Server(port);
            System.out.println("Servidor iniciado en el puerto " + port);
            ControladorHibernate.abrirSesion();
            ControladorHibernate.verificarSesion();
        }catch (Exception e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            return;
        }

            while (true) {
                try {
                    // Aceptar una nueva conexión de cliente
                    SocketConexion cliente = new SocketConexion(server.accept(), "Cliente " + server.getInetAddress().getHostAddress());
                    // Crear un nuevo hilo para manejar al cliente, el hilo será el encargado de gestionar la conexión
                    // y la comunicación con el cliente de manera independiente.
                    new Thread(new ManejadorDeClientes(cliente)).start();
                }catch (Exception e) {
                    System.err.println("Error al aceptar la conexión: " + e.getMessage());
                    continue;
                }

            }
    }
}
