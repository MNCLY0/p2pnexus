package com.p2pnexus.comun;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.p2pnexus.comun.gsonAdaptadores.LocalDateTimeAdaptador;

import java.time.LocalDateTime;
import java.util.List;


public class JsonHerramientas {

    // Este en un metodo super util para convertir un JsonArray a una lista de objetos, es necesario porque la comunicacion
    // entre el cliente y el servidor es a traves de Json
    public static <T> List<T> obtenerListaDeJsonObject(JsonObject json, Class<T> clase) {
        // Siempre se deberia recibir un JsonObject con una lista dentro de la propiedad lista
        System.out.println("Recibiendo lista de objetos");
        JsonElement element = json.get("lista");
        System.out.println("Elemento: " + element);
        JsonArray array = element.getAsJsonArray();
        System.out.println("Array: " + array);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdaptador())
                .create();

        System.out.println("Gson inicializado ");
        // Typetoken.getParameterized le dice a Gson  que tipo de lista queremos deserealizar
        List<T> lista = gson.fromJson(array, TypeToken.getParameterized(List.class, clase).getType());
        System.out.println("Lista convertida: " + lista.size());
        return lista;
    }
    // Empaquetamos una lista en un JsonObject
    public static JsonObject empaquetarListaEnJsonObject(List<?> lista) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdaptador())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        JsonObject json = new JsonObject();
        JsonArray jsonArray = gson.toJsonTree(lista).getAsJsonArray();
        json.add("lista", jsonArray);
        return json;
    }

    public static <T> JsonObject convertirObjetoAJson(T objeto) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdaptador())
                .create();
        JsonObject json = new JsonObject();
        JsonElement jsonElement = gson.toJsonTree(objeto);
        json.add("objeto", jsonElement);
        return json;
    }

    public static <T> T convertirJsonAObjeto(JsonObject json, Class<T> clase) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdaptador())
                .create();
        JsonElement jsonElement = json.get("objeto");
        return gson.fromJson(jsonElement, clase);
    }

}
