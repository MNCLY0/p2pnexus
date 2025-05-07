package org.p2pnexus.cliente.controladores.componentes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import org.p2pnexus.cliente.controladores.vistasModales.ControladorEdicionEspacio;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.io.IOException;

public class ControladorTarjetaEspacio {
    @FXML
    Label labelNombreEspacio;
    @FXML
    Label labelRutaEspacio;

    EspacioCompartido espacioCompartido;

    public void inicializarTarjetaEspacio(EspacioCompartido espacio) {
        System.out.println("Inicializando tarjeta de espacio compartido para " + espacio.getNombrePropiedadProperty().get());
        labelNombreEspacio.textProperty().bind(espacio.getNombrePropiedadProperty());
        labelRutaEspacio.textProperty().bind(espacio.getRutaPropiedadProperty());
        this.espacioCompartido = espacio;
    }

    @FXML
    public void abrirModoEditar()
    {
        try {
            FXMLLoader loader = GestorVentanas.crearFXMLoader(Ventanas.MODAL_EDITAR_ESPACIO);
            Parent root = loader.load();
            ControladorEdicionEspacio controlador = loader.getController();
            controlador.inicializarEdicion(espacioCompartido);
            GestorVentanas.abrirModal(root, "Editar espacio compartido", false);
        } catch (IOException e) {
            System.out.println("Error al cargar el modal de edición de espacio: " + e);
        }
    }
}
