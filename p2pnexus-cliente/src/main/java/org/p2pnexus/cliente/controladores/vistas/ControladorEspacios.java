package org.p2pnexus.cliente.controladores.vistas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.EspaciosCompartidos;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.util.List;

public class ControladorEspacios {

    @FXML
    public void initialize() {
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario", Sesion.getUsuario().getId_usuario());
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_ESPACIOS_POR_ID,json));
    }

    public void inicializarEspacios(List<EspaciosCompartidos> espaciosCompartidos) {

    }

    @FXML
    public void abrirCreacionEspacio()
    {
        try {
            Parent root = GestorVentanas.crearFXMLoader(Ventanas.MODAL_CREAR_ESPACIO).load();
            Stage stage = new Stage();
            stage.setTitle("Crear Espacio");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }catch (Exception e) {
            System.out.println("Error al abrir la ventana de creación de espacio: " + e);
            e.printStackTrace();
        }

    }
}
