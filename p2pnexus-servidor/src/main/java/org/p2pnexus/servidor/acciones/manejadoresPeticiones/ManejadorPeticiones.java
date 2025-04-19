package org.p2pnexus.servidor.acciones.manejadoresPeticiones;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeption;

public interface ManejadorPeticiones {

    public void manejarDatos(JsonObject datos) throws ManejarPeticionesExeption;
}
