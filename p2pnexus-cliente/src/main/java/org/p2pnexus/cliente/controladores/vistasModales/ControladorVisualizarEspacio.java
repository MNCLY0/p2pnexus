package org.p2pnexus.cliente.controladores.vistasModales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.p2pnexus.cliente.p2p.Fichero;
import org.p2pnexus.cliente.p2p.FicheroListCell;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;

import java.io.File;
import java.util.List;


public class ControladorVisualizarEspacio {
    @FXML
    Label labelNombreEspacio;

    @FXML
    Label labelRuta;

    @FXML
    ListView<Fichero> listaFicheros;

    public void inicializarConEspacio(EspacioCompartido espacioCompartido, List<Fichero> ficheros) {
        labelNombreEspacio.textProperty().bind(espacioCompartido.getNombrePropiedadProperty());
        labelRuta.textProperty().bind(espacioCompartido.getRutaPropiedadProperty());

        listaFicheros.setCellFactory(fichero -> new FicheroListCell());
        listaFicheros.itemsProperty().set(FXCollections.observableArrayList(ficheros));

    }

}
