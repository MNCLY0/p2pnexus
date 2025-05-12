package org.p2pnexus.cliente.p2p.manejador.manejadores.descarga;

public class EstadoDescarga {
    public long totalPeso = 0;
    public long pesoActualDescargado = 0;
    public String nombre;


    public EstadoDescarga(long totalPeso, String nombre) {
        this.totalPeso = totalPeso;
        this.nombre = nombre;
    }

    public void sumarPeso(long peso) {
        this.pesoActualDescargado += peso;
    }

}
