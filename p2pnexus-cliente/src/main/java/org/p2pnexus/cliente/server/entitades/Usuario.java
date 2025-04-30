package org.p2pnexus.cliente.server.entitades;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Usuario {

    public Usuario(String nombre, Integer id_usuario) {
        this.nombre = nombre;
        this.id_usuario = id_usuario;
    }

    private Integer id_usuario;

    private String nombre;

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
