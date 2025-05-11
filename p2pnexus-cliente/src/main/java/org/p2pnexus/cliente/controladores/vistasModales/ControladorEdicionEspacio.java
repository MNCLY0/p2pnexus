package org.p2pnexus.cliente.controladores.vistasModales;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.p2pnexus.cliente.controladores.vistas.ControladorEspacios;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Notificaciones;

import java.io.File;

public class ControladorEdicionEspacio {
    @FXML
    TextField textFieldNombre;
    @FXML
    TextField textFieldRuta;
    @FXML
    Button botonSeleccionarRuta;

    @FXML
    GridPane gridPanePrincipal;

    @FXML
    StackPane stackPanePrincipal;

    EspacioCompartido espacioCompartidoOriginal;


    @FXML
    public void seleccionarRuta()
    {
       DirectoryChooser directoryChooser = new DirectoryChooser();
       directoryChooser.setTitle("Seleccionar ruta");
       File rutaSeleccionada = directoryChooser.showDialog(botonSeleccionarRuta.getScene().getWindow());
         if (rutaSeleccionada != null) {
              textFieldRuta.setText(rutaSeleccionada.getAbsolutePath());
         }
    }

    public void inicializarEdicion(EspacioCompartido espacioCompartido)
    {
        this.espacioCompartidoOriginal = espacioCompartido;
        textFieldNombre.setText(espacioCompartido.getNombrePropiedadProperty().get());
        textFieldRuta.setText(espacioCompartido.getRutaPropiedadProperty().get());
    }

    @FXML
    public void eliminarEspacio()
    {
        boolean confirmacion = GestorVentanas.pedirConfirmacion(
                "Eliminar espacio",
                "¿Estás seguro de que deseas eliminar el espacio \"" +
                        espacioCompartidoOriginal.getNombrePropiedadProperty().get() + "\"?\n\n" +
                        "Esta acción eliminará permanentemente el espacio de tu lista y dejará de estar compartido con los usuarios que tenían acceso.",
                Alert.AlertType.ERROR,
                textFieldRuta.getScene()
        );

        if (!confirmacion) {return;}
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.S_ELIMINAR_ESPACIO, JsonHerramientas.convertirObjetoAJson(espacioCompartidoOriginal)));
        System.out.println("Se va a tratar de eliminar el espacio: " + espacioCompartidoOriginal.getNombrePropiedadProperty().get() + "con id " + espacioCompartidoOriginal.getId_espacio());
        ControladorEspacios.instancia.eliminarTarjetaEspacio(espacioCompartidoOriginal);
        cerrarVentana();
    }

    @FXML
    public void editarEspacio()
    {
        String rutaOriginal = espacioCompartidoOriginal.getRutaPropiedadProperty().get();
        String rutaNueva = textFieldRuta.getText();
        String nombreOriginal = espacioCompartidoOriginal.getNombrePropiedadProperty().get();
        String nombreNuevo = textFieldNombre.getText();

        // Comprobamos si hay cambios
        if (rutaOriginal.equals(rutaNueva) && nombreOriginal.equals(nombreNuevo))
        {
            Notificaciones.mostrarNotificacion("No se han realizado cambios", TipoNotificacion.AVISO);
            cerrarVentana();
            return;
        }
        // Comprobamos si los campos están vacios
        if (rutaNueva.isEmpty() || nombreNuevo.isEmpty())
        {
            Notificaciones.mostrarNotificacion("No se han realizado cambios, ningún campo puede estar vacio", TipoNotificacion.AVISO);
            cerrarVentana();
            return;
        }

        EspacioCompartido espacioCompartidoModificado = new EspacioCompartido(nombreNuevo, rutaNueva, espacioCompartidoOriginal.getPropietario());
        espacioCompartidoModificado.setId_espacio(espacioCompartidoOriginal.getId_espacio());

        JsonObject json = new JsonObject();
        //Envaiamos los dos espacios por si ocurre un error devolver el original
        json.add("espacio_original", JsonHerramientas.convertirObjetoAJson(espacioCompartidoOriginal));
        json.add("espacio_modificado", JsonHerramientas.convertirObjetoAJson(espacioCompartidoModificado));

        Conexion.enviarMensaje(new Mensaje(TipoMensaje.S_EDITAR_ESPACIO, json));

        cerrarVentana();

    }

    public void cerrarVentana()
    {
        Stage stage = (Stage) botonSeleccionarRuta.getScene().getWindow();
        stage.close();
    }





}
