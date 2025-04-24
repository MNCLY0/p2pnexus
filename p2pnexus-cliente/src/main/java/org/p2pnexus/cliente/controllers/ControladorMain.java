package org.p2pnexus.cliente.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

public class ControladorMain {
    @FXML
    public StackPane contenedorPrincipal;

    public void initialize() {
        GestorVentanas.inicializar(contenedorPrincipal);
        GestorVentanas.transicionarVentana(Ventanas.CARGANDO_CONEXION);
    }
}
