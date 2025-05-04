package org.p2pnexus.cliente.ventanas;

import atlantafx.base.util.Animations;
import javafx.scene.Node;


public class Animaciones {
    public static void animarError(Node nodo)
    {
        var animacion = Animations.flash(nodo);
        animacion.playFromStart();
    }
}
