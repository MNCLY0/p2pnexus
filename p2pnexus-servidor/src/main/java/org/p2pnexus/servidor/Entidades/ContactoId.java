package org.p2pnexus.servidor.Entidades;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;

@Embeddable
public class ContactoId implements Serializable {

    private Integer usuario1;
    private Integer usuario2;

    public ContactoId() {}

    public ContactoId(Integer usuario1, Integer usuario2) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
    }

    public Integer getUsuario1() { return usuario1; }
    public void setUsuario1(Integer usuario1) { this.usuario1 = usuario1; }

    public Integer getUsuario2() { return usuario2; }
    public void setUsuario2(Integer usuario2) { this.usuario2 = usuario2; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContactoId contactoidOtro)) return false;
        return Objects.equals(usuario1, contactoidOtro.usuario1) &&
                Objects.equals(usuario2, contactoidOtro.usuario2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario1, usuario2);
    }
}

