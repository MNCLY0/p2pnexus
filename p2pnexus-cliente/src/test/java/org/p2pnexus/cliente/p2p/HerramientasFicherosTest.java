package org.p2pnexus.cliente.p2p;

import org.junit.jupiter.api.Test;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;

class HerramientasFicherosTest {

    @Test
    void obtenerInformacionRuta() {
        EspacioCompartido espacioCompartido = new EspacioCompartido();
        espacioCompartido.setRuta_directorio("src/main/resources/org/p2pnexus/cliente/fxml/componentes");

        HerramientasFicheros.obtenerInformacionRuta(espacioCompartido);

    }
}