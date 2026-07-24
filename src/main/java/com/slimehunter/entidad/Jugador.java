package com.slimehunter.entidad;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import com.slimehunter.Constantes;
import com.slimehunter.estado.TablaEstados;
import com.slimehunter.grafico.Direccion;
import com.slimehunter.grafico.EstadoAnimacion;
import com.slimehunter.grafico.GestorSprites;
import com.slimehunter.input.Entrada;

public class Jugador extends EntidadDinamica {

    private static GestorSprites gestorCache;

    private final GestorSprites gestorSprites;
    private float tiempoAnimacion;

    private float tiempoAtaque;
    private boolean atacandoActivo;
    private float cooldownRestante;

    private static final float DURACION_ATAQUE = 0.64f;
    private static final float COOLDOWN_ATAQUE = 0.5f;
    private static final float ANCHO_ATAQUE = 80f;

    public Jugador(float x, float y, Entrada entrada) {
        super(obtenerFrameInicial(), x, y,
              Constantes.JUGADOR_ANCHO_COLISION, Constantes.JUGADOR_ALTO_COLISION,
              Constantes.ACELERACION_JUGADOR, Constantes.FRICCION_JUGADOR,
              EstadoAnimacion.values().length, entrada);
        this.gestorSprites = gestorCache;
        gestorCache = null;
        this.tiempoAnimacion = 0f;
        this.tiempoAtaque = 0f;
        this.atacandoActivo = false;
        this.cooldownRestante = 0f;

        setSize(this.gestorSprites.getAnchoFrame() * 3f, this.gestorSprites.getAltoFrame() * 3f);
        setOriginCenter();

        this.registrarTransiciones();
    }

    private static TextureRegion obtenerFrameInicial() {
        gestorCache = new GestorSprites(Constantes.ARCHIVO_SPRITE_KNIGHT, Constantes.ARCHIVO_DATA_KNIGHT);
        return gestorCache.obtenerFrameInactivo();
    }

    private void registrarTransiciones() {
        TablaEstados t = this.getTablaEstados();

        t.registrarTransicion(EstadoAnimacion.INACTIVO, EstadoAnimacion.CAMINANDO);
        t.registrarTransicion(EstadoAnimacion.INACTIVO, EstadoAnimacion.SALTANDO);
        t.registrarTransicion(EstadoAnimacion.INACTIVO, EstadoAnimacion.CAYENDO);
        t.registrarTransicion(EstadoAnimacion.INACTIVO, EstadoAnimacion.ATACANDO);

        t.registrarTransicion(EstadoAnimacion.CAMINANDO, EstadoAnimacion.INACTIVO);
        t.registrarTransicion(EstadoAnimacion.CAMINANDO, EstadoAnimacion.SALTANDO);
        t.registrarTransicion(EstadoAnimacion.CAMINANDO, EstadoAnimacion.CAYENDO);
        t.registrarTransicion(EstadoAnimacion.CAMINANDO, EstadoAnimacion.ATACANDO);

        t.registrarTransicion(EstadoAnimacion.SALTANDO, EstadoAnimacion.CAYENDO);
        t.registrarTransicion(EstadoAnimacion.SALTANDO, EstadoAnimacion.ATACANDO);

        t.registrarTransicion(EstadoAnimacion.CAYENDO, EstadoAnimacion.INACTIVO);
        t.registrarTransicion(EstadoAnimacion.CAYENDO, EstadoAnimacion.CAMINANDO);
        t.registrarTransicion(EstadoAnimacion.CAYENDO, EstadoAnimacion.ATACANDO);

        t.registrarTransicion(EstadoAnimacion.ATACANDO, EstadoAnimacion.INACTIVO);
        t.registrarTransicion(EstadoAnimacion.ATACANDO, EstadoAnimacion.CAMINANDO);
        t.registrarTransicion(EstadoAnimacion.ATACANDO, EstadoAnimacion.SALTANDO);
        t.registrarTransicion(EstadoAnimacion.ATACANDO, EstadoAnimacion.CAYENDO);
    }

    @Override
    protected void actualizarEstado(float delta) {
        this.tiempoAnimacion += delta;

        if (this.cooldownRestante > 0) {
            this.cooldownRestante -= delta;
        }

        EstadoAnimacion estado = this.getTablaEstados().getEstadoActual();
        switch (estado) {
            case INACTIVO: this.updateInactivo(delta); break;
            case CAMINANDO: this.updateCaminando(delta); break;
            case SALTANDO: this.updateSaltando(delta); break;
            case CAYENDO: this.updateCayendo(delta); break;
            case ATACANDO: this.updateAtacando(delta); break;
            default: break;
        }

        String nombre = this.getNombreAnimacion(estado);
        if (this.gestorSprites.existeAnimacion(nombre)) {
            if (estado == EstadoAnimacion.ATACANDO) {
                this.setRegion(this.gestorSprites.obtenerFrameSinLoop(nombre, this.tiempoAnimacion));
            } else {
                this.setRegion(this.gestorSprites.obtenerFrame(nombre, this.tiempoAnimacion));
            }
        }

        this.voltearSprite(this.getDireccion() == Direccion.DERECHA);
    }

    private void updateInactivo(float delta) {
        if (this.getTablaEstados().getEstadoAnterior() != EstadoAnimacion.INACTIVO) {
            this.setRegion(this.gestorSprites.obtenerFrame("inactivo", 0));
            this.detener();
        }

        Entrada e = this.getEntrada();
        if (e.debeAtacar() && this.cooldownRestante <= 0) {
            this.iniciarAtaque();
        } else if (e.debeSaltar()) {
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
        if (e.debeAtacar() && this.cooldownRestante <= 0) {
            this.iniciarAtaque();
        } else if (e.debeSaltar()) {
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

        Entrada e = this.getEntrada();
        if (e.debeAtacar() && this.cooldownRestante <= 0) {
            this.iniciarAtaque();
        }

        if (this.getVelocidad().y < 0) {
            this.getTablaEstados().cambiarEstado(EstadoAnimacion.CAYENDO);
        }
    }

    private void updateCayendo(float delta) {
        if (this.getTablaEstados().getEstadoAnterior() != EstadoAnimacion.CAYENDO) {
            this.setRegion(this.gestorSprites.obtenerFrame("caida", 0));
        }

        Entrada e = this.getEntrada();
        if (e.debeAtacar() && this.cooldownRestante <= 0) {
            this.iniciarAtaque();
        }

        if (this.estaEnElSuelo()) {
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

    private void updateAtacando(float delta) {
        this.tiempoAtaque += delta;

        float tiempoNormalizado = this.tiempoAtaque / DURACION_ATAQUE;
        this.atacandoActivo = (tiempoNormalizado >= 0.25f && tiempoNormalizado <= 0.75f);

        if (this.tiempoAtaque >= DURACION_ATAQUE) {
            this.atacandoActivo = false;
            this.tiempoAtaque = 0;
            if (this.estaEnElSuelo()) {
                if (Math.abs(this.getVelocidad().x) > 0.5f) {
                    this.getTablaEstados().cambiarEstado(EstadoAnimacion.CAMINANDO);
                } else {
                    this.getTablaEstados().cambiarEstado(EstadoAnimacion.INACTIVO);
                }
            } else {
                if (this.getVelocidad().y > 0) {
                    this.getTablaEstados().cambiarEstado(EstadoAnimacion.SALTANDO);
                } else {
                    this.getTablaEstados().cambiarEstado(EstadoAnimacion.CAYENDO);
                }
            }
        }
    }

    private void iniciarAtaque() {
        this.getTablaEstados().cambiarEstado(EstadoAnimacion.ATACANDO);
        this.tiempoAtaque = 0;
        this.tiempoAnimacion = 0;
        this.cooldownRestante = COOLDOWN_ATAQUE;
    }

    public Rectangle obtenerHitboxAtaque() {
        if (!this.atacandoActivo) return null;

        float altoAtaque = this.altoColision;
        float xAtaque;

        if (this.getDireccion() == Direccion.DERECHA) {
            xAtaque = this.posicion.x + this.anchoColision;
        } else {
            xAtaque = this.posicion.x - ANCHO_ATAQUE;
        }

        return new Rectangle(xAtaque, this.posicion.y, ANCHO_ATAQUE, altoAtaque);
    }

    public boolean estaAtacando() {
        return this.atacandoActivo;
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

    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    public void dispose() {
        this.gestorSprites.dispose();
    }

    public GestorSprites getGestorSprites() {
        return this.gestorSprites;
    }
}
