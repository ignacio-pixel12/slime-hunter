package com.slimehunter.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import com.slimehunter.entidad.Entidad;

public class ManejadorEntrada extends InputAdapter {

    private final Entidad entidad;

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
                entidad.moverIzquierda();
                return true;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                entidad.moverDerecha();
                return true;
            case Input.Keys.SPACE:
                entidad.saltar();
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
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                entidad.detener();
                return true;
            default:
                return false;
        }
    }
}
