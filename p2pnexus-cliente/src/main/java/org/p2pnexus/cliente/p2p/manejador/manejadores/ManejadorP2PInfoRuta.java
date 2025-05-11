package org.p2pnexus.cliente.p2p.manejador.manejadores;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.p2p.HerramientasFicheros;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;

public class ManejadorP2PInfoRuta implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {

        try {
            System.out.println("Intentando convertir a espacio compartido");
            EspacioCompartido espacioCompartido = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("espacio").getAsJsonObject(), EspacioCompartido.class);
            espacioCompartido.inializarPropiedades();
            System.out.println("Espacio compartido convertido: " + espacioCompartido  + " ruta: " + espacioCompartido.getRutaPropiedadProperty().getValue());

            JsonObject json = HerramientasFicheros.obtenerInformacionRuta(espacioCompartido);

            return new ResultadoMensaje(new Mensaje(TipoMensaje.P2P_R_INFO_RUTA, json));

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
