package org.p2pnexus.cliente.controladores.vistasModales;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import org.p2pnexus.cliente.p2p.manejador.manejadores.descarga.EstadoDescarga;

public class ControladorProcesoDescarga {
    @FXML
    Label lblEstado;

    @FXML
    Label lblTitulo;

    @FXML
    ProgressBar pbProgreso;

    int progresoActual = 0;

    EstadoDescarga estadoDescarga;


    public void inicializarValores(EstadoDescarga estadoDescarga)
    {
        this.estadoDescarga = estadoDescarga;
        this.lblTitulo.setText("Descargando...");
    }

    public void actualizarProgreso() {
        this.progresoActual = (int) ((estadoDescarga.pesoActualDescargado * 100) / estadoDescarga.totalPeso); // normalizamos el progreso sobre el total
        this.pbProgreso.setProgress(progresoActual / 100.0); // como el progress bar espera un valor entre 0 y 1, lo dividimos entre 100
        this.lblEstado.setText(estadoDescarga.pesoActualDescargado + " de " + estadoDescarga.totalPeso + " bytes descargados" + " (" + progresoActual + "%)");
    }

    public void cerrarVentana() {
         ((Stage) lblEstado.getScene().getWindow()).close();
    }







}
