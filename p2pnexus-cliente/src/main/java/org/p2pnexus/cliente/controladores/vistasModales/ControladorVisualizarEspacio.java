package org.p2pnexus.cliente.controladores.vistasModales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.p2pnexus.cliente.p2p.Fichero;
import org.p2pnexus.cliente.p2p.FicheroListCell;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;


public class ControladorVisualizarEspacio {
    @FXML
    Label labelNombreEspacio;

    @FXML
    Label labelRuta;

    @FXML
    ListView<Fichero> listaFicheros;

    public void inicializarConEspacio(EspacioCompartido espacioCompartido) {
        labelNombreEspacio.textProperty().bind(espacioCompartido.getNombrePropiedadProperty());
        labelRuta.textProperty().bind(espacioCompartido.getRutaPropiedadProperty());

        ObservableList<Fichero> ficheros = FXCollections.observableArrayList();
        for (int i = 0; i < 20; i++) {
            Fichero fichero = new Fichero("Fichero " + i, "Ruta del fichero " + i, "100MB", "txt");
            ficheros.add(fichero);
        }
        listaFicheros.setCellFactory(fichero -> new FicheroListCell());
        listaFicheros.itemsProperty().set(ficheros);

    }

}
