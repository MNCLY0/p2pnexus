package org.p2pnexus.servidor.Entidades;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

@Entity
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Necesitamos para poder serializar el objeto en json, sin el expose se serializa todo, y no queremos la contraseña
    @Expose
    private Integer id_usuario;

    @Expose
    private String nombre;

    private String contrasena;
    @Expose
    private String imagen_perfil;

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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getImagen_perfil() {return imagen_perfil;}

    public void setImagen_perfil(String imagen_perfil) {this.imagen_perfil = imagen_perfil;}

    @Expose
    @Transient
    public boolean conectado = false;

    public void establecerConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public boolean getConectado() {
        return conectado;
    }


}
