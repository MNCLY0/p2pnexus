package org.p2pnexus.cliente.sesion.datos.datosEspecificos;

import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.server.entitades.MensajeChat;

import java.util.List;

public class DatosConversacion {

    List<MensajeChat> mensajes;
    List<EspacioCompartido> espaciosCompartidosPorUsuarioCliente;
    List<EspacioCompartido> espaciosCompartidosPorUsuarioConversacion;

    public List<MensajeChat> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<MensajeChat> mensajes) {
        this.mensajes = mensajes;
    }

    public List<EspacioCompartido> getEspaciosCompartidosPorUsuarioCliente() {
        return espaciosCompartidosPorUsuarioCliente;
    }

    public void setEspaciosCompartidosPorUsuarioCliente(List<EspacioCompartido> espaciosCompartidosPorUsuarioCliente) {
        this.espaciosCompartidosPorUsuarioCliente = espaciosCompartidosPorUsuarioCliente;
    }

    public List<EspacioCompartido> getEspaciosCompartidosPorUsuarioConversacion() {
        return espaciosCompartidosPorUsuarioConversacion;
    }

    public void setEspaciosCompartidosPorUsuarioConversacion(List<EspacioCompartido> espaciosCompartidosPorUsuarioConversacion) {
        this.espaciosCompartidosPorUsuarioConversacion = espaciosCompartidosPorUsuarioConversacion;
    }
}
