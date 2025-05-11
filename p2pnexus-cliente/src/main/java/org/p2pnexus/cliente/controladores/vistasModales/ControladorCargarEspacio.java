package org.p2pnexus.cliente.controladores.vistasModales;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.p2pnexus.cliente.p2p.Fichero;
import org.p2pnexus.cliente.p2p.conexion.GestorP2P;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;

public class ControladorCargarEspacio {
    @FXML
    Label labelRuta;

    EspacioCompartido espacioCompartido;

    GestorP2P gestorP2P;

    public void inicializarConEspacio(EspacioCompartido espacioCompartido) {
        labelRuta.textProperty().bind(espacioCompartido.getRutaPropiedadProperty());
        this.espacioCompartido = espacioCompartido;

        gestorP2P = new GestorP2P();
        gestorP2P.hacerOferta(espacioCompartido.getPropietario());

        esperar();
    }

    public void esperar()
    {
        new Thread(() -> {
           int intentos = 0;
           int maxIntentos = 20;
           int intervalo = 500;

           while (intentos < maxIntentos) {
                if (gestorP2P.canalAbierto())
                {
                    System.out.println("Canal abierto");
                    solicitarInfoEspacio();
                    break;
                }
                else
                {
                    System.out.println("Esperando canal...");
                    try {
                        Thread.sleep(intervalo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    intentos++;
                }
           }
        }).start();
    }

    public void solicitarInfoEspacio()
    {
        JsonObject json = new JsonObject();
        json.add("espacio", JsonHerramientas.convertirObjetoAJson(espacioCompartido));
        gestorP2P.manejador.enviarMensaje(new Mensaje(TipoMensaje.P2P_S_INFO_RUTA,json));
    }

}
