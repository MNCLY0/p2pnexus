package org.p2pnexus.cliente.controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2RoundAL;
import org.p2pnexus.cliente.controllers.componentes.tabMenu.ControladorTabMenu;
import org.p2pnexus.cliente.controllers.componentes.tabMenu.TabMenu;
import org.p2pnexus.cliente.controllers.componentes.tabMenu.tabacciones.AccionTab;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControladorMenuPrincipal {
    @FXML
    public VBox vboxSecciones;

    @FXML
    ArrayList<TabMenu> tabsMenu;

    @FXML
    public TabPane tabPanePrincipal;

    @FXML
    public void initialize() {

        boolean primero = true;

        inializarTabs();
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

        // Necesitamos crear un listener que actualice el estado de los tabs, para ver cual esta activo
        tabPanePrincipal.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            actualizarEstadoTabs(newTab);
        });

    }

    public Parent inicializarTabPane(TabMenu tabMenu) throws IOException
    {
        Parent root = null;

        try {
            FXMLLoader loader = GestorVentanas.crearFXMLoader(Ventanas.TAB_MENU);
            root = loader.load();
            ControladorTabMenu controladorTabMenu = loader.getController();
            controladorTabMenu.establecerDatos(tabMenu);

            if (tabMenu.getVentana() != null)
            {
                Tab tab = new Tab(tabMenu.getNombre(), GestorVentanas.crearVentana(tabMenu.getVentana()));
                tabPanePrincipal.getTabs().add(tab);
                tabMenu.setAccion(tab, tabPanePrincipal);
                tabMenu.setControladorTabMenu(controladorTabMenu);
            }
        }catch (Exception e )
        {
            System.out.println("Error al cargar el tab menu: " + e.getMessage());
        }

        return root;
    }

    public void actualizarEstadoTabs(Tab tab)
    {
        for (TabMenu tabMenu : tabsMenu)
        {
            tabMenu.getControladorTabMenu().setSeleccionado(tabMenu.getAccion().getTab() == tab);
        }
    }

    public void inializarTabs()
    {
        tabsMenu = new ArrayList<>(
                List.of(
                        new TabMenu("Solicitudes", new FontIcon(Material2MZ.PERSON), Ventanas.TAB_SOLICITUDES),
                        new TabMenu("Chat", new FontIcon(Material2MZ.PERSON), Ventanas.TAB_CHAT)
//                        new TabMenu("Espacios", new FontIcon(Material2RoundAL.CREATE_NEW_FOLDER), null, null)
                )
        );
    }
}
