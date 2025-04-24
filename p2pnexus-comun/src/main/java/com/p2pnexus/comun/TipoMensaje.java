package com.p2pnexus.comun;

public enum TipoMensaje {

    NOTIFICACION,
    
    // Mensajes de peticion
    P_TEST,
    P_LOGIN,
    P_REGISTRO,
    P_SOLICITAR_CLAVE_PUBLICA,

    // Mensajes de respuesta
    R_LOGIN_OK,
    R_TEST,
    R_CONFIRMACION,
    R_ERROR

}
