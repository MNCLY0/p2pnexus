package org.p2pnexus.cliente.controladores.vistasModales;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.p2pnexus.cliente.controladores.vistas.ControladorMenuPrincipal;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.ventanas.Animaciones;
import org.p2pnexus.cliente.ventanas.Notificaciones;


public class ControladorEstablecerImagenPerfil {
    @FXML
    ImageView imageViewImagenPerfil;

    @FXML
    TextField textLink;

    @FXML
    Button btnCargar;

    @FXML
    Button bntGuardar;

    @FXML
    ProgressIndicator progressCargandoImagen;

    String enlace = "";

    Image imagenPerfil = null;

    @FXML
    public void cargarImagen()
    {
        // cargamos la imagen desde un hilo para evitar que la interfaz se congele y tener control sobre la carga
            new Thread(() -> {
                bntGuardar.setDisable(true);
                textLink.setPromptText("");

                if (textLink.getText().isEmpty())
                {
                    Platform.runLater(() -> {
                        Animaciones.animarError(textLink);
                        textLink.clear();
                        textLink.setPromptText("El enlace no puede estar vacío");
                    });
                    return;
                }

                if (!(textLink.getText().contains("drive.google.com/file/d/")))
                {
                    Platform.runLater(() -> {
                        Animaciones.animarError(textLink);
                        textLink.clear();
                        textLink.setPromptText("Enlace no válido");
                    });
                    return;
                }
                imageViewImagenPerfil.setImage(null);
                progressCargandoImagen.setVisible(true);
                enlace = transformarDriveURL(textLink.getText());
                imagenPerfil = new Image(enlace);
                imageViewImagenPerfil.setImage(imagenPerfil);
                progressCargandoImagen.setVisible(false);
                bntGuardar.setDisable(false);
            }).start();
    }

    public String transformarDriveURL(String enlace) {
        if (enlace.contains("drive.google.com/file/d/")) {
            String id = enlace.split("/d/")[1].split("/")[0];
            return "https://drive.google.com/uc?export=view&id=" + id;
        }
        return enlace;
    }

    @FXML
    public void guardarImagenPerfil()
    {
        if (enlace.isEmpty())
        {
            // esto no deberia pasar porque el boton solo se desbloquea cuando hay un enlace correcto cargado,
            // pero por si acaso
            Animaciones.animarError(imageViewImagenPerfil);
            return;
        }

        JsonObject json = new JsonObject();
        json.addProperty("enlace", enlace);
        Sesion.gestionImagenes.cacheImagenes.put(enlace, imagenPerfil);
        Sesion.getUsuario().setImagen_perfil(enlace);
        Sesion.getUsuario().setImagen(imagenPerfil);
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.S_GUARDAR_IMAGEN_PERFIL,json));

        Notificaciones.mostrarNotificacion("Imagen de perfil actualizada", TipoNotificacion.EXITO);
        // cerramos la ventana
        ControladorMenuPrincipal.instancia.actualizarImagenMenuPerfil();
        ((Stage) bntGuardar.getScene().getWindow()).close();

    }



}
