package org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.p2pnexus.cliente.controladores.componentes.ControladorComponenteMenuBase;
import org.p2pnexus.cliente.controladores.vistas.ControladorMenuPrincipal;
import org.p2pnexus.cliente.server.entitades.Usuario;

public class ControladorTarjetaContacto extends ControladorComponenteMenuBase {

    @FXML
    public Label labelNombre;

    @FXML
    public AnchorPane anchorPane;

    private Usuario usuario;

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
            ControladorMenuPrincipal.controladorMenuPrincipalActual.abrirChatConUsuario(usuario);
        });
    }

    @Override
    protected Pane getContenedorPrincipal() {
        return anchorPane;
    }

    public void setOnClickListener(Runnable accion) {
        anchorPane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> accion.run());
    }
}