package org.p2pnexus.cliente.controladores.componentes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;

public class ControladorTarjetaEspacioRecibida {
    @FXML
    VBox vboxPadre;
    @FXML
    Label labelNombreEspacio;
    @FXML
    Label labelRutaEspacio;

    boolean puedeAnimar = true;

    EspacioCompartido espacioCompartido;
    Conversacion conversacion;

    public void inicializarTarjetaEspacio(EspacioCompartido espacio, Conversacion conversacion) {
        labelNombreEspacio.textProperty().bind(espacio.getNombrePropiedadProperty());
        labelRutaEspacio.textProperty().bind(espacio.getRutaPropiedadProperty());
        this.espacioCompartido = espacio;
        this.conversacion = conversacion;
    }

    @FXML
    void animarInteracion()
    {
        if (puedeAnimar)
        {
            puedeAnimar = false;
            vboxPadre.getStyleClass().add("context-menu-seleccionado");
        }

    }

    @FXML
    void animarInteracionSalida()
    {
        if (!puedeAnimar)
        {
            puedeAnimar = true;
            vboxPadre.getStyleClass().remove("context-menu-seleccionado");
        }
    }

    void acceder()
    {

    }

}
