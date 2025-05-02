package org.p2pnexus.cliente.server.entitades;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class MensajeChat {


    public MensajeChat() {}

    // Cuando no utilizo camelCase es porque necesito deserializar atributos de entidades que vienen de la base de datos y por temas
    // de mapeo de hibernate no puedo cambiar el nombre de los atributos sin liarla de más
    public MensajeChat(Usuario emisor, String contenido, LocalDateTime fecha_envio, Conversacion conversacion) {
        this.emisor = emisor;
        this.contenido = contenido;
        this.fecha_envio = fecha_envio;
        this.conversacion = conversacion;
    }

    private Usuario emisor;

    private String contenido;

    private LocalDateTime fecha_envio;

    private Conversacion conversacion;

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

    public Conversacion getConversacion() {return conversacion;}
    public void setConversacion(Conversacion conversacion) {this.conversacion = conversacion;}

    @Override
    public String toString() {
        return "MensajeChat{" +
                "conversacion=" + conversacion +
                ", emisor=" + emisor +
                ", contenido='" + contenido + '\'' +
                ", fecha_envio=" + fecha_envio +
                '}';
    }
}
