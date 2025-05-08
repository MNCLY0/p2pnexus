package org.p2pnexus.cliente.server.manejadores;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.controladores.vistas.controladorChat.ControladorChat;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.server.entitades.MensajeChat;
import org.p2pnexus.cliente.sesion.datos.datosEspecificos.DatosConversacion;
import org.p2pnexus.cliente.sesion.datos.datosEspecificos.DatosPaqueteEspaciosCompartidos;

import java.util.List;

public class ManejarRespuestaActualizarChat implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        Conversacion conversacion = new Conversacion(mensaje.getData().get("id_conversacion").getAsInt());
        JsonObject mensajesJson = mensaje.getData().get("mensajes").getAsJsonObject();
        JsonObject espaciosCompartidosEnviadosJson = mensaje.getData().get("espacios_compartidos_enviados").getAsJsonObject();
        JsonObject espaciosCompartidosRecibidosJson = mensaje.getData().get("espacios_compartidos_recibidos").getAsJsonObject();

        List<MensajeChat> mensajes = JsonHerramientas.obtenerListaDeJsonObject(mensajesJson, MensajeChat.class);

        List<EspacioCompartido> espacioCompartidosPorCliente = JsonHerramientas.obtenerListaDeJsonObject(
                espaciosCompartidosEnviadosJson,
                EspacioCompartido.class);

        List<EspacioCompartido> espacioCompartidosPorSolicitado = JsonHerramientas.obtenerListaDeJsonObject(
                espaciosCompartidosRecibidosJson,
                EspacioCompartido.class);

        DatosPaqueteEspaciosCompartidos datosPaqueteEspaciosCompartidos = new DatosPaqueteEspaciosCompartidos(
                espacioCompartidosPorCliente,
                espacioCompartidosPorSolicitado);

        DatosConversacion datosConversacion = new DatosConversacion();
        datosConversacion.setMensajes(mensajes);
        datosConversacion.setDatosPaqueteEspaciosCompartidos(datosPaqueteEspaciosCompartidos);

        System.out.printf("Espacios compartidos enviados: %s\n", datosPaqueteEspaciosCompartidos.getEnviados());
        System.out.printf("Espacios compartidos recibidos: %s\n", datosPaqueteEspaciosCompartidos.getRecibidos());



        ControladorChat.instancia.gestorChat.actualizarDatosCoversacion(datosConversacion, conversacion);
        return null;
    }
}
