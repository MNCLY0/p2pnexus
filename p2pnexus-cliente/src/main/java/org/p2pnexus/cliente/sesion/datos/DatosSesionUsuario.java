package org.p2pnexus.cliente.sesion.datos;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.sesion.datos.datosEspecificos.DatosConversacion;

import java.util.HashMap;
import java.util.Map;

public class DatosSesionUsuario {

    // Lista de espacios creados por el usuario
    SimpleListProperty<EspacioCompartido> espacios = new SimpleListProperty<>(FXCollections.observableArrayList());

    public SimpleListProperty<EspacioCompartido> getEspacios() {return espacios;}
    public void setEspacios(SimpleListProperty<EspacioCompartido> espaciosUsuario) {this.espacios = espaciosUsuario;}
    public void agregarEspacio(EspacioCompartido espacioCompartido) {this.espacios.add(espacioCompartido);}
    public boolean eliminarEspacio(EspacioCompartido espacioCompartido) {return this.espacios.remove(espacioCompartido);}


    // Dicccionario de mensajes de la conversacion, donde la clave es el id del chat y el valor es una lista de mensajes
    Map<Integer, DatosConversacion> cacheDatosConversacion = new HashMap<>();

    public Map<Integer, DatosConversacion> getCacheDatosConversacion() {return cacheDatosConversacion;}
    public void setCacheDatosConversacion(Map<Integer, DatosConversacion> cacheDatosConversacion) {this.cacheDatosConversacion = cacheDatosConversacion;}

    //

}
