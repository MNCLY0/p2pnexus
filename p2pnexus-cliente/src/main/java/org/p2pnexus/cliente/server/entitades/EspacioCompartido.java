package org.p2pnexus.cliente.server.entitades;

import com.google.gson.annotations.Expose;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class EspacioCompartido {

    @Expose
    private Integer id_espacio;

    @Expose
    private String nombre;

    @Expose
    private String ruta_directorio;

    @Expose
    private Usuario propietario;

    SimpleStringProperty nombrePropiedad = new SimpleStringProperty();
    SimpleStringProperty rutaPropiedad = new SimpleStringProperty();


    public EspacioCompartido() {}

    public EspacioCompartido(Integer id_espacio, String nombre, String ruta_directorio, Usuario propietario) {
        this.id_espacio = id_espacio;
        this.nombre = nombre;
        this.ruta_directorio = ruta_directorio;
        this.propietario = propietario;
        this.nombrePropiedad.set(nombre);
        this.rutaPropiedad.set(ruta_directorio);
        System.out.printf("Inicializado EspacioCompartido: %s, %s, %s, %s\n", id_espacio, nombre, ruta_directorio, propietario);
    }


    public EspacioCompartido(String nombre, String ruta_directorio, Usuario propietario) {
        this(null, nombre, ruta_directorio, propietario);
    }


    public Integer getId_espacio() {
        return id_espacio;
    }

    public void setId_espacio(Integer id_espacio) {
        this.id_espacio = id_espacio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.nombrePropiedad.set(nombre);
    }

    public SimpleStringProperty getNombrePropiedadProperty() {
        return nombrePropiedad;
    }


    public SimpleStringProperty getRutaPropiedadProperty() {
        return rutaPropiedad;
    }

    public void setRuta_directorio(String ruta_directorio) {
        this.ruta_directorio = ruta_directorio;
        this.rutaPropiedad.set(ruta_directorio);
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }



    @Override
    public String toString() {return nombre;}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (((EspacioCompartido) o).propietario == null || propietario == null ) return false;
        EspacioCompartido that = (EspacioCompartido) o;
        return (Objects.equals(nombre, that.nombre) && Objects.equals(propietario.getId_usuario(), that.propietario.getId_usuario()))
                || Objects.equals(that.id_espacio, id_espacio);    }

    @Override
    public int hashCode() {
        return Objects.hash(id_espacio, nombre, propietario);
    }
}