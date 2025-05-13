package org.p2pnexus.cliente.p2p.manejador.manejadores.descarga;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import dev.onvoid.webrtc.RTCDataChannelBuffer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.FileChooser;
import org.p2pnexus.cliente.controladores.vistasModales.ControladorProcesoDescarga;
import org.p2pnexus.cliente.p2p.conexion.GestorP2P;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.*;

public class ManejadorP2PDescargaReceptor implements IManejadorMensaje {

    // Byte simple para confirmación
    private static final byte CONFIRMACION = 1;

    private final Map<String, FileOutputStream> flujos = new HashMap<>();
    private final Map<String, File> temporales = new HashMap<>();
    private final Map<String, EstadoDescarga> estados = new HashMap<>();
    private final Map<String, ControladorProcesoDescarga> controladores = new HashMap<>();
    private final Map<String, GestorP2P> gestoresConexion = new HashMap<>();
    private final Set<String> ventanasAbiertas = new HashSet<>();

    private String archivoActual = null;

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) {
        if (mensaje.getTipo() == TipoMensaje.P2P_R_DESCARGAR_FICHERO) {
            try {
                JsonObject data = mensaje.getData();
                String nombre = data.get("nombre").getAsString();
                int idSolicitante = data.get("idSolicitante").getAsInt();
                long pesoTotal = data.get("pesoTotal").getAsLong();

                GestorP2P gestor = GestorP2P.conexiones.get(idSolicitante);
                if (gestor != null) {
                    iniciarDescarga(nombre, pesoTotal, gestor);
                }
            } catch (Exception e) {
                System.err.println("Error al iniciar descarga: " + e.getMessage());
            }
        }
        return null;
    }

    private void iniciarDescarga(String nombre, long pesoTotal, GestorP2P gestor) throws IOException {
        System.out.println("Iniciando descarga: " + nombre + " (" + pesoTotal + " bytes)");

        // Crear archivo temporal
        File tempFile = File.createTempFile("descarga_" + nombre, ".tmp");
        tempFile.deleteOnExit();

        FileOutputStream salida = new FileOutputStream(tempFile);

        flujos.put(nombre, salida);
        temporales.put(nombre, tempFile);
        gestoresConexion.put(nombre, gestor);

        EstadoDescarga estado = new EstadoDescarga(pesoTotal, nombre);
        estados.put(nombre, estado);

        archivoActual = nombre;

        abrirVentanaProgreso(estado);
    }

    public void recibirFragmentoBinario(byte[] fragmento) {
        if (archivoActual == null) {
            return;
        }

        if (fragmento.length == 0) {
            finalizarDescarga(archivoActual);
            return;
        }

        try {

            flujos.get(archivoActual).write(fragmento);

            EstadoDescarga estado = estados.get(archivoActual);
            estado.sumarPeso(fragmento.length);
            estado.aumentarFragmentosRecibidos();


            if (estado.getFragmentosRecibidos() % 50 == 0) {
                flujos.get(archivoActual).flush();
            }

            if (estado.getFragmentosRecibidos() % 20 == 0) {
                actualizarUI(archivoActual);
            }

            enviarConfirmacionDirecta(archivoActual);

        } catch (IOException e) {
            System.err.println("Error al procesar fragmento: " + e.getMessage());
        }
    }

    // Método simplificado para enviar confirmación
    private void enviarConfirmacionDirecta(String nombre) {
        try {
            GestorP2P gestor = gestoresConexion.get(nombre);
            if (gestor != null) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1);
                byteBuffer.put(CONFIRMACION);
                byteBuffer.flip();

                gestor.getCanal().send(new RTCDataChannelBuffer(byteBuffer, true));
            }
        } catch (Exception e) {
            System.err.println("Error al enviar confirmación: " + e.getMessage());
        }
    }

    private void actualizarUI(String nombre) {
        if (controladores.containsKey(nombre)) {
            Platform.runLater(() -> {
                ControladorProcesoDescarga controlador = controladores.get(nombre);
                if (controlador != null) {
                    controlador.actualizarProgreso();
                }
            });
        }
    }

    private void finalizarDescarga(String nombre) {
        try {
            if (flujos.containsKey(nombre)) {
                flujos.get(nombre).close();
            }

            if (estados.containsKey(nombre)) {
                EstadoDescarga estado = estados.get(nombre);
                estado.pesoActualDescargado = estado.totalPeso;
                actualizarUI(nombre);
            }

            guardarArchivo(nombre);
        } catch (IOException e) {
            System.err.println("Error al finalizar descarga: " + e.getMessage());
        }
    }

    private void guardarArchivo(String nombre) {
        File temp = temporales.get(nombre);
        if (temp == null || !temp.exists()) {
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar archivo");
        chooser.setInitialFileName(nombre);

        Platform.runLater(() -> {
            File destino = chooser.showSaveDialog(null);
            if (destino != null) {
                try {
                    Files.copy(temp.toPath(), destino.toPath());
                } catch (IOException e) {
                    System.err.println("Error al guardar: " + e.getMessage());
                }
            }

            if (controladores.containsKey(nombre)) {
                controladores.get(nombre).cerrarVentana();
            }

            limpiar(nombre);
        });
    }

    private void limpiar(String nombre) {
        flujos.remove(nombre);
        gestoresConexion.remove(nombre);

        File temp = temporales.get(nombre);
        if (temp != null && temp.exists()) {
            temp.delete();
        }
        temporales.remove(nombre);

        estados.remove(nombre);
        controladores.remove(nombre);
        ventanasAbiertas.remove(nombre);

        if (archivoActual != null && archivoActual.equals(nombre)) {
            archivoActual = null;
        }
    }

    private void abrirVentanaProgreso(EstadoDescarga estado) {
        if (ventanasAbiertas.contains(estado.nombre)) {
            return;
        }

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = GestorVentanas.crearFXMLoader(Ventanas.MODAL_PROCESO_DESCARGA);
                Parent root = loader.load();

                ControladorProcesoDescarga controlador = loader.getController();
                controladores.put(estado.nombre, controlador);

                controlador.inicializarValores(estado);
                GestorVentanas.abrirModal(root, "Descargando " + estado.nombre, false);

                ventanasAbiertas.add(estado.nombre);
            } catch (IOException e) {
                System.err.println("Error en ventana: " + e.getMessage());
            }
        });
    }
}