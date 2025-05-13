package org.p2pnexus.cliente.ventanas;

import java.net.URL;

public enum Componentes implements IEnumVistaCargable  {

    COMPONENTE_TAB_MENU("org/p2pnexus/cliente/fxml/componentes/TabMenu.fxml"),
    COMPONENTE_TARJETA_CONTACTO_SOLICITABLE("org/p2pnexus/cliente/fxml/componentes/TarjetaContactoSolicitable.fxml"),
    COMPONENTE_TARJETA_SOLICITUD("org/p2pnexus/cliente/fxml/componentes/TarjetaSolicitudContacto.fxml"),
    COMPONENTE_TARJETA_CONTACTO("org/p2pnexus/cliente/fxml/componentes/TarjetaContacto.fxml"),
    COMPONENTE_TARJETA_MENSAJE("org/p2pnexus/cliente/fxml/componentes/TarjetaMensaje.fxml"),
    COMPONENTE_TARJETA_ESPACIO_COMPARTIDO("org/p2pnexus/cliente/fxml/componentes/TarjetaEspacio.fxml"),
    COMPONENTE_TARJETA_ESPACIO_COMPARTIDO_ENVIADO("org/p2pnexus/cliente/fxml/componentes/TarjetaEspacioEnviada.fxml"),
    COMPONENTE_TARJETA_ESPACIO_COMPARTIDO_RECIBIDO("org/p2pnexus/cliente/fxml/componentes/TarjetaEspacioRecibida.fxml"),;
    Componentes(String ruta) {
        try {
            this.ruta = ruta;
        } catch (NullPointerException e) {
            System.out.println("Error al cargar la ventana: " + ruta + e);
            e.printStackTrace();
        }
    }
    String ruta;

    public URL getRuta() {
        System.out.println("Ruta: " + ruta);
        System.out.println("Ruta resouerce : " + Thread.currentThread().getContextClassLoader().getResource(ruta));
        return Thread.currentThread().getContextClassLoader().getResource(ruta);
    }
}
