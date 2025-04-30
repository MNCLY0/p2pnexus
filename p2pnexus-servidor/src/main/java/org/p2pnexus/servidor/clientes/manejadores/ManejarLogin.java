package org.p2pnexus.servidor.clientes.manejadores;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import org.p2pnexus.servidor.Entidades.DAO.UsuarioDAO;
import org.p2pnexus.servidor.Entidades.Usuario;

public class ManejarLogin implements IManejadorMensaje {

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        JsonObject datos = mensaje.getData();

        String usuario = datos.get("usuario").getAsString();
        String contrasena = datos.get("pass").getAsString();

        Usuario usuarioO;

        System.out.println("El usuario intenta iniciar sesión con el usuario: " + usuario + " y la contraseña: " + contrasena);

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioO = usuarioDAO.validarCredenciales(usuario, contrasena);
            if (usuarioO == null) {throw new Exception();}
        }catch (Exception e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
            return new ResultadoMensaje("Error al iniciar sesión", TipoNotificacion.ERROR);
        }

        return new ResultadoMensaje("Login exitoso, bienvenido " + usuarioO.getNombre(),
                TipoNotificacion.EXITO,
                rellenarRespuesta(mensaje.generarRespuesta(), usuarioO));
    }


    Mensaje rellenarRespuesta(Mensaje mensaje, Usuario usuario)
    {
        mensaje.setTipo(TipoMensaje.R_LOGIN_OK);
        JsonObject data = new JsonObject();
        data.addProperty("usuario", usuario.getNombre());
        data.addProperty("id", usuario.getId_usuario());
        mensaje.setData(data);
        return mensaje;
    }
}
