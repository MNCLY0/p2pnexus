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

public class ControladorMenuPrincipal {
    @FXML
    public VBox vboxSecciones;

    @FXML
    ArrayList<TabMenu> tabsMenu;

    @FXML
    public TabPane tabPanePrincipal;

    @FXML
    public void initialize() {


        inializarTabs();
        for (TabMenu tab : tabsMenu) {
            try {
                Parent root = inicializarTabPane(tab);
                vboxSecciones.getChildren().add(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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

                tabMenu.setAccion(new AccionTab(tab, tabPanePrincipal));
            }
        }catch (Exception e )
        {
            System.out.println("Error al cargar el tab menu: " + e.getMessage());

        }


        return root;
    }


    public void inializarTabs()
    {
        tabsMenu = new ArrayList<>(
                List.of(
                        new TabMenu("Solicitudes", new FontIcon(Material2MZ.PERSON), null, Ventanas.TAB_SOLICITUDES)
//                        new TabMenu("Espacios", new FontIcon(Material2RoundAL.CREATE_NEW_FOLDER), null, null)
                )
        );
    }
}
