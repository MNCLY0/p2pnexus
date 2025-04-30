package org.p2pnexus.cliente.controladores.componentes.tarjetaContactoSolicitable;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2RoundAL;
import org.p2pnexus.cliente.server.entitades.SolicitudContacto;
import org.p2pnexus.cliente.server.entitades.Usuario;

public class ControladorTarjetaSolicitudContacto {


    @FXML
    public Label labelNombre;

    @FXML
    public VBox contenedorIconoAceptar;

    @FXML
    public VBox contenedorIconoDenegar;

    SolicitudContacto solicitudContacto;

    public void inicializarConSolicitud(SolicitudContacto solicitudContacto)
    {
        this.solicitudContacto = solicitudContacto;
        labelNombre.setText(solicitudContacto.getUsuarioOrigen().getNombre());
        inicializarIconos();
    }

    public void inicializarIconos()
    {
        FontIcon iconoAceptar = new FontIcon(Material2RoundAL.ADD_CIRCLE);
        FontIcon iconoDenegar = new FontIcon(Material2RoundAL.CANCEL);

        contenedorIconoAceptar.getChildren().add(iconoAceptar);
        contenedorIconoDenegar.getChildren().add(iconoDenegar);

    }

}
