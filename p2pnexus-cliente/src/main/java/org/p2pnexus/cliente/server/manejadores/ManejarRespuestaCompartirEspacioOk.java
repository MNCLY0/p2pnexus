package org.p2pnexus.cliente.server.manejadores;

import com.p2pnexus.comun.JsonHerramientas;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.comunicacion.IManejadorMensaje;
import com.p2pnexus.comun.comunicacion.ResultadoMensaje;
import com.p2pnexus.comun.comunicacion.SocketConexion;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;
import org.p2pnexus.cliente.controladores.vistas.ControladorChat;
import org.p2pnexus.cliente.server.entitades.Conversacion;
import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.server.entitades.Usuario;
import org.p2pnexus.cliente.sesion.Sesion;
import org.p2pnexus.cliente.sesion.datos.datosEspecificos.DatosPaqueteEspaciosCompartidos;

import java.util.List;

public class ManejarRespuestaCompartirEspacioOk implements IManejadorMensaje {
    @Override
    public ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError {
        EspacioCompartido espacioCompartido = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("espacio").getAsJsonObject(), EspacioCompartido.class);
        espacioCompartido.inializarPropiedades();
        System.out.println("Espacios compartidos con el usuario: " + espacioCompartido.getNombrePropiedadProperty().getValue());
        Conversacion conversacion = JsonHerramientas.convertirJsonAObjeto(mensaje.getData().get("conversacion").getAsJsonObject(), Conversacion.class);

        DatosPaqueteEspaciosCompartidos datosPaqueteEspaciosCompartidos = Sesion.getDatosSesionUsuario().getCacheDatosConversacion()
                .get(conversacion.getIdConversacion())
                .getDatosPaqueteEspaciosCompartidos();

        datosPaqueteEspaciosCompartidos.getEnviados().add(espacioCompartido);

        ControladorChat.instancia.actualizarFiltroComboBox(conversacion);

        return null;
    }
}
