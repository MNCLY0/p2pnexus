package com.p2pnexus.comun.comunicacion;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase abstracta que maneja los mensajes recibidos por el socket.
 * Se encarga de inicializar los manejadores de mensajes y de recibir los mensajes del socket.
 * Se debe extender para implementar los manejadores de mensajes específicos.
 * @author mncly
 */

public abstract class ManejadorDeMensajes implements Runnable {

    public ManejadorDeMensajes(SocketConexion socketConexion) {
        this.socketConexion = socketConexion;
    }

    public ManejadorDeMensajes(SocketConexion socketConexion, Boolean notificable) {
        this(socketConexion);
        this.notificable = notificable;
    }

    boolean notificable = false;

    SocketConexion socketConexion;
    public Map<TipoMensaje, IManejadorMensaje> manejadoresPeticiones = new HashMap<>();

    int nullsSeguidos = 3;
    int nullsRecibidos = 0;

    public void run() {
        // Aquí se maneja la petición del socket
        inicializarManejadores();

        while (!socketConexion.getSocket().isClosed()) {
            try {
                // Aquí se recibiría la petición del socket
                Mensaje mensaje = socketConexion.recibirMensaje();
                // A veces se reciben mensajes nulos por la magia divina de java, así que simplemente los ignoramos
                // pero si hay varios seguidos es que el cliente se ha desconectado
                if (mensaje == null) {
                    nullsRecibidos++;
                    if (nullsRecibidos >= nullsSeguidos) {
                        System.out.println("Demasiados mensajes nulos seguidos, cerrando conexión: " + socketConexion.getSocket().getInetAddress().getHostAddress());
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

    public abstract void inicializarManejadores();

    void manejarPeticion(Mensaje mensaje)
    {
        ResultadoMensaje resultado = null;

        if (mensaje == null) {
            System.err.println("Mensaje nulo recibido");
            return;
        }
        TipoMensaje tipoMensaje = mensaje.getTipo();
        IManejadorMensaje manejador = manejadoresPeticiones.get(tipoMensaje);

        if (manejador == null) {
            System.err.println("No se encontró manejador para: " + tipoMensaje);
            return;
        }

        try {
            resultado = manejador.manejarDatos(mensaje, socketConexion);
        }catch (ManejarPeticionesExeptionError e) {
            System.err.println("El mensaje ha devuelto un error: " + e.getMessage());
            if (notificable)
            {
                intentarNotificar(e.getMessage(), TipoNotificacion.ERROR);
            }
        }
        catch (Exception e) {
            if (notificable)
            {
                intentarNotificar(e.getMessage(), TipoNotificacion.ERROR);
            }
        }

        if (resultado != null) {

            if (resultado.getTipoNotificacion() != null)
            {
                // Comprobamos si el resultado pide que se notifique al usuario
                intentarNotificar(resultado.getMensajeNotificacion(), resultado.getTipoNotificacion());
            }
            if(resultado.getMensaje() != null)
            {
                // Si el resultado tiene un tipo de mensaje (como puede ser el resultado de un login o algo asi)
                // se lo enviamos al socket
                Mensaje mensajeResultado = resultado.getMensaje();
                socketConexion.enviarMensaje(mensajeResultado);
            }
        }
    }

    void intentarNotificar(String mensaje, TipoNotificacion tipoNotificacion)
    {
        JsonObject json = new JsonObject();
        json.addProperty("tipo", tipoNotificacion.name());
        json.addProperty("mensaje", mensaje);
        Mensaje mensajeNoti = new Mensaje(TipoMensaje.NOTIFICACION,json);
        socketConexion.enviarMensaje(mensajeNoti);
    }

    public IManejadorMensaje getManejador(TipoMensaje tipoMensaje)
    {
        return manejadoresPeticiones.get(tipoMensaje);
    }

}
