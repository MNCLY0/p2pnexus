package org.p2pnexus.servidor.clientes.acciones;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IAccionMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.DAO.UsuarioDAO;

public class ManejarRegistro implements IAccionMensaje {

    @Override
    public ResultadoMensaje manejarDatos(JsonObject datos, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        String usuario = datos.get("usuario").getAsString();
        String contrasena = datos.get("pass").getAsString();
        Integer id = null;
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            if (usuarioDAO.yaExiste(usuario)) {
                throw new ManejarPeticionesExeptionError("Ya existe una cuenta con ese usuario");
            }
            try {
                id = usuarioDAO.crearUsuario(usuario, contrasena);
                if (id == null) {
                    throw new ManejarPeticionesExeptionError("No se ha podido crear la cuenta");
                }

            } catch (Exception e) {
                throw new ManejarPeticionesExeptionError("Ha ocurrido un error al crear la cuenta");
            }
        }catch (Exception e) {
            throw new ManejarPeticionesExeptionError(e.getMessage());
        }
        return new ResultadoMensaje("¡Cuenta creada con éxito!", TipoNotificacion.EXITO);
    }

    void iniciarSaludo(SocketConexion socketConexion)
    {
//        Mensaje mensaje = new Mensaje(TipoMensaje.S_SOLICITAR_CLAVE_PUBLICA);
//        socketConexion.enviarMensaje(mensaje);
    }
}
