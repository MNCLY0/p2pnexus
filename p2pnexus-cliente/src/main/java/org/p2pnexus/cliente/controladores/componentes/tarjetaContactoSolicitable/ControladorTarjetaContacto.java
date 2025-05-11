package org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.p2pnexus.cliente.controladores.componentes.ControladorComponenteMenuBase;
import org.p2pnexus.cliente.controladores.vistas.ControladorMenuPrincipal;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;

public class ControladorTarjetaContacto extends ControladorComponenteMenuBase {

    @FXML
    public Label labelNombre;

    @FXML
    public AnchorPane anchorPane;

    private Usuario usuario;

    private Conversacion conversacion;

    @FXML
    VBox vboxImagen;


    @FXML
    public void initialize() {
        inicializarEventosHover();
    }

    public void inicializarConUsuario(Usuario usuario) {
        labelNombre.setText(usuario.getNombre());
        this.usuario = usuario;

        anchorPane.getProperties().put("controller", this);

        setOnClickListener(() -> {
            setSeleccionado(true);
            if (conversacion == null) {
                solicitarConversacion();
                return;
            }
            abrirConversacion();
        });

        actualizarEstado();
    }

    public void establecerConversacion(Conversacion conversacion) {
        this.conversacion = conversacion;
        abrirConversacion();
    }

    public void abrirConversacion() {
        ControladorMenuPrincipal.instancia.abrirConversacion(usuario, conversacion);
    }

    public void solicitarConversacion() {
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario_busqueda", usuario.getId_usuario());
        json.addProperty("id_usuario_origen", Sesion.getUsuario().getId_usuario());
        Mensaje mensaje = new Mensaje(TipoMensaje.S_CONVERSACION_CON_USUARIO, json);
        Conexion.enviarMensaje(mensaje);
    }

    @Override
    protected Pane getContenedorPrincipal() {
        return anchorPane;
    }

    public void setOnClickListener(Runnable accion) {
        anchorPane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> accion.run());
    }

    public void actualizarEstado()
    {
        Platform.runLater(() ->
        {

            vboxImagen.getStyleClass().removeAll("icono-conectado", "icono-desconectado");

            System.out.printf("Actualizando estado de %s\n", usuario.getNombre() + " a " + usuario.getConectado());
            if (usuario.getConectado())
            {
                vboxImagen.getStyleClass().add("icono-conectado");
            }
            else
            {
                vboxImagen.getStyleClass().add("icono-desconectado");
            }

            vboxImagen.applyCss();
        });
    }

    public Usuario getUsuario() {
        return usuario;
    }
}