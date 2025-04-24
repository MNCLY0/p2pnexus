package com.p2pnexus.comun.comunicacion;

import com.google.gson.JsonObject;
import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;

public interface IAccionMensaje {

    ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError;
}
