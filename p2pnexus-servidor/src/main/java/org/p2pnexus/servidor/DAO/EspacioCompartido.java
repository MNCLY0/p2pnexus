package org.p2pnexus.servidor.DAO;

import jakarta.persistence.*;

@Entity
@Table(name = "EspacioCompartido")
public class EspacioCompartido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_espacio;

    private String nombre;

    private String ruta_directorio;

    @ManyToOne
    @JoinColumn(name = "id_propietario")
    private Usuario propietario;

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
