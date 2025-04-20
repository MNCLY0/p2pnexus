package org.p2pnexus.servidor.DAO;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Conversacion")
public class Conversacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_conversacion;

    private LocalDateTime fecha_creacion;

    public Integer getId_conversacion() {
        return id_conversacion;
    }

    public void setId_conversacion(Integer id_conversacion) {
        this.id_conversacion = id_conversacion;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}
