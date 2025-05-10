package org.p2pnexus.cliente.p2p.observers;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import dev.onvoid.webrtc.*;
import org.p2pnexus.cliente.p2p.conexion.GestorP2P;
import org.p2pnexus.cliente.p2p.conexion.MensajesP2P;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;

public class ReceptorDeOfertaObserver implements SetSessionDescriptionObserver {

    final RTCPeerConnection peer;
    final Usuario usuario;
    final GestorP2P gestor;

    public ReceptorDeOfertaObserver(RTCPeerConnection peer, Usuario usuario, GestorP2P gestor) {
        this.gestor = gestor;
        this.usuario = usuario;
        this.peer = peer;
    }

    @Override
    public void onSuccess() {
        System.out.println("Descripción remota establecida correctamente.");

        peer.createAnswer(new RTCAnswerOptions(), new CreateSessionDescriptionObserver() {
            @Override
            public void onSuccess(RTCSessionDescription answer) {
                peer.setLocalDescription(answer, new SetSessionDescriptionObserver() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Respuesta establecida como descripción local correctamente.");

                        JsonObject jsonRespuesta = new JsonObject();
                        jsonRespuesta.addProperty("sdp", answer.sdp);
                        jsonRespuesta.add("usuario_remoto", JsonHerramientas.convertirObjetoAJson(usuario));
                        jsonRespuesta.add("usuario_local", JsonHerramientas.convertirObjetoAJson(Sesion.getUsuario()));

                        MensajesP2P.enviarRespuesta(jsonRespuesta);
                    }

                    @Override
                    public void onFailure(String s) {
                        System.err.println("Error al establecer descripción local con respuesta: " + s);
                    }
                });
            }

            @Override
            public void onFailure(String s) {
                System.err.println("Error al crear respuesta SDP: " + s);
            }
        });
    }

    @Override
    public void onFailure(String s) {
        System.err.println("Error al establecer la descripción remota: " + s);
    }
}
