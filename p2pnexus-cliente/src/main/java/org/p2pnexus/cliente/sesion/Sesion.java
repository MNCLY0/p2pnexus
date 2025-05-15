package org.p2pnexus.cliente.sesion;

import javafx.scene.image.Image;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.datos.DatosSesionUsuario;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

import java.util.HashMap;
import java.util.Map;

public class Sesion {

    public static Usuario usuario;
    public static DatosSesionUsuario datosSesionUsuario;

    public static GestionImagenes gestionImagenes;

    public static void iniciarSesion(Usuario usuario){
        gestionImagenes = new GestionImagenes();
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
