package org.p2pnexus.cliente.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Notificaciones;
import org.p2pnexus.cliente.ventanas.VENTANAS;

public class ControladorPrincipal {
    @FXML
    public StackPane contenedorPrincipal;

    public void initialize() {
        GestorVentanas.inicializar(contenedorPrincipal);
        GestorVentanas.transicionarVentana(VENTANAS.CARGANDO_CONEXION);
    }
}
