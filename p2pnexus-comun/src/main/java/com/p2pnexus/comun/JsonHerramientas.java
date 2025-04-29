package com.p2pnexus.comun;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class JsonHerramientas {

    // Este en un metodo super util para convertir un JsonArray a una lista de objetos, es necesario porque la comunicacion
    // entre el cliente y el servidor es a traves de Json
    public static <T> List<T> convertirJsonArrayALista(JsonArray array, Class<T> clase) {
        Gson gson = new Gson();
        // Typetoken.getParameterized le dice a Gson  que tipo de lista queremos deserealizar
        return gson.fromJson(array, TypeToken.getParameterized(List.class, clase).getType());
    }

}
