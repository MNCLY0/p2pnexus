package org.p2pnexus.cliente.controladores.vistas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.p2pnexus.cliente.controladores.componentes.ControladorTarjetaEspacioEnviada;
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

    @FXML
    ComboBox<EspacioCompartido> comboBoxSeleccionEspacio;

    DatosConversacion datosConversacionActual;
    Map<Integer, DatosConversacion> cacheDatosConversacion;

    @FXML
    FlowPane flowPaneEspaciosEnviados;


    @FXML
    public void initialize() {
        instancia = this;

        areaContenidoMensaje.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && event.isControlDown()) {
                enviarMensaje();
                // Event cosume evita que se agregue un salto de linea al hacer control + enter
                event.consume();
            }
        });

        cacheDatosConversacion = Sesion.getDatosSesionUsuario().getCacheDatosConversacion();
    }

    public void abrirChat(Usuario usuario, Conversacion conversacion) {
        this.conversacionActual = conversacion;
        //No me gusta el metodo que recomienda intellij asi que ignoro el warning
        if (cacheDatosConversacion.get(conversacion.getIdConversacion()) == null) {
            cacheDatosConversacion.put(conversacion.getIdConversacion(), new DatosConversacion());
        }
        datosConversacionActual = cacheDatosConversacion.get(conversacion.getIdConversacion());
        solicitarActualizarConversacion(usuario, conversacion);
        // Si la conversacion no existe en la cache se crea

    }

    @FXML
    public void enviarMensaje() {
        String mensaje = areaContenidoMensaje.getText();
        if (mensaje.isEmpty())
            return;
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario_emisor", Sesion.getUsuario().getId_usuario());
        json.addProperty("id_conversacion", conversacionActual.getIdConversacion());
        json.addProperty("contenido", mensaje);
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_ENVIAR_MENSAJE_CHAT, json));
        areaContenidoMensaje.clear();

    }

    public void nuevoMensaje(MensajeChat mensaje) {
        System.out.println("Nuevo mensaje recibido  " + mensaje);
        List<MensajeChat> mensajes = datosConversacionActual.getMensajes();
        System.out.println("Mensajes: " + mensajes);
        if (mensajes == null) return;
        mensajes.add(mensaje);
        System.out.println("Mensaje añadido a la cache");
        Platform.runLater(() ->
        {
            cargarConversacionDesdeCache(mensaje.getConversacion());
        });
    }

    void solicitarActualizarConversacion(Usuario usuario, Conversacion conversacion) {
        // Si ya tenemos los mensajes de la conversacion no necesitamos solicitarlos nuevo y los cargamos desde la cache

        if (cacheDatosConversacion.get(conversacion.getIdConversacion()).getMensajes() != null) {
            cargarConversacionDesdeCache(conversacion);
            return;
        }
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario_cliente", Sesion.getUsuario().getId_usuario());
        json.addProperty("id_usuario_solicitado", usuario.getId_usuario());
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_ACTUALIZAR_CHAT, json));
    }

    public void actualizarDatosCoversacion(DatosConversacion cache, Conversacion conversacion) {
        cacheDatosConversacion.get(conversacion.getIdConversacion()).setMensajes(
                cache.getMensajes()
        );

        cacheDatosConversacion.get(conversacion.getIdConversacion()).setDatosPaqueteEspaciosCompartidos(
                cache.getDatosPaqueteEspaciosCompartidos()
        );

        cargarConversacionDesdeCache(conversacion);
    }

    public void cargarConversacionDesdeCache(Conversacion conversacion) {
        actualizarFiltroComboBox(conversacion);
        Platform.runLater(() -> {
            contenedorMensajes.getChildren().clear();
            for (MensajeChat mensaje : cacheDatosConversacion.get(conversacion.getIdConversacion()).getMensajes()) {
                crearVistaMensaje(mensaje);
            }
            bajarChat();
        });


    }

    public void crearVistaMensaje(MensajeChat mensaje) {
        try {
            FXMLLoader loader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TARJETA_MENSAJE);
            Parent parent = loader.load();
            ControladorTarjetaMensaje controladorTarjetaMensaje = loader.getController();
            controladorTarjetaMensaje.establecerMensaje(mensaje);
            contenedorMensajes.getChildren().add(parent);
        } catch (IOException e) {
            System.out.println("Error al cargar la vista de mensaje");
        }
    }

    public void bajarChat() {
        Platform.runLater(() -> scrollPaneMensajes.setVvalue(1.0));
    }

    public void compartirEspacioSeleccionado()
    {
        EspacioCompartido espacioCompartido = comboBoxSeleccionEspacio.getSelectionModel().getSelectedItem();
        if (espacioCompartido == null || espacioCompartido.getId_espacio() == -1) {
            Notificaciones.MostrarNotificacion("No se ha seleccionado ningún espacio", TipoNotificacion.ERROR);
            return;
        }
        JsonObject json = new JsonObject();
        json.add("espacio", JsonHerramientas.convertirObjetoAJson(espacioCompartido));
        json.add("conversacion", JsonHerramientas.convertirObjetoAJson(conversacionActual));
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_COMPARTIR_ESPACIO, json));
    }

    public void actualizarFiltroComboBox(Conversacion conversacion)
    {
        DatosPaqueteEspaciosCompartidos datosPaqueteEspaciosCompartidos = cacheDatosConversacion.get(conversacion.getIdConversacion()).getDatosPaqueteEspaciosCompartidos();
        Platform.runLater(() -> {
            FilteredList<EspacioCompartido> espaciosNoEnviados = datosPaqueteEspaciosCompartidos.getEspaciosNoEnviados();
            System.out.printf("Espacios no enviados: %s\n", espaciosNoEnviados);
            // Si no hay espacios disponibles se desactiva el comboBox y se muestra un mensaje
            if (espaciosNoEnviados.isEmpty()) {
                comboBoxSeleccionEspacio.setItems(FXCollections.observableArrayList());
                comboBoxSeleccionEspacio.setDisable(true);
                return;
            }
            // Si hay espacios se selecciona el primero y activa por si no lo estaba
            comboBoxSeleccionEspacio.setDisable(false);
            comboBoxSeleccionEspacio.setItems(espaciosNoEnviados);
            comboBoxSeleccionEspacio.getSelectionModel().selectFirst();
        });
        // Una vez ya está todo cargado se inicializan los listeners encargados de actualizar la vista
        cargarEspaciosDesdeCache();
    }

    public Conversacion getConversacionActual() {
        return conversacionActual;
    }

    void cargarEspaciosDesdeCache()
    {
        Platform.runLater(() -> {
            flowPaneEspaciosEnviados.getChildren().clear();
            System.out.print("Se van a cargar los siguientes espacios: " + datosConversacionActual.getDatosPaqueteEspaciosCompartidos().getEnviados());
            for (EspacioCompartido espacio : datosConversacionActual.getDatosPaqueteEspaciosCompartidos().getEnviados())
            {
                if (espacio == null)
                {
                    System.out.println("Espacio nulo");
                    return;
                }
                crearVistaEspacio(Sesion.getDatosSesionUsuario().getEspacios().get(espacio.getId_espacio()));
            }
        });

    }

    void crearVistaEspacio(EspacioCompartido espacio)
    {
        try {
            FXMLLoader loader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TARJETA_ESPACIO_COMPARTIDO_ENVIADO);
            Parent parent = loader.load();
            ControladorTarjetaEspacioEnviada controlador = loader.getController();
            controlador.inicializarTarjetaEspacio(espacio);
            flowPaneEspaciosEnviados.getChildren().add(parent);
        } catch (IOException e) {
            System.out.println("Error al cargar la vista de espacio");
        }
    }





}
