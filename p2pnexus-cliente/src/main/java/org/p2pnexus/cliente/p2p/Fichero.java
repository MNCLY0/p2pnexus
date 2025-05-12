package org.p2pnexus.cliente.p2p;

import com.google.gson.annotations.Expose;
import org.p2pnexus.cliente.server.entitades.Usuario;

public class Fichero {
    @Expose
    String nombre;
    @Expose
    String ruta;
    @Expose
    String size;
    @Expose
    String extension;
    @Expose
    Usuario usuarioOrigen;

    public Fichero(String nombre, String ruta, String size, String extension, Usuario usuario) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.size = size;
        this.extension = extension;
        this.usuarioOrigen = usuario;
    }

}

