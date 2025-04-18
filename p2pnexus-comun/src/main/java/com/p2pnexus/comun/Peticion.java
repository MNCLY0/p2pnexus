package com.p2pnexus.comun;

import com.google.gson.JsonObject;

public class Peticion {
    private String tipo;
    private JsonObject data;

    public Peticion(String type, JsonObject data) {
        this.tipo = type;
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}
