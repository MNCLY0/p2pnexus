package org.p2pnexus.cliente.sesion.datos;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.sesion.datos.datosEspecificos.DatosConversacion;

import java.util.HashMap;
import java.util.Map;

public class DatosSesionUsuario {

    // Lista de espacios creados por el usuario
    ObservableMap<Integer, EspacioCompartido> espacios = FXCollections.observableHashMap();
    private ObservableList<EspacioCompartido> espaciosList = FXCollections.observableArrayList();

    public ObservableMap<Integer,EspacioCompartido> getEspacios() {return espacios;}
    public void setEspacios(ObservableMap<Integer,EspacioCompartido> espaciosUsuario) {this.espacios = espaciosUsuario;}

    public boolean agregarEspacio(EspacioCompartido espacioCompartido)
    {
        if (this.espacios.get(espacioCompartido.getId_espacio()) != null)
        {
            EspacioCompartido espacioExistente = this.espacios.get(espacioCompartido.getId_espacio());
            espacioExistente.setNombre(espacioCompartido.getNombrePropiedadProperty().get());
            espacioExistente.setRuta_directorio(espacioCompartido.getRutaPropiedadProperty().get());
            // Devuelve false si el espacio ya existe y se actualiza
            return false;
        }
        this.espacios.put(espacioCompartido.getId_espacio(), espacioCompartido);
        espaciosList.add(espacioCompartido);
        // Devuelve true si el espacio se agrega correctamente
        return true;
    }

    public ObservableList<EspacioCompartido> getObservableListEspacios()
    {
        return espaciosList;
    }

    public void eliminarEspacio(EspacioCompartido espacioCompartido) {
        this.espacios.remove(espacioCompartido.getId_espacio());
        this.espaciosList.remove(espacioCompartido);
    }


    // Dicccionario de mensajes de la conversacion, donde la clave es el id del chat y el valor es una lista de mensajes
    Map<Integer, DatosConversacion> cacheDatosConversacion = new HashMap<>();

    public Map<Integer, DatosConversacion> getCacheDatosConversacion() {return cacheDatosConversacion;}
    public void setCacheDatosConversacion(Map<Integer, DatosConversacion> cacheDatosConversacion) {this.cacheDatosConversacion = cacheDatosConversacion;}

    //

}
