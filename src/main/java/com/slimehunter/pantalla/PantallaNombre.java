package com.slimehunter.pantalla;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Matrix4;

import com.slimehunter.SlimeHunter;

public class PantallaNombre implements Screen {

    private final SlimeHunter juego;
    private String nombre = "";
    private boolean confirmado = false;
    private BitmapFont fontTitulo;
    private BitmapFont fontInput;
    private BitmapFont fontHint;

    public PantallaNombre(SlimeHunter juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        this.nombre = "";
        this.confirmado = false;
        this.fontTitulo = new BitmapFont();
        this.fontInput = new BitmapFont();
        this.fontHint = new BitmapFont();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char character) {
                if (confirmado) return false;
                if (character == '\b') {
                    if (nombre.length() > 0) {
                        nombre = nombre.substring(0, nombre.length() - 1);
                    }
                } else if (character == '\r' || character == '\n') {
                    if (!nombre.trim().isEmpty()) {
                        confirmado = true;
                        juego.setScreen(new PantallaJuego(juego, nombre.trim()));
                    }
                } else if (character >= 32 && character < 127 && nombre.length() < 20) {
                    nombre += character;
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float centroX = Gdx.graphics.getWidth() / 2f;
        float centroY = Gdx.graphics.getHeight() / 2f;

        this.juego.getBatch().setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        this.juego.getBatch().begin();

        this.fontTitulo.setColor(Color.WHITE);
        this.fontTitulo.getData().setScale(2.5f);
        GlyphLayout tituloLayout = new GlyphLayout(this.fontTitulo, "Slime Hunter");
        this.fontTitulo.draw(this.juego.getBatch(), tituloLayout,
            centroX - tituloLayout.width / 2f, centroY + 150);

        this.fontHint.setColor(Color.GRAY);
        this.fontHint.getData().setScale(1.2f);
        GlyphLayout hintLayout = new GlyphLayout(this.fontHint, "Escribe tu nombre y presiona ENTER");
        this.fontHint.draw(this.juego.getBatch(), hintLayout,
            centroX - hintLayout.width / 2f, centroY + 80);

        this.fontInput.setColor(Color.YELLOW);
        this.fontInput.getData().setScale(1.8f);
        String textoMostrar = this.nombre + "_";
        GlyphLayout inputLayout = new GlyphLayout(this.fontInput, textoMostrar);
        this.fontInput.draw(this.juego.getBatch(), textoMostrar,
            centroX - inputLayout.width / 2f, centroY);

        this.fontHint.setColor(Color.LIGHT_GRAY);
        this.fontHint.getData().setScale(1f);
        GlyphLayout jugarLayout = new GlyphLayout(this.fontHint, "[ Presiona ENTER para jugar ]");
        this.fontHint.draw(this.juego.getBatch(), jugarLayout,
            centroX - jugarLayout.width / 2f, centroY - 80);

        this.juego.getBatch().end();
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
        this.fontInput.dispose();
        this.fontHint.dispose();
    }
}
