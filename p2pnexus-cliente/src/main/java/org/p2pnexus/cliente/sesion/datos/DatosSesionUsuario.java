package org.p2pnexus.cliente.sesion.datos;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.sesion.datos.datosEspecificos.DatosConversacion;

import java.util.HashMap;
import java.util.Map;

public class DatosSesionUsuario {

    // Lista de espacios creados por el usuario
    ObservableList<EspacioCompartido> espacios = FXCollections.observableArrayList();

    public ObservableList<EspacioCompartido> getEspacios() {return espacios;}
    public void setEspacios(ObservableList<EspacioCompartido> espaciosUsuario) {this.espacios = espaciosUsuario;}
    public void agregarEspacio(EspacioCompartido espacioCompartido)
    {
        if (this.espacios.contains(espacioCompartido))
        {
            EspacioCompartido espacioExistente = this.espacios.get(this.espacios.indexOf(espacioCompartido));
            espacioExistente.setNombre(espacioCompartido.getNombrePropiedadProperty().get());
            espacioExistente.setRuta_directorio(espacioCompartido.getRutaPropiedadProperty().get());
            return;
        }
        this.espacios.add(espacioCompartido);
    }
    public boolean eliminarEspacio(EspacioCompartido espacioCompartido) {return this.espacios.remove(espacioCompartido);}


    // Dicccionario de mensajes de la conversacion, donde la clave es el id del chat y el valor es una lista de mensajes
    Map<Integer, DatosConversacion> cacheDatosConversacion = new HashMap<>();

    public Map<Integer, DatosConversacion> getCacheDatosConversacion() {return cacheDatosConversacion;}
    public void setCacheDatosConversacion(Map<Integer, DatosConversacion> cacheDatosConversacion) {this.cacheDatosConversacion = cacheDatosConversacion;}

    //

}
