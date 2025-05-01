package org.p2pnexus.servidor.clientes.manejadores.consultas;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.servidor.Entidades.DAO.ContactoDAO;
import org.p2pnexus.servidor.Entidades.DAO.UsuarioDAO;
import org.p2pnexus.servidor.Entidades.Usuario;

import java.util.List;

public class ManejarConsultaListaContactos implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        int idUsuario = mensaje.getData().get("id_usuario").getAsInt();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        List<Usuario> listaContactos = usuarioDAO.listarContactos(idUsuario);

        JsonObject json = JsonHerramientas.empaquetarListaEnJsonObject(listaContactos);


        return new ResultadoMensaje(new Mensaje(TipoMensaje.R_LISTA_CONTACTOS, json));
    }
}
