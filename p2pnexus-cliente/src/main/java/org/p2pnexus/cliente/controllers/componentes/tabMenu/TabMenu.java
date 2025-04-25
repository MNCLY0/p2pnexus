package org.p2pnexus.cliente.controllers.componentes.tabMenu;

import org.kordamp.ikonli.javafx.FontIcon;
import org.p2pnexus.cliente.ventanas.Ventanas;

public class TabMenu {
    private String nombre;
    private FontIcon icono;
    private Ventanas ventana;

    public TabMenu(String nombre, FontIcon icono, Ventanas ventana) {
        this.nombre = nombre;
        this.icono = icono;
        this.ventana = ventana;
    }

    public String getNombre() {
        return nombre;
    }

    public FontIcon getIcono() {
        return icono;
    }

    public Ventanas getVentana() {
        return ventana;
    }
}

