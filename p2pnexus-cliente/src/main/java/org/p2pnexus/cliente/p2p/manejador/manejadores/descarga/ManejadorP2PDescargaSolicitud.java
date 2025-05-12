package org.p2pnexus.cliente.p2p.manejador.manejadores.descarga;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import dev.onvoid.webrtc.RTCDataChannelBuffer;
import org.p2pnexus.cliente.controladores.vistas.ControladorMenuPrincipal;
import org.p2pnexus.cliente.p2p.conexion.GestorP2P;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class ManejadorP2PDescargaSolicitud implements IManejadorMensaje {

    // Tamaño del fragmento: 128KB
    private static final int TAMANO_FRAGMENTO = 128 * 1024;
    // Fragmentos a enviar antes de una pausa
    private static final int FRAGMENTOS_POR_PAUSA = 30;

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) {
        System.out.println("Recibida solicitud de descarga. Tipo: " + mensaje.getTipo());

        // Verificar datos necesarios
        if (!mensaje.getData().has("ruta") || !mensaje.getData().has("nombre") || !mensaje.getData().has("solicitante")) {
            System.err.println("Error: Faltan datos en solicitud de descarga");
            return null;
        }

        // Extraer datos
        String ruta = mensaje.getData().get("ruta").getAsString();
        String nombre = mensaje.getData().get("nombre").getAsString();
        Usuario solicitante = JsonHerramientas.convertirJsonAObjeto(
                mensaje.getData().get("solicitante").getAsJsonObject(), Usuario.class);

        System.out.println("Solicitud de descarga para archivo: " + nombre + " por usuario: " + solicitante.getNombre());

        // Verificar conexión
        GestorP2P gestor = GestorP2P.conexiones.get(solicitante.getId_usuario());
        if (gestor == null) {
            System.err.println("Error: No existe conexión con el usuario " + solicitante.getNombre());
            return null;
        }

        // Verificar ruta
        if (!comprobarValidezRuta(ruta, solicitante)) {
            System.err.println("Error: Ruta inválida para el archivo " + nombre);
            return null;
        }

        // Verificar archivo
        File archivo = new File(ruta);
        if (!archivo.exists() || !archivo.isFile()) {
            System.err.println("Error: Archivo no encontrado en ruta " + ruta);
            return null;
        }

        // Iniciar envío en un hilo separado
        new Thread(() -> {
            try {
                System.out.println("Iniciando envío de archivo: " + nombre + " (" + archivo.length() + " bytes)");
                enviarArchivo(gestor, archivo, nombre);
            } catch (Exception e) {
                System.err.println("Error al enviar archivo: " + e.getMessage());
                e.printStackTrace();
            }
        }, "EnviadorArchivo-" + nombre).start();

        return null;
    }

    private void enviarArchivo(GestorP2P gestor, File archivo, String nombre) throws Exception {
        // 1. Enviar mensaje inicial con metadatos
        JsonObject dataInicio = new JsonObject();
        dataInicio.addProperty("nombre", nombre);
        dataInicio.addProperty("pesoTotal", archivo.length());

        gestor.manejador.enviarMensaje(new Mensaje(TipoMensaje.P2P_R_DESCARGAR_FICHERO, dataInicio));
        System.out.println("Enviando mensaje de inicio de descarga para: " + nombre);

        // Pequeña pausa para que el receptor prepare sus recursos
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 2. Enviar fragmentos del archivo
        try (FileInputStream in = new FileInputStream(archivo)) {
            byte[] buffer = new byte[TAMANO_FRAGMENTO];
            int leidos;
            int fragmentosEnviados = 0;
            long totalEnviado = 0;
            long tiempoInicio = System.currentTimeMillis();

            while ((leidos = in.read(buffer)) != -1) {
                byte[] fragmento;
                if (leidos < buffer.length) {
                    // Último fragmento parcial
                    fragmento = new byte[leidos];
                    System.arraycopy(buffer, 0, fragmento, 0, leidos);
                } else {
                    fragmento = buffer;
                }

                // Enviar fragmento
                ByteBuffer byteBuffer = ByteBuffer.wrap(fragmento);
                gestor.getCanal().send(new RTCDataChannelBuffer(byteBuffer, true));

                totalEnviado += leidos;
                fragmentosEnviados++;

                // Pausa breve cada cierto número de fragmentos para no saturar el canal
                if (fragmentosEnviados % FRAGMENTOS_POR_PAUSA == 0) {
                    Thread.sleep(50);
                }
            }

            // Mostrar estadísticas finales
            long tiempoFinal = System.currentTimeMillis();
            double tiempoTotalSeg = (tiempoFinal - tiempoInicio) / 1000.0;
            double velocidadKBps = (totalEnviado / 1024.0) / tiempoTotalSeg;

            System.out.println(String.format("Envío completado: %d bytes en %.2f segundos (%.2f KB/s)",
                    totalEnviado, tiempoTotalSeg, velocidadKBps));

        } catch (Exception e) {
            System.err.println("Error durante envío de archivo: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 3. Enviar fragmento vacío como señal de finalización
        gestor.getCanal().send(new RTCDataChannelBuffer(ByteBuffer.wrap(new byte[0]), true));
        System.out.println("Señal de fin enviada para: " + nombre);
    }

    public boolean comprobarValidezRuta(String ruta, Usuario usuario) {
        int id = ControladorMenuPrincipal.instancia.getControladoresTarjetaContacto().get(usuario).getConversacion().getIdConversacion();
        List<EspacioCompartido> espacios = Sesion.getDatosSesionUsuario()
                .getCacheDatosConversacion().get(id)
                .getDatosPaqueteEspaciosCompartidos().getEnviados();

        return espacios.stream().anyMatch(e -> ruta.startsWith(e.getRutaPropiedadProperty().get()));
    }
}