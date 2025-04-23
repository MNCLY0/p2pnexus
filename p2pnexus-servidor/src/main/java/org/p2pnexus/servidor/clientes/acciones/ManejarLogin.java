package org.p2pnexus.servidor.clientes.acciones;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IAccionMensaje;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeption;
import com.p2pnexus.comun.comunicacion.SocketConexion;

public class ManejarLogin implements IAccionMensaje {

    @Override
    public void manejarDatos(JsonObject datos, SocketConexion socketConexion) throws ManejarPeticionesExeption {

        String usuario = datos.get("usuario").getAsString();
        String contrasena = datos.get("pass").getAsString();

        if (usuario.isEmpty() || contrasena.isEmpty()) {

            throw new ManejarPeticionesExeption("Usuario o contraseña vacíos");
        }

        System.out.println("El usuario intenta iniciar sesión con el usuario: " + usuario + " y la contraseña: " + contrasena);


        iniciarSaludo(socketConexion);
        throw new ManejarPeticionesExeption("Taquivocao");
    }

    void iniciarSaludo(SocketConexion socketConexion)
    {
//        Mensaje mensaje = new Mensaje(TipoMensaje.S_SOLICITAR_CLAVE_PUBLICA);
//        socketConexion.enviarMensaje(mensaje);
    }
}
