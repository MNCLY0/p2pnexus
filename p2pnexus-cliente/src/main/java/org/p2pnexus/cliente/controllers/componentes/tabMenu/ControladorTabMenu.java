package org.p2pnexus.cliente.controllers.componentes.tabMenu;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.kordamp.ikonli.javafx.FontIcon;


public class ControladorTabMenu {
    @FXML
    public Label nombreTitulo;

    @FXML
    public VBox contenedorIcono;

    @FXML
    public HBox contenedorPrincipal;

    int iconoSize = 16;

    public void establecerDatos(TabMenu tabMenu)
    {
        nombreTitulo.setText(tabMenu.getNombre());
        // La unica manera que he encontrado de hacer que el icono se ajuste al tamaño del contenedor es usando un binding
        // el problema es que lanza erroes por consola, pero vamos que funciona bien
        tabMenu.getIcono().iconSizeProperty().bind(Bindings.createIntegerBinding(() -> (int) (contenedorIcono.getHeight() * 0.5), contenedorIcono.heightProperty()));
        contenedorIcono.getChildren().add(tabMenu.getIcono());

        contenedorPrincipal.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> aplicarEfectoHover(true));
        contenedorPrincipal.addEventHandler(MouseEvent.MOUSE_EXITED, e -> aplicarEfectoHover(false));

        contenedorPrincipal.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> tabMenu.getAccion().moverTab());

    }

    private void aplicarEfectoHover(boolean aplicar)
    {
        if(aplicar)
        {
            contenedorPrincipal.getStyleClass().add("bg-neutral-muted");
        }
        else
        {
            contenedorPrincipal.getStyleClass().remove("bg-neutral-muted");
        }
    }
}
