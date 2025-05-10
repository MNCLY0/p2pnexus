package com.p2pnexus.comun;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import java.util.List;

public class Mensaje {
    @Expose
    private TipoMensaje tipo;
    @Expose
    private JsonObject data;
    @Expose
    private String id;
    @Expose
    private boolean esRespuesta = false;

    public Mensaje(TipoMensaje tipo, JsonObject data) {
        this.tipo = tipo;
        this.data = data;
        this.id = generarId();
    }

    public Mensaje(String id) {
        this.tipo = null;
        this.data = null;
        this.id = id;
    }

    public Mensaje(TipoMensaje tipo) {
        this(tipo, null);
    }

    public TipoMensaje getTipo() {
        return tipo;
    }

    public void setTipo(TipoMensaje tipo) {
        this.tipo = tipo;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public String getId() {return id;}

    private String generarId()
    {
        return java.util.UUID.randomUUID().toString();
    }

    //Este metodo devuelve un mensaje de respuesta, para que se rellene y se envie al cliente.
    //El id del mensaje de respuesta es el mismo que el del mensaje original, asi los relacionamos
    public Mensaje generarRespuesta()
    {
        Mensaje respuesta = new Mensaje(this.id);
        respuesta.setEsRespuesta(true);
        return respuesta;
    }

    public static <T> Mensaje empaquetarListaEnMensaje(List<T> lista, TipoMensaje tipo)
    {
        JsonObject json = JsonHerramientas.empaquetarListaEnJsonObject(lista);
        Mensaje mensaje = new Mensaje(tipo, json);
        mensaje.setData(json);
        return mensaje;
    }

    public boolean EsRespuesta() {return esRespuesta;}
    public void setEsRespuesta(boolean esRespuesta) {
        this.esRespuesta = esRespuesta;
    }
}
