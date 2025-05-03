package org.p2pnexus.cliente.ventanas;

import java.net.URL;

public enum Ventanas implements IEnumVistaCargable{
    MAIN("org/p2pnexus/cliente/fxml/vistas/Main.fxml"),
    CARGANDO_CONEXION("org/p2pnexus/cliente/fxml/vistas/CargandoConexion.fxml"),
    LOGIN("org/p2pnexus/cliente/fxml/vistas/Login.fxml"),
    REGISTRO("org/p2pnexus/cliente/fxml/vistas/Registro.fxml"),
    MENU_PRINCIPAL("org/p2pnexus/cliente/fxml/vistas/MenuPrincipal.fxml"),
    TAB_SOLICITUDES("org/p2pnexus/cliente/fxml/vistas/Solicitudes.fxml"),
    TAB_CHAT("org/p2pnexus/cliente/fxml/vistas/Chat.fxml"),
    ESPACIOS("org/p2pnexus/cliente/fxml/vistas/Espacios.fxml"),
    MODAL_CREAR_ESPACIO("org/p2pnexus/cliente/fxml/vistasModales/CreacionEspacio.fxml"),;

    Ventanas(String ruta) {
        try {
            this.ruta = getClass().getClassLoader().getResource(ruta);
        } catch (NullPointerException e) {
            System.out.println("Error al cargar la ventana: " + ruta + e);
            e.printStackTrace();
        }
    }
    URL ruta;

    public URL getRuta() {
        return ruta;
    }
}
