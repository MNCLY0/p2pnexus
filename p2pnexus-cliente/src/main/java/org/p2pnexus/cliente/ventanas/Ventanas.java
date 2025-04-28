package org.p2pnexus.cliente.ventanas;

import java.net.URL;

public enum Ventanas {
    CARGANDO_CONEXION("org/p2pnexus/cliente/fxml/CargandoConexion.fxml"),
    LOGIN("org/p2pnexus/cliente/fxml/Login.fxml"),
    REGISTRO("org/p2pnexus/cliente/fxml/Registro.fxml"),
    MENU_PRINCIPAL("org/p2pnexus/cliente/fxml/MenuPrincipal.fxml"),
    TAB_SOLICITUDES("org/p2pnexus/cliente/fxml/Solicitudes.fxml"),
    TAB_CHAT("org/p2pnexus/cliente/fxml/Chat.fxml"),

    // Componentes (esto me gustaria hacerlo de otra manera, por ahora lo voy a dejar asi)
    TAB_MENU("org/p2pnexus/cliente/fxml/componentes/TabMenu.fxml");

    Ventanas(String ruta) {
        try {
            this.ruta = getClass().getClassLoader().getResource(ruta);
        } catch (NullPointerException e) {
            System.out.println("Error al cargar la ventana: " + ruta + e);
            e.printStackTrace();
        }
    }
    URL ruta;
}
