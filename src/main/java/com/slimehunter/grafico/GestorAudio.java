package com.slimehunter.grafico;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GestorAudio {

    private static GestorAudio instancia;

    private Music musicaFondo;
    private Sound sonidoSaltar;
    private Sound sonidoAtacar;
    private Sound sonidoRecibirDano;
    private Sound sonidoMorirJugador;
    private Sound sonidoGolpearEnemigo;
    private Sound sonidoMorirEnemigo;
    private Sound sonidoVictoria;
    private Sound sonidoDerrota;

    private boolean mute = false;
    private float volumenMusica = 0.5f;
    private float volumenEfectos = 0.8f;

    private GestorAudio() {
        cargarAudios();
    }

    public static GestorAudio getInstancia() {
        if (instancia == null) {
            instancia = new GestorAudio();
        }
        return instancia;
    }

    private void cargarAudios() {
        this.musicaFondo = cargarMusica("audio/14. Village of Lupia.mp3");
        this.sonidoSaltar = cargarSound("audio/joentnt-walk-on-dirt-1-291981.mp3");
        this.sonidoAtacar = cargarSound("audio/dragon-studio-sword-slice-393847.mp3");
        this.sonidoRecibirDano = cargarSound("audio/freesound_community-damage-40114.mp3");
        this.sonidoMorirJugador = cargarSound("audio/phatphrogstudio-defeat-outros-game-sounds-collection-477823.mp3");
        this.sonidoGolpearEnemigo = cargarSound("audio/universfield-slime-impact-352473.mp3");
        this.sonidoMorirEnemigo = cargarSound("audio/freesound_gamestudio-button-394464.mp3");
        this.sonidoVictoria = cargarSound("audio/freesound_community-goodresult-82807.mp3");
        this.sonidoDerrota = cargarSound("audio/phatphrogstudio-defeat-outros-game-sounds-collection-477823.mp3");
    }

    private Music cargarMusica(String ruta) {
        try {
            if (Gdx.files.internal(ruta).exists()) {
                return Gdx.audio.newMusic(Gdx.files.internal(ruta));
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar musica: " + ruta);
        }
        return null;
    }

    private Sound cargarSound(String ruta) {
        try {
            if (Gdx.files.internal(ruta).exists()) {
                return Gdx.audio.newSound(Gdx.files.internal(ruta));
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar sonido: " + ruta);
        }
        return null;
    }

    public void reproducirMusica() {
        if (this.musicaFondo != null && !this.mute) {
            this.musicaFondo.setLooping(true);
            this.musicaFondo.setVolume(this.volumenMusica);
            this.musicaFondo.play();
        }
    }

    public void detenerMusica() {
        if (this.musicaFondo != null) {
            this.musicaFondo.stop();
        }
    }

    public void saltar() { reproducir(this.sonidoSaltar); }
    public void atacar() { reproducir(this.sonidoAtacar); }
    public void recibirDano() { reproducir(this.sonidoRecibirDano); }
    public void morirJugador() { reproducir(this.sonidoMorirJugador); }
    public void golpearEnemigo() { reproducir(this.sonidoGolpearEnemigo); }
    public void morirEnemigo() { reproducir(this.sonidoMorirEnemigo); }
    public void victoria() { reproducir(this.sonidoVictoria); }
    public void derrota() { reproducir(this.sonidoDerrota); }

    private void reproducir(Sound sonido) {
        if (sonido != null && !this.mute) {
            sonido.play(this.volumenEfectos);
        }
    }

    public void toggleMute() {
        this.mute = !this.mute;
        if (this.mute) {
            detenerMusica();
        } else {
            reproducirMusica();
        }
    }

    public boolean isMute() { return this.mute; }

    public void setVolumenMusica(float volumen) {
        this.volumenMusica = volumen;
        if (this.musicaFondo != null) {
            this.musicaFondo.setVolume(volumen);
        }
    }

    public void setVolumenEfectos(float volumen) {
        this.volumenEfectos = volumen;
    }

    public void dispose() {
        if (this.musicaFondo != null) this.musicaFondo.dispose();
        if (this.sonidoSaltar != null) this.sonidoSaltar.dispose();
        if (this.sonidoAtacar != null) this.sonidoAtacar.dispose();
        if (this.sonidoRecibirDano != null) this.sonidoRecibirDano.dispose();
        if (this.sonidoMorirJugador != null) this.sonidoMorirJugador.dispose();
        if (this.sonidoGolpearEnemigo != null) this.sonidoGolpearEnemigo.dispose();
        if (this.sonidoMorirEnemigo != null) this.sonidoMorirEnemigo.dispose();
        if (this.sonidoVictoria != null) this.sonidoVictoria.dispose();
        if (this.sonidoDerrota != null) this.sonidoDerrota.dispose();
    }
}
