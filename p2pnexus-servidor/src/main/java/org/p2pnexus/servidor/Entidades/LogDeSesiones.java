package org.p2pnexus.servidor.Entidades;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LogDeSesiones")
public class LogDeSesiones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_log;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private LocalDateTime fecha_inicio;

    private String direccion_ip;

    public Integer getId_log() {
        return id_log;
    }

    public void setId_log(Integer id_log) {
        this.id_log = id_log;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDateTime fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getDireccion_ip() {
        return direccion_ip;
    }

    public void setDireccion_ip(String direccion_ip) {
        this.direccion_ip = direccion_ip;
    }
}
