package org.p2pnexus.cliente.server.entitades;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class SolicitudContacto {

    private Integer id_solicitud;

    private Usuario usuarioOrigen;

    public Integer getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(Integer id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public Usuario getUsuarioOrigen() {
        return usuarioOrigen;
    }

    public void setUsuarioOrigen(Usuario usuarioOrigen) {
        this.usuarioOrigen = usuarioOrigen;
    }

}
