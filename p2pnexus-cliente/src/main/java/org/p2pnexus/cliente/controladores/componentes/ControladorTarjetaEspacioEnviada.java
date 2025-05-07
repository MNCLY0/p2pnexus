package org.p2pnexus.cliente.controladores.componentes;

import javafx.beans.value.ObservableStringValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;

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

}
