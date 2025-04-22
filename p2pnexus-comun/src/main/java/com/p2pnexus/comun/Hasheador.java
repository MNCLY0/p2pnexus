package com.p2pnexus.comun;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Hasheador {

    public static String hashear(String texto) {
        String salida = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digerido = md.digest(texto.getBytes());
            salida = Base64.getEncoder().encodeToString(digerido);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return salida;
    }

}
