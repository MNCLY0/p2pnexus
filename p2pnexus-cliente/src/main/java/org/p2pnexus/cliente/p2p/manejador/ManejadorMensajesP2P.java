package org.p2pnexus.cliente.p2p.manejador;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import dev.onvoid.webrtc.RTCDataChannel;
import dev.onvoid.webrtc.RTCDataChannelBuffer;
import dev.onvoid.webrtc.RTCDataChannelObserver;
import org.p2pnexus.cliente.p2p.manejador.manejadores.ManejadorP2PDebugMensaje;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class ManejadorMensajesP2P {

    private final RTCDataChannel canal;
    private final Map<TipoMensaje, IManejadorMensaje> manejadores = new HashMap<>();

    public ManejadorMensajesP2P(RTCDataChannel canal) {
        this.canal = canal;
        inicializarManejadores();
        registrarListener();
    }

    private void registrarListener() {
        canal.registerObserver(new RTCDataChannelObserver() {
            @Override
            public void onMessage(RTCDataChannelBuffer buffer) {
                System.out.println("Mensaje recibido en el canal P2P: " + buffer.data.remaining());
                byte[] datos = new byte[buffer.data.remaining()];
                buffer.data.get(datos);
                System.out.println("Datos recibidos: " + datos.length);
                String texto = new String(datos);
                System.out.println("Texto recibido: " + texto);
                JsonObject json = JsonParser.parseString(texto).getAsJsonObject();
                System.out.println("JSON recibido: " + json.toString());

                try {
                    Mensaje mensaje = JsonHerramientas.convertirJsonAObjeto(json, Mensaje.class);
                    manejarPeticion(mensaje);
                } catch (Exception e) {
                    System.err.println("Error al procesar mensaje P2P: " + e.getMessage());
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
            manejador.manejarDatos(mensaje, null);
        } catch (ManejarPeticionesExeptionError e) {
            System.err.println("Error en petición P2P: " + e.getMessage());
        }
    }

    public void enviarMensaje(Mensaje mensaje) {
        System.out.println("Enviando mensaje P2P: " + mensaje.getTipo() + " " + mensaje.getData());
        JsonObject json = JsonHerramientas.convertirObjetoAJson(mensaje);
        System.out.println("JSON a enviar: " + json);
        try {
            String jsonString = json.toString();
            System.out.println("JSON a enviar como string: " + jsonString);
            canal.send(new RTCDataChannelBuffer(ByteBuffer.wrap(jsonString.getBytes()), false));
        }catch (Exception e) {
            System.err.println("Error al enviar mensaje P2P: ");
            e.printStackTrace();
        }

    }

    public void inicializarManejadores() {
        manejadores.put(TipoMensaje.P2P_DEBUG_MENSAJE, new ManejadorP2PDebugMensaje());
    }
}