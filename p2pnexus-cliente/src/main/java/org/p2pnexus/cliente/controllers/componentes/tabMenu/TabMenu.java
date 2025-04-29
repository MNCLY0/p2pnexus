package org.p2pnexus.cliente.controllers.componentes.tabMenu;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.kordamp.ikonli.javafx.FontIcon;
import org.p2pnexus.cliente.controllers.componentes.tabMenu.tabAcciones.AccionTab;
import org.p2pnexus.cliente.ventanas.Ventanas;

public class TabMenu {
    private String nombre;
    private FontIcon icono;
    private AccionTab accion;
    private Ventanas ventana;
    private ControladorTabMenu controladorTabMenu;

    public TabMenu(String nombre, FontIcon icono, Ventanas ventana) {
        this.nombre = nombre;
        this.icono = icono;
        this.accion = new AccionTab();
        this.ventana = ventana;
    }


    public void setControladorTabMenu(ControladorTabMenu controladorTabMenu) {
        this.controladorTabMenu = controladorTabMenu;
    }

    public ControladorTabMenu getControladorTabMenu() {
        return controladorTabMenu;
    }

    public String getNombre() {
        return nombre;
    }

    public FontIcon getIcono() {
        return icono;
    }

    public AccionTab getAccion() {
        return accion;
    }

    public Ventanas getVentana() {return ventana;}

    public void setAccion(Tab tab, TabPane tabPane) {
        this.accion.inicializar(tab, tabPane);
    }
}

