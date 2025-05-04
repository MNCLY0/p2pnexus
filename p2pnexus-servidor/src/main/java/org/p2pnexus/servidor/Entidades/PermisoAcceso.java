package org.p2pnexus.servidor.Entidades;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PermisoAcceso")
public class PermisoAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_permiso;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_espacio", nullable = false)
    private EspacioCompartido espacioCompartido;

    private LocalDateTime fecha_asignacion;

    // Getters y setters

    public Integer getId_permiso() {
        return id_permiso;
    }

    public void setId_permiso(Integer id_permiso) {
        this.id_permiso = id_permiso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EspacioCompartido getEspacioCompartido() {
        return espacioCompartido;
    }

    public void setEspacioCompartido(EspacioCompartido espacioCompartido) {
        this.espacioCompartido = espacioCompartido;
    }

    public LocalDateTime getFecha_asignacion() {
        return fecha_asignacion;
    }

    public void setFecha_asignacion(LocalDateTime fecha_asignacion) {
        this.fecha_asignacion = fecha_asignacion;
    }
}
