package org.p2pnexus.cliente.p2p.observers;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import dev.onvoid.webrtc.CreateSessionDescriptionObserver;
import dev.onvoid.webrtc.RTCPeerConnection;
import dev.onvoid.webrtc.RTCSessionDescription;
import dev.onvoid.webrtc.SetSessionDescriptionObserver;
import org.p2pnexus.cliente.p2p.conexion.GestorP2P;
import org.p2pnexus.cliente.p2p.conexion.MensajesP2P;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;

public class CreadorDeOfertaObserver implements CreateSessionDescriptionObserver {

    final RTCPeerConnection peer;
    final Usuario usuario;
    final GestorP2P gestor;

    public CreadorDeOfertaObserver(RTCPeerConnection peer, Usuario usuario, GestorP2P gestor) {
        this.peer = peer;
        this.usuario = usuario;
        this.gestor = gestor;
    }

    @Override
    public void onSuccess(RTCSessionDescription rtcSessionDescription) {
        peer.setLocalDescription(rtcSessionDescription, new SetSessionDescriptionObserver() {
            @Override
            public void onSuccess() {
                System.out.println("Local description establecida correctamente.");

                JsonObject json = new JsonObject();
                json.addProperty("sdp", rtcSessionDescription.sdp);
                json.add("usuario_remoto", JsonHerramientas.convertirObjetoAJson(usuario));
                json.add("usuario_local", JsonHerramientas.convertirObjetoAJson(Sesion.getUsuario()));
                System.out.println("Enviando oferta SDP: " + json);
                MensajesP2P.enviarOferta(json);
                GestorP2P.conexiones.put(usuario.getId_usuario(), gestor);
            }

            @Override
            public void onFailure(String s) {
                System.err.println("Error al establecer la descripción: " + s);
            }
        });
    }

    @Override
        public void onFailure(String s) {
        System.err.println("Error al crear la oferta SDP: " + s);
    }
}

