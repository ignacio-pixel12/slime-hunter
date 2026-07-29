package com.slimehunter.entidad;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slimehunter.Constantes;
import com.slimehunter.grafico.GestorSprites;

public class Enemigo extends EntidadDinamica {

	private GestorSprites gestorSprites;
	private float tiempoAnimacion;
	private int vida;
	private int maxVida;
	private boolean muerto;
	private float tiempoMuerte;
	private float limiteIzquierdo;
	private float limiteDerecho;
	private boolean yendoDerecha;

	@Override
	protected void actualizarEstado(float delta) {
		if (this.muerto) {
			this.tiempoMuerte += delta;
			float alpha = 1f - (this.tiempoMuerte / 0.3f);
			setAlpha(Math.max(0, alpha));
			return;
		}

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
		this.voltearSprite(this.yendoDerecha);
	}

	private static GestorSprites gestorCache;

	public Enemigo(float x, float y, float limiteIzq, float limiteDer) {
		super(obtenerFrameInicial(), x, y, Constantes.SLIME_ANCHO_COLISION, Constantes.SLIME_ALTO_COLISION,
				Constantes.SLIME_VELOCIDAD, 8f, 2, null);

		this.gestorSprites = gestorCache;
		gestorCache = null;
		this.tiempoAnimacion = 0f;
		this.vida = Constantes.SLIME_VIDA_MAXIMA;
		this.maxVida = Constantes.SLIME_VIDA_MAXIMA;
		this.muerto = false;
		this.tiempoMuerte = 0f;
		this.limiteIzquierdo = limiteIzq;
		this.limiteDerecho = limiteDer;
		this.yendoDerecha = true;

		setSize(64 * Constantes.SLIME_ESCALA, 64 * Constantes.SLIME_ESCALA);
		setOriginCenter();
	}

	private static TextureRegion obtenerFrameInicial() {
		gestorCache = new GestorSprites("slime1-sheet.png", "slime1-data.json");
		return gestorCache.obtenerFrameInactivo();

	}

	public void recibirDano(int cantidad) {
		if (this.muerto)
			return;
		this.vida -= cantidad;
		if (this.vida <= 0) {
			this.vida = 0;
			this.muerto = true;
			this.detener();
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

	@Override
	public void render(SpriteBatch batch) {
		float anchoSprite = getWidth() * getScaleX();
		float altoSprite = getHeight() * getScaleY();
		float offsetX = (anchoSprite - this.anchoColision) / 2f;
		float offsetY = 24f;
		setPosition(this.posicion.x - offsetX, this.posicion.y - offsetY);
		draw(batch);
	}

	public void dispose() {
		this.gestorSprites.dispose();
	}
}
