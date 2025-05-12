package org.p2pnexus.cliente.p2p.manejador.manejadores.descarga;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.FileChooser;
import org.p2pnexus.cliente.controladores.vistasModales.ControladorProcesoDescarga;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class ManejadorP2PDescargaReceptor implements IManejadorMensaje {

    private final Map<String, FileOutputStream> flujos = new HashMap<>();
    private final Map<String, File> temporales = new HashMap<>();
    private final Map<String, EstadoDescarga> estados = new HashMap<>();
    private final Map<String, ControladorProcesoDescarga> controladores = new HashMap<>();
    private final Set<String> ventanasAbiertas = new HashSet<>();

    private String archivoActual = null;

    private int contadorFragmentos = 0;
    private long ultimaActualizacion = 0;

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) {
        System.out.println("Receptor: Mensaje recibido tipo: " + mensaje.getTipo());

        JsonObject data = mensaje.getData();
        if (!data.has("nombre")) {
            System.err.println("Error: El mensaje no contiene la clave 'nombre'");
            return null;
        }

        String nombre = data.get("nombre").getAsString();

        try {
            if (mensaje.getTipo() == TipoMensaje.P2P_R_DESCARGAR_FICHERO) {
                iniciarDescarga(nombre, data.get("pesoTotal").getAsLong());
            }
        } catch (Exception e) {
            System.err.println("Error en receptor: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void iniciarDescarga(String nombre, long pesoTotal) throws IOException {
        System.out.println("Iniciando descarga para: " + nombre + " (" + pesoTotal + " bytes)");

        // Crear archivo temporal
        File tempFile = File.createTempFile("descarga_" + nombre, ".tmp");
        tempFile.deleteOnExit();
        System.out.println("Receptor: Archivo temporal creado en: " + tempFile.getAbsolutePath());

        FileOutputStream salida = new FileOutputStream(tempFile);

        flujos.put(nombre, salida);
        temporales.put(nombre, tempFile);

        EstadoDescarga estado = new EstadoDescarga(pesoTotal, nombre);
        estados.put(nombre, estado);

        archivoActual = nombre;

        abrirVentanaProgreso(estado);
        System.out.println("Receptor: Tamaño total del archivo: " + pesoTotal + " bytes");
    }

    // Método para recibir fragmentos binarios
    public void recibirFragmentoBinario(byte[] fragmento) {
        if (archivoActual == null) {
            System.err.println("Error: Recibido fragmento pero no hay archivo actual");
            return;
        }

        // Si recibimos un fragmento vacío, es la señal de finalización
        if (fragmento.length == 0) {
            System.out.println("Fragmento vacío recibido - finalizando descarga de: " + archivoActual);
            finalizarDescarga(archivoActual);
            return;
        }

        try {
            FileOutputStream salida = flujos.get(archivoActual);
            if (salida == null) {
                System.err.println("Error: FileOutputStream es null para " + archivoActual);
                return;
            }

            salida.write(fragmento);

            EstadoDescarga estado = estados.get(archivoActual);
            if (estado == null) {
                return;
            }

            estado.sumarPeso(fragmento.length);
            estado.aumentarFragmentosRecibidos();
            contadorFragmentos++;

            long ahora = System.currentTimeMillis();
            if (contadorFragmentos >= 20) {
                contadorFragmentos = 0;
                ultimaActualizacion = ahora;
                actualizarUI(archivoActual);
            }
        } catch (IOException e) {
            System.err.println("Error al escribir fragmento: " + e.getMessage());
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
        System.out.println("Finalizando descarga para: " + nombre);

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

    // Método para guardar archivo final
    private void guardarArchivo(String nombre) {
        System.out.println("Guardando archivo: " + nombre);

        File temp = temporales.get(nombre);
        if (temp == null || !temp.exists()) {
            System.err.println("Error: Archivo temporal no encontrado para " + nombre);
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar archivo");
        chooser.setInitialFileName(nombre);

        Platform.runLater(() -> {
            File destino = chooser.showSaveDialog(null);
            if (destino != null) {
                try {
                    // Copiar archivo temporal al destino
                    Files.copy(temp.toPath(), destino.toPath());
                    System.out.println("Archivo guardado como: " + destino.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("Error al guardar archivo: " + e.getMessage());
                }
            }

            if (controladores.containsKey(nombre)) {
                controladores.get(nombre).cerrarVentana();
            }

            limpiar(nombre);
        });
    }

    // Método para limpiar recursos
    private void limpiar(String nombre) {
        flujos.remove(nombre);

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

        System.out.println("Recursos liberados para: " + nombre);
    }

    // Método para abrir ventana de progreso
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
                System.out.println("Ventana de progreso abierta para: " + estado.nombre);
            } catch (IOException e) {
                System.err.println("Error al abrir ventana de progreso: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}