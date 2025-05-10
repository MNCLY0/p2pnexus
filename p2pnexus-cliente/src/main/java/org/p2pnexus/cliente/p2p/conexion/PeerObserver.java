package org.p2pnexus.cliente.p2p.conexion;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import dev.onvoid.webrtc.*;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;


public class PeerObserver implements PeerConnectionObserver {

    Usuario usuarioRemoto;

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
        System.out.println("Canal de datos recibido del otro peer");
        dataChannel.registerObserver(new RTCDataChannelObserver() {

            @Override
            public void onBufferedAmountChange(long l) {

            }

            @Override
            public void onStateChange() {
                System.out.println("Estado del canal: " + dataChannel.getState());
            }

            @Override
            public void onMessage(RTCDataChannelBuffer rtcDataChannelBuffer) {
                System.out.println();
            }
        });
    }


}
