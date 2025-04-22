package org.p2pnexus.cliente.ventanas;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2RoundAL;

import java.util.Queue;
import java.util.LinkedList;

public class Notificaciones {
    private static final Duration DURACION = Duration.millis(250);
    private static final Duration TIEMPO_MOSTRADO = Duration.seconds(3);
    private static final Queue<NotificacionPendiente> colaNotificaciones = new LinkedList<>();
    private static boolean mostrandoNotificacion = false;

    private static class NotificacionPendiente {
        String mensaje;
        TipoNoticificacion tipo;
        StackPane stackPane;

        public NotificacionPendiente(String mensaje, TipoNoticificacion tipo, StackPane stackPane) {
            this.mensaje = mensaje;
            this.tipo = tipo;
            this.stackPane = stackPane;
        }
    }

    public static void MostrarNotificacion(String mensaje, TipoNoticificacion tipo, StackPane stackPane) {
        if (stackPane == null) {
            System.err.println("Error: StackPane es nulo. No se puede mostrar la notificación.");
            return;
        }

        Platform.runLater(() -> {
            colaNotificaciones.add(new NotificacionPendiente(mensaje, tipo, stackPane));

            // si no hay ninguna notificación en pantalla procesamos la cola para ver si hay notificaciones
            if (!mostrandoNotificacion) {
                procesarColaSiHay();
            }
        });
    }

    private static void procesarColaSiHay() {
        Platform.runLater(() -> {
            if (colaNotificaciones.isEmpty() || mostrandoNotificacion) {
                return;
            }

            mostrandoNotificacion = true;
            NotificacionPendiente pendiente = colaNotificaciones.poll();
            mostrarNotificacionActual(pendiente.mensaje, pendiente.tipo, pendiente.stackPane);
        });
    }

    private static void mostrarNotificacionActual(String mensaje, TipoNoticificacion tipo, StackPane stackPane) {
        Notification notificacion = crearNotificacion(mensaje, tipo);

        // Configuramos la posicion y margenes
        StackPane.setAlignment(notificacion, Pos.TOP_RIGHT);
        StackPane.setMargin(notificacion, new Insets(10, 20, 0, 10));

        // Configuramos el comportamiento de cierre manual (si se hace clic en la notificacion, bastante comodo)
        notificacion.setOnMouseClicked(event -> {
            cerrarNotificacion(notificacion, stackPane);
        });


        // Agregamos la notificacion al stackPane para mostrarla
        stackPane.getChildren().add(notificacion);

        // Y la animacion la ejecutamos en el hilo de la interfaz grafica
        Platform.runLater(() ->
        {
            var entrada = Animations.fadeIn(notificacion, DURACION);
            entrada.play();
        } );


        // Programamos el cierre automatico
        javafx.animation.PauseTransition temporizador = new javafx.animation.PauseTransition(TIEMPO_MOSTRADO);
        temporizador.setOnFinished(event -> {
            cerrarNotificacion(notificacion, stackPane);
        });
        temporizador.play();
    }

    private static void cerrarNotificacion(Notification notificacion, StackPane stackPane) {
        // Verificamos si la notificación todavia está en el stackPane
        if (stackPane.getChildren().contains(notificacion)) {
            var salida = Animations.fadeOut(notificacion, DURACION);
            salida.setOnFinished(f -> {
                // y la cerramos cuando termine la animacion
                stackPane.getChildren().remove(notificacion);
                mostrandoNotificacion = false;
                procesarColaSiHay();
            });
            salida.play();
        }
    }

    private static Notification crearNotificacion(String mensaje, TipoNoticificacion tipo) {
        FontIcon icono = null;

        //todo si da tiempo lo dejamos mas bonito
        String estilo = switch (tipo) {
            case EXITO -> {
                icono = new FontIcon(Material2RoundAL.DONE_ALL);
                yield Styles.SUCCESS;
            }
            case ERROR -> {
                icono = new FontIcon(Material2RoundAL.ERROR);
                yield Styles.DANGER;
            }
            case AVISO -> {
                icono = new FontIcon(Material2RoundAL.ESCALATOR_WARNING);
                yield Styles.WARNING;
            }
            default -> "";
        };

        Notification notificacion = new Notification(mensaje, icono);
        notificacion.getStyleClass().add(Styles.INTERACTIVE);
        notificacion.getStyleClass().add(estilo);
        notificacion.setPrefHeight(Region.USE_PREF_SIZE);
        notificacion.setMaxHeight(Region.USE_PREF_SIZE);

        return notificacion;
    }
}