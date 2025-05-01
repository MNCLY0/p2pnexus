package org.p2pnexus.cliente.controladores.componentes.tabMenu;

import javafx.beans.binding.Bindings;
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

    int iconoSize = 16;

    public void establecerDatos(TabMenu tabMenu) {
        nombreTitulo.setText(tabMenu.getNombre());
        // La unica manera que he encontrado de hacer que el icono se ajuste al tamaño del contenedor es usando un binding
        tabMenu.getIcono().iconSizeProperty().bind(Bindings.createIntegerBinding(() -> (int) (contenedorIcono.getHeight() * 0.5), contenedorIcono.heightProperty()));
        contenedorIcono.getChildren().add(tabMenu.getIcono());

        // Inicializar eventos de hover desde la clase base
        inicializarEventosHover();

        // Evento de clic para cambiar de tab
        contenedorPrincipal.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> tabMenu.getAccion().moverTab());
    }

    @Override
    protected Pane getContenedorPrincipal() {
        return contenedorPrincipal;
    }
}