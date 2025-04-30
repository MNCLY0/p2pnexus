package com.p2pnexus.comun;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class JsonHerramientas {

    // Este en un metodo super util para convertir un JsonArray a una lista de objetos, es necesario porque la comunicacion
    // entre el cliente y el servidor es a traves de Json
    public static <T> List<T> obtenerListaDeJsonObject(JsonObject json, Class<T> clase) {
        // Siempre se deberia recibir un JsonObject con una lista dentro de la propiedad lista
        JsonElement element = json.get("lista");
        JsonArray array = element.getAsJsonArray();
        Gson gson = new Gson();
        // Typetoken.getParameterized le dice a Gson  que tipo de lista queremos deserealizar
        return gson.fromJson(array, TypeToken.getParameterized(List.class, clase).getType());
    }
    // Empaquetamos una lista en un JsonObject
    public static JsonObject empaquetarListaEnJsonObject(List<?> lista) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        JsonObject json = new JsonObject();
        JsonArray jsonArray = gson.toJsonTree(lista).getAsJsonArray();
        json.add("lista", jsonArray);
        return json;
    }

}
