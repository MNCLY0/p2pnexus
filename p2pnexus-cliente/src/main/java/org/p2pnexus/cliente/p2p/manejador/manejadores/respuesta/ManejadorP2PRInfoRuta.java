package org.p2pnexus.cliente.p2p.manejador.manejadores.respuesta;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.controladores.vistasModales.ControladorCargarEspacio;
import org.p2pnexus.cliente.p2p.Fichero;

import java.io.File;
import java.util.List;

public class ManejadorP2PRInfoRuta implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        try {
            List<Fichero> archivos = JsonHerramientas.obtenerListaDeJsonObject(mensaje.getData().get("ficheros").getAsJsonObject(), Fichero.class);
            ControladorCargarEspacio.instancia.cargarDatos(archivos);
            return null;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
