package com.p2pnexus.comun.comunicacion;

import com.p2pnexus.comun.TipoMensaje;
import com.p2pnexus.comun.TipoNotificacion;

public class ResultadoMensaje {
    private String mensaje;
    private TipoNotificacion tipo;

    public ResultadoMensaje(String mensaje, TipoNotificacion tipoNotificacion) {
        this.mensaje = mensaje;
        this.tipo = tipoNotificacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

}
