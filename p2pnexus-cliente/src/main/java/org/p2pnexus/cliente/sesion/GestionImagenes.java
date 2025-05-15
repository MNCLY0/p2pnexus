package org.p2pnexus.cliente.sesion;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class GestionImagenes {
    public Map<String, Image> cacheImagenes;

    public GestionImagenes(){
        cacheImagenes = new HashMap<>();
        cacheImagenes.put("default", new Image("org/p2pnexus/cliente/imagenes/logo_default.png", true));
    }


}
