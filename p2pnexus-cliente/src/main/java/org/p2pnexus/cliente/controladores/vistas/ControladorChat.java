package org.p2pnexus.cliente.controladores.vistas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.p2pnexus.cliente.controladores.componentes.ControladorTarjetaMensaje;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.MensajeChat;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.ventanas.Componentes;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.List;

public class ControladorChat {

    Usuario usuarioActual;
    public static ControladorChat controladorChatActual;

    public VBox contenedorMensajes;

    public ScrollPane scrollPaneMensajes;

    @FXML
    public void initialize() {
        controladorChatActual = this;
    }

    public void abrirChat(Usuario usuario)
    {
        this.usuarioActual = usuario;
        solicitarMensajes();
    }

    void solicitarMensajes()
    {
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario_solicitante", Sesion.getUsuario().getId_usuario());
        json.addProperty("id_usuario", usuarioActual.getId_usuario());
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_MENSAJES_CHAT, json));
    }

    public void establecerMensajes(List<MensajeChat> mensajes)
    {
        Platform.runLater(() -> {
            contenedorMensajes.getChildren().clear();
            for(MensajeChat mensaje : mensajes)
            {
                crearVistaMensaje(mensaje);
            }
            // Cuando se han añadido todos los mensajes, se desplaza la barra de desplazamiento al final
            Platform.runLater(() -> scrollPaneMensajes.setVvalue(1.0));
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




}
