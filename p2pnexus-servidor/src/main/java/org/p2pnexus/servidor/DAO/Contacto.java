package org.p2pnexus.servidor.DAO;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Contacto")
public class Contacto {

    @EmbeddedId
    private ContactoId id;

    @ManyToOne
    @MapsId("usuario1")
    @JoinColumn(name = "id_usuario1")
    private Usuario usuario1;

    @ManyToOne
    @MapsId("usuario2")
    @JoinColumn(name = "id_usuario2")
    private Usuario usuario2;

    private LocalDateTime fecha_agregado;

    // Getters y setters
    public ContactoId getId() { return id; }
    public void setId(ContactoId id) { this.id = id; }

    public Usuario getUsuario1() { return usuario1; }
    public void setUsuario1(Usuario usuario1) { this.usuario1 = usuario1; }

    public Usuario getUsuario2() { return usuario2; }
    public void setUsuario2(Usuario usuario2) { this.usuario2 = usuario2; }

    public LocalDateTime getFecha_agregado() { return fecha_agregado; }
    public void setFecha_agregado(LocalDateTime fecha_agregado) { this.fecha_agregado = fecha_agregado; }
}