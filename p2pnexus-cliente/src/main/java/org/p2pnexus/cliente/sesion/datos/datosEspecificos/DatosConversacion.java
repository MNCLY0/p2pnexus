package org.p2pnexus.cliente.sesion.datos.datosEspecificos;

import org.p2pnexus.cliente.server.entitades.EspacioCompartido;
import org.p2pnexus.cliente.server.entitades.MensajeChat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DatosConversacion {

    List<MensajeChat> mensajes;
    DatosPaqueteEspaciosCompartidos datosPaqueteEspaciosCompartidos;

    public List<MensajeChat> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<MensajeChat> mensajes) {
        this.mensajes = mensajes;
    }

    public DatosPaqueteEspaciosCompartidos getDatosPaqueteEspaciosCompartidos() {
        return datosPaqueteEspaciosCompartidos;
    }

    public void setDatosPaqueteEspaciosCompartidos(DatosPaqueteEspaciosCompartidos datosPaqueteEspaciosCompartidos) {
        this.datosPaqueteEspaciosCompartidos = datosPaqueteEspaciosCompartidos;
    }
}
