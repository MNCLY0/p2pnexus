package org.p2pnexus.servidor.Entidades;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

@Entity
@Table(name = "EspacioCompartido", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nombre", "id_propietario"})
})
public class EspacioCompartido {

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_espacio;

    @Expose
    @Column(nullable = false)
    private String nombre;

    @Expose
    @Column(nullable = false)
    private String ruta_directorio;

    @Expose
    @ManyToOne
    @JoinColumn(name = "id_propietario", nullable = false)
    private Usuario propietario;

    // Getters y setters

    public Integer getId_espacio() {
        return id_espacio;
    }

    public void setId_espacio(Integer id_espacio) {
        this.id_espacio = id_espacio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta_directorio() {
        return ruta_directorio;
    }

    public void setRuta_directorio(String ruta_directorio) {
        this.ruta_directorio = ruta_directorio;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }
}