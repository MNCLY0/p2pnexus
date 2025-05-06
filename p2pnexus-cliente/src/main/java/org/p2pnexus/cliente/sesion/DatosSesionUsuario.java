package org.p2pnexus.cliente.sesion;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;

public class DatosSesionUsuario {
    SimpleListProperty<EspacioCompartido> espacios = new SimpleListProperty<>(FXCollections.observableArrayList());

    public SimpleListProperty<EspacioCompartido> getEspaciosUsuario() {return espacios;}
    public void setEspaciosUsuario(SimpleListProperty<EspacioCompartido> espaciosUsuario) {this.espacios = espaciosUsuario;}
    public void agregarEspacio(EspacioCompartido espacioCompartido) {this.espacios.add(espacioCompartido);}
    public boolean eliminarEspacio(EspacioCompartido espacioCompartido) {return this.espacios.remove(espacioCompartido);}
}
