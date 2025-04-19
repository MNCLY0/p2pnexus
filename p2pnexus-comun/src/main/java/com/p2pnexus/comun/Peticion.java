package com.p2pnexus.comun;

import com.google.gson.JsonObject;

public class Peticion {
    private TipoPeticion tipo;
    private JsonObject data;

    public Peticion(TipoPeticion tipo, JsonObject data) {
        this.tipo = tipo;
        this.data = data;
    }

    public TipoPeticion getTipo() {
        return tipo;
    }

    public void setTipo(TipoPeticion tipo) {
        this.tipo = tipo;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}
