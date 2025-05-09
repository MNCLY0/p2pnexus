package org.p2pnexus.cliente.server.entitades;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Transient;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;
import java.util.Set;

public class Usuario {

    public Usuario(String nombre, Integer id_usuario) {
        this.nombre = nombre;
        this.id_usuario = id_usuario;
    }


    @Expose
    private Integer id_usuario;
    @Expose
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id_usuario, usuario.id_usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id_usuario);
    }

    @Expose
    public boolean conectado = false;

    public void establecerConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public boolean getConectado() {
        return conectado;
    }
}
