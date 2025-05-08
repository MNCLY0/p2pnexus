package org.p2pnexus.cliente.controladores.vistas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2RoundAL;
import org.p2pnexus.cliente.controladores.componentes.tabMenu.ControladorTabMenu;
import org.p2pnexus.cliente.controladores.componentes.tabMenu.TabMenu;
import org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable.ControladorTarjetaContacto;
import org.p2pnexus.cliente.controladores.vistas.controladorChat.ControladorChat;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.ventanas.Componentes;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Notificaciones;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorMenuPrincipal {
    @FXML
    public VBox vboxSecciones;

    @FXML
    public VBox vboxContactos;

    @FXML
    ArrayList<TabMenu> tabsMenu;

    @FXML
    ImageView imagenTest;

    @FXML
    Tab tabChat = null;

    ControladorChat controladorChat = null;

    @FXML
    public TabPane tabPanePrincipal;

    public static ControladorMenuPrincipal instancia;

    Map<Usuario, ControladorTarjetaContacto> controladoresTarjetaContacto = new HashMap<>();


    @FXML
    public void initialize() {

        instancia = this;

        inializarTabs();

        solicitarContactos();

        // Necesitamos crear un listener que actualice el estado de los tabs, para ver cual esta activo
        tabPanePrincipal.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            actualizarEstadoTabs(newTab);
        });

    }

    public Parent inicializarTabPane(TabMenu tabMenu) throws IOException
    {
        Parent root = null;

        try {
            FXMLLoader loader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TAB_MENU);
            root = loader.load();
            ControladorTabMenu controladorTabMenu = loader.getController();
            controladorTabMenu.establecerDatos(tabMenu);

            if (tabMenu.getVentana() != null)
            {
                Tab tab = new Tab(tabMenu.getNombre(), GestorVentanas.crearVentana(tabMenu.getVentana()));
                tabPanePrincipal.getTabs().add(tab);
                tabMenu.setAccion(tab, tabPanePrincipal, tabMenu);
                tabMenu.setControladorTabMenu(controladorTabMenu);
            }
        }catch (Exception e )
        {
            System.out.println("Error al cargar el tab menu: " + e.getMessage());
        }

        return root;
    }

    public void solicitarContactos()
    {
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario", Sesion.getUsuario().getId_usuario());
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.C_LISTA_CONTACTOS, json));
    }

    public void actualizarListaContactos(List<Usuario> usuariosContacto)
    {
        System.out.println("se procede a actualizar la lista de contactos con " + usuariosContacto.size() + " contactos");
        Platform.runLater(() -> {
            vboxContactos.getChildren().clear();
            for (Usuario usuario : usuariosContacto)
            {
                try {
                    FXMLLoader loader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TARJETA_CONTACTO);
                    Parent parent = loader.load();
                    ControladorTarjetaContacto controladorTarjetaContacto = loader.getController();
                    controladorTarjetaContacto.inicializarConUsuario(usuario);
                    controladoresTarjetaContacto.put(usuario, controladorTarjetaContacto);
                    vboxContactos.getChildren().add(parent);
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void actualizarEstadoTabs(Tab tab) {
        for (TabMenu tabMenu : tabsMenu) {
            if (tabMenu.getControladorTabMenu() != null) {
                boolean esTabSeleccionado = tabMenu.getAccion().getTab() == tab;
                if (esTabSeleccionado) {
                    tabMenu.getControladorTabMenu().setSeleccionado(true);
                }
            }
        }
    }

    public void abrirConversacion(Usuario usuario, Conversacion conversacion) {
        if (tabChat == null) {
            inicializarTabChat();
        }

        controladorChat.abrirChat(usuario,conversacion);
        tabPanePrincipal.getSelectionModel().select(tabChat);
    }

    public void inializarTabs()
    {
        tabsMenu = new ArrayList<>(
                List.of(
                        new TabMenu("Solicitudes", new FontIcon(Material2MZ.PERSON), Ventanas.TAB_SOLICITUDES),
                        new TabMenu("Espacios", new FontIcon(Material2RoundAL.CREATE_NEW_FOLDER), Ventanas.ESPACIOS)
                )
        );

        boolean primero = true;

        for (TabMenu tab : tabsMenu) {
            try {
                Parent root = inicializarTabPane(tab);
                vboxSecciones.getChildren().add(root);
                // Si es el primero, lo seleccionamos
                if (primero) {
                    tab.getControladorTabMenu().setSeleccionado(true);
                    primero = false;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Por como está montado merece la pena no inicializar el chat hasta que se necesite, luego podemos reutilizarlo para todos los usuarios
    public void inicializarTabChat()
    {
        Tab chat = new Tab("Chat");
        try {
            FXMLLoader loader = GestorVentanas.crearFXMLoader(Ventanas.TAB_CHAT);
            Parent root = loader.load();
            controladorChat = loader.getController();
            chat.setContent(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tabChat = chat;
        tabPanePrincipal.getTabs().add(tabChat);
    }

    public Map<Usuario, ControladorTarjetaContacto> getControladoresTarjetaContacto() {
        return controladoresTarjetaContacto;
    }

    @FXML
    void solicitarEstablecerImagenTest()
    {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Establece la ruta de la imagen");
            dialog.setHeaderText("Introduce un enlace directo a la imagen");
            dialog.setContentText("Ruta de la imagen:");
            dialog.setGraphic(new FontIcon(Material2AL.FILE_UPLOAD));
            dialog.showAndWait().ifPresent(ruta ->
                {
                    if (ruta.isEmpty()) return;
                    Platform.runLater(() -> {
                        Image imagen = new Image(GestorVentanas.transformarDriveURL(ruta));
                        imagenTest.setImage(imagen);
                        if (imagenTest.getImage() != null) {
                            System.out.printf("Imagen establecida: %s%n", ruta);
                            System.out.print("Dimensiones de la imagen: " + imagen.getWidth() + ":" + imagen.getHeight());
                            if (imagen.getWidth() == 0 || imagen.getHeight() == 0) {
                                Notificaciones.MostrarNotificacion("Error al cargar la imagen", TipoNotificacion.ERROR);
                            }
                        } else {
                            System.out.println("No se ha podido establecer la imagen");
                        }
                    });
                }
            );
    }
}
