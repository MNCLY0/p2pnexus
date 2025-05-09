package org.p2pnexus.cliente.p2p.conexion;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import dev.onvoid.webrtc.*;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;

import java.util.ArrayList;
import java.util.List;

public class GestorP2P {

    RTCPeerConnection peer;
    Usuario usuarioRemoto;
    RTCDataChannel canal;

    public GestorP2P() {
        peer = crearPeerConection();
    }

    public void hacerOferta(Usuario usuario)
    {
        peer.createOffer(new RTCOfferOptions(), new CreateSessionDescriptionObserver() {
            @Override
            public void onSuccess(RTCSessionDescription offer) {

                peer.setLocalDescription(offer, new SetSessionDescriptionObserver() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Local description establecida correctamente.");

                        JsonObject json = new JsonObject();
                        json.addProperty("sdp", offer.sdp);
                        json.add("usuario_remoto", JsonHerramientas.convertirObjetoAJson(usuario));
                        json.add("usuario_local", JsonHerramientas.convertirObjetoAJson(Sesion.getUsuario()));

                        MensajesP2P.enviarOferta(json);
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
        });

    }

    public void recibirOferta(JsonObject json)
    {
        String sdp = json.get("sdp").getAsString();
        usuarioRemoto = JsonHerramientas.convertirJsonAObjeto(json.get("usuario_remoto").getAsJsonObject(), Usuario.class);

        peer.setRemoteDescription(new RTCSessionDescription(RTCSdpType.OFFER, sdp), new SetSessionDescriptionObserver() {
            @Override
            public void onSuccess() {
                System.out.println("Descripción remota establecida correctamente.");
                // Crear respuesta SDP
                peer.createAnswer(new RTCAnswerOptions(), new CreateSessionDescriptionObserver() {
                    @Override
                    public void onSuccess(RTCSessionDescription answer) {
                        peer.setLocalDescription(answer, new SetSessionDescriptionObserver() {
                            @Override
                            public void onSuccess() {
                                System.out.println("Respuesta establecida como descripción local correctamente.");

                                JsonObject jsonRespuesta = new JsonObject();
                                jsonRespuesta.addProperty("sdp", answer.sdp);
                                jsonRespuesta.add("usuario_remoto", JsonHerramientas.convertirObjetoAJson(usuarioRemoto));
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
        });
    }

    public void recibirRespuesta(JsonObject json)
    {
        String sdp = json.get("sdp").getAsString();
        usuarioRemoto = JsonHerramientas.convertirJsonAObjeto(json.get("usuario_remoto").getAsJsonObject(), Usuario.class);

        peer.setRemoteDescription(new RTCSessionDescription(RTCSdpType.ANSWER, sdp), new SetSessionDescriptionObserver() {
            @Override
            public void onSuccess() {
                System.out.println("Descripción remota establecida correctamente.");
            }

            @Override
            public void onFailure(String s) {
                System.err.println("Error al establecer la descripción remota: " + s);
            }
        });
    }

    RTCPeerConnection crearPeerConection()
    {
        String[] servidoreStun = {
                "stun:stun.l.google.com:19302",
                "stun:stun.l.google.com:5349",
                "stun:stun1.l.google.com:3478",
                "stun:stun1.l.google.com:5349",
                "stun:stun2.l.google.com:19302",
                "stun:stun2.l.google.com:5349",
                "stun:stun3.l.google.com:3478",
                "stun:stun3.l.google.com:5349",
                "stun:stun4.l.google.com:19302",
                "stun:stun4.l.google.com:5349"
        };

        PeerConnectionFactory factory = new PeerConnectionFactory();

        RTCConfiguration configuracion = new RTCConfiguration();

        configuracion.iceServers = crearListaIceServer(servidoreStun);

        return factory.createPeerConnection(configuracion,new PeerObserver());
    }


    public RTCIceServer crearIceServer(String url)
    {
        RTCIceServer iceServer = new RTCIceServer();
        iceServer.urls = List.of(url);
        return iceServer;
    }

    public List<RTCIceServer> crearListaIceServer(String[] urls)
    {
        List<RTCIceServer> iceServers = new ArrayList<>();
        for (String url : urls) {
            RTCIceServer iceServer = crearIceServer(url);
            iceServers.add(iceServer);
        }
        return iceServers;
    }

}
