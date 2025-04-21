package org.p2pnexus.servidor.acciones.manejadoresPeticiones;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeption;
import com.p2pnexus.comun.SocketConexion;

public interface ManejadorMensajes {

    public void manejarDatos(JsonObject datos, SocketConexion socketConexion) throws ManejarPeticionesExeption;
}
