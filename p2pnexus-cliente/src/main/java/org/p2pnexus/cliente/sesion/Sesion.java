package org.p2pnexus.cliente.sesion;

import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

public class Sesion {

    public static Usuario usuario;

    public static void iniciarSesion(Usuario usuario){
        Sesion.usuario = usuario;
        GestorVentanas.transicionarVentana(Ventanas.MENU_PRINCIPAL);
    }

    public static void cerrarSesion(){
        GestorVentanas.transicionarVentana(Ventanas.LOGIN);
    }

    public static Usuario getUsuario() {
        return usuario;
    }
}
