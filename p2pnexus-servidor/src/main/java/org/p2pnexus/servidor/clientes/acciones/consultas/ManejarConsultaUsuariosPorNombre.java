package org.p2pnexus.servidor.clientes.acciones.consultas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IAccionMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.DAO.UsuarioDAO;
import org.p2pnexus.servidor.Entidades.Usuario;

import java.util.ArrayList;

public class ManejarConsultaUsuariosPorNombre implements IAccionMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        String nombreBuscar = mensaje.getData().get("nombre").getAsString();

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ArrayList<Usuario> usuarios = usuarioDAO.buscarUsuariosPorNombre(nombreBuscar);

        if (usuarios.isEmpty()) {
            throw new ManejarPeticionesExeptionError("No se han encontrado usuarios");
        }

        // Creamos un gson que excluye los campos sin la anotación @Expose
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        JsonObject json = new JsonObject();
        JsonArray jsonArray = gson.toJsonTree(usuarios).getAsJsonArray();
        json.add("usuarios", jsonArray);
        Mensaje mensajeRespuesta = new Mensaje(TipoMensaje.R_BUSCAR_USUARIOS_POR_NOMBRE, json);
        mensajeRespuesta.setData(json);

        return new ResultadoMensaje(mensajeRespuesta);
    }
}
