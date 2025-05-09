package org.p2pnexus.cliente.ventanas;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.p2pnexus.comun.TipoNotificacion;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.kordamp.ikonli.material2.Material2RoundAL;
import org.kordamp.ikonli.javafx.FontIcon;

import java.nio.charset.StandardCharsets;

public class Notificaciones {
    private static final Duration DURACION = Duration.millis(250);
    private static final Duration TIEMPO_MOSTRADO = Duration.seconds(3);
    private static final double ESPACIO_VERTICAL = 10.0; // Espacio entre notificaciones
    private static final double ALTURA_ESTIMADA = 50.0;  // Mas o menos el tamaño de la notificación (se hace a ojo :p)

    // Usamos una lista observable para mantener el estado de las notificaciones activas
    private static final ObservableList<NotificacionConSize> notificacionesActivas = FXCollections.observableArrayList();


    public static void mostrarNotificacion(String mensaje, TipoNotificacion tipo, int size) {

        if (GestorVentanas.getCapaNotificaciones() == null) {
            System.err.println("No hay stackPane disponible para mostrar la notificacion");
            return;
        }

        Platform.runLater(() -> {
            NotificacionConSize notificacion = crearNotificacion(mensaje, tipo,size);
            notificacion.setPickOnBounds(true);
            generarNotificacion(notificacion,size);
        });
    }

    public static void mostrarNotificacion(String mensaje, TipoNotificacion tipo) {
        mostrarNotificacion(mensaje, tipo, 1);
    }


    private static void generarNotificacion(NotificacionConSize notificacion, int extraMargen) {
        // Configuramos la posición inicial (arriba a la derecha)
        StackPane.setAlignment(notificacion, Pos.TOP_RIGHT);

        // Calculamos el margen superior basado en notificaciones existentes
        double margenSuperior = 10;
        if (!notificacionesActivas.isEmpty()) {
            margenSuperior += notificacionesActivas.size() * (ALTURA_ESTIMADA  + ESPACIO_VERTICAL * notificacion.size);
        }

        // Establecemos los márgenes
        StackPane.setMargin(notificacion, new Insets(margenSuperior, 20, 0, 10));

        // Añadimos la nueva notificación a la lista y al panel
        notificacionesActivas.add(notificacion);
        GestorVentanas.getCapaNotificaciones().getChildren().add(notificacion);

        // Animación de entrada
        notificacion.setOpacity(0);
        Animations.fadeIn(notificacion, DURACION).play();

        // Configuramos el evento de clic para cerrar la notificación
        notificacion.setOnMouseClicked(event -> cerrarNotificacion(notificacion));

        // Configuramos el cierre automático
        Timeline timeline = new Timeline(new KeyFrame(TIEMPO_MOSTRADO,
                e -> cerrarNotificacion(notificacion)));
        timeline.play();
    }

    private static void cerrarNotificacion(NotificacionConSize notificacion) {
        // Si ya se ha eliminado, no hacemos nada
        if (!GestorVentanas.getCapaNotificaciones().getChildren().contains(notificacion)) {
            return;
        }

        // Animación de salida
        var animacionSalida = Animations.fadeOut(notificacion, DURACION);
        animacionSalida.setOnFinished(event -> {
            // Eliminamos la notificación
            GestorVentanas.getCapaNotificaciones().getChildren().remove(notificacion);
            int indiceEliminado = notificacionesActivas.indexOf(notificacion);
            notificacionesActivas.remove(notificacion);

            // Reposicionamos las notificaciones que estaban debajo
            reposicionarNotificaciones(indiceEliminado);
        });
        animacionSalida.play();
    }

    private static void reposicionarNotificaciones(int indiceEliminado) {
        // Solo reposicionamos las notificaciones que estaban debajo de la eliminada
        try {
            for (int i = indiceEliminado; i < notificacionesActivas.size(); i++) {
                NotificacionConSize notif = notificacionesActivas.get(i);

                // Calculamos el nuevo margen superior
                Insets margenesPrevios = StackPane.getMargin(notif);
                double nuevoMargenSuperior = 10 + (i * (ALTURA_ESTIMADA + ESPACIO_VERTICAL * notif.size));

                // Animamos el movimiento hacia arriba
                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(notif.translateYProperty(),
                        nuevoMargenSuperior - margenesPrevios.getTop());
                KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
                timeline.getKeyFrames().add(kf);
                timeline.setOnFinished(e -> {
                    // Actualizamos los margenes reales después de la animacion
                    StackPane.setMargin(notif, new Insets(nuevoMargenSuperior,
                            margenesPrevios.getRight(),
                            margenesPrevios.getBottom(),
                            margenesPrevios.getLeft()));
                    notif.setTranslateY(0); // Reseteamos el movimiento
                });
                timeline.play();
            }
        }catch (Exception e) {}

    }

    private static NotificacionConSize crearNotificacion(String mensaje, TipoNotificacion tipo, int size) {
        FontIcon icono = null;
        String estilo;

        switch (tipo) {
            case EXITO -> {
                icono = new FontIcon(Material2RoundAL.DONE_ALL);
                estilo = Styles.SUCCESS;
            }
            case ERROR -> {
                icono = new FontIcon(Material2RoundAL.ERROR);
                estilo = Styles.DANGER;
            }
            case AVISO -> {
                icono = new FontIcon(Material2RoundAL.ESCALATOR_WARNING);
                estilo = Styles.WARNING;
            }
            default -> estilo = "";
        }


        // Solución: normalizar explícitamente las secuencias de bytes UTF-8
        String mensajeCorregido = new String(mensaje.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        NotificacionConSize notificacion = new NotificacionConSize(mensajeCorregido, icono, size);

        notificacion.getStyleClass().add(Styles.INTERACTIVE);
        notificacion.getStyleClass().add(estilo);
        notificacion.getStyleClass().add(Styles.TEXT_BOLD);
        notificacion.setPrefHeight(Region.USE_PREF_SIZE);
        notificacion.setMaxHeight(Region.USE_PREF_SIZE);

        return notificacion;
    }
}