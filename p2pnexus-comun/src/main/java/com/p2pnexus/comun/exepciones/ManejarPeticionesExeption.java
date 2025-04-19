package com.p2pnexus.comun.exepciones;


public class ManejarPeticionesExeption extends Exception {

    public ManejarPeticionesExeption(String message) {
        super(message);
    }

    public ManejarPeticionesExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public ManejarPeticionesExeption(Throwable cause) {
        super(cause);
    }

}
