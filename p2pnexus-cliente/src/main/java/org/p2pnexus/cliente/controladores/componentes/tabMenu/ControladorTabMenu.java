package org.p2pnexus.cliente.controladores.componentes.tabMenu;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.p2pnexus.cliente.controladores.componentes.ControladorComponenteMenuBase;

public class ControladorTabMenu extends ControladorComponenteMenuBase {
    @FXML
    public Label nombreTitulo;

    @FXML
    public VBox contenedorIcono;

    @FXML
    public HBox contenedorPrincipal;

    IntegerProperty iconSize = new SimpleIntegerProperty(32);


    public void establecerDatos(TabMenu tabMenu) {
        Platform.runLater(() -> {
            System.out.println("TABMENU: " + tabMenu.getNombre());
            nombreTitulo.setText(tabMenu.getNombre());
            tabMenu.getIcono().getStyleClass().remove("ikonli-font-icon");
            tabMenu.getIcono().getStyleClass().add("icono-menu");
            contenedorIcono.getChildren().add(tabMenu.getIcono());

            // Inicializar eventos de hover desde la clase base
            inicializarEventosHover();
            // Evento de clic para cambiar de tab
            contenedorPrincipal.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> tabMenu.getAccion().moverTab());
        });
    }

    @Override
    protected Pane getContenedorPrincipal() {
        return contenedorPrincipal;
    }
}