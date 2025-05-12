package org.p2pnexus.cliente.controladores.vistasModales;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.p2pnexus.cliente.p2p.Fichero;
import org.p2pnexus.cliente.p2p.conexion.GestorP2P;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ControladorCargarEspacio {
    @FXML
    Label labelRuta;

    EspacioCompartido espacioCompartido;

    GestorP2P gestorP2P;

    public static ControladorCargarEspacio instancia;

    boolean cargados = false;
    boolean inaccesible = false;

    @FXML
    public void initialize() {
        instancia = this;
    }

    public void inicializarConEspacio(EspacioCompartido espacioCompartido) {
        labelRuta.textProperty().bind(espacioCompartido.getRutaPropiedadProperty());
        this.espacioCompartido = espacioCompartido;

        gestorP2P = new GestorP2P();
        gestorP2P.hacerOferta(espacioCompartido.getPropietario());

        esperar();
    }

    public void esperar()
    {
        new Thread(() -> {
           int intentos = 0;
           int maxIntentos = 20;
           int intervalo = 500;

           while (intentos < maxIntentos && !cargados ) {
                if (gestorP2P.canalAbierto())
                {
                    System.out.println("Canal abierto");
                    solicitarInfoEspacio();
                    break;
                }
                else
                {
                    System.out.println("Esperando canal...");
                    try {
                        Thread.sleep(intervalo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    intentos++;
                }
           }
        }).start();
    }

    public void establecerComoInaccesible()
    {
        cargados = true;
        inaccesible = true;
        cargarDatos(null);
    }

    public void solicitarInfoEspacio()
    {
        JsonObject json = new JsonObject();
        json.add("espacio", JsonHerramientas.convertirObjetoAJson(espacioCompartido));
        gestorP2P.manejador.enviarMensaje(new Mensaje(TipoMensaje.P2P_S_INFO_RUTA,json));
    }

    public void cargarDatos(List<Fichero> ficheros)
    {
        if (inaccesible)
        {
            Platform.runLater(() -> {
                // Si no es accesible se cierra la ventana de carga de conexion
                ((Stage)(labelRuta.getScene().getWindow())).close();
            });

            return;
        }
        Platform.runLater(() -> {
            System.out.println("Archivos recibidos con exito");
            Stage stage = (Stage) labelRuta.getScene().getWindow();
            try {
                FXMLLoader loader = GestorVentanas.crearFXMLoader(Ventanas.MODAL_VISUALIZAR_ESPACIO);
                Parent root = loader.load();
                ControladorVisualizarEspacio controlador = loader.getController();
                controlador.inicializarConEspacio(espacioCompartido, ficheros);
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                stage.close();
                throw new RuntimeException(e);
            }
        });
    }
}
