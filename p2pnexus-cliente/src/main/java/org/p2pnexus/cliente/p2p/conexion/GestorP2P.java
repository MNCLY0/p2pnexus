package org.p2pnexus.cliente.p2p.conexion;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import dev.onvoid.webrtc.*;
import org.p2pnexus.cliente.p2p.observers.CreadorDeOfertaObserver;
import org.p2pnexus.cliente.p2p.observers.ReceptorDeOfertaObserver;
import org.p2pnexus.cliente.p2p.observers.ReceptorDeRespuestaObserver;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorP2P {

    public static Map<Integer, GestorP2P> conexiones = new HashMap<>();

    RTCPeerConnection peer;
    PeerObserver peerObserver;
    Usuario usuarioRemoto;
    RTCDataChannel canal;

    public GestorP2P() {
        peer = crearPeerConection();
    }

    public void hacerOferta(Usuario usuario)
    {
        usuarioRemoto = usuario;
        peerObserver.setUsuarioRemoto(usuario);
        canal = peer.createDataChannel("canal", new RTCDataChannelInit());
        peer.createOffer(new RTCOfferOptions(), new CreadorDeOfertaObserver(peer,usuario,this));
    }

    public void recibirOferta(JsonObject json)
    {
        usuarioRemoto = JsonHerramientas.convertirJsonAObjeto(json.get("usuario_remoto").getAsJsonObject(), Usuario.class);
        peerObserver.setUsuarioRemoto(usuarioRemoto);

        String sdp = json.get("sdp").getAsString();
        RTCSessionDescription oferta = new RTCSessionDescription(RTCSdpType.OFFER, sdp);
        peer.setRemoteDescription(oferta, new ReceptorDeOfertaObserver(peer,usuarioRemoto,this));

    }

    public void recibirRespuesta(String sdp)
    {
        RTCSessionDescription respuesta = new RTCSessionDescription(RTCSdpType.ANSWER, sdp);
        peer.setRemoteDescription(respuesta, new ReceptorDeRespuestaObserver());
    }

    public void recibirIce(JsonObject json) {
        RTCIceCandidate candidate = new RTCIceCandidate(
                json.get("sdpMid").getAsString(),
                json.get("sdpMLineIndex").getAsInt(),
                json.get("candidate").getAsString()
        );

        peer.addIceCandidate(candidate);
        System.out.println("ICE candidate recibido y añadido");
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
        peerObserver = new PeerObserver();
        return factory.createPeerConnection(configuracion,peerObserver);
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
