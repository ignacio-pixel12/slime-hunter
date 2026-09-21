package com.slimehunter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.slimehunter.grafico.GestorAudio;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SlimeHunter extends Game {

    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new com.slimehunter.pantalla.PantallaNombre(this));
    }

    @Override
    public void setScreen(Screen screen) {
        Screen anterior = this.getScreen();
        if (anterior != null) {
            anterior.dispose();
        }
        super.setScreen(screen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        Screen actual = this.getScreen();
        if (actual != null) {
            actual.dispose();
        }
        GestorAudio.getInstancia().dispose();
        batch.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
