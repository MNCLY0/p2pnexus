package org.p2pnexus.cliente.p2p;

import com.google.gson.annotations.Expose;

public class Fichero {
    @Expose
    String nombre;
    @Expose
    String ruta;
    @Expose
    String size;
    @Expose
    String extension;

    public Fichero(String nombre, String ruta, String size, String extension) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.size = size;
        this.extension = extension;
    }

}

