package org.p2pnexus.cliente.ventanas;

import java.net.URL;

public enum VENTANAS {
    CARGANDO_CONEXION("org/p2pnexus/cliente/fxml/CargandoConexion.fxml"),
    LOGIN("org/p2pnexus/cliente/fxml/Login.fxml"),;

    VENTANAS(String ruta) {
        try {
            this.ruta = getClass().getClassLoader().getResource(ruta);
        } catch (NullPointerException e) {
            System.out.println("Error al cargar la ventana: " + ruta);
            e.printStackTrace();
        }
    }
    URL ruta;
}
