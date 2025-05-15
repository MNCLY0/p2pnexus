package org.p2pnexus.cliente.ventanas;

import atlantafx.base.util.Animations;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.List;


public class Animaciones {
    public static void animarError(Node nodo)
    {
        var animacion = Animations.flash(nodo);
        animacion.playFromStart();
    }

    public static void animarEntradaIzqda(Node nodo, Duration duracion)
    {
        var animacion = Animations.slideInLeft(nodo, duracion);
        //usamos playfrom para que sea menos brusco
        animacion.playFrom(new Duration(50));
    }

    public static void animarEntradaListaNodosIzqda(List<Node> nodos, float duracion, double delayEntreNodo) {
        double delay = 0;

        for (int i = nodos.size() - 1; i >= 0; i--) {
            Node nodo = nodos.get(i);

            var animacion = Animations.slideInLeft(nodo, new Duration(duracion));

            nodo.setVisible(true);
            nodo.setManaged(true);

            animacion.setDelay(new Duration(delay));
            animacion.playFrom(new Duration(10));

            delay += delayEntreNodo;
        }
    }

    public static void animarEntradaListaNodosDesdeAbajo(List<Node> nodos, float duracion, double delayEntreNodo) {
        double delay = 0;

        for (int i = nodos.size() - 1; i >= 0; i--) {
            Node nodo = nodos.get(i);

            double delayFinal = delay;
            // antes usaba animacion.setDelay pero no funcionaba bien dentro de un bucle porque
            // el nodo se hacia visible antes de que se ejecutara la animacion (feisimo)
            PauseTransition espera = new PauseTransition(Duration.millis(delayFinal));
            espera.setOnFinished(e -> {

                var animacion = Animations.slideInUp(nodo, new Duration(duracion));
                animacion.playFrom(new Duration(30));

                var animacionFade = Animations.fadeIn(nodo, new Duration(duracion * 2));
                animacionFade.playFrom(new Duration(30));

                nodo.setVisible(true);
                nodo.setManaged(true);

            });

            espera.play();
            delay += delayEntreNodo;
        }
    }


}
