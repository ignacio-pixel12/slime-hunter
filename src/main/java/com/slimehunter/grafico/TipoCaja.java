package com.slimehunter.grafico;

public enum TipoCaja {
    COLBOX("colbox"),
    HITBOX("hitbox"),
    HURTBOX("hurtbox");

    private final String nombreJson;

    TipoCaja(String nombreJson) {
        this.nombreJson = nombreJson;
    }

    public String getNombreJson() {
        return this.nombreJson;
    }
}
