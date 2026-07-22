package com.slimehunter.pantalla;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import com.slimehunter.Constantes;
import com.slimehunter.SlimeHunter;
import com.slimehunter.entidad.Jugador;
import com.slimehunter.input.ManejadorEntrada;

public class PantallaJuego implements Screen {

    private final SlimeHunter juego;
    private Jugador jugador;
    private ManejadorEntrada manejadorEntrada;

    public PantallaJuego(SlimeHunter juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        float centroX = Gdx.graphics.getWidth() / 2f;
        float centroY = Gdx.graphics.getHeight() / 2f;

        this.manejadorEntrada = new ManejadorEntrada();
        this.jugador = new Jugador(centroX, centroY, this.manejadorEntrada);
        Gdx.input.setInputProcessor(this.manejadorEntrada);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
            Constantes.COLOR_FONDO_R,
            Constantes.COLOR_FONDO_G,
            Constantes.COLOR_FONDO_B,
            Constantes.COLOR_FONDO_A
        );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        jugador.actualizar(delta);

        juego.getBatch().begin();
        jugador.render(juego.getBatch());
        juego.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        jugador.dispose();
    }
}
