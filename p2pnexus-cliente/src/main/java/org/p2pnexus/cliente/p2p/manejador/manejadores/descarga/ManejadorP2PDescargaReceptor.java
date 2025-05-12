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
import java.util.*;

public class ManejadorP2PDescargaReceptor implements IManejadorMensaje {

    // ya está pensado para crear colas de descarga pero no se va a implementar en el tfg
    final Map<String, List<byte[]>> archivosEnDescarga = new HashMap<>();
    final Map<String, File> rutasDestino = new HashMap<>();
    final Map<String, EstadoDescarga> estadoDescargas = new HashMap<>();
    final Map<String, ControladorProcesoDescarga> controladores = new HashMap<>();

    boolean ventanaAbierta = false;

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) {
        JsonObject data = mensaje.getData();
        String nombre = data.get("nombre").getAsString();

        if (!estadoDescargas.containsKey(nombre)) {
            estadoDescargas.put(nombre, new EstadoDescarga(data.get("pesoTotal").getAsLong(), nombre));
        }

        if (!ventanaAbierta) {
            Platform.runLater(() -> {
                ventanaAbierta = true;
                abrirVentanaProgreso(estadoDescargas.get(nombre));
            });
        }


        if (mensaje.getTipo() == TipoMensaje.P2P_R_FRAGMENTO_ARCHIVO) {
            byte[] fragmento = Base64.getDecoder().decode(data.get("fragmento").getAsString());

            if (!archivosEnDescarga.containsKey(nombre)) {
                archivosEnDescarga.put(nombre, new ArrayList<>());
            }

            archivosEnDescarga.get(nombre).add(fragmento);
            estadoDescargas.get(nombre).sumarPeso(fragmento.length);

            Platform.runLater(() -> {
                if (controladores.containsKey(nombre)) {
                    controladores.get(nombre).actualizarProgreso();
                }
            });
        }

        if (mensaje.getTipo() == TipoMensaje.P2P_R_FIN_ARCHIVO) {
            Platform.runLater(() -> {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Guardar archivo");
                chooser.setInitialFileName(nombre);
                File destino = chooser.showSaveDialog(null);

                if (destino != null) {
                    rutasDestino.put(nombre, destino);
                    guardarArchivo(nombre);
                } else {
                    System.err.println("Descarga cancelada por el usuario.");
                }
            });
        }

        return null;
    }

    private void guardarArchivo(String nombre) {
        List<byte[]> fragmentos = archivosEnDescarga.get(nombre);
        File destino = rutasDestino.get(nombre);

        if (fragmentos == null || destino == null) return;

        try (FileOutputStream out = new FileOutputStream(destino)) {
            for (byte[] parte : fragmentos) {
                out.write(parte);
            }
            System.out.println("Archivo descargado con éxito: " + destino.getAbsolutePath());
            controladores.get(nombre).cerrarVentana();
            ventanaAbierta = false;
        } catch (IOException e) {
            System.err.println("Error al guardar archivo: " + e.getMessage());
        }

        archivosEnDescarga.remove(nombre);
        rutasDestino.remove(nombre);
    }

    public void abrirVentanaProgreso(EstadoDescarga estadoDescarga)
    {

        try {
            FXMLLoader loader = GestorVentanas.crearFXMLoader(Ventanas.MODAL_PROCESO_DESCARGA);
            Parent root = loader.load();
            ControladorProcesoDescarga controlador = loader.getController();
            controladores.put(estadoDescarga.nombre, controlador);
            controlador.inicializarValores(estadoDescarga);
            GestorVentanas.abrirModal(root, "Descargando " + estadoDescarga.nombre, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

