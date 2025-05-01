package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.DAO.SolicitudContactoDAO;
import org.p2pnexus.servidor.Entidades.DAO.UsuarioDAO;
import org.p2pnexus.servidor.Entidades.SolicitudContacto;
import org.p2pnexus.servidor.Entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ManejarConsultaUsuariosPorNombre implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        String nombreBuscar = mensaje.getData().get("nombre").getAsString();
        int idUsuarioOrigen = mensaje.getData().get("id_usuario_origen").getAsInt();

        SolicitudContactoDAO dao = new SolicitudContactoDAO();
        List<Usuario> usuarios = dao.buscarUsuariosSolicitables(nombreBuscar, idUsuarioOrigen);

        if (usuarios.isEmpty()) {
            return new ResultadoMensaje("No se ha encontrado ningún usuario", TipoNotificacion.AVISO);
        }

        Mensaje respuesta = Mensaje.empaquetarListaEnMensaje(usuarios, TipoMensaje.R_BUSCAR_USUARIOS_POR_NOMBRE);

        return new ResultadoMensaje(respuesta);
    }
}
