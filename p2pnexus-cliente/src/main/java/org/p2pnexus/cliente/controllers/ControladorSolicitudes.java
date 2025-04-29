package org.p2pnexus.cliente.controllers;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.RecibirMensajes;
import org.p2pnexus.cliente.ventanas.Notificaciones;

public class ControladorSolicitudes {

    @FXML
    public TextField campo_busqueda;
    @FXML
    public Button boton_buscar;
    @FXML
    public VBox vbox_resultados;

    public static ControladorSolicitudes controladorSolicitudesActual;

    @FXML
    public void initialize()
    {
        controladorSolicitudesActual = this;
    }

    @FXML
    public void solicitarBusqueda()
    {
        if (campo_busqueda.getText().isEmpty())
        {
            Notificaciones.MostrarNotificacion("Campo de búsqueda vacío", TipoNotificacion.AVISO);
            return;
        }

        JsonObject json = new JsonObject();
        json.addProperty("nombre", campo_busqueda.getText());
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_BUSCAR_USUARIOS_POR_NOMBRE, json));
    }

    public void agregarResultado(JsonObject json)
    {
        System.out.println("Resultado: " + json.get("usuarios"));
    }
}
