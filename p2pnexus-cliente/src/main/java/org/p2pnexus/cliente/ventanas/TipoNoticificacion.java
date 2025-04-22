package org.p2pnexus.cliente.ventanas;

public enum TipoNoticificacion {
    ERROR("Error"),
    INFO("Info"),
    AVISO("Aviso"),
    EXITO("Exito");

    private String tipo;

    TipoNoticificacion(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
