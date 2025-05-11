package org.p2pnexus.cliente.p2p;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

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
                System.out.println("Descargando: " + fichero.ruta);

            });
            setGraphic(contenido);
        }
    }
}
