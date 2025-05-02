package org.p2pnexus.cliente.server.entitades;

public class Conversacion {
    int id_conversacion;
    public Conversacion(int id_conversacion) {
        this.id_conversacion = id_conversacion;
    }

    public int getIdConversacion() {return id_conversacion;}

    @Override
    public String toString() {
        return "Conversacion{" +
                "id_conversacion=" + id_conversacion +
                '}';
    }
}
