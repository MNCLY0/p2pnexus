package org.p2pnexus.servidor.clientes.acciones;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IAccionMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import org.p2pnexus.servidor.ControladorHibernate;
import org.p2pnexus.servidor.Entidades.DAO.UsuarioDAO;
import org.p2pnexus.servidor.clientes.ControladorSesiones;

public class ManejarLogin implements IAccionMensaje {

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        JsonObject datos = mensaje.getData();

        String usuario = datos.get("usuario").getAsString();
        String contrasena = datos.get("pass").getAsString();

        String nombre;

        System.out.println("El usuario intenta iniciar sesión con el usuario: " + usuario + " y la contraseña: " + contrasena);

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            nombre = usuarioDAO.validarCredenciales(usuario, contrasena);
            if (nombre == null) {throw new Exception();}
        }catch (Exception e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
            return new ResultadoMensaje("Error al iniciar sesión", TipoNotificacion.ERROR);
        }

        iniciarSaludo(socketConexion, nombre);

        return new ResultadoMensaje("Login exitoso, bienvenido " + nombre,
                TipoNotificacion.EXITO,
                rellenarRespuesta(mensaje.generarRespuesta()));
    }

    void iniciarSaludo(SocketConexion socketConexion, String nombre)
    {
        ControladorSesiones.agregarSesion(socketConexion, nombre);
    }

    Mensaje rellenarRespuesta(Mensaje mensaje)
    {
        mensaje.setTipo(TipoMensaje.R_LOGIN_OK);
        JsonObject data = new JsonObject();
        data.addProperty("usuario", "test");
        mensaje.setData(data);
        return mensaje;
    }
}
