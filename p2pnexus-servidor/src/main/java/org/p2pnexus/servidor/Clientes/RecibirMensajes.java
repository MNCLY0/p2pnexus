package org.p2pnexus.servidor.Clientes;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.SocketConexion;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeption;
import org.p2pnexus.servidor.acciones.manejadoresPeticiones.ManejadorMensajes;
import org.p2pnexus.servidor.acciones.manejadoresPeticiones.ManejarLogin;

import java.util.HashMap;
import java.util.Map;

public class RecibirMensajes implements Runnable {

    SocketConexion socketConexion;

    Map<TipoMensaje, ManejadorMensajes> manejadoresPeticiones = new HashMap<>();
    int nullsSeguidos = 3;
    int nullsRecibidos = 0;

    public RecibirMensajes(SocketConexion cliente) {
        this.socketConexion = cliente;
    }

    @Override
    public void run() {
        // Aquí se maneja la petición del cliente
        inicialidarManejadores();

        while (!socketConexion.getSocket().isClosed()) {
            try {
                // Aquí se recibiría la petición del cliente
                 Mensaje mensaje = socketConexion.recibirMensaje();
                 // A veces se reciben mensajes nulos por la magia divina de java, así que simplemente los ignoramos
                 // pero si hay varios seguidos es que el cliente se ha desconectado
                if (mensaje == null) {
                    nullsRecibidos++;
                    if (nullsRecibidos >= nullsSeguidos) {
                        System.out.println("Demasiados mensajes nulos seguidos, cerrando conexión con el cliente: " + socketConexion.getSocket().getInetAddress().getHostAddress());
                        socketConexion.cerrar();
                        break;
                    }
                    continue;
                }
                nullsRecibidos = 0;
                manejarPeticion(mensaje);
            } catch (Exception e) {
                System.err.println("Error al manejar la petición: " + e.getMessage());
            }
        }
    }

    void inicialidarManejadores()
    {
        manejadoresPeticiones.put(TipoMensaje.P_LOGIN, new ManejarLogin());
    }

    void manejarPeticion(Mensaje mensaje)
    {
        if (mensaje == null) {
            System.err.println("Mensaje nulo recibido");
            return;
        }
        TipoMensaje tipoMensaje = mensaje.getTipo();
        ManejadorMensajes manejador = manejadoresPeticiones.get(tipoMensaje);
        try {
            manejador.manejarDatos(mensaje.getData(), socketConexion);
        }catch (ManejarPeticionesExeption e) {
            System.err.println("Error al manejar la petición: " + e.getMessage());
            //todo Enviar error al cliente (quiero implementar primero el sistema de notificaciones en el cliente)
        }

    }




}
