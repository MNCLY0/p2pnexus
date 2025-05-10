package org.p2pnexus.cliente.controladores.componentes;

import com.p2pnexus.comun.TipoNotificacion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.p2pnexus.cliente.controladores.vistas.ControladorMenuPrincipal;
import org.p2pnexus.cliente.controladores.vistasModales.ControladorVisualizarEspacio;
import org.p2pnexus.cliente.p2p.conexion.GestorP2P;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Notificaciones;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.io.IOException;

public class ControladorTarjetaEspacioRecibida {
    @FXML
    VBox vboxPadre;
    @FXML
    Label labelNombreEspacio;
    @FXML
    Label labelRutaEspacio;

    boolean puedeAnimar = true;

    EspacioCompartido espacioCompartido;
    Conversacion conversacion;

    public void inicializarTarjetaEspacio(EspacioCompartido espacio, Conversacion conversacion) {
        labelNombreEspacio.textProperty().bind(espacio.getNombrePropiedadProperty());
        labelRutaEspacio.textProperty().bind(espacio.getRutaPropiedadProperty());
        this.espacioCompartido = espacio;
        this.conversacion = conversacion;
    }

    @FXML
    void animarInteracion()
    {
        if (puedeAnimar)
        {
            puedeAnimar = false;
            vboxPadre.getStyleClass().add("context-menu-seleccionado");
        }

    }

    @FXML
    void animarInteracionSalida()
    {
        if (!puedeAnimar)
        {
            puedeAnimar = true;
            vboxPadre.getStyleClass().remove("context-menu-seleccionado");
        }
    }

    @FXML
    void acceder()
    {
        GestorP2P gestor = new GestorP2P();
        gestor.hacerOferta(espacioCompartido.getPropietario());

//        // Sincronizar el espacio compartido con el usuario real (con el que llevamos el control del estado)
//        Usuario usuarioReal = ControladorMenuPrincipal.instancia.getControladoresTarjetaContacto().get(espacioCompartido.getPropietario()).getUsuario();
//        espacioCompartido.setPropietario(usuarioReal);
//
////        if (!espacioCompartido.getPropietario().getConectado())
////        {
////            Notificaciones.mostrarNotificacion("No puedes acceder al espacio porque el propietario no está conectado", TipoNotificacion.ERROR,2);
////            return;
////        }
//        try {
//            FXMLLoader loader = GestorVentanas.crearFXMLoader(Ventanas.MODAL_VISUALIZAR_ESPACIO);
//            Parent parent = loader.load();
//            ControladorVisualizarEspacio controlador = loader.getController();
//            controlador.inicializarConEspacio(espacioCompartido);
//            GestorVentanas.abrirModal(parent, espacioCompartido.getNombrePropiedadProperty().get(), false);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }

}
