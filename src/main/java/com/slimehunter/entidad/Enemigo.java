package com.slimehunter.entidad;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.slimehunter.Constantes;
import com.slimehunter.estado.TablaEstados;
import com.slimehunter.grafico.EstadoAnimacion;
import com.slimehunter.grafico.GestorCajas;
import com.slimehunter.grafico.GestorSprites;

public class Enemigo extends EntidadDinamica {

	private GestorSprites gestorSprites;
	private GestorCajas gestorCajas;
	private float tiempoAnimacion;
	private int vida;
	private int maxVida;
	private boolean muerto;
	private float tiempoMuerte;
	private float limiteIzquierdo;
	private float limiteDerecho;
	private boolean yendoDerecha;

	private static final float DURACION_HIT = 0.5f;
	private float tiempoHit;

	private static GestorSprites gestorCache;
	private static GestorCajas gestorCajasCache;

	public Enemigo(float x, float y, float limiteIzq, float limiteDer) {
		super(obtenerFrameInicial(), x, y, Constantes.SLIME_ANCHO_COLISION, Constantes.SLIME_ALTO_COLISION,
				Constantes.SLIME_VELOCIDAD, 8f, EstadoAnimacion.values().length, null);

		this.gestorSprites = gestorCache;
		this.gestorCajas = gestorCajasCache;
		gestorCache = null;
		gestorCajasCache = null;
		this.tiempoAnimacion = 0f;
		this.vida = Constantes.SLIME_VIDA_MAXIMA;
		this.maxVida = Constantes.SLIME_VIDA_MAXIMA;
		this.muerto = false;
		this.tiempoMuerte = 0f;
		this.limiteIzquierdo = limiteIzq;
		this.limiteDerecho = limiteDer;
		this.yendoDerecha = true;
		this.tiempoHit = 0f;

		setSize(64 * Constantes.SLIME_ESCALA, 64 * Constantes.SLIME_ESCALA);
		setOriginCenter();

		this.registrarTransiciones();
	}

	private static TextureRegion obtenerFrameInicial() {
		gestorCache = new GestorSprites("slime1-sheet.png", "slime1-data.json");
		gestorCajasCache = new GestorCajas("slime1-cajas.json");
		return gestorCache.obtenerFrameInactivo();
	}

	private void registrarTransiciones() {
		TablaEstados t = this.getTablaEstados();

		t.registrarTransicion(EstadoAnimacion.CAMINANDO, EstadoAnimacion.RECIBIENDO_DANO);

		t.registrarTransicion(EstadoAnimacion.RECIBIENDO_DANO, EstadoAnimacion.CAMINANDO);
	}

	@Override
	protected void actualizarEstado(float delta) {
		if (this.muerto) {
			this.tiempoMuerte += delta;
			float alpha = 1f - (this.tiempoMuerte / 0.3f);
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
		if (this.posicion.x >= this.limiteDerecho) {
			this.yendoDerecha = false;
		} else if (this.posicion.x <= this.limiteIzquierdo) {
			this.yendoDerecha = true;
		}

		if (this.yendoDerecha) {
			this.mover(Constantes.SLIME_VELOCIDAD);
		} else {
			this.mover(-Constantes.SLIME_VELOCIDAD);
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

	public void recibirDano(int cantidad) {
		if (this.muerto)
			return;
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

	public boolean estaMuerto() {
		return this.muerto;
	}

	public boolean debeEliminar() {
		return this.muerto && this.tiempoMuerte >= 0.3f;
	}

	public int getVida() {
		return this.vida;
	}

	public int getMaxVida() {
		return this.maxVida;
	}

	private String getNombreAnimacion() {
		return this.muerto ? "muerte" :
			this.getTablaEstados().getEstadoActual() == EstadoAnimacion.RECIBIENDO_DANO ? "hit" : "caminar";
	}

	private int getFrameActual() {
		String nombre = this.getNombreAnimacion();
		if (!this.gestorSprites.existeAnimacion(nombre)) {
			return 0;
		}
		float tiempo = this.getTablaEstados().getEstadoActual() == EstadoAnimacion.RECIBIENDO_DANO
				? this.tiempoHit : this.tiempoAnimacion;
		return this.gestorSprites.obtenerIndiceFrame(nombre, tiempo);
	}

	private float getEscala() {
		return Constantes.SLIME_ESCALA;
	}

	private float getAltoFrame() {
		return this.gestorSprites.getAltoFrame();
	}

	private float getXInicioSprite() {
		float anchoSprite = getWidth() * getScaleX();
		return this.posicion.x - (anchoSprite - this.anchoColision) / 2f;
	}

	private float getYInicioSprite() {
		float altoSprite = getHeight() * getScaleY();
		return this.posicion.y - altoSprite / 2f;
	}

    public Rectangle obtenerHurtbox() {
        Rectangle caja = this.gestorCajas.getCaja(GestorCajas.TipoCaja.HURTBOX,
                this.getNombreAnimacion(), this.getFrameActual());
        if (caja == null) {
            return null;
        }
        return GestorCajas.convertirAMundo(caja, this.getXInicioSprite(), this.getYInicioSprite(),
                this.getEscala(), this.getAltoFrame());
    }

    @Override
    public Rectangle obtenerLimites() {
        Rectangle caja = this.gestorCajas.getCaja(GestorCajas.TipoCaja.COLBOX,
                this.getNombreAnimacion(), this.getFrameActual());
        if (caja == null) {
            return null;
        }
        return GestorCajas.convertirAMundo(caja, this.getXInicioSprite(), this.getYInicioSprite(),
                this.getEscala(), this.getAltoFrame());
    }

	public void dispose() {
		this.gestorSprites.dispose();
	}
}
