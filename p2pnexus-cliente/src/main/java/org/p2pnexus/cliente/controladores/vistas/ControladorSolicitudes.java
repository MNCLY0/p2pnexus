package org.p2pnexus.cliente.controladores.vistas;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
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
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.ventanas.Componentes;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Notificaciones;

import java.io.IOException;
import java.util.List;

public class ControladorSolicitudes {

    @FXML
    public TextField campobuscar;
    @FXML
    public Button botonbuscar;
    @FXML
    public VBox vboxResultados;

    public List<Usuario> resultadosBusqueda = null;

    public static ControladorSolicitudes controladorSolicitudesActual;

    @FXML
    public void initialize()
    {
        controladorSolicitudesActual = this;
    }

    @FXML
    public void solicitarBusqueda()
    {
        if (campobuscar.getText().isEmpty())
        {
            Notificaciones.MostrarNotificacion("Campo de búsqueda vacío", TipoNotificacion.AVISO);
            return;
        }

        JsonObject json = new JsonObject();
        json.addProperty("nombre", campobuscar.getText());
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_BUSCAR_USUARIOS_POR_NOMBRE, json));
    }

    public void agregarResultado(JsonObject json)
    {
        // Limpiamos la lista de resultados porque vamos a meter nuevos
        System.out.println("Intentando agregar resultados, json: " + json);
        System.out.println("Convertimos el json a una lista de usuarios");
        JsonElement usuariosElement = json.get("usuarios");
        System.out.println("Convertidos correctamente : " + usuariosElement);
        System.out.println("Intentando convertir el json a una lista de usuarios");
        JsonArray usuariosArray = usuariosElement.getAsJsonArray();
        System.out.println("Convertido correctamente a un JsonArray" + usuariosArray);

        Platform.runLater(() -> vboxResultados.getChildren().clear());

        resultadosBusqueda = JsonHerramientas.convertirJsonArrayALista(usuariosArray, Usuario.class);

        if (resultadosBusqueda == null )
        {
            System.out.println("No se ha podido convertir el json a una lista de usuarios");
            return;
        }

        System.out.println("Se van a agregar " + resultadosBusqueda.size() + " resultados");
        for (Usuario usuario : resultadosBusqueda)
        {
            agregarResultadoAVbox(usuario);
        }

    }

    public void agregarResultadoAVbox(Usuario usuario)
    {
        Platform.runLater(() -> {;
            try {
                FXMLLoader fxmlLoader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TARJETA_CONTACTO_SOLICITABLE);
                Parent parent = fxmlLoader.load();
                ControladorTarjetaContactoSolicitable controlador = fxmlLoader.getController();

                controlador.inicializarConUsuario(usuario);

                vboxResultados.getChildren().add(parent);

            }catch (IOException e)
            {
                System.out.println("Error al cargar la tarjeta del usuario " + usuario.getNombre());
            }

        });

    }
}
