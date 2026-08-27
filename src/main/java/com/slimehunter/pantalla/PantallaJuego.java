package com.slimehunter.pantalla;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

import com.slimehunter.Constantes;
import com.slimehunter.SlimeHunter;
import com.slimehunter.entidad.Enemigo;
import com.slimehunter.entidad.EnemigoSaltarin;
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
    private final String nombreJugador;
    private Jugador jugador;
    private MapaJuego mapa;
    private CamaraJuego camara;
    private ManejadorEntrada manejadorEntrada;
    private DebugColisiones debugColisiones;
    private InterfazHUD hud;
    private List<Enemigo> enemigos;
    private List<EnemigoSaltarin> enemigosSaltarines;
    private float tiempoPartida;
    private boolean terminado;

    public PantallaJuego(SlimeHunter juego, String nombreJugador) {
        this.juego = juego;
        this.nombreJugador = nombreJugador;
    }

    @Override
    public void show() {
        this.manejadorEntrada = new ManejadorEntrada();
        this.tiempoPartida = 0f;
        this.terminado = false;

        this.mapa = new MapaJuego();
        this.mapa.cargar(Constantes.ARCHIVO_MAPA);

        this.camara = new CamaraJuego(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        com.badlogic.gdx.math.Vector2 spawn = this.mapa.obtenerSpawn();
        float spawnX = spawn != null ? spawn.x : Constantes.INICIO_JUGADOR_X;
        float spawnY = spawn != null ? spawn.y : Constantes.INICIO_JUGADOR_Y;

        this.jugador = new Jugador(spawnX, spawnY, this.manejadorEntrada);
        this.jugador.setPuntoAparicion(spawnX, spawnY);

        this.enemigos = new ArrayList<>();
        float rangoPatrulla = 50f;
        for (com.badlogic.gdx.math.Vector2 spawnEnemigo : this.mapa.obtenerSpawnsEnemigos()) {
            this.enemigos.add(new Enemigo(
                spawnEnemigo.x, spawnEnemigo.y,
                spawnEnemigo.x - rangoPatrulla, spawnEnemigo.x + rangoPatrulla));
        }

        this.enemigosSaltarines = new ArrayList<>();
        float rangoPatrullaSaltarin = 30f;
        for (com.badlogic.gdx.math.Vector2 spawnSaltarin : this.mapa.obtenerSpawnsEnemigosSaltarines()) {
            this.enemigosSaltarines.add(new EnemigoSaltarin(
                spawnSaltarin.x, spawnSaltarin.y,
                spawnSaltarin.x - rangoPatrullaSaltarin, spawnSaltarin.x + rangoPatrullaSaltarin));
        }

        this.debugColisiones = new DebugColisiones();
        this.hud = new InterfazHUD();
        Gdx.input.setInputProcessor(this.manejadorEntrada);
    }

    @Override
    public void render(float delta) {
        if (this.terminado) return;

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.tiempoPartida += delta;

        this.camara.getViewport().apply();
        this.camara.seguir(this.jugador.getPosicion());

        Matrix4 matrizMundo = this.camara.getCamara().combined;
        this.juego.getBatch().setProjectionMatrix(matrizMundo);

        boolean estabaVivo = !this.jugador.estaMuerto();
        this.jugador.actualizar(delta, this.mapa.obtenerColisiones(), this.mapa.obtenerPlataformas());

        for (Enemigo enemigo : this.enemigos) {
            enemigo.actualizar(delta, this.mapa.obtenerColisiones(), this.mapa.obtenerPlataformas());
        }

        for (EnemigoSaltarin saltarin : this.enemigosSaltarines) {
            saltarin.actualizar(delta, this.mapa.obtenerColisiones(), this.mapa.obtenerPlataformas());
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
            for (EnemigoSaltarin saltarin : this.enemigosSaltarines) {
                if (!saltarin.estaMuerto()) {
                    Rectangle hurtbox = saltarin.obtenerHurtbox();
                    if (hurtbox != null && hurtbox.overlaps(hitboxAtaque)) {
                        saltarin.recibirDano(1);
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
                for (EnemigoSaltarin saltarin : this.enemigosSaltarines) {
                    if (!saltarin.estaMuerto()) {
                        Rectangle hurtbox = saltarin.obtenerHurtbox();
                        if (hurtbox != null && hurtbox.overlaps(hurtboxJugador)) {
                            this.jugador.recibirDano(Constantes.SLIME_DANO);
                        }
                    }
                }
            }
        }

        this.mapa.render(this.camara.getCamara());

        if (this.manejadorEntrada.debeMostrarDebug()) {
            this.mapa.renderizarColisiones(matrizMundo);
            List<Entidad> entidadesDebug = new ArrayList<>();
            entidadesDebug.add(this.jugador);
            for (Enemigo enemigo : this.enemigos) {
                if (!enemigo.estaMuerto()) {
                    entidadesDebug.add(enemigo);
                }
            }
            for (EnemigoSaltarin saltarin : this.enemigosSaltarines) {
                if (!saltarin.estaMuerto()) {
                    entidadesDebug.add(saltarin);
                }
            }
            this.debugColisiones.renderizar(entidadesDebug, matrizMundo);
        }

        this.juego.getBatch().begin();
        this.jugador.render(this.juego.getBatch());
        for (Enemigo enemigo : this.enemigos) {
            enemigo.render(this.juego.getBatch());
        }
        for (EnemigoSaltarin saltarin : this.enemigosSaltarines) {
            saltarin.render(this.juego.getBatch());
        }
        this.juego.getBatch().end();

        Matrix4 matrizPantalla = new Matrix4();
        matrizPantalla.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.hud.renderizar(this.jugador, matrizPantalla);

        this.enemigos.removeIf(Enemigo::debeEliminar);
        this.enemigosSaltarines.removeIf(EnemigoSaltarin::debeEliminar);

        if (estabaVivo && this.jugador.estaMuerto()) {
            this.terminado = true;
            this.juego.setScreen(new PantallaDerrota(this.juego, this.nombreJugador, this.tiempoPartida));
        }
    }

    @Override
    public void resize(int width, int height) {
        this.camara.redimensionar(width, height);
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
        for (EnemigoSaltarin saltarin : this.enemigosSaltarines) {
            saltarin.dispose();
        }
    }
}
