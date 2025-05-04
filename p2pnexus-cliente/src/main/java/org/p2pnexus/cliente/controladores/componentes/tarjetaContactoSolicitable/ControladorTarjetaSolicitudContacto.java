package org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2RoundAL;
import org.p2pnexus.cliente.controladores.vistas.ControladorMenuPrincipal;
import org.p2pnexus.cliente.controladores.vistas.ControladorSolicitudes;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.SolicitudContacto;

public class ControladorTarjetaSolicitudContacto {


    @FXML
    public Label labelNombre;

    @FXML
    Button botonAceptar;

    @FXML
    Button botonDenegar;

    SolicitudContacto solicitudContacto;

    public void inicializarConSolicitud(SolicitudContacto solicitudContacto)
    {
        this.solicitudContacto = solicitudContacto;
        labelNombre.setText(solicitudContacto.getUsuarioOrigen().getNombre());
        inicializarIconos();
    }

    public void inicializarIconos()
    {
        FontIcon iconoAceptar = new FontIcon(Material2RoundAL.ADD_CIRCLE);
        FontIcon iconoDenegar = new FontIcon(Material2RoundAL.CANCEL);

        botonAceptar.setGraphic(iconoAceptar);
        botonDenegar.setGraphic(iconoDenegar);

        Tooltip.install(botonAceptar, crearTooltip("Aceptar solicitud"));
        Tooltip.install(botonDenegar, crearTooltip("Denegar solicitud"));

    }

    @FXML
    public void aceptarSolicitud()
    {
        actualizarSolicitud(TipoMensaje.C_ACEPTAR_SOLICITUD);
    }

    @FXML
    public void denegarSolicitud()
    {
        actualizarSolicitud(TipoMensaje.C_DENEGAR_SOLICITUD);
    }

    public void actualizarSolicitud(TipoMensaje tipoMensaje)
    {
        JsonObject json = new JsonObject();
        json.addProperty(getIdSolicitud(), solicitudContacto.getId_solicitud());
        Mensaje mensaje = new Mensaje(tipoMensaje, json);
        Conexion.enviarMensaje(mensaje);
        ControladorSolicitudes.instancia.vboxResultadosSolicitudes.getChildren().remove(labelNombre.getParent());
        ControladorSolicitudes.instancia.solicitarActualizacionSolicitudes();

        // Actualizar la lista de usuarios
        Platform.runLater(() -> {
            ControladorMenuPrincipal.instancia.solicitarContactos();
        });
    }

    private static String getIdSolicitud() {
        return "idSolicitud";
    }


    public Tooltip crearTooltip(String texto)
    {
        var tooltip = new Tooltip(texto);
        tooltip.setShowDelay(javafx.util.Duration.millis(300));
        tooltip.setHideOnEscape(true);
        tooltip.setAnchorLocation(Tooltip.AnchorLocation.WINDOW_TOP_LEFT);
        return tooltip;
    }

}
