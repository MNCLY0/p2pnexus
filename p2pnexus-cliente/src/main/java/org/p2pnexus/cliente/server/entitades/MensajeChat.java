package org.p2pnexus.cliente.server.entitades;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class MensajeChat {

    public MensajeChat(Usuario emisor, String contenido, LocalDateTime fecha_envio) {
        this.emisor = emisor;
        this.contenido = contenido;
        this.fecha_envio = fecha_envio;
    }

    private Usuario emisor;

    private String contenido;

    private LocalDateTime fecha_envio;

    public Usuario getEmisor() {
        return emisor;
    }

    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFecha_envio() {
        return fecha_envio;
    }

    public void setFecha_envio(LocalDateTime fecha_envio) {
        this.fecha_envio = fecha_envio;
    }
}
