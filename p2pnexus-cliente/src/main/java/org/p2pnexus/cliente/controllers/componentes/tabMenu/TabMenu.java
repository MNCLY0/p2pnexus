package org.p2pnexus.cliente.controllers.componentes.tabMenu;

import org.kordamp.ikonli.javafx.FontIcon;
import org.p2pnexus.cliente.controllers.componentes.tabMenu.tabacciones.AccionTab;
import org.p2pnexus.cliente.ventanas.Ventanas;

public class TabMenu {
    private String nombre;
    private FontIcon icono;
    private AccionTab accion;
    private Ventanas ventana;

    public TabMenu(String nombre, FontIcon icono, AccionTab accion, Ventanas ventana) {
        this.nombre = nombre;
        this.icono = icono;
        this.accion = accion;
        this.ventana = ventana;

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

    public void setAccion(AccionTab accion) {
        this.accion = accion;
    }
}

