package com.p2pnexus.comun.exepciones;


public class ConectarExeption extends Exception {

    public ConectarExeption(String message) {
        super(message);
    }

    public ConectarExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public ConectarExeption(Throwable cause) {
        super(cause);
    }

}
