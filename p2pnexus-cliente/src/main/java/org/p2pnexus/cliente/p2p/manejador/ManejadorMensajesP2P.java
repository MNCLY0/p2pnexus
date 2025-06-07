package org.p2pnexus.cliente.p2p.manejador;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import dev.onvoid.webrtc.RTCDataChannel;
import dev.onvoid.webrtc.RTCDataChannelBuffer;
import dev.onvoid.webrtc.RTCDataChannelObserver;
import org.p2pnexus.cliente.p2p.manejador.manejadores.descarga.ManejadorP2PDescargaReceptor;
import org.p2pnexus.cliente.p2p.manejador.manejadores.descarga.ManejadorP2PDescargaSolicitud;
import org.p2pnexus.cliente.p2p.manejador.manejadores.respuesta.ManejadorP2PRInfoRuta;
import org.p2pnexus.cliente.p2p.manejador.manejadores.solicitud.ManejadorP2PDebugMensaje;
import org.p2pnexus.cliente.p2p.manejador.manejadores.solicitud.ManejadorP2PInfoRuta;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class ManejadorMensajesP2P {

    private final RTCDataChannel canal;
    private final Map<TipoMensaje, IManejadorMensaje> manejadores = new HashMap<>();
    private final ManejadorP2PDescargaReceptor manejadorDescarga = new ManejadorP2PDescargaReceptor();
    private final ManejadorP2PDescargaSolicitud manejadorDescargaSolicitud = new ManejadorP2PDescargaSolicitud();

    public ManejadorMensajesP2P(RTCDataChannel canal) {
        this.canal = canal;
        inicializarManejadores();
        registrarListener();
    }

    public void inicializarManejadores() {
        manejadores.put(TipoMensaje.P2P_DEBUG_MENSAJE, new ManejadorP2PDebugMensaje());
        manejadores.put(TipoMensaje.P2P_S_INFO_RUTA, new ManejadorP2PInfoRuta());
        manejadores.put(TipoMensaje.P2P_R_INFO_RUTA, new ManejadorP2PRInfoRuta());

        manejadores.put(TipoMensaje.P2P_S_DESCARGAR_FICHERO, manejadorDescargaSolicitud);

        manejadores.put(TipoMensaje.P2P_R_DESCARGAR_FICHERO, manejadorDescarga);
    }

    private void registrarListener() {
        canal.registerObserver(new RTCDataChannelObserver() {
            @Override
            public void onMessage(RTCDataChannelBuffer buffer) {
                try {
                    if (buffer.binary) {
                        byte[] datos = new byte[buffer.data.remaining()];
                        buffer.data.get(datos);

                        if (datos.length == 1 && datos[0] == ManejadorP2PDescargaSolicitud.CONFIRMACION) {
                            manejadorDescargaSolicitud.manejarConfirmacionBinaria();
                        } else {
                            manejadorDescarga.recibirFragmentoBinario(datos);
                        }
                    } else {
                        byte[] datos = new byte[buffer.data.remaining()];
                        buffer.data.get(datos);
                        String mensaje = new String(datos);

                        try {
                            JsonObject json = JsonParser.parseString(mensaje).getAsJsonObject();
                            Mensaje mensajeObjeto = JsonHerramientas.convertirJsonAObjeto(json, Mensaje.class);
                            manejarPeticion(mensajeObjeto);
                        } catch (JsonSyntaxException e) {
                            System.err.println("Error de sintaxis JSON: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error procesando mensaje: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override public void onStateChange() {}
            @Override public void onBufferedAmountChange(long l) {}
        });
    }

    public void manejarPeticion(Mensaje mensaje) {
        IManejadorMensaje manejador = manejadores.get(mensaje.getTipo());
        if (manejador == null) {
            System.err.println("No se encontró manejador para: " + mensaje.getTipo());
            return;
        }
        try {
            ResultadoMensaje resultado = manejador.manejarDatos(mensaje, null);
            if (resultado != null) {
                enviarMensaje(resultado.getMensaje());
            }
        } catch (ManejarPeticionesExeptionError e) {
            System.err.println("Error en petición P2P: " + e.getMessage());
        }
    }

    public void enviarMensaje(Mensaje mensaje) {
        JsonObject json = JsonHerramientas.convertirObjetoAJson(mensaje);
        try {
            String jsonString = json.toString();
            canal.send(new RTCDataChannelBuffer(ByteBuffer.wrap(jsonString.getBytes()), false));
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}