package org.p2pnexus.cliente.controladores.vistas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2RoundAL;
import org.p2pnexus.cliente.configuracion.Configuracion;
import org.p2pnexus.cliente.controladores.componentes.tabMenu.ControladorTabMenu;
import org.p2pnexus.cliente.controladores.componentes.tabMenu.TabMenu;
import org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable.ControladorTarjetaContacto;
import org.p2pnexus.cliente.controladores.vistas.controladorChat.ControladorChat;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.ventanas.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    @FXML
    VBox menuPerfil;
    @FXML
    HBox perfilBar;
    @FXML
    MediaView mediaViewtTransicion;
    @FXML
    VBox vboxMedia;

    ControladorChat controladorChat = null;

    @FXML
    public TabPane tabPanePrincipal;

    public static ControladorMenuPrincipal instancia;

    Map<Usuario, ControladorTarjetaContacto> controladoresTarjetaContacto = new HashMap<>();

    boolean transicionando = false;

    @FXML
    ImageView imageViewFotoUsuario;

    @FXML
    Label lblNombreUsuario;

    @FXML
    public void initialize() {

        instancia = this;
        Platform.runLater(() -> {
            inializarTabs();
            solicitarContactos();
        });

        // Necesitamos crear un listener que actualice el estado de los tabs, para ver cual esta activo
        tabPanePrincipal.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            actualizarEstadoTabs(newTab);
        });

        lblNombreUsuario.setText(Sesion.getUsuario().getNombre());
        actualizarImagenMenuPerfil();

        mediaViewtTransicion.fitHeightProperty().bind(vboxMedia.heightProperty());
        mediaViewtTransicion.fitWidthProperty().bind(vboxMedia.widthProperty());

    }


    public Parent inicializarTabPane(TabMenu tabMenu) throws IOException {

        Parent root = null;

        try {
            FXMLLoader loader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TAB_MENU);
            root = loader.load();
            ControladorTabMenu controladorTabMenu = loader.getController();
            controladorTabMenu.establecerDatos(tabMenu);

            if (tabMenu.getVentana() != null) {
                Tab tab = new Tab(tabMenu.getNombre(), GestorVentanas.crearVentana(tabMenu.getVentana()));
                tabPanePrincipal.getTabs().add(tab);
                tabMenu.setAccion(tab, tabPanePrincipal, tabMenu);
                tabMenu.setControladorTabMenu(controladorTabMenu);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar el tab menu: " + e.getMessage());
        }

        return root;
    }

    public void solicitarContactos() {
        JsonObject json = new JsonObject();
        json.addProperty("id_usuario", Sesion.getUsuario().getId_usuario());
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.S_LISTA_CONTACTOS, json));
    }

    public void actualizarListaContactos(List<Usuario> usuariosContacto) {
        System.out.println("se procede a actualizar la lista de contactos con " + usuariosContacto.size() + " contactos");
        Platform.runLater(() -> {
            vboxContactos.getChildren().clear();
            for (Usuario usuario : usuariosContacto) {
                try {
                    FXMLLoader loader = GestorVentanas.crearFXMLoader(Componentes.COMPONENTE_TARJETA_CONTACTO);
                    Parent parent = loader.load();
                    ControladorTarjetaContacto controladorTarjetaContacto = loader.getController();
                    controladorTarjetaContacto.inicializarConUsuario(usuario);
                    controladoresTarjetaContacto.put(usuario, controladorTarjetaContacto);
//                    Sesion.gestionImagenes.cacheImagenes.put(usuario.getImagen_perfil(), usuario.getImagen());
                    vboxContactos.getChildren().add(parent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
//            Sesion.gestionImagenes.cargarImagenes();
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

        controladorChat.abrirChat(usuario, conversacion);
        // solo si el usuario ha seleccionado la tarjeta se abre visualmente, lo he hecho de esta manera porque a veces queremos que el servidor
        // mande los datos de un chat pero que no se abra la ventana de este. De esta manera solo se abre si el usuario lo ha seleccionado en la interfaz
        if (controladoresTarjetaContacto.get(usuario).seleccionado()) {
            tabPanePrincipal.getSelectionModel().select(tabChat);
        }
    }

    public void inializarTabs() {
        Platform.runLater(() -> {

            tabsMenu = new ArrayList<>(
                    List.of(
                            new TabMenu("Solicitudes", new FontIcon(Material2MZ.PERSON), Ventanas.TAB_SOLICITUDES),
                            new TabMenu("Espacios", new FontIcon(Material2RoundAL.CREATE_NEW_FOLDER), Ventanas.ESPACIOS)
                    )
            );

            System.out.println("Ruta de los tabs inicializados: " + tabsMenu.get(0).getVentana().getRuta());
            System.out.println("Ruta de los tabs inicializados: " + tabsMenu.get(1).getVentana().getRuta());

            Platform.runLater(() -> {
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
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            });
        });
    }

    // Por como está montado merece la pena no inicializar el chat hasta que se necesite, luego podemos reutilizarlo para todos los usuarios
    public void inicializarTabChat() {
        Tab chat = new Tab("Chat");
        try {
            FXMLLoader loader = GestorVentanas.crearFXMLoader(Ventanas.TAB_CHAT);
            Parent root = loader.load();
            controladorChat = loader.getController();
            chat.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        tabChat = chat;
        tabPanePrincipal.getTabs().add(tabChat);
    }

    public Map<Usuario, ControladorTarjetaContacto> getControladoresTarjetaContacto() {
        return controladoresTarjetaContacto;
    }

    public void actualizarEstadoTarjetaContacto(Usuario usuario) {
        Platform.runLater(() -> {
            ControladorTarjetaContacto controladorTarjetaContacto = controladoresTarjetaContacto.get(usuario);
            System.out.printf("Estableciendo el estado de la tarjeta de contacto del usuario %s a %s%n", usuario.getNombre(), usuario.getConectado());
            System.out.printf("Existe la tarjeta? %s%n", controladorTarjetaContacto != null);
            if (controladorTarjetaContacto != null) {
                controladorTarjetaContacto.getUsuario().establecerConectado(usuario.getConectado());
                controladorTarjetaContacto.actualizarEstado();
            }
        });

    }

    @FXML
    public void cerrarSesion() {
        Sesion.cerrarSesion();
    }

    @FXML
    public void alternarTema() {
        if (transicionando) return;
        transicionando = true;
        Configuracion configuracion = new Configuracion();
        String modoActual = configuracion.getModoTema();
        vboxMedia.setVisible(true);
        vboxMedia.setManaged(true);

        Platform.runLater(() -> {
            URL rutavideo;
            if (modoActual.equals("nocturno")) {
                rutavideo = getClass().getResource("/org/p2pnexus/cliente/videos/transicion_tema.mp4");
                System.out.println("Mostrando video de oscuro a diurno");
            } else {
                rutavideo = getClass().getResource("/org/p2pnexus/cliente/videos/transicion_tema_reversa.mp4");
                System.out.println("Mostrando video de diurno a oscuro");
            }

            System.out.printf("Ruta del video: %s%n", rutavideo);

            System.out.printf("Intentando cargar el video de transicion: %s%n", rutavideo);
            MediaPlayer player = new MediaPlayer(new Media(rutavideo.toExternalForm()));
            System.out.printf("Video cargado: %s%n", player.getMedia().getSource());
            mediaViewtTransicion.setMediaPlayer(player);
            System.out.printf("Player asignado: %s%n", player);
            mediaViewtTransicion.setPreserveRatio(false);
            player.play();
            System.out.printf("Video iniciado: %s%n", player.getMedia().getSource());
            new Thread(() -> {
                try {
                    Thread.sleep(1800);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                configuracion.alternarModoTema();
                vboxMedia.setVisible(false);
                vboxMedia.setManaged(false);
                player.dispose();
                mediaViewtTransicion.setMediaPlayer(null);
                transicionando = false;
            }).start();
        });
    }


    @FXML
    public void alternarMenuPerfil() {
        Platform.runLater(() -> {
            double alturaInicial = menuPerfil.getPrefHeight();
            double alturaFinal = menuPerfil.isVisible() ? 0 : 50 * menuPerfil.getChildren().size(); //50 por cada elemento
            Duration duracion = Duration.millis(80);

            if (!menuPerfil.isVisible()) {
                menuPerfil.setVisible(true);
                menuPerfil.setManaged(true);
            } else {
                menuPerfil.getChildren().forEach(tabmenu -> {
                    tabmenu.setVisible(false);
                    tabmenu.setManaged(false);
                });
            }
            // creamos la linea de tiempo para animar el menu
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(menuPerfil.prefHeightProperty(), alturaInicial),
                            new KeyValue(menuPerfil.minHeightProperty(), alturaInicial),
                            new KeyValue(menuPerfil.maxHeightProperty(), alturaInicial)
                    ),
                    new KeyFrame(duracion,
                            new KeyValue(menuPerfil.prefHeightProperty(), alturaFinal),
                            new KeyValue(menuPerfil.minHeightProperty(), alturaFinal),
                            new KeyValue(menuPerfil.maxHeightProperty(), alturaFinal)
                    )
            );

            timeline.setOnFinished(e -> {
                if (alturaFinal == 0) {
                    menuPerfil.setVisible(false);
                    menuPerfil.setManaged(false);
                }
                if (menuPerfil.isVisible()) {
                    // le damos una animacion de entrada a cada uno de los hijos para que de la sensacion de que se despliegan
                    // desde la izquierda, queda más dinamico
                    Animaciones.animarEntradaListaNodosIzqda(menuPerfil.getChildren(), 200, 20);
                }
            });

            timeline.play();
        });
    }

    @FXML
    public void abrirCambioImagen() {
        try {
            Parent root = GestorVentanas.crearFXMLoader(Ventanas.MODAL_ESTABLECER_IMAGEN_PERFIL).load();
            GestorVentanas.abrirModal(root, "Establecer imagen de perfil", false);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar la ventana de establecer imagen de perfil: " + e.getMessage());
        }
    }

    public void actualizarImagenMenuPerfil() {
        Platform.runLater(() -> {
            imageViewFotoUsuario.setImage(Sesion.getUsuario().getImagen());
        });

    }

}
