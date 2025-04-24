package org.p2pnexus.cliente.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2RoundAL;
import org.p2pnexus.cliente.controllers.componentes.TabMenu;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControladorMenuPrincipal {
    @FXML
    public VBox vboxSecciones;

    ArrayList<TabMenu> tabs = new ArrayList<>(
            List.of(
                new TabMenu("Solicitudes", new FontIcon(Material2MZ.PERSON), null),
                new TabMenu("Espacios", new FontIcon(Material2RoundAL.CREATE_NEW_FOLDER), null)
            )
    );

    @FXML
    public void initialize() {
        for (TabMenu tab : tabs) {
            try {
                vboxSecciones.getChildren().add(GestorVentanas.crearVentana(Ventanas.TAB_MENU));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
