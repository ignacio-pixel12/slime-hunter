package com.slimehunter.pantalla;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import com.slimehunter.Constantes;
import com.slimehunter.SlimeHunter;
import com.slimehunter.entidad.Entidad;
import com.slimehunter.entidad.Jugador;
import com.slimehunter.grafico.DebugColisiones;
import com.slimehunter.input.ManejadorEntrada;
import com.slimehunter.mapa.Suelo;

import java.util.List;

public class PantallaJuego implements Screen {

    private final SlimeHunter juego;
    private Jugador jugador;
    private Suelo suelo;
    private ManejadorEntrada manejadorEntrada;
    private DebugColisiones debugColisiones;

    public PantallaJuego(SlimeHunter juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        float centroX = Gdx.graphics.getWidth() / 2f;
        float centroY = Gdx.graphics.getHeight() / 2f;

        this.manejadorEntrada = new ManejadorEntrada();
        this.jugador = new Jugador(centroX, centroY + 200, this.manejadorEntrada);
        this.suelo = new Suelo(0, 0, Gdx.graphics.getWidth() * 2, Constantes.NIVEL_SUELO);
        this.debugColisiones = new DebugColisiones();
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

        this.jugador.actualizar(delta, List.of(this.suelo));

        this.juego.getBatch().begin();
        this.suelo.render(this.juego.getBatch());
        this.jugador.render(this.juego.getBatch());
        this.juego.getBatch().end();

        this.debugColisiones.renderizar(List.of(this.jugador, this.suelo));
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
        this.jugador.dispose();
        this.debugColisiones.dispose();
    }
}
