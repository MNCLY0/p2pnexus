package com.p2pnexus.comun.comunicacion;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;

public class ResultadoMensaje {

    // Mensaje de respuesta
    Mensaje mensaje;

    // Notificacion
    private String mensajeNotificacion;
    private TipoNotificacion tipoNotificacion;

    public ResultadoMensaje(String mensajeNotificacion, TipoNotificacion tipoNotificacion, Mensaje mensaje) {
        this.mensajeNotificacion = mensajeNotificacion;
        this.tipoNotificacion = tipoNotificacion;
        this.mensaje = mensaje;
    }


    public ResultadoMensaje(String mensaje, TipoNotificacion tipo) {
        this(mensaje, tipo, null);
    }

    public ResultadoMensaje(Mensaje mensaje) {
        this(null, null, mensaje);
    }

    public Mensaje getMensaje() {
        return mensaje;
    }


    public String getMensajeNotificacion() {
        return mensajeNotificacion;
    }

    public TipoNotificacion getTipoNotificacion() {
        return tipoNotificacion;
    }

}
