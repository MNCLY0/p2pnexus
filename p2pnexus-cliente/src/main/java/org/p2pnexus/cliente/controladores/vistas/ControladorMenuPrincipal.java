package org.p2pnexus.cliente.controladores.vistas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2RoundAL;
import org.p2pnexus.cliente.controladores.componentes.tabMenu.ControladorTabMenu;
import org.p2pnexus.cliente.controladores.componentes.tabMenu.TabMenu;
import org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable.ControladorTarjetaContacto;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.ventanas.Componentes;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControladorMenuPrincipal {
    @FXML
    public VBox vboxSecciones;

    @FXML
    public VBox vboxContactos;

    @FXML
    ArrayList<TabMenu> tabsMenu;

    @FXML
    public TabPane tabPanePrincipal;

    public static ControladorMenuPrincipal controladorMenuPrincipalActual;


    @FXML
    public void initialize() {

        controladorMenuPrincipalActual = this;

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
        Platform.runLater(() -> {
            vboxContactos.getChildren().clear();
            for (Usuario usuario : usuariosContacto)
            {
                try {
                    FXMLLoader loader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TARJETA_CONTACTO);
                    Parent parent = loader.load();
                    ControladorTarjetaContacto controladorTarjetaContacto = loader.getController();
                    controladorTarjetaContacto.inicializarConUsuario(usuario);
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

    public void abrirChatConUsuario(Usuario usuario) {

    }

    public void inializarTabs()
    {
        tabsMenu = new ArrayList<>(
                List.of(
                        new TabMenu("Solicitudes", new FontIcon(Material2MZ.PERSON), Ventanas.TAB_SOLICITUDES),
                        new TabMenu("Espacios", new FontIcon(Material2RoundAL.CREATE_NEW_FOLDER), Ventanas.TAB_CHAT)
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
}
