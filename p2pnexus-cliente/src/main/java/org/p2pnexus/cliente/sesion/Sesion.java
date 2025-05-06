package org.p2pnexus.cliente.sesion;

import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

public class Sesion {

    public static Usuario usuario;
    public static DatosSesionUsuario datosSesionUsuario;

    public static void iniciarSesion(Usuario usuario){
        Sesion.usuario = usuario;
        Sesion.datosSesionUsuario = new DatosSesionUsuario();
    }

    public static void cerrarSesion(){
        Sesion.usuario = null;
        GestorVentanas.transicionarVentana(Ventanas.LOGIN);
    }

    public static DatosSesionUsuario getDatosSesionUsuario() {return datosSesionUsuario;}

    public static Usuario getUsuario() {
        return usuario;
    }
}
