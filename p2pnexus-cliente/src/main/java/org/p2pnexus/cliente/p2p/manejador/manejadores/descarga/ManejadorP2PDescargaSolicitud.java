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
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ManejadorP2PDescargaSolicitud implements IManejadorMensaje {

    public static final int TAMANO_FRAGMENTO = 60 * 1024; // 60 KB
    public static final int TAMANO_VENTANA = 30; // vetnana de tranferencia de 30 fragmentos
    public static final byte CONFIRMACION = 1; // byte que indica recepcion

    private final Map<String, EstadoTransferencia> transferencias = new ConcurrentHashMap<>();

    private static class EstadoTransferencia {
        final File archivo;
        final String nombre;
        final FileInputStream fileInputStream;
        final GestorP2P gestor;
        final AtomicInteger fragmentosEnVuelo = new AtomicInteger(0);
        boolean finalizado = false;

        public EstadoTransferencia(File archivo, String nombre, FileInputStream fis, GestorP2P gestor) {
            this.archivo = archivo;
            this.nombre = nombre;
            this.fileInputStream = fis;
            this.gestor = gestor;
        }
    }

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) {
        if (mensaje.getTipo() == TipoMensaje.P2P_S_DESCARGAR_FICHERO) {
            return manejarSolicitudDescarga(mensaje);
        }
        return null;
    }

    public void manejarConfirmacionBinaria() {

        for (EstadoTransferencia estado : transferencias.values()) {
            if (!estado.finalizado) {
                estado.fragmentosEnVuelo.decrementAndGet();

                try {
                    // Intentar enviar más fragmentos hasta llenar la ventana
                    while (estado.fragmentosEnVuelo.get() < TAMANO_VENTANA) {
                        if (!enviarSiguienteFragmento(estado)) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error enviando fragmentos: " + e.getMessage());
                }
            }
        }
    }

    private ResultadoMensaje manejarSolicitudDescarga(Mensaje mensaje) {
        System.out.println("Recibida solicitud de descarga");

        if (!mensaje.getData().has("ruta") || !mensaje.getData().has("nombre") || !mensaje.getData().has("solicitante")) {
            System.err.println("Error: Faltan datos en la solicitud");
            return null;
        }

        String ruta = mensaje.getData().get("ruta").getAsString();
        String nombre = mensaje.getData().get("nombre").getAsString();
        Usuario solicitante = JsonHerramientas.convertirJsonAObjeto(
                mensaje.getData().get("solicitante").getAsJsonObject(), Usuario.class);

        System.out.println("Solicitud de descarga para archivo: " + nombre + " por usuario: " + solicitante.getNombre());


        GestorP2P gestor = GestorP2P.conexiones.get(solicitante.getId_usuario());
        if (gestor == null) {
            System.err.println("Error: No existe conexión con el usuario");
            return null;
        }

        if (!comprobarValidezRuta(ruta, solicitante)) {
            System.err.println("Error: Ruta inválida para el archivo");
            return null;
        }

        File archivo = new File(ruta);
        if (!archivo.exists() || !archivo.isFile()) {
            System.err.println("Error: Archivo no encontrado");
            return null;
        }

        new Thread(() -> {
            try {
                System.out.println("Iniciando envío de archivo: " + nombre + " (" + archivo.length() + " bytes)");
                iniciarTransferenciaArchivo(gestor, archivo, nombre);
            } catch (Exception e) {
                System.err.println("Error al enviar archivo: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        return null;
    }

    private void iniciarTransferenciaArchivo(GestorP2P gestor, File archivo, String nombre) throws Exception {
        JsonObject dataInicio = new JsonObject();
        dataInicio.addProperty("nombre", nombre);
        dataInicio.addProperty("pesoTotal", archivo.length());
        dataInicio.addProperty("idSolicitante", Sesion.getUsuario().getId_usuario());

        gestor.manejador.enviarMensaje(new Mensaje(TipoMensaje.P2P_R_DESCARGAR_FICHERO, dataInicio));

        Thread.sleep(200);

        FileInputStream fileInputStream = new FileInputStream(archivo);
        EstadoTransferencia estado = new EstadoTransferencia(archivo, nombre, fileInputStream, gestor);
        transferencias.put(nombre, estado);

        for (int i = 0; i < TAMANO_VENTANA; i++) {
            if (!enviarSiguienteFragmento(estado)) {
                break;
            }
        }
    }

    private boolean enviarSiguienteFragmento(EstadoTransferencia estado) throws Exception {
        if (estado.fragmentosEnVuelo.get() >= TAMANO_VENTANA) {
            return false;
        }

        byte[] buffer = new byte[TAMANO_FRAGMENTO];
        int leidos = estado.fileInputStream.read(buffer);

        if (leidos == -1) {
            if (estado.fragmentosEnVuelo.get() == 0 && !estado.finalizado) {
                finalizarTransferencia(estado);
                estado.finalizado = true;
            }
            return false;
        }

        byte[] fragmento;
        if (leidos < buffer.length) {
            fragmento = new byte[leidos];
            System.arraycopy(buffer, 0, fragmento, 0, leidos);
        } else {
            fragmento = buffer;
        }

        estado.gestor.getCanal().send(new RTCDataChannelBuffer(ByteBuffer.wrap(fragmento), true));
        estado.fragmentosEnVuelo.incrementAndGet();

        return true;
    }

    private void finalizarTransferencia(EstadoTransferencia estado) throws Exception {
        estado.gestor.getCanal().send(new RTCDataChannelBuffer(ByteBuffer.wrap(new byte[0]), true));

        estado.fileInputStream.close();
        transferencias.remove(estado.nombre);

        System.out.println("Transferencia completada para: " + estado.nombre);
    }

    public boolean comprobarValidezRuta(String ruta, Usuario usuario) {
        int id = ControladorMenuPrincipal.instancia.getControladoresTarjetaContacto().get(usuario).getConversacion().getIdConversacion();
        List<EspacioCompartido> espacios = Sesion.getDatosSesionUsuario()
                .getCacheDatosConversacion().get(id)
                .getDatosPaqueteEspaciosCompartidos().getEnviados();

        for (EspacioCompartido espacio : espacios) {
            System.out.println("Comprobando espacio compartido: " + espacio.getRutaPropiedadProperty().get() + " contra " + ruta + " resultado:" + espacio.getRutaPropiedadProperty().get().startsWith(ruta));
            if (ruta.startsWith(espacio.getRutaPropiedadProperty().get())) {
                return true;
            }
        }

        return false;
    }
}