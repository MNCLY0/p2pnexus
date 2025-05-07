package org.p2pnexus.cliente.controladores.componentes;

import javafx.beans.value.ObservableStringValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.ventanas.GestorVentanas;

public class ControladorTarjetaEspacioEnviada {
    @FXML
    Label labelNombreEspacio;
    @FXML
    Label labelRutaEspacio;

    EspacioCompartido espacioCompartido;

    public void inicializarTarjetaEspacio(EspacioCompartido espacio) {
        labelNombreEspacio.textProperty().bind(espacio.getNombrePropiedadProperty());
        labelRutaEspacio.textProperty().bind(espacio.getRutaPropiedadProperty());
        this.espacioCompartido = espacio;
    }

    @FXML
    public void dejarDeCompartir() {
        Boolean confirmacion = GestorVentanas.pedirConfirmacion(
                "Dejar de compartir el espacio",
                "¿Estás seguro de que deseas dejar de compartir el espacio \"" +
                        espacioCompartido.getNombrePropiedadProperty().get() + "\"?\n\n" +
                        "El usuario con el que lo compartiste ya no podrá acceder a él.",
                Alert.AlertType.ERROR,
                labelNombreEspacio.getScene()
        );
        System.out.println(confirmacion);
    }

}
