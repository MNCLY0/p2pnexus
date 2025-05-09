package org.p2pnexus.cliente.controladores.vistas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable.ControladorTarjetaContactoSolicitable;
import org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable.ControladorTarjetaSolicitudContacto;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.SolicitudContacto;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.ventanas.Componentes;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Notificaciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControladorSolicitudes {

    @FXML
    public TextField campobuscar;
    @FXML
    public Button botonbuscar;
    @FXML
    public VBox vboxResultadosUsuarios;

    @FXML
    public VBox vboxResultadosSolicitudes;

    public List<Usuario> resultadosBusquedaUsuario = new ArrayList<>();
    public List<SolicitudContacto> resultadosBusquedaSolicitudes = new ArrayList<>();

    public static ControladorSolicitudes instancia;

    @FXML
    public void initialize()
    {
        instancia = this;
        Platform.runLater(() ->
        {
            solicitarActualizacionSolicitudes(false);
        });
    }

    @FXML
    public void solicitarBusquedaUsuarios()
    {
        if (campobuscar.getText().isEmpty())
        {
            Notificaciones.mostrarNotificacion("Campo de búsqueda vacío", TipoNotificacion.AVISO);
            vboxResultadosUsuarios.getChildren().clear();
            return;
        }

        JsonObject json = new JsonObject();
        json.addProperty("nombre", campobuscar.getText());
        json.addProperty("id_usuario_origen", Sesion.getUsuario().getId_usuario());
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_BUSCAR_USUARIOS_POR_NOMBRE, json));
    }

    @FXML
    public void solicitarActualizacionSolicitudes(boolean notificable)
    {
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario", Sesion.getUsuario().getId_usuario());
        json.addProperty("notificable", notificable);
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_SOLICITUDES_POR_ID,json));
    }

    public void solicitarActualizacionSolicitudes()
    {
        solicitarActualizacionSolicitudes(true);
    }

    public void agregarUsuarios(List<Usuario> usuarios) {
        resultadosBusquedaUsuario = usuarios;
        Platform.runLater(() -> {
            vboxResultadosUsuarios.getChildren().clear();
            for (Usuario usuario : resultadosBusquedaUsuario) {
                System.out.println("Agregando usuario " + usuario.getNombre());
                agregarResultadoAVboxUsuarios(usuario);
            }
        });
    }

    public void agregarSolicitudes(List<SolicitudContacto> solicitudContactos) {
        resultadosBusquedaSolicitudes = solicitudContactos;
        Platform.runLater(() -> {
            vboxResultadosSolicitudes.getChildren().clear();
            for (SolicitudContacto solicitud : resultadosBusquedaSolicitudes) {
                agregarResultadoAVboxSolicitudes(solicitud);
            }
        });

    }

    public void agregarResultadoAVboxUsuarios(Usuario usuario)
    {
            try {
                FXMLLoader fxmlLoader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TARJETA_CONTACTO_SOLICITABLE);
                Parent parent = fxmlLoader.load();
                ControladorTarjetaContactoSolicitable controlador = fxmlLoader.getController();
                controlador.inicializarConUsuario(usuario);
                vboxResultadosUsuarios.getChildren().add(parent);
            }catch (IOException e)
            {
                System.out.println("Error al cargar la tarjeta del usuario " + usuario.getNombre());
            }

    }

    public void agregarResultadoAVboxSolicitudes(SolicitudContacto solicitud)
    {
            try {
                FXMLLoader fxmlLoader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TARJETA_SOLICITUD);
                Parent parent = fxmlLoader.load();
                ControladorTarjetaSolicitudContacto controlador = fxmlLoader.getController();
                controlador.inicializarConSolicitud(solicitud);
                vboxResultadosSolicitudes.getChildren().add(parent);
            }catch (IOException e)
            {
                System.out.println("Error al cargar la solicitud de " + solicitud.getUsuarioOrigen().getNombre());
            }
    }
}
