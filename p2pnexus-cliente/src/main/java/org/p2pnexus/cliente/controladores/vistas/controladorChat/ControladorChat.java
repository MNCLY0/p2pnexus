package org.p2pnexus.cliente.controladores.vistas.controladorChat;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.p2pnexus.cliente.controladores.componentes.ControladorTarjetaEspacioEnviada;
import org.p2pnexus.cliente.controladores.componentes.ControladorTarjetaEspacioRecibida;
import org.p2pnexus.cliente.controladores.componentes.ControladorTarjetaMensaje;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.server.entitades.MensajeChat;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.sesion.datos.datosEspecificos.DatosConversacion;
import org.p2pnexus.cliente.sesion.datos.datosEspecificos.DatosPaqueteEspaciosCompartidos;
import org.p2pnexus.cliente.ventanas.Componentes;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Notificaciones;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorChat {

    Conversacion conversacionActual;

    public static ControladorChat instancia;
    @FXML
    public VBox contenedorMensajes;
    @FXML
    public ScrollPane scrollPaneMensajes;
    @FXML
    public TextArea areaContenidoMensaje;


    // Chat

    public GestorChat gestorChat;

    DatosConversacion datosConversacionActual;
    Map<Integer, DatosConversacion> cacheDatosConversacion;


    // Espacios

    public GestorEspacios gestorEspacios;

    @FXML
    ComboBox<EspacioCompartido> comboBoxSeleccionEspacio;

    @FXML
    FlowPane flowPaneEspaciosEnviados;
    Map<EspacioCompartido, Parent> tarjetasEspaciosEnviados = new HashMap<>();

    @FXML
    FlowPane flowPlaneEspaciosRecibidos;
    Map<EspacioCompartido, Parent> tarjetasEspaciosRecibidos = new HashMap<>();


    @FXML
    public void initialize() {
        instancia = this;
        //Me di cuenta que necesitaba estas clases gestoras porque estaba creando toda la logica
        // en esta clase y se estaba volviendo un poco caotico / largo
        gestorChat = new GestorChat(this);
        gestorEspacios = new GestorEspacios(this);

        areaContenidoMensaje.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && event.isControlDown()) {
                gestorChat.enviarMensaje();
                // Event cosume evita que se agregue un salto de linea al hacer control + enter
                event.consume();
            }
        });

        cacheDatosConversacion = Sesion.getDatosSesionUsuario().getCacheDatosConversacion();
    }

    public void abrirChat(Usuario usuario, Conversacion conversacion) {
        conversacionActual = conversacion;
        //No me gusta el metodo que recomienda intellij asi que ignoro el warning
        if (cacheDatosConversacion.get(conversacion.getIdConversacion()) == null) {
            cacheDatosConversacion.put(conversacion.getIdConversacion(), new DatosConversacion());
        }
        datosConversacionActual = cacheDatosConversacion.get(conversacion.getIdConversacion());
        gestorChat.solicitarActualizarConversacion(usuario, conversacion);
        // Si la conversacion no existe en la cache se crea
    }


    public Conversacion getConversacionActual() {
        return conversacionActual;
    }

    @FXML
    public void enviarMensaje()
    {
        gestorChat.enviarMensaje();
    }

    @FXML
    public void compartirEspacioSeleccionado()
    {
        gestorEspacios.compartirEspacioSeleccionado();
    }

}
