package org.p2pnexus.cliente.sesion;

import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

public class Sesion {

    public static void cerrarSesion(){
        GestorVentanas.transicionarVentana(Ventanas.LOGIN);
    }
}
