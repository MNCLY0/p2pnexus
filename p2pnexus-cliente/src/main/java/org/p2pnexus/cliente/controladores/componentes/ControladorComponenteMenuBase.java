package org.p2pnexus.cliente.controladores.componentes;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public abstract class ControladorComponenteMenuBase {
    protected boolean seleccionado = false;
    private static List<ControladorComponenteMenuBase> componentesRegistrados = new ArrayList<>();

    public ControladorComponenteMenuBase() {
        componentesRegistrados.add(this);
    }

    protected abstract Pane getContenedorPrincipal();

    protected void aplicarEfectoHover(boolean aplicar) {
        if (seleccionado) return;
        cambiarEstilo(aplicar);
    }

    public void cambiarEstilo(boolean activo) {
        Pane contenedor = getContenedorPrincipal();
        if (activo) {
            if (!contenedor.getStyleClass().contains("bg-neutral-muted")) {
                contenedor.getStyleClass().add("bg-neutral-muted");
            }
        } else {
            contenedor.getStyleClass().remove("bg-neutral-muted");
        }
    }

    public boolean seleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        // Si ya está en el estado solicitado, no hacemos nada
        if (this.seleccionado == seleccionado) return;

        // Deseleccionamos todos los demás componentes
        if (seleccionado) {
            deseleccionarTodos();
        }

        // Actualizamos el estado
        this.seleccionado = seleccionado;
        cambiarEstilo(seleccionado);
    }

    protected void inicializarEventosHover() {
        Pane contenedor = getContenedorPrincipal();
        contenedor.setOnMouseEntered(e -> aplicarEfectoHover(true));
        contenedor.setOnMouseExited(e -> aplicarEfectoHover(false));
    }

    private void deseleccionarTodos() {
        for (ControladorComponenteMenuBase componente : componentesRegistrados) {
            if (componente != this) {
                componente.seleccionado = false;
                componente.cambiarEstilo(false);
            }
        }
    }


    public static void eliminarComponente(ControladorComponenteMenuBase componente) {
        componentesRegistrados.remove(componente);
    }
}