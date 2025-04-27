package org.p2pnexus.cliente.controllers.componentes.tabMenu.tabacciones;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class AccionTab {

    private final Tab tab;
    private final TabPane tabPane;

    public AccionTab(Tab tab, TabPane tabPane) {
        this.tab = tab;
        this.tabPane = tabPane;
    }

    public void moverTab()
    {
        tabPane.getSelectionModel().select(tab);
    };
}
