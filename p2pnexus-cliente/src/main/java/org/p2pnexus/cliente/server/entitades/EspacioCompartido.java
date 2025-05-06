package org.p2pnexus.cliente.server.entitades;

import com.google.gson.annotations.Expose;

public class EspacioCompartido {

    @Expose
    private Integer id_espacio;

    @Expose
    private String nombre;

    @Expose
    private String ruta_directorio;

    @Expose
    private Usuario propietario;

    public EspacioCompartido() {}

    public EspacioCompartido(String nombre, String ruta_directorio, Usuario propietario) {
        this.nombre = nombre;
        this.ruta_directorio = ruta_directorio;
        this.propietario = propietario;
    }

    public EspacioCompartido(Integer id_espacio, String nombre, String ruta_directorio, Usuario propietario) {
        this.id_espacio = id_espacio;
        this.nombre = nombre;
        this.ruta_directorio = ruta_directorio;
        this.propietario = propietario;
    }

    public Integer getId_espacio() {
        return id_espacio;
    }

    public void setId_espacio(Integer id_espacio) {
        this.id_espacio = id_espacio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta_directorio() {
        return ruta_directorio;
    }

    public void setRuta_directorio(String ruta_directorio) {
        this.ruta_directorio = ruta_directorio;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    @Override
    public String toString() {
        return nombre;
    }
}