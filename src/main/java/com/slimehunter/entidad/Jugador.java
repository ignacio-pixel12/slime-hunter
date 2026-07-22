package com.slimehunter.entidad;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.slimehunter.Constantes;
import com.slimehunter.estado.TablaEstados;
import com.slimehunter.grafico.Direccion;
import com.slimehunter.grafico.EstadoAnimacion;
import com.slimehunter.grafico.GestorSprites;
import com.slimehunter.input.Entrada;

public class Jugador extends Entidad {

    private static GestorSprites gestorCache;

    private final GestorSprites gestorSprites;
    private float tiempoAnimacion;

    public Jugador(float x, float y, Entrada entrada) {
        super(obtenerFrameInicial(), x, y,
              Constantes.ACELERACION_JUGADOR, Constantes.FRICCION_JUGADOR,
              EstadoAnimacion.values().length, gestorCache, entrada);
        this.gestorSprites = gestorCache;
        gestorCache = null;
        this.tiempoAnimacion = 0f;

        setSize(this.gestorSprites.getAnchoFrame() * 3f, this.gestorSprites.getAltoFrame() * 3f);
        setOriginCenter();

        this.registrarTransiciones();
    }

    private static TextureRegion obtenerFrameInicial() {
        gestorCache = new GestorSprites(
            Constantes.ARCHIVO_SPRITE_KNIGHT,
            Constantes.ARCHIVO_DATA_KNIGHT
        );
        return gestorCache.obtenerFrameInactivo();
    }

    private void registrarTransiciones() {
        TablaEstados t = this.getTablaEstados();

        t.registrarTransicion(EstadoAnimacion.INACTIVO, EstadoAnimacion.CAMINANDO);
        t.registrarTransicion(EstadoAnimacion.INACTIVO, EstadoAnimacion.SALTANDO);
        t.registrarTransicion(EstadoAnimacion.INACTIVO, EstadoAnimacion.CAYENDO);

        t.registrarTransicion(EstadoAnimacion.CAMINANDO, EstadoAnimacion.INACTIVO);
        t.registrarTransicion(EstadoAnimacion.CAMINANDO, EstadoAnimacion.SALTANDO);
        t.registrarTransicion(EstadoAnimacion.CAMINANDO, EstadoAnimacion.CAYENDO);

        t.registrarTransicion(EstadoAnimacion.SALTANDO, EstadoAnimacion.CAYENDO);

        t.registrarTransicion(EstadoAnimacion.CAYENDO, EstadoAnimacion.INACTIVO);
        t.registrarTransicion(EstadoAnimacion.CAYENDO, EstadoAnimacion.CAMINANDO);
    }

    @Override
    protected void actualizarEstado(float delta) {
        this.tiempoAnimacion += delta;

        EstadoAnimacion estado = this.getTablaEstados().getEstadoActual();
        switch (estado) {
            case INACTIVO: this.updateInactivo(delta); break;
            case CAMINANDO: this.updateCaminando(delta); break;
            case SALTANDO: this.updateSaltando(delta); break;
            case CAYENDO: this.updateCayendo(delta); break;
            default: break;
        }

        String nombre = this.getNombreAnimacion(estado);
        if (this.gestorSprites.existeAnimacion(nombre)) {
            this.setRegion(this.gestorSprites.obtenerFrame(nombre, this.tiempoAnimacion));
        }

        this.voltearSprite(this.getDireccion() == Direccion.DERECHA);
    }

    private void updateInactivo(float delta) {
        if (this.getTablaEstados().getEstadoAnterior() != EstadoAnimacion.INACTIVO) {
            this.setRegion(this.gestorSprites.obtenerFrame("inactivo", 0));
            this.detener();
        }

        Entrada e = this.getEntrada();
        if (e.debeSaltar()) {
            this.saltar();
            this.getTablaEstados().cambiarEstado(EstadoAnimacion.SALTANDO);
        } else if (e.debeMoverIzquierda()) {
            this.mover(-Constantes.ACELERACION_JUGADOR);
            this.getTablaEstados().cambiarEstado(EstadoAnimacion.CAMINANDO);
        } else if (e.debeMoverDerecha()) {
            this.mover(Constantes.ACELERACION_JUGADOR);
            this.getTablaEstados().cambiarEstado(EstadoAnimacion.CAMINANDO);
        }
        
        if (this.getVelocidad().y < 0) {
            this.getTablaEstados().cambiarEstado(EstadoAnimacion.CAYENDO);
        }
    }

    private void updateCaminando(float delta) {
        if (this.getTablaEstados().getEstadoAnterior() != EstadoAnimacion.CAMINANDO) {
            this.setRegion(this.gestorSprites.obtenerFrame("caminar", 0));
        }

        Entrada e = this.getEntrada();
        if (e.debeSaltar()) {
            this.saltar();
            this.getTablaEstados().cambiarEstado(EstadoAnimacion.SALTANDO);
        } else if (e.debeMoverIzquierda()) {
            this.mover(-Constantes.ACELERACION_JUGADOR);
        } else if (e.debeMoverDerecha()) {
            this.mover(Constantes.ACELERACION_JUGADOR);
        } else {
            this.detener();
            this.getTablaEstados().cambiarEstado(EstadoAnimacion.INACTIVO);
        }
        if (this.getVelocidad().y < 0) {
            this.getTablaEstados().cambiarEstado(EstadoAnimacion.CAYENDO);
        }
    }

    private void updateSaltando(float delta) {
        if (this.getTablaEstados().getEstadoAnterior() != EstadoAnimacion.SALTANDO) {
            this.setRegion(this.gestorSprites.obtenerFrame("salto", 0));
        }

        if (this.getVelocidad().y < 0) {
            this.getTablaEstados().cambiarEstado(EstadoAnimacion.CAYENDO);
        }
    }

    private void updateCayendo(float delta) {
        if (this.getTablaEstados().getEstadoAnterior() != EstadoAnimacion.CAYENDO) {
            this.setRegion(this.gestorSprites.obtenerFrame("caida", 0));
        }

        if (this.estaEnElSuelo()) {
            Entrada e = this.getEntrada();
            if (e.debeMoverIzquierda()) {
                this.mover(-Constantes.ACELERACION_JUGADOR);
                this.getTablaEstados().cambiarEstado(EstadoAnimacion.CAMINANDO);
            } else if (e.debeMoverDerecha()) {
                this.mover(Constantes.ACELERACION_JUGADOR);
                this.getTablaEstados().cambiarEstado(EstadoAnimacion.CAMINANDO);
            } else {
                this.getTablaEstados().cambiarEstado(EstadoAnimacion.INACTIVO);
            }
        }
    }

    private String getNombreAnimacion(EstadoAnimacion estado) {
        return switch (estado) {
            case INACTIVO -> "inactivo";
            case CAMINANDO -> "caminar";
            case SALTANDO -> "salto";
            case CAYENDO -> "caida";
            case ATACANDO -> "ataque";
            case RECIBIENDO_DANO -> "hit";
            case MURIENDO -> "muerte";
            case DESPLAZANDO -> "dash";
        };
    }

    @Override
    protected TextureRegion obtenerFrameSegunEstado() {
        return null;
    }

    public void render(SpriteBatch batch) {
        draw(batch);
    }

    public void dispose() {
        this.gestorSprites.dispose();
    }

    public GestorSprites obtenerGestorSprites() {
        return this.gestorSprites;
    }
}
