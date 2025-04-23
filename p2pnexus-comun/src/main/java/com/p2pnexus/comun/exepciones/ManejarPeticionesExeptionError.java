package com.p2pnexus.comun.exepciones;


public class ManejarPeticionesExeptionError extends Exception {

    public ManejarPeticionesExeptionError(String message) {
        super(message);
    }

    public ManejarPeticionesExeptionError(String message, Throwable cause) {
        super(message, cause);
    }

    public ManejarPeticionesExeptionError(Throwable cause) {
        super(cause);
    }

}
