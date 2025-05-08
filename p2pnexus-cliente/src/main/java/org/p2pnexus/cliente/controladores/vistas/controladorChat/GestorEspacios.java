package org.p2pnexus.cliente.controladores.vistas.controladorChat;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.p2pnexus.cliente.controladores.componentes.ControladorTarjetaEspacioEnviada;
import org.p2pnexus.cliente.controladores.componentes.ControladorTarjetaEspacioRecibida;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.sesion.datos.datosEspecificos.DatosPaqueteEspaciosCompartidos;
import org.p2pnexus.cliente.ventanas.Componentes;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Notificaciones;

import java.io.IOException;

public class GestorEspacios {
    ControladorChat controladorChat;

    GestorEspacios(ControladorChat controladorChat)
    {
        this.controladorChat = controladorChat;
    }

    public void compartirEspacioSeleccionado()
    {
        EspacioCompartido espacioCompartido = controladorChat.comboBoxSeleccionEspacio.getSelectionModel().getSelectedItem();
        if (espacioCompartido == null || espacioCompartido.getId_espacio() == -1) {
            Notificaciones.MostrarNotificacion("No se ha seleccionado ningún espacio", TipoNotificacion.ERROR);
            return;
        }
        JsonObject json = new JsonObject();
        json.add("espacio", JsonHerramientas.convertirObjetoAJson(espacioCompartido));
        json.add("conversacion", JsonHerramientas.convertirObjetoAJson(controladorChat.conversacionActual));
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_COMPARTIR_ESPACIO, json));
    }

    public void actualizarFiltroComboBox(Conversacion conversacion)
    {
        DatosPaqueteEspaciosCompartidos datosPaqueteEspaciosCompartidos = controladorChat.cacheDatosConversacion.get(conversacion.getIdConversacion()).getDatosPaqueteEspaciosCompartidos();
        Platform.runLater(() -> {
            FilteredList<EspacioCompartido> espaciosNoEnviados = datosPaqueteEspaciosCompartidos.getEspaciosNoEnviados();
            System.out.printf("Espacios no enviados: %s\n", espaciosNoEnviados);
            // Si no hay espacios disponibles se desactiva el comboBox y se muestra un mensaje
            if (espaciosNoEnviados.isEmpty()) {
                controladorChat.comboBoxSeleccionEspacio.setItems(FXCollections.observableArrayList());
                controladorChat.comboBoxSeleccionEspacio.setDisable(true);
                return;
            }
            // Si hay espacios se selecciona el primero y activa por si no lo estaba
            controladorChat.comboBoxSeleccionEspacio.setDisable(false);
            controladorChat.comboBoxSeleccionEspacio.setItems(espaciosNoEnviados);
            controladorChat.comboBoxSeleccionEspacio.getSelectionModel().selectFirst();
        });
        // Una vez ya está todo cargado se inicializan los listeners encargados de actualizar la vista
        cargarEspaciosDesdeCache();
    }

    public void nuevoEspacioRecibido(EspacioCompartido espacio, Conversacion conversacion)
    {
        controladorChat.cacheDatosConversacion.get(conversacion.getIdConversacion()).getDatosPaqueteEspaciosCompartidos().getRecibidos().add(espacio);
        Platform.runLater(() ->{
            crearVistaEspacioRecibido(espacio);
        });

    }

    void cargarEspaciosDesdeCache()
    {
        Platform.runLater(() -> {
            controladorChat.flowPaneEspaciosEnviados.getChildren().clear();
            for (EspacioCompartido espacio : controladorChat.datosConversacionActual.getDatosPaqueteEspaciosCompartidos().getEnviados())
            {
                if (espacio == null) {return;}
                crearVistaEspacioEnviado(Sesion.getDatosSesionUsuario().getEspacios().get(espacio.getId_espacio()));
            }
            controladorChat.flowPlaneEspaciosRecibidos.getChildren().clear();
            for (EspacioCompartido espacio : controladorChat.datosConversacionActual.getDatosPaqueteEspaciosCompartidos().getRecibidos())
            {
                if (espacio == null) {return;}
                System.out.printf("Id de espacio recibido: %s\n", espacio.getId_espacio());
                crearVistaEspacioRecibido(espacio);
            }
        });
    }

    void crearVistaEspacioEnviado(EspacioCompartido espacio)
    {
        try {
            FXMLLoader loader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TARJETA_ESPACIO_COMPARTIDO_ENVIADO);
            Parent parent = loader.load();
            ControladorTarjetaEspacioEnviada controlador = loader.getController();
            controlador.inicializarTarjetaEspacio(espacio,controladorChat.conversacionActual);
            controladorChat.flowPaneEspaciosEnviados.getChildren().add(parent);
            controladorChat.tarjetasEspaciosEnviados.put(espacio, parent);
        } catch (IOException e) {
            System.out.println("Error al cargar la vista de espacio");
        }
    }

    void crearVistaEspacioRecibido(EspacioCompartido espacio)
    {
        try {
            FXMLLoader loader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TARJETA_ESPACIO_COMPARTIDO_RECIBIDO);
            Parent parent = loader.load();
            ControladorTarjetaEspacioRecibida controlador = loader.getController();
            controlador.inicializarTarjetaEspacio(espacio,controladorChat.conversacionActual);
            controladorChat.flowPlaneEspaciosRecibidos.getChildren().add(parent);
            controladorChat.tarjetasEspaciosRecibidos.put(espacio, parent);
        } catch (IOException e) {
            System.out.println("Error al cargar la vista de espacio");
            e.printStackTrace();
        }
    }

    public void eliminarEspacioEnviado(EspacioCompartido espacio)
    {
        Parent parent = controladorChat.tarjetasEspaciosEnviados.get(espacio);
        if (parent != null)
        {
            controladorChat.flowPaneEspaciosEnviados.getChildren().remove(parent);
            controladorChat.tarjetasEspaciosEnviados.remove(espacio);
            controladorChat.datosConversacionActual.getDatosPaqueteEspaciosCompartidos().getEnviados().remove(espacio);
        }
    }

    public void eliminarEspacioRecibido(EspacioCompartido espacio, Conversacion conversacion)
    {
        Platform.runLater(() ->{
            Platform.runLater(() ->
            {
                Parent parent = controladorChat.tarjetasEspaciosRecibidos.get(espacio);
                // en caso de que las tarjetas estén en pantalla
                if (parent != null)
                {
                    controladorChat.flowPlaneEspaciosRecibidos.getChildren().remove(parent);
                    controladorChat.tarjetasEspaciosRecibidos.remove(espacio);
                }
                controladorChat.cacheDatosConversacion.get(conversacion.getIdConversacion()).getDatosPaqueteEspaciosCompartidos().getRecibidos().remove(espacio);

                });
            });
        
    }

    public void actualizarEspacioRecibido(EspacioCompartido espacio,Conversacion conversacion)
    {
        Platform.runLater(() ->
        {
            System.out.print("Intentando actualizar el espacio " + espacio.getId_espacio() + " en la conversacion " + conversacion.getIdConversacion());
            System.out.printf("Espacios en la conversacion: %s\n", controladorChat.cacheDatosConversacion.get(conversacion.getIdConversacion()).getDatosPaqueteEspaciosCompartidos().getRecibidos());
            ObservableList<EspacioCompartido> recibidos = controladorChat.cacheDatosConversacion.get(conversacion.getIdConversacion()).getDatosPaqueteEspaciosCompartidos().getRecibidos();

            EspacioCompartido espacioEditar = recibidos.get(recibidos.indexOf(espacio));

            // si no está cargada la conversacion con ese usuario no hacemos nada
            if (espacioEditar == null)
            {
                System.out.println("No se ha encontrado el espacio en la conversacion");
                return;
            }

            espacioEditar.setNombre(espacio.getNombrePropiedadProperty().get());
            espacioEditar.setRuta_directorio(espacio.getRutaPropiedadProperty().get());
        });

    }

}
