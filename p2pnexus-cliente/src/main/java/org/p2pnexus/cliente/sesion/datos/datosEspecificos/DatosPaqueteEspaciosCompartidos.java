package org.p2pnexus.cliente.sesion.datos.datosEspecificos;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.sesion.Sesion;

import java.util.List;

public class DatosPaqueteEspaciosCompartidos {
    ObservableList<EspacioCompartido> enviados = FXCollections.observableArrayList();
    ObservableList<EspacioCompartido> recibidos = FXCollections.observableArrayList();
    FilteredList<EspacioCompartido> espaciosNoEnviados;

    public DatosPaqueteEspaciosCompartidos(List<EspacioCompartido> enviados, List<EspacioCompartido> recibidos) {
        this.enviados.setAll(enviados);
        this.recibidos.setAll(recibidos);
        actualizarFiltroNoCompartidos();
        this.enviados.addListener((ListChangeListener<EspacioCompartido>) cambio -> {
           actualizarFiltroNoCompartidos();
        });
        inicializarPropiedades();
    }

    void inicializarPropiedades() {
        for (EspacioCompartido espacioCompartido : enviados) {
            espacioCompartido.inializarPropiedades();
            EspacioCompartido espacioCompartidoSesion = Sesion.getDatosSesionUsuario().getEspacios().get(espacioCompartido.getId_espacio());
            espacioCompartido.getNombrePropiedadProperty().bind(espacioCompartidoSesion.getNombrePropiedadProperty());
            espacioCompartido.getRutaPropiedadProperty().bind(espacioCompartidoSesion.getRutaPropiedadProperty());
        }
        for (EspacioCompartido espacioCompartido : recibidos) {
            espacioCompartido.inializarPropiedades();
        }
    }

    public ObservableList<EspacioCompartido> getEnviados() {
        return enviados;
    }

    public ObservableList<EspacioCompartido> getRecibidos() {
        return recibidos;
    }

    public FilteredList<EspacioCompartido> getEspaciosNoEnviados() {
        return espaciosNoEnviados;
    }

    // Este filtro sirve para los espacios que aun no se han compartido en una conversacion sobre el total de todos los espacios creados por el usuario
    public void actualizarFiltroNoCompartidos() {
        Platform.runLater(() ->{
            if (this.espaciosNoEnviados == null)
            {
                this.espaciosNoEnviados = new FilteredList<>(Sesion.datosSesionUsuario.getObservableListEspacios());
            }
            espaciosNoEnviados.setPredicate(espacioCompartido -> !enviados.contains(espacioCompartido));
        });

    }

}
