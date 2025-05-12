package org.p2pnexus.cliente.controladores.vistas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import org.p2pnexus.cliente.controladores.componentes.ControladorTarjetaEspacio;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.ventanas.Componentes;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorEspacios {


    public static ControladorEspacios instancia;

    @FXML
    FlowPane flowPaneEspaciosCreados;

    Map<Integer, Parent> tarjetasEspacios = new HashMap<>();

    @FXML
    public void initialize() {
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario", Sesion.getUsuario().getId_usuario());
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.S_ESPACIOS_POR_ID,json));
        instancia = this;
    }

    public void inicializarEspacios(List<EspacioCompartido> espacioCompartidos) {

            for (EspacioCompartido espacio : espacioCompartidos) {
                espacio.inializarPropiedades();
                inicializarTarjetaEspacio(espacio);
            }
    }

    public void inicializarTarjetaEspacio(EspacioCompartido espacio)
    {
        Platform.runLater(() -> {
            try {
                // Si el espacio que se intenta agregar ya existe va a devolver false y no se va a agregar pero si se va a actualizar
                if (!Sesion.datosSesionUsuario.agregarEspacio(espacio)) return;

                FXMLLoader loader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TARJETA_ESPACIO_COMPARTIDO);
                Parent root = loader.load();
                ControladorTarjetaEspacio controlador = loader.getController();
                controlador.inicializarTarjetaEspacio(espacio);
                flowPaneEspaciosCreados.getChildren().add(0,root);
                tarjetasEspacios.put(espacio.getId_espacio(), root);

            }catch (IOException e) {
                System.out.println("Error al cargar el componente de tarjeta de espacio compartido: " + e);
            }
        });
    }

    public void eliminarTarjetaEspacio(EspacioCompartido espacio)
    {
        Platform.runLater(() -> {
            try {
                System.out.println("Tarjetas de espacios compartidos: " + tarjetasEspacios);
                Parent tarjeta = tarjetasEspacios.get(espacio.getId_espacio());
                if (tarjeta != null) {
                    flowPaneEspaciosCreados.getChildren().remove(tarjeta);
                    tarjetasEspacios.remove(espacio.getId_espacio());
                    Sesion.getDatosSesionUsuario().eliminarEspacio(espacio);
                }
            }catch (
                    Exception e) {
                System.out.println("Error al eliminar la tarjeta de espacio compartido: " + e);
            }
        });
    }

    @FXML
    public void abrirCreacionEspacio()
    {
        try {
            Parent root = GestorVentanas.crearFXMLoader(Ventanas.MODAL_CREAR_ESPACIO).load();
            GestorVentanas.abrirModal(root, "Crear espacio compartido", false);
        }catch (Exception e) {
            System.out.println("Error al abrir la ventana de creación de espacio: " + e);
            e.printStackTrace();
        }
    }
}
