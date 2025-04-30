package com.p2pnexus.comun.comunicacion;

import com.p2pnexus.comun.Mensaje;
import com.p2pnexus.comun.exepciones.ManejarPeticionesExeptionError;

public interface IManejadorMensaje {

    ResultadoMensaje manejarDatos(Mensaje mensaje, SocketConexion socketConexion) throws ManejarPeticionesExeptionError;
}
