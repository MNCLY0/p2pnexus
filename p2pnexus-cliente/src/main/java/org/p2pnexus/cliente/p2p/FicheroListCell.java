package org.p2pnexus.cliente.p2p;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.p2pnexus.cliente.p2p.conexion.GestorP2P;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.ventanas.Notificaciones;

public class FicheroListCell extends ListCell<Fichero> {
    private final HBox contenido = new HBox();
    private final Label nombreLabel = new Label();
    private final Button botonDescargar = new Button("Descargar");
    private final Region espaciador = new Region();

    public FicheroListCell() {
        HBox.setHgrow(espaciador, Priority.ALWAYS);
        contenido.getChildren().addAll(nombreLabel, espaciador, botonDescargar);
        contenido.setAlignment(Pos.CENTER_LEFT);
        contenido.setSpacing(10);
        contenido.setPadding(new Insets(6));
        nombreLabel.textOverrunProperty().set(OverrunStyle.CENTER_WORD_ELLIPSIS);
        nombreLabel.setMaxWidth(400);
    }

    @Override
    protected void updateItem(Fichero fichero, boolean empty) {
        super.updateItem(fichero, empty);

        if (empty || fichero == null) {
            setGraphic(null);
        } else {
            nombreLabel.setText(fichero.nombre + " (" + fichero.size + ")");
            botonDescargar.setOnAction(e -> {
                // Deshabilitar el botón mientras se procesa la descarga
                botonDescargar.setDisable(true);
                botonDescargar.setText("Descargando...");

                JsonObject json = new JsonObject();
                json.addProperty("nombre", fichero.nombre);
                json.addProperty("ruta", fichero.ruta);
                json.add("solicitante", JsonHerramientas.convertirObjetoAJson(Sesion.getUsuario()));

                GestorP2P gestor = GestorP2P.conexiones.get(fichero.usuarioOrigen.getId_usuario());
                if (gestor == null) {
                    Notificaciones.mostrarNotificacion("No hay conexión con: " +
                            fichero.usuarioOrigen.getNombre(), TipoNotificacion.ERROR);
                    botonDescargar.setDisable(false);
                    botonDescargar.setText("Descargar");
                    return;
                }

                // Verificar que el canal está abierto
                if (!gestor.canalAbierto()) {
                    Notificaciones.mostrarNotificacion("Canal no disponible. Reintente.",
                            TipoNotificacion.ERROR);
                    botonDescargar.setDisable(false);
                    botonDescargar.setText("Descargar");
                    return;
                }

                // Enviar solicitud
                gestor.manejador.enviarMensaje(new Mensaje(TipoMensaje.P2P_S_DESCARGAR_FICHERO, json));
                System.out.println("Solicitud enviada para: " + fichero.nombre);
            });

            setGraphic(contenido);
        }
    }
}
