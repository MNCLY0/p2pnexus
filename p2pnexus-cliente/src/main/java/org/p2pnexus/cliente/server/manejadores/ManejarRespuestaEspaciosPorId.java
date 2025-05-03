package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.server.entitades.EspaciosCompartidos;

import java.util.List;

public class ManejarRespuestaEspaciosPorId implements IManejadorMensaje {

    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        List<EspaciosCompartidos> espaciosCompartidos = JsonHerramientas.obtenerListaDeJsonObject(mensaje.getData(), EspaciosCompartidos.class);
        for (EspaciosCompartidos e : espaciosCompartidos) {
            System.out.println("Espacio compartido: " + e.getId_espacio() + " " + e.getNombre() + " " + e.getRuta_directorio() + " " + e.getPropietario().getNombre());
        }
        return null;
    }
}
