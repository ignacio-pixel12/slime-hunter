package com.slimehunter.pantalla;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Matrix4;

import com.slimehunter.SlimeHunter;

public class PantallaDerrota implements Screen {

    private final SlimeHunter juego;
    private final String nombreJugador;
    private final float tiempoPartida;
    private BitmapFont fontTitulo;
    private BitmapFont fontInfo;

    public PantallaDerrota(SlimeHunter juego, String nombreJugador, float tiempoPartida) {
        this.juego = juego;
        this.nombreJugador = nombreJugador;
        this.tiempoPartida = tiempoPartida;
    }

    @Override
    public void show() {
        this.fontTitulo = new BitmapFont();
        this.fontInfo = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.02f, 0.02f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float centroX = Gdx.graphics.getWidth() / 2f;
        float centroY = Gdx.graphics.getHeight() / 2f;

        this.juego.getBatch().setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        this.juego.getBatch().begin();

        this.fontTitulo.setColor(Color.RED);
        this.fontTitulo.getData().setScale(3f);
        GlyphLayout tituloLayout = new GlyphLayout(this.fontTitulo, "DERROTA");
        this.fontTitulo.draw(this.juego.getBatch(), tituloLayout,
            centroX - tituloLayout.width / 2f, centroY + 140);

        this.fontInfo.setColor(Color.WHITE);
        this.fontInfo.getData().setScale(1.5f);

        GlyphLayout nombreLayout = new GlyphLayout(this.fontInfo, "Jugador: " + this.nombreJugador);
        this.fontInfo.draw(this.juego.getBatch(), nombreLayout,
            centroX - nombreLayout.width / 2f, centroY + 60);

        String tiempoFormato = formatearTiempo(this.tiempoPartida);
        GlyphLayout tiempoLayout = new GlyphLayout(this.fontInfo, "Tiempo: " + tiempoFormato);
        this.fontInfo.draw(this.juego.getBatch(), tiempoLayout,
            centroX - tiempoLayout.width / 2f, centroY + 20);

        this.fontInfo.setColor(Color.LIGHT_GRAY);
        this.fontInfo.getData().setScale(1f);
        GlyphLayout reintentarLayout = new GlyphLayout(this.fontInfo, "[ Presiona ENTER para reintentar ]");
        this.fontInfo.draw(this.juego.getBatch(), reintentarLayout,
            centroX - reintentarLayout.width / 2f, centroY - 80);

        this.juego.getBatch().end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            this.juego.setScreen(new PantallaNombre(this.juego));
        }
    }

    private String formatearTiempo(float segundos) {
        int mins = (int) (segundos / 60);
        int secs = (int) (segundos % 60);
        return String.format("%d:%02d", mins, secs);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        this.fontTitulo.dispose();
        this.fontInfo.dispose();
    }
}
