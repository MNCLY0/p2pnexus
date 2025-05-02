package org.p2pnexus.cliente.controladores.vistas;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import org.p2pnexus.cliente.controladores.componentes.ControladorTarjetaMensaje;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.MensajeChat;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.ventanas.Componentes;
import org.p2pnexus.cliente.ventanas.GestorVentanas;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorChat {

    Conversacion conversacionActual;

    public static ControladorChat controladorChatActual;
    @FXML
    public VBox contenedorMensajes;
    @FXML
    public ScrollPane scrollPaneMensajes;
    @FXML
    public TextArea areaContenidoMensaje;

    Map<Integer, List<MensajeChat>> mensajesChats = new HashMap<>();



    @FXML
    public void initialize() {
        controladorChatActual = this;

        areaContenidoMensaje.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && event.isControlDown()) {
                enviarMensaje();

                // Event cosume evita que se agregue un salto de linea
                event.consume();
            }
        });

    }

    public void abrirChat(Usuario usuario, Conversacion conversacion)
    {
        this.conversacionActual = conversacion;
        solicitarMensajes(usuario, conversacion);
    }

    @FXML
    public void enviarMensaje()
    {
        String mensaje = areaContenidoMensaje.getText();
        if(mensaje.isEmpty())
            return;
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario_emisor", Sesion.getUsuario().getId_usuario());
        json.addProperty("id_conversacion", conversacionActual.getIdConversacion());
        json.addProperty("contenido", mensaje);
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_ENVIAR_MENSAJE_CHAT, json));
        areaContenidoMensaje.clear();

    }

    public void nuevoMensaje(MensajeChat mensaje)
    {
        System.out.println("Nuevo mensaje recibido  " + mensaje);
        List<MensajeChat> mensajes = mensajesChats.get(mensaje.getConversacion().getIdConversacion());
        System.out.println("Mensajes: " + mensajes);
        if (mensajes == null) return;
        mensajes.add(mensaje);
        System.out.println("Mensaje añadido a la cache");
        Platform.runLater(() ->
        {
            cargarDesdeCache(mensaje.getConversacion());
        });


    }

    void solicitarMensajes(Usuario usuario, Conversacion conversacion)
    {
        // Si ya tenemos los mensajes de la conversacion no necesitamos solicitarlos nuevo y los cargamos desde la cache

        if (mensajesChats.containsKey(conversacion.getIdConversacion()))
        {
            cargarDesdeCache(conversacion);
            return;
        }
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario_solicitante", Sesion.getUsuario().getId_usuario());
        json.addProperty("id_usuario", usuario.getId_usuario());
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_MENSAJES_CHAT, json));
    }

    public void establecerMensajes(List<MensajeChat> mensajes, Conversacion conversacion)
    {
        mensajesChats.put(conversacion.getIdConversacion(), mensajes);
        cargarDesdeCache(conversacion);
    }

    public void cargarDesdeCache(Conversacion conversacion)
    {
        Platform.runLater(() -> {
            contenedorMensajes.getChildren().clear();
            for(MensajeChat mensaje : mensajesChats.get(conversacion.getIdConversacion()))
            {
                crearVistaMensaje(mensaje);
            }
            bajarChat();
        });

    }

     public void crearVistaMensaje(MensajeChat mensaje)
     {
         try {
            FXMLLoader loader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TARJETA_MENSAJE);
            Parent parent = loader.load();
            ControladorTarjetaMensaje controladorTarjetaMensaje = loader.getController();
            controladorTarjetaMensaje.establecerMensaje(mensaje);
            contenedorMensajes.getChildren().add(parent);
         }catch (IOException e)
         {
             System.out.println("Error al cargar la vista de mensaje");
         }
     }

     public void bajarChat(){
         Platform.runLater(() -> scrollPaneMensajes.setVvalue(1.0));
     }

     public Conversacion getConversacionActual() {return conversacionActual;}




}
