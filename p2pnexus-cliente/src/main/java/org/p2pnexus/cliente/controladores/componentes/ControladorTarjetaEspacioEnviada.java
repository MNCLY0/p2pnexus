package org.p2pnexus.cliente.controladores.componentes;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.beans.value.ObservableStringValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.p2pnexus.cliente.controladores.vistas.ControladorChat;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.ventanas.GestorVentanas;

public class ControladorTarjetaEspacioEnviada {
    @FXML
    VBox vboxPadre;
    @FXML
    Label labelNombreEspacio;
    @FXML
    Label labelRutaEspacio;

    EspacioCompartido espacioCompartido;
    Conversacion conversacion;

    public void inicializarTarjetaEspacio(EspacioCompartido espacio, Conversacion conversacion) {
        labelNombreEspacio.textProperty().bind(espacio.getNombrePropiedadProperty());
        labelRutaEspacio.textProperty().bind(espacio.getRutaPropiedadProperty());
        this.espacioCompartido = espacio;
        this.conversacion = conversacion;
    }

    @FXML
    public void dejarDeCompartir() {
        boolean confirmacion = GestorVentanas.pedirConfirmacion(
                "Dejar de compartir el espacio",
                "¿Estás seguro de que deseas dejar de compartir el espacio \"" +
                        espacioCompartido.getNombrePropiedadProperty().get() + "\"?\n\n" +
                        "El usuario con el que lo compartiste ya no podrá acceder a él.",
                Alert.AlertType.ERROR,
                labelNombreEspacio.getScene()
        );
        if (!confirmacion) {return;}
        JsonObject json = new JsonObject();
        json.add("conversacion", JsonHerramientas.convertirObjetoAJson(conversacion));
        json.add("espacio", JsonHerramientas.convertirObjetoAJson(espacioCompartido));
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_DEJAR_DE_COMPARTIR_ESPACIO, json));
        ControladorChat.instancia.eliminarEspacioEnviado(espacioCompartido);
        ControladorChat.instancia.actualizarFiltroComboBox(conversacion);

    }

}
