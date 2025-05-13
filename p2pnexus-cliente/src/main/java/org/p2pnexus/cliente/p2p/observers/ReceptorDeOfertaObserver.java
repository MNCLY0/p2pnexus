package org.p2pnexus.cliente.p2p.observers;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import dev.onvoid.webrtc.*;
import org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable.ControladorTarjetaContacto;
import org.p2pnexus.cliente.controladores.vistas.ControladorMenuPrincipal;
import org.p2pnexus.cliente.controladores.vistas.controladorChat.ControladorChat;
import org.p2pnexus.cliente.p2p.conexion.GestorP2P;
import org.p2pnexus.cliente.p2p.conexion.MensajesP2P;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;

import java.util.List;

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

                        comprobarCargaChat();

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

    void comprobarCargaChat()
    {
        Conversacion conversacion = ControladorMenuPrincipal.instancia.getControladoresTarjetaContacto().get(usuario).getConversacion();
        // si no existen los datos de la conversacion con el usuario solicitado se lo pedimos al servidor para luego poder comprobar si la ruta
        // a la que quiere acceder el usuario es valida o no
        if (conversacion == null)
        {
            ControladorTarjetaContacto controladorTarjetaContacto = ControladorMenuPrincipal.instancia.getControladoresTarjetaContacto().get(usuario);
            controladorTarjetaContacto.solicitarConversacion();
        }
    }

}
