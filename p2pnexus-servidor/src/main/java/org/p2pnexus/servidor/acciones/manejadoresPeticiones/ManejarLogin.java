package org.p2pnexus.servidor.acciones.manejadoresPeticiones;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeption;

public class ManejarLogin implements ManejadorPeticiones{
    @Override
    public void manejarDatos(JsonObject datos) throws ManejarPeticionesExeption {

        String usuario = datos.get("usuario").getAsString();
        String contrasena = datos.get("pass").getAsString();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            throw new ManejarPeticionesExeption("Usuario o contraseña vacíos");
        }

        System.out.println("El usuario intenta iniciar sesión con el usuario: " + usuario + " y la contraseña: " + contrasena);
    }
}
