package com.slimehunter.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class ManejadorEntrada extends InputAdapter implements Entrada {

    private boolean izquierdaPresionada;
    private boolean derechaPresionada;
    private boolean espacioPresionado;
    private boolean atacando;

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                this.izquierdaPresionada = true;
                return true;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                this.derechaPresionada = true;
                return true;
            case Input.Keys.SPACE:
                this.espacioPresionado = true;
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
                return true;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                this.derechaPresionada = false;
                return true;
            case Input.Keys.SPACE:
                this.espacioPresionado = false;
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            this.atacando = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean debeMoverIzquierda() {
        return this.izquierdaPresionada;
    }

    @Override
    public boolean debeMoverDerecha() {
        return this.derechaPresionada;
    }

    @Override
    public boolean debeSaltar() {
        return this.espacioPresionado;
    }

    @Override
    public boolean debeAtacar() {
        boolean resultado = this.atacando;
        this.atacando = false;
        return resultado;
    }
}
