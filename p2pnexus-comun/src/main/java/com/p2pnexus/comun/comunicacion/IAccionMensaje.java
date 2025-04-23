package com.p2pnexus.comun.comunicacion;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeption;

public interface IAccionMensaje {

    public void manejarDatos(JsonObject datos, SocketConexion socketConexion) throws ManejarPeticionesExeption;
}
