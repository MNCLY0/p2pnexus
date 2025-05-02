package org.p2pnexus.cliente.ventanas;

import java.net.URL;

public enum Componentes implements IEnumVistaCargable  {

    COMPONENTE_TAB_MENU("org/p2pnexus/cliente/fxml/componentes/TabMenu.fxml"),
    COMPONENTE_TARJETA_CONTACTO_SOLICITABLE("org/p2pnexus/cliente/fxml/componentes/TarjetaContactoSolicitable.fxml"),
    COMPONENTE_TARJETA_SOLICITUD("org/p2pnexus/cliente/fxml/componentes/TarjetaSolicitudContacto.fxml"),
    COMPONENTE_TARJETA_CONTACTO("org/p2pnexus/cliente/fxml/componentes/TarjetaContacto.fxml"),
    COMPONENTE_TARJETA_MENSAJE("org/p2pnexus/cliente/fxml/componentes/TarjetaMensaje.fxml"),;
    Componentes(String ruta) {
        try {
            this.ruta = getClass().getClassLoader().getResource(ruta);
        } catch (NullPointerException e) {
            System.out.println("Error al cargar el componente: " + ruta + e);
            e.printStackTrace();
        }
    }
    URL ruta;

    public URL getRuta() {
        return ruta;
    }
}
