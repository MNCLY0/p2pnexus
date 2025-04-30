package org.p2pnexus.cliente.controladores.componentes.tabMenu.tabAcciones;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class AccionTab {

    private Tab tab;
    private TabPane tabPane;

    BooleanProperty seleccionado;

    public AccionTab() {
        this.tab = null;
        this.tabPane = null;
    }

    public void inicializar(Tab tab, TabPane tabPane)
    {
        this.tab = tab;
        this.tabPane = tabPane;
    }

    public void moverTab()
    {
        if (tab == null || tabPane == null) {
            throw new IllegalStateException("Las variables tab y tabPane primero deben ser inicializadas");
        }
        tabPane.getSelectionModel().select(tab);
    };

    public TabPane getTabPane() {
        return tabPane;
    }

    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }
}
