package com.slimehunter.entidad;

import com.badlogic.gdx.math.Rectangle;
import com.slimehunter.Constantes;
import com.slimehunter.estado.TablaEstados;
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

    private float limiteIzquierdo;
    private float limiteDerecho;
    private boolean yendoDerecha;

    private float temporizadorAccion;
    private static final float DURACION_HIT = 0.5f;
    private float tiempoHit;

    public JefeFinal(float x, float y, float limiteIzq, float limiteDer,
                     GestorSprites gestorSprites, GestorCajas gestorCajas) {
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
        this.limiteIzquierdo = limiteIzq;
        this.limiteDerecho = limiteDer;
        this.yendoDerecha = true;
        this.temporizadorAccion = 0f;
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

        this.voltearSprite(this.yendoDerecha);
    }

    private void updateCaminando(float delta) {
        this.temporizadorAccion += delta;

        if (this.temporizadorAccion >= Constantes.JEFE_INTERVALO_ACCION) {
            this.temporizadorAccion = 0f;
            double rand = Math.random();
            if (rand < 0.6) {
                this.saltar();
            } else {
                if (this.yendoDerecha) {
                    this.mover(Constantes.JEFE_VELOCIDAD);
                } else {
                    this.mover(-Constantes.JEFE_VELOCIDAD);
                }
            }
        }

        if (this.posicion.x >= this.limiteDerecho) {
            this.yendoDerecha = false;
        } else if (this.posicion.x <= this.limiteIzquierdo) {
            this.yendoDerecha = true;
        }

        if (this.yendoDerecha) {
            this.mover(Constantes.JEFE_VELOCIDAD);
        } else {
            this.mover(-Constantes.JEFE_VELOCIDAD);
        }

        this.tiempoAnimacion += delta;
        this.setRegion(this.gestorSprites.obtenerFrame("caminar", this.tiempoAnimacion));
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
        return this.getTablaEstados().getEstadoActual() == EstadoAnimacion.RECIBIENDO_DANO ? "hit" : "caminar";
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
