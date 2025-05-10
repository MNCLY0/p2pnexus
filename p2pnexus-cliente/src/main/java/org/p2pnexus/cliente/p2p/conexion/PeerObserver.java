package org.p2pnexus.cliente.p2p.conexion;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import dev.onvoid.webrtc.*;
import org.p2pnexus.cliente.p2p.manejador.ManejadorMensajesP2P;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;

import java.nio.ByteBuffer;


public class PeerObserver implements PeerConnectionObserver {

    Usuario usuarioRemoto;
    GestorP2P gestorP2P;

    public PeerObserver(GestorP2P gestorP2P) {
        this.gestorP2P = gestorP2P;
    }

    public void setUsuarioRemoto(Usuario usuarioRemoto) {
        this.usuarioRemoto = usuarioRemoto;
    }

    @Override
    public void onSignalingChange(RTCSignalingState state) {
        PeerConnectionObserver.super.onSignalingChange(state);
    }

    @Override
    public void onConnectionChange(RTCPeerConnectionState state) {
        System.out.println("Estado de la conexión: " + state);
    }

    @Override
    public void onIceCandidate(RTCIceCandidate candidate) {
        System.out.println("Nuevo ICE candidate generado: " + candidate.sdpMid + " " + candidate.sdpMLineIndex);

        JsonObject json = new JsonObject();
        json.addProperty("sdpMid", candidate.sdpMid);
        json.addProperty("sdpMLineIndex", candidate.sdpMLineIndex);
        json.addProperty("candidate", candidate.sdp);
        json.add("usuario_remoto", JsonHerramientas.convertirObjetoAJson(usuarioRemoto));
        json.add("usuario_local", JsonHerramientas.convertirObjetoAJson(Sesion.getUsuario()));

        MensajesP2P.enviarIce(json);
    }

    @Override
    public void onDataChannel(RTCDataChannel dataChannel) {
        System.out.println("Canal de datos recibido: " + dataChannel.getLabel());
        gestorP2P.canal = dataChannel;

        gestorP2P.manejador = new ManejadorMensajesP2P(dataChannel);

        dataChannel.registerObserver(new RTCDataChannelObserver() {
            @Override
            public void onStateChange() {
                System.out.println("Estado del canal recibido: " + dataChannel.getState());
                if (dataChannel.getState() == RTCDataChannelState.OPEN) {
                    JsonObject json = new JsonObject();
                    json.addProperty("mensajePrueba", "Hola desde receptor");
                    gestorP2P.manejador.enviarMensaje(new Mensaje(TipoMensaje.P2P_DEBUG_MENSAJE, json));
                }
            }

            @Override public void onBufferedAmountChange(long l) {}
            @Override public void onMessage(RTCDataChannelBuffer buffer) {}
        });
    }


}
