package com.slimehunter.entidad;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.slimehunter.Constantes;
import com.slimehunter.estado.TablaEstados;
import com.slimehunter.grafico.Direccion;
import com.slimehunter.grafico.EstadoAnimacion;
import com.slimehunter.grafico.GestorCajas;
import com.slimehunter.grafico.GestorSprites;
import com.slimehunter.grafico.TipoCaja;

public class JefeFinal extends EntidadDinamica {

    private final GestorSprites gestorSprites;
    private final GestorCajas gestorCajas;
    private float tiempoAnimacion;

    private int vida;
    private int maxVida;
    private int dano;
    private boolean muerto;
    private float tiempoMuerte;

    private enum Fase {
        QUIETO,
        PERSEGUIR,
        ATACAR
    }

    private final Vector2 haciaJugador = new Vector2();
    private final Vector2 spawn = new Vector2();
    private boolean activado;
    private Fase fase;
    private float temporizadorAccion;
    private boolean yaAtaco;
    private String animacionActual;
    private static final float DURACION_HIT = 0.5f;
    private static final float UMBRAL_DIRECCION = 4f;
    private float tiempoHit;

    public JefeFinal(float x, float y, GestorSprites gestorSprites, GestorCajas gestorCajas) {
        super(gestorSprites.obtenerFrameInactivo(), x, y,
                Constantes.JEFE_ANCHO_COLISION, Constantes.JEFE_ALTO_COLISION,
                Constantes.JEFE_VELOCIDAD, 8f, EstadoAnimacion.values().length, null);

        this.gestorSprites = gestorSprites;
        this.gestorCajas = gestorCajas;
        this.tiempoAnimacion = 0f;
        this.vida = Constantes.JEFE_VIDA_MAXIMA;
        this.maxVida = Constantes.JEFE_VIDA_MAXIMA;
        this.dano = Constantes.JEFE_DANO;
        this.muerto = false;
        this.tiempoMuerte = 0f;
        this.spawn.set(x, y);
        this.activado = false;
        this.fase = Fase.PERSEGUIR;
        this.temporizadorAccion = 0f;
        this.yaAtaco = false;
        this.animacionActual = "caminar";
        this.tiempoHit = 0f;

        setSize(64 * Constantes.JEFE_ESCALA, 64 * Constantes.JEFE_ESCALA);
        setOriginCenter();

        this.registrarTransiciones();
        this.getTablaEstados().cambiarEstado(EstadoAnimacion.CAMINANDO);
    }

    private void registrarTransiciones() {
        TablaEstados t = this.getTablaEstados();
        t.registrarTransicion(EstadoAnimacion.INACTIVO, EstadoAnimacion.CAMINANDO);
        t.registrarTransicion(EstadoAnimacion.CAMINANDO, EstadoAnimacion.RECIBIENDO_DANO);
        t.registrarTransicion(EstadoAnimacion.RECIBIENDO_DANO, EstadoAnimacion.CAMINANDO);
    }

    @Override
    protected void actualizarEstado(float delta) {
        if (this.muerto) {
            this.tiempoMuerte += delta;
            float alpha = 1f - (this.tiempoMuerte / 0.5f);
            setAlpha(Math.max(0, alpha));
            return;
        }

        EstadoAnimacion estado = this.getTablaEstados().getEstadoActual();
        switch (estado) {
            case CAMINANDO: this.updateCaminando(delta); break;
            case RECIBIENDO_DANO: this.updateRecibiendoDano(delta); break;
            default: break;
        }

        this.mirarAlJugador();
    }

    public void fijarObjetivo(Vector2 posicionJugador) {
        this.haciaJugador.set(posicionJugador).sub(this.posicion);
    }

    private void updateCaminando(float delta) {
        if (!this.activado) {
            if (this.jugadorEnArena()) {
                this.activado = true;
            } else {
                this.detener();
                this.animacionActual = "caminar";
                this.setRegion(this.gestorSprites.obtenerFrame("caminar", 0f));
                return;
            }
        }

        if (!(this.fase == Fase.ATACAR && !this.estaEnRangoDeAtaque())) {
            this.temporizadorAccion += delta;
        }
        if (this.temporizadorAccion >= this.duracionFase()) {
            this.cambiarFase();
        }

        switch (this.fase) {
            case QUIETO:
                this.detener();
                if (this.enElSuelo) {
                    this.animacionActual = "caminar";
                    this.setRegion(this.gestorSprites.obtenerFrame("caminar", 0f));
                } else {
                    this.avanzarAnimacion(delta);
                }
                break;
            case PERSEGUIR:
                this.perseguir();
                this.saltarSiElJugadorEstaArriba();
                this.avanzarAnimacion(delta);
                break;
            case ATACAR:
                this.perseguir();
                if (!this.yaAtaco && this.estaEnRangoDeAtaque()) {
                    this.saltar();
                    this.yaAtaco = true;
                    this.tiempoAnimacion = 0f;
                } else if (!this.estaEnRangoDeAtaque()) {
                    this.saltarSiElJugadorEstaArriba();
                }
                this.avanzarAnimacion(delta);
                break;
        }
    }

    private void cambiarFase() {
        this.temporizadorAccion = 0f;
        switch (this.fase) {
            case QUIETO:
                this.fase = Fase.PERSEGUIR;
                break;
            case PERSEGUIR:
                this.fase = Fase.ATACAR;
                this.yaAtaco = false;
                break;
            case ATACAR:
                this.fase = Math.random() < 0.4 ? Fase.QUIETO : Fase.PERSEGUIR;
                break;
        }
    }

    private float duracionFase() {
        switch (this.fase) {
            case QUIETO:
                return Constantes.JEFE_DURACION_QUIETO;
            case ATACAR:
                return Constantes.JEFE_DURACION_ATACAR;
            default:
                return Constantes.JEFE_DURACION_PERSEGUIR;
        }
    }

    private boolean estaEnRangoDeAtaque() {
        return Math.abs(this.haciaJugador.x) <= Constantes.JEFE_RANGO_ATAQUE;
    }

    private boolean jugadorEnArena() {
        return Math.abs(this.haciaJugador.x) <= Constantes.JEFE_RANGO_ACTIVACION
                && Math.abs(this.haciaJugador.y) <= Constantes.JEFE_RANGO_ACTIVACION_Y;
    }

    private void perseguir() {
        float izquierda = this.spawn.x - Constantes.JEFE_LIMITE_ARENA;
        float derecha = this.spawn.x + Constantes.JEFE_LIMITE_ARENA;
        float objetivoX = MathUtils.clamp(this.posicion.x + this.haciaJugador.x, izquierda, derecha);
        float direccionX = objetivoX - this.posicion.x;
        if (Math.abs(direccionX) <= UMBRAL_DIRECCION) {
            this.detener();
            return;
        }
        this.mover(Math.signum(direccionX) * Constantes.JEFE_VELOCIDAD);
    }

    private void saltarSiElJugadorEstaArriba() {
        if (this.haciaJugador.y > Constantes.JEFE_ALTURA_SALTO
                && Math.abs(this.haciaJugador.x) < Constantes.JEFE_RANGO_SALTO_X) {
            this.saltar();
        }
    }

    private void mirarAlJugador() {
        if (Math.abs(this.haciaJugador.x) <= UMBRAL_DIRECCION) {
            return;
        }
        boolean derecha = this.haciaJugador.x > 0;
        this.direccion = derecha ? Direccion.DERECHA : Direccion.IZQUIERDA;
        this.voltearSprite(derecha);
    }

    private void avanzarAnimacion(float delta) {
        this.tiempoAnimacion += delta;
        if (!this.enElSuelo && this.gestorSprites.existeAnimacion("saltar")) {
            this.animacionActual = "saltar";
            this.setRegion(this.gestorSprites.obtenerFrame("saltar", this.tiempoAnimacion));
        } else {
            this.animacionActual = "caminar";
            this.setRegion(this.gestorSprites.obtenerFrame("caminar", this.tiempoAnimacion));
        }
    }

    private void updateRecibiendoDano(float delta) {
        this.detener();
        this.tiempoHit += delta;
        this.setRegion(this.gestorSprites.obtenerFrameSinLoop("hit", this.tiempoHit));

        if (this.tiempoHit >= DURACION_HIT) {
            this.tiempoHit = 0f;
            this.getTablaEstados().cambiarEstado(EstadoAnimacion.CAMINANDO);
        }
    }

    @Override
    public void saltar() {
        if (this.enElSuelo) {
            this.velocidad.y = Constantes.JEFE_FUERZA_SALTO;
            this.enElSuelo = false;
        }
    }

    public void recibirDano(int cantidad) {
        if (this.muerto) return;
        this.vida -= cantidad;
        if (this.vida <= 0) {
            this.vida = 0;
            this.muerto = true;
            this.detener();
        } else {
            this.tiempoHit = 0f;
            this.getTablaEstados().cambiarEstado(EstadoAnimacion.RECIBIENDO_DANO);
        }
    }

    public boolean estaMuerto() { return this.muerto; }
    public boolean debeEliminar() { return this.muerto && this.tiempoMuerte >= 0.5f; }
    public int getVida() { return this.vida; }
    public int getMaxVida() { return this.maxVida; }
    public int getDano() { return this.dano; }

    private String getNombreAnimacion() {
        if (this.getTablaEstados().getEstadoActual() == EstadoAnimacion.RECIBIENDO_DANO) {
            return "hit";
        }
        return this.animacionActual;
    }

    private int getFrameActual() {
        String nombre = this.getNombreAnimacion();
        if (!this.gestorSprites.existeAnimacion(nombre)) return 0;
        float tiempo = this.getTablaEstados().getEstadoActual() == EstadoAnimacion.RECIBIENDO_DANO
                ? this.tiempoHit : this.tiempoAnimacion;
        return this.gestorSprites.obtenerIndiceFrame(nombre, tiempo);
    }

    private float getEscala() { return Constantes.JEFE_ESCALA; }
    private float getAltoFrame() { return this.gestorSprites.getAltoFrame(); }

    private float getXInicioSprite() {
        float anchoSprite = getWidth() * getScaleX();
        return this.posicion.x - (anchoSprite - this.anchoColision) / 2f;
    }

    private float getYInicioSprite() {
        float altoSprite = getHeight() * getScaleY();
        return this.posicion.y - altoSprite / 2f;
    }

    public Rectangle obtenerHurtbox() {
        Rectangle caja = this.gestorCajas.getCaja(TipoCaja.HURTBOX,
                this.getNombreAnimacion(), this.getFrameActual());
        if (caja == null) {
            return new Rectangle(this.posicion.x, this.posicion.y,
                    this.anchoColision, this.altoColision);
        }
        Rectangle hurtbox = GestorCajas.convertirAMundo(caja, this.getXInicioSprite(), this.getYInicioSprite(),
                this.getEscala(), this.getAltoFrame());
        if (this.getDireccion() == com.slimehunter.grafico.Direccion.IZQUIERDA) {
            hurtbox = GestorCajas.voltearHorizontalmente(hurtbox, this.getXInicioSprite(), this.getAnchoSprite());
        }
        return hurtbox;
    }

    @Override
    public Rectangle obtenerLimites() {
        Rectangle caja = this.gestorCajas.getCaja(TipoCaja.COLBOX,
                this.getNombreAnimacion(), this.getFrameActual());
        if (caja == null) {
            return new Rectangle(this.posicion.x, this.posicion.y,
                    this.anchoColision, this.altoColision);
        }
        Rectangle limites = GestorCajas.convertirAMundo(caja, this.getXInicioSprite(), this.getYInicioSprite(),
                this.getEscala(), this.getAltoFrame());
        if (this.getDireccion() == com.slimehunter.grafico.Direccion.IZQUIERDA) {
            limites = GestorCajas.voltearHorizontalmente(limites, this.getXInicioSprite(), this.getAnchoSprite());
        }
        return limites;
    }

    private float getAnchoSprite() {
        return getWidth() * getScaleX();
    }
}
