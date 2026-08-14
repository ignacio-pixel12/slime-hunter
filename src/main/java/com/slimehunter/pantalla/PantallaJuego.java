package com.slimehunter.pantalla;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

import com.slimehunter.Constantes;
import com.slimehunter.SlimeHunter;
import com.slimehunter.entidad.Enemigo;
import com.slimehunter.entidad.Entidad;
import com.slimehunter.entidad.Jugador;
import com.slimehunter.grafico.CamaraJuego;
import com.slimehunter.grafico.DebugColisiones;
import com.slimehunter.input.ManejadorEntrada;
import com.slimehunter.mapa.MapaJuego;

import java.util.ArrayList;
import java.util.List;

public class PantallaJuego implements Screen {

    private final SlimeHunter juego;
    private Jugador jugador;
    private MapaJuego mapa;
    private CamaraJuego camara;
    private ManejadorEntrada manejadorEntrada;
    private DebugColisiones debugColisiones;
    private InterfazHUD hud;
    private List<Enemigo> enemigos;

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

        this.enemigos = new ArrayList<>();
        this.enemigos.add(new Enemigo(500, 600, 400, 700));

        this.debugColisiones = new DebugColisiones();
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

        for (Enemigo enemigo : this.enemigos) {
            enemigo.actualizar(delta, this.mapa.obtenerColisiones());
        }

        Rectangle hitboxAtaque = this.jugador.obtenerHitboxAtaque();
        if (hitboxAtaque != null) {
            for (Enemigo enemigo : this.enemigos) {
                if (!enemigo.estaMuerto()) {
                    Rectangle hurtbox = enemigo.obtenerHurtbox();
                    if (hurtbox != null && hurtbox.overlaps(hitboxAtaque)) {
                        enemigo.recibirDano(1);
                    }
                }
            }
        }

        if (!this.jugador.estaMuerto()) {
            Rectangle hurtboxJugador = this.jugador.obtenerHurtbox();
            if (hurtboxJugador != null) {
                for (Enemigo enemigo : this.enemigos) {
                    if (!enemigo.estaMuerto()) {
                        Rectangle hurtbox = enemigo.obtenerHurtbox();
                        if (hurtbox != null && hurtbox.overlaps(hurtboxJugador)) {
                            this.jugador.recibirDano(Constantes.SLIME_DANO);
                        }
                    }
                }
            }
        }

        this.mapa.renderizarColisiones(matrizMundo);

        if (this.manejadorEntrada.debeMostrarDebug()) {
            List<Entidad> entidadesDebug = new ArrayList<>();
            entidadesDebug.add(this.jugador);
            for (Enemigo enemigo : this.enemigos) {
                if (!enemigo.estaMuerto()) {
                    entidadesDebug.add(enemigo);
                }
            }
            this.debugColisiones.renderizar(entidadesDebug, matrizMundo);
        }

        this.juego.getBatch().begin();
        this.jugador.render(this.juego.getBatch());
        for (Enemigo enemigo : this.enemigos) {
            enemigo.render(this.juego.getBatch());
        }
        this.juego.getBatch().end();

        Matrix4 matrizPantalla = new Matrix4();
        matrizPantalla.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.hud.renderizar(this.jugador, matrizPantalla);

        this.enemigos.removeIf(Enemigo::debeEliminar);
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
        this.hud.dispose();
        for (Enemigo enemigo : this.enemigos) {
            enemigo.dispose();
        }
    }
}
