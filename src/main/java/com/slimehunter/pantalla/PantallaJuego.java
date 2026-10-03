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
import com.slimehunter.entidad.JefeFinal;
import com.slimehunter.grafico.GestorAudio;
import com.slimehunter.entidad.Jugador;
import com.slimehunter.grafico.CamaraJuego;
import com.slimehunter.grafico.DebugColisiones;
import com.slimehunter.input.ManejadorEntrada;
import com.slimehunter.mapa.MapaJuego;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private JefeFinal jefe;
    private float tiempoPartida;
    private boolean terminado;
    private static final int ID_JEFE_GOLPEADO = 2000;
    private final Set<Integer> enemigosGolpeados = new HashSet<>();
    private boolean ataqueAnteriorActivo = false;

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
        this.camara.setLimites(0, this.mapa.obtenerAnchoMapa(), 0, this.mapa.obtenerAltoMapa());

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
                spawnEnemigo.x - rangoPatrulla, spawnEnemigo.x + rangoPatrulla,
                this.mapa.getGestorSpritesSlime1(), this.mapa.getGestorCajasSlime1()));
        }

        this.enemigosSaltarines = new ArrayList<>();
        float rangoPatrullaSaltarin = 30f;
        for (com.badlogic.gdx.math.Vector2 spawnSaltarin : this.mapa.obtenerSpawnsEnemigosSaltarines()) {
            this.enemigosSaltarines.add(new EnemigoSaltarin(
                spawnSaltarin.x, spawnSaltarin.y,
                spawnSaltarin.x - rangoPatrullaSaltarin, spawnSaltarin.x + rangoPatrullaSaltarin,
                this.mapa.getGestorSpritesSlime2(), this.mapa.getGestorCajasSlime2()));
        }

        this.debugColisiones = new DebugColisiones();
        this.hud = new InterfazHUD();

        com.badlogic.gdx.math.Vector2 spawnJefe = this.mapa.obtenerSpawnJefe();
        if (spawnJefe != null) {
            this.jefe = new JefeFinal(spawnJefe.x, spawnJefe.y,
                this.mapa.getGestorSpritesJefe(), this.mapa.getGestorCajasJefe());
        }

        Gdx.input.setInputProcessor(this.manejadorEntrada);
        GestorAudio.getInstancia().reproducirMusica();
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

        if (this.jefe != null && !this.jefe.estaMuerto()) {
            this.jefe.fijarObjetivo(this.jugador.getPosicion());
            this.jefe.actualizar(delta, this.mapa.obtenerColisiones(), this.mapa.obtenerPlataformas());
        }

        Rectangle hitboxAtaque = this.jugador.obtenerHitboxAtaque();
        boolean ataqueActivo = hitboxAtaque != null;
        if (ataqueActivo && !this.ataqueAnteriorActivo) {
            this.enemigosGolpeados.clear();
        }
        this.ataqueAnteriorActivo = ataqueActivo;

        if (hitboxAtaque != null) {
            int indice = 0;
            for (Enemigo enemigo : this.enemigos) {
                if (!enemigo.estaMuerto() && !this.enemigosGolpeados.contains(indice)) {
                    Rectangle hurtbox = enemigo.obtenerHurtbox();
                    if (hurtbox != null && hurtbox.overlaps(hitboxAtaque)) {
                        enemigo.recibirDano(this.jugador.getDano());
                        this.enemigosGolpeados.add(indice);
                    }
                }
                indice++;
            }
            indice = 0;
            for (EnemigoSaltarin saltarin : this.enemigosSaltarines) {
                if (!saltarin.estaMuerto() && !this.enemigosGolpeados.contains(1000 + indice)) {
                    Rectangle hurtbox = saltarin.obtenerHurtbox();
                    if (hurtbox != null && hurtbox.overlaps(hitboxAtaque)) {
                        saltarin.recibirDano(this.jugador.getDano());
                        this.enemigosGolpeados.add(1000 + indice);
                    }
                }
                indice++;
            }
            if (this.jefe != null && !this.jefe.estaMuerto()
                    && !this.enemigosGolpeados.contains(ID_JEFE_GOLPEADO)) {
                Rectangle hurtboxJefe = this.jefe.obtenerHurtbox();
                if (hurtboxJefe != null && hurtboxJefe.overlaps(hitboxAtaque)) {
                    this.jefe.recibirDano(this.jugador.getDano());
                    this.enemigosGolpeados.add(ID_JEFE_GOLPEADO);
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
                            this.jugador.recibirDano(enemigo.getDano());
                        }
                    }
                }
                for (EnemigoSaltarin saltarin : this.enemigosSaltarines) {
                    if (!saltarin.estaMuerto()) {
                        Rectangle hurtbox = saltarin.obtenerHurtbox();
                        if (hurtbox != null && hurtbox.overlaps(hurtboxJugador)) {
                            this.jugador.recibirDano(saltarin.getDano());
                        }
                    }
                }
            }
        }

        if (!this.jugador.estaMuerto()) {
            Rectangle hurtboxJugador = this.jugador.obtenerHurtbox();
            if (hurtboxJugador != null) {
                for (Rectangle pinche : this.mapa.obtenerPinches()) {
                    if (hurtboxJugador.overlaps(pinche)) {
                        this.jugador.recibirDano(Constantes.PINCHES_DANO);
                        this.jugador.rebotar(400f);
                        break;
                    }
                }
            }
        }

        for (Enemigo enemigo : this.enemigos) {
            if (!enemigo.estaMuerto()) {
                Rectangle hurtboxEnemigo = enemigo.obtenerHurtbox();
                if (hurtboxEnemigo != null) {
                    for (Rectangle pinche : this.mapa.obtenerPinches()) {
                        if (hurtboxEnemigo.overlaps(pinche)) {
                            enemigo.recibirDano(Constantes.PINCHES_DANO);
                            break;
                        }
                    }
                }
            }
        }

        for (EnemigoSaltarin saltarin : this.enemigosSaltarines) {
            if (!saltarin.estaMuerto()) {
                Rectangle hurtboxSaltarin = saltarin.obtenerHurtbox();
                if (hurtboxSaltarin != null) {
                    for (Rectangle pinche : this.mapa.obtenerPinches()) {
                        if (hurtboxSaltarin.overlaps(pinche)) {
                            saltarin.recibirDano(Constantes.PINCHES_DANO);
                            break;
                        }
                    }
                }
            }
        }

        if (!this.jugador.estaMuerto()) {
            Rectangle hurtboxJugador = this.jugador.obtenerHurtbox();
            if (hurtboxJugador != null) {
                java.util.Map<String, Rectangle> hab = this.mapa.obtenerHabilidades();
                System.out.println("Habilidades: " + hab.size() + " hurtbox: " + hurtboxJugador);
                for (java.util.Map.Entry<String, Rectangle> entry : hab.entrySet()) {
                    System.out.println("  " + entry.getKey() + " -> " + entry.getValue() + " overlaps: " + hurtboxJugador.overlaps(entry.getValue()));
                }
                Iterator<Map.Entry<String, Rectangle>> it = hab.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Rectangle> entry = it.next();
                    if (hurtboxJugador.overlaps(entry.getValue())) {
                        switch (entry.getKey()) {
                            case "doble salto":
                                this.jugador.setTieneDobleSalto(true);
                                this.jugador.setSaltosRestantes(2);
                                break;
                            case "dash":
                                this.jugador.setTieneDash(true);
                                break;
                        }
                        GestorAudio.getInstancia().victoria();
                        it.remove();
                    }
                }
            }
        }

        if (this.jefe != null && !this.jefe.estaMuerto()) {
            Rectangle hurtboxJefe = this.jefe.obtenerHurtbox();
            Rectangle hurtboxJugador2 = this.jugador.obtenerHurtbox();
            if (hurtboxJugador2 != null && hurtboxJefe != null) {
                if (hurtboxJugador2.overlaps(hurtboxJefe)) {
                    this.jugador.recibirDano(this.jefe.getDano());
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
        if (this.jefe != null) {
            this.jefe.render(this.juego.getBatch());
        }
        this.juego.getBatch().end();

        Matrix4 matrizPantalla = new Matrix4();
        matrizPantalla.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.hud.renderizar(this.jugador, matrizPantalla);

        this.enemigos.removeIf(Enemigo::debeEliminar);
        this.enemigosSaltarines.removeIf(EnemigoSaltarin::debeEliminar);

        if (estabaVivo && this.jugador.estaMuerto()) {
            this.terminado = true;
            GestorAudio.getInstancia().detenerMusica();
            GestorAudio.getInstancia().derrota();
        }

        if (!this.terminado && this.jefe != null && this.jefe.estaMuerto()) {
            this.terminado = true;
            GestorAudio.getInstancia().detenerMusica();
            GestorAudio.getInstancia().victoria();
        }

        if (this.terminado && !this.jugador.estaEnAnimacionMuerte()) {
            if (this.jugador.estaMuerto()) {
                this.juego.setScreen(new PantallaDerrota(this.juego, this.nombreJugador, this.tiempoPartida));
            } else {
                this.juego.setScreen(new PantallaVictoria(this.juego, this.nombreJugador, this.tiempoPartida));
            }
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
        GestorAudio.getInstancia().detenerMusica();
    }

    @Override
    public void dispose() {
        this.jugador.dispose();
        this.mapa.dispose();
        this.debugColisiones.dispose();
        this.hud.dispose();
    }
}
