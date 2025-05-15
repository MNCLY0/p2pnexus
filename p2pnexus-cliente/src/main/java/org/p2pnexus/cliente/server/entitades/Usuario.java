package org.p2pnexus.cliente.server.entitades;

import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;
import org.p2pnexus.cliente.sesion.Sesion;

import java.util.Objects;

public class Usuario {

    public Usuario(String nombre, Integer id_usuario) {
        this.nombre = nombre;
        this.id_usuario = id_usuario;
        this.imagen_perfil = "";
    }
    @Expose
    private Integer id_usuario;
    @Expose
    private String nombre;
    @Expose
    private String imagen_perfil;
    @Expose
    public boolean conectado = false;

    transient Image imagen;

    public String getImagen_perfil() {
        return imagen_perfil;
    }

    public void setImagen_perfil(String imagen_perfil) {
        this.imagen_perfil = imagen_perfil;
    }

    public Image getImagen() {
        if (imagen_perfil == null) {
            imagen_perfil = "";
        }
        if (imagen_perfil.isEmpty()) {
            return Sesion.gestionImagenes.cacheImagenes.get("default");
        }
        imagen = Sesion.gestionImagenes.cacheImagenes.get(imagen_perfil);
        if (imagen == null) {
            imagen = new Image(imagen_perfil, true);
            Sesion.gestionImagenes.cacheImagenes.put(imagen_perfil, imagen);
        }
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }

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


    public void establecerConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public boolean getConectado() {
        return conectado;
    }
}
