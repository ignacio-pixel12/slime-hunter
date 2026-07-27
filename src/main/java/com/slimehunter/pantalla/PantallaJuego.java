package com.slimehunter.pantalla;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

import com.slimehunter.Constantes;
import com.slimehunter.SlimeHunter;
import com.slimehunter.entidad.Jugador;
import com.slimehunter.grafico.CamaraJuego;
import com.slimehunter.grafico.DebugColisiones;
import com.slimehunter.input.ManejadorEntrada;
import com.slimehunter.mapa.MapaJuego;

import java.util.List;

public class PantallaJuego implements Screen {

    private final SlimeHunter juego;
    private Jugador jugador;
    private MapaJuego mapa;
    private CamaraJuego camara;
    private ManejadorEntrada manejadorEntrada;
    private DebugColisiones debugColisiones;
    private ShapeRenderer shapeRendererAtaque;
    private InterfazHUD hud;

    public PantallaJuego(SlimeHunter juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        this.manejadorEntrada = new ManejadorEntrada();

        this.mapa = new MapaJuego();
        this.mapa.cargar("mapa-test.tmx");

        this.camara = new CamaraJuego(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.jugador = new Jugador(200, 600, this.manejadorEntrada);

        this.debugColisiones = new DebugColisiones();
        this.shapeRendererAtaque = new ShapeRenderer();
        this.hud = new InterfazHUD();
        Gdx.input.setInputProcessor(this.manejadorEntrada);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.camara.seguir(this.jugador.getPosicion());

        Matrix4 matrizMundo = this.camara.getCamara().combined;
        this.juego.getBatch().setProjectionMatrix(matrizMundo);

        this.jugador.actualizar(delta, this.mapa.obtenerColisiones());

        this.mapa.renderizarColisiones(matrizMundo);

        this.debugColisiones.renderizar(List.of(this.jugador), matrizMundo);

        this.renderHitboxAtaque();

        this.juego.getBatch().begin();
        this.jugador.render(this.juego.getBatch());
        this.juego.getBatch().end();

        Matrix4 matrizPantalla = new Matrix4();
        matrizPantalla.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.hud.renderizar(this.jugador, matrizPantalla);
    }

    private void renderHitboxAtaque() {
        Rectangle hitbox = this.jugador.obtenerHitboxAtaque();
        if (hitbox != null) {
            this.shapeRendererAtaque.setProjectionMatrix(this.camara.getCamara().combined);
            this.shapeRendererAtaque.begin(ShapeRenderer.ShapeType.Line);
            this.shapeRendererAtaque.setColor(Color.YELLOW);
            this.shapeRendererAtaque.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
            this.shapeRendererAtaque.end();
        }
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
        this.mapa.dispose();
        this.debugColisiones.dispose();
        this.shapeRendererAtaque.dispose();
        this.hud.dispose();
    }
}
