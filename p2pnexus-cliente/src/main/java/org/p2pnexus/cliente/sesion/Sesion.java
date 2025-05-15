package org.p2pnexus.cliente.sesion;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import org.p2pnexus.cliente.server.Conexion;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.datos.DatosSesionUsuario;
import org.p2pnexus.cliente.ventanas.GestorVentanas;
import org.p2pnexus.cliente.ventanas.Ventanas;

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
        Conexion.enviarMensaje(new Mensaje(TipoMensaje.S_CERRAR_SESION));
        Sesion.usuario = null;
        GestorVentanas.transicionarVentana(Ventanas.LOGIN);
    }

    public static DatosSesionUsuario getDatosSesionUsuario() {return datosSesionUsuario;}

    public static Usuario getUsuario() {
        return usuario;
    }


}
