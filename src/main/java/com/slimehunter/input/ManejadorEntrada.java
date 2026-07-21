package com.slimehunter.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import com.slimehunter.entidad.Entidad;

public class ManejadorEntrada extends InputAdapter {

    private final Entidad entidad;
    private boolean izquierdaPresionada;
    private boolean derechaPresionada;

    public ManejadorEntrada(Entidad entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        this.entidad = entidad;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                this.izquierdaPresionada = true;
                this.entidad.moverIzquierda();
                return true;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                this.derechaPresionada = true;
                this.entidad.moverDerecha();
                return true;
            case Input.Keys.SPACE:
                this.entidad.saltar();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                this.izquierdaPresionada = false;
                if (this.derechaPresionada) {
                    this.entidad.moverDerecha();
                } else {
                    this.entidad.detener();
                }
                return true;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                this.derechaPresionada = false;
                if (this.izquierdaPresionada) {
                    this.entidad.moverIzquierda();
                } else {
                    this.entidad.detener();
                }
                return true;
            default:
                return false;
        }
    }
}
