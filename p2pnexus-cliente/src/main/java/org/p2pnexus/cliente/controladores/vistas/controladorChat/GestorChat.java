package org.p2pnexus.cliente.controladores.vistas.controladorChat;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.p2pnexus.cliente.controladores.componentes.ControladorTarjetaMensaje;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.MensajeChat;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.sesion.datos.datosEspecificos.DatosConversacion;
import org.p2pnexus.cliente.ventanas.Animaciones;
import org.p2pnexus.cliente.ventanas.Componentes;
import org.p2pnexus.cliente.ventanas.GestorVentanas;

import java.io.IOException;
import java.util.List;

public class GestorChat {
    ControladorChat controladorChat;
    public GestorChat(ControladorChat controladorChat) {
        this.controladorChat = controladorChat;
    }


    @FXML
    public void enviarMensaje() {
        String mensaje = controladorChat.areaContenidoMensaje.getText();
        if (mensaje.isEmpty())
            return;
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario_emisor", Sesion.getUsuario().getId_usuario());
        json.addProperty("id_conversacion", controladorChat.conversacionActual.getIdConversacion());
        json.addProperty("contenido", mensaje);
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.S_ENVIAR_MENSAJE_CHAT, json));
        controladorChat.areaContenidoMensaje.clear();

    }

    public void nuevoMensaje(MensajeChat mensaje) {
        System.out.println("Nuevo mensaje recibido  " + mensaje);
        List<MensajeChat> mensajes = controladorChat.datosConversacionActual.getMensajes();
        System.out.println("Mensajes: " + mensajes);
        if (mensajes == null) return;
        mensajes.add(mensaje);
        System.out.println("Mensaje añadido a la cache");
        Platform.runLater(() ->
        {
            cargarConversacionDesdeCache(mensaje.getConversacion());
        });
    }

    public void solicitarActualizarConversacion(Usuario usuario, Conversacion conversacion) {
        // Si ya tenemos los mensajes de la conversacion no necesitamos solicitarlos nuevo y los cargamos desde la cache

        if (controladorChat.cacheDatosConversacion.get(conversacion.getIdConversacion()).getMensajes() != null) {
            cargarConversacionDesdeCache(conversacion);
            return;
        }
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario_cliente", Sesion.getUsuario().getId_usuario());
        json.addProperty("id_usuario_solicitado", usuario.getId_usuario());
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.S_ACTUALIZAR_CHAT, json));
    }

    public void actualizarDatosCoversacion(DatosConversacion cache, Conversacion conversacion) {
        controladorChat.cacheDatosConversacion.get(conversacion.getIdConversacion()).setMensajes(
                cache.getMensajes()
        );

        controladorChat.cacheDatosConversacion.get(conversacion.getIdConversacion()).setDatosPaqueteEspaciosCompartidos(
                cache.getDatosPaqueteEspaciosCompartidos()
        );

        cargarConversacionDesdeCache(conversacion);
    }

    public void cargarConversacionDesdeCache(Conversacion conversacion) {

        controladorChat.gestorEspacios.actualizarFiltroComboBox(conversacion);

        Platform.runLater(() -> {
            controladorChat.contenedorMensajes.getChildren().clear();
            for (MensajeChat mensaje : controladorChat.cacheDatosConversacion.get(conversacion.getIdConversacion()).getMensajes()) {
                crearVistaMensaje(mensaje);
            }
            bajarChat();
            Platform.runLater(()->
            {
                Animaciones.animarEntradaListaNodosDesdeAbajo(controladorChat.contenedorMensajes.getChildren(), 250,50);
            });
        });
    }

    public void crearVistaMensaje(MensajeChat mensaje) {
        try {
            FXMLLoader loader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TARJETA_MENSAJE);
            Parent parent = loader.load();
            parent.setVisible(false);
            ControladorTarjetaMensaje controladorTarjetaMensaje = loader.getController();
            controladorTarjetaMensaje.establecerMensaje(mensaje);
            controladorChat.contenedorMensajes.getChildren().add(parent);

        } catch (IOException e) {
            System.out.println("Error al cargar la vista de mensaje");
        }
    }

    public void bajarChat() {
        Platform.runLater(() -> controladorChat.scrollPaneMensajes.setVvalue(1.0));
    }
}
