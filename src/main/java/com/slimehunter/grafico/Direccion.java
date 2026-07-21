package com.slimehunter.grafico;

public enum Direccion {
    IZQUIERDA(-1),
    DERECHA(1);

    private final int factor;

    Direccion(int factor) {
        this.factor = factor;
    }

    public int getFactor() {
        return factor;
    }

    public Direccion opuesta() {
        return this == IZQUIERDA ? DERECHA : IZQUIERDA;
    }
}
