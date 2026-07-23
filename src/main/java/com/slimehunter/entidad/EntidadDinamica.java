package com.slimehunter.entidad;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import com.slimehunter.Constantes;
import com.slimehunter.estado.TablaEstados;
import com.slimehunter.grafico.Direccion;
import com.slimehunter.input.Entrada;

public abstract class EntidadDinamica extends Entidad {

    protected final Vector2 velocidad;
    protected final Vector2 aceleracion;
    protected final Vector2 friccion;
    protected final TablaEstados tablaEstados;
    protected boolean enElSuelo;
    protected Direccion direccion;
    protected Entrada entrada;

    protected EntidadDinamica(TextureRegion region, float x, float y,
                              float anchoColision, float altoColision,
                              float aceleracionMovimiento, float friccionMovimiento,
                              int cantidadEstados, Entrada entrada) {
        super(region, x, y, anchoColision, altoColision);
        this.velocidad = new Vector2(0, 0);
        this.aceleracion = new Vector2(0, 0);
        this.friccion = new Vector2(friccionMovimiento, 0);
        this.tablaEstados = new TablaEstados(cantidadEstados);
        this.enElSuelo = true;
        this.direccion = Direccion.DERECHA;
        this.entrada = entrada;
    }

    public void actualizar(float delta, List<EntidadEstatica> entidadesEstaticas) {
        this.aceleracion.x = 0;

        actualizarEstado(delta);

        if (!this.enElSuelo) {
            this.aceleracion.y = -Constantes.GRAVEDAD;
        } else {
            this.velocidad.y = 0;
        }

        this.velocidad.x += this.aceleracion.x * delta;
        this.velocidad.y += this.aceleracion.y * delta;

        if (this.enElSuelo) {
            this.velocidad.x *= (1 - this.friccion.x * delta);
            if (Math.abs(this.velocidad.x) < 0.5f) {
                this.velocidad.x = 0;
            }
        } else {
            if (this.friccion.y > 0) {
                this.velocidad.y *= (1 - this.friccion.y * delta);
            }
        }

        this.posicion.x += this.velocidad.x * delta;
        this.posicion.y += this.velocidad.y * delta;

        this.enElSuelo = false;
        for (EntidadEstatica estatica : entidadesEstaticas) {
            if (this.colisionaCon(estatica)) {
                this.resolverColision(estatica);
            }
        }
    }

    private boolean colisionaCon(EntidadEstatica estatica) {
        Rectangle miHitbox = obtenerLimites();
        Rectangle hitboxEstatica = estatica.obtenerLimites();
        return miHitbox.overlaps(hitboxEstatica);
    }

    private void resolverColision(EntidadEstatica estatica) {
        Rectangle miHitbox = obtenerLimites();
        Rectangle hitboxEstatica = estatica.obtenerLimites();

        float solapeDerecha = miHitbox.x + miHitbox.width - hitboxEstatica.x;
        float solapeIzquierda = hitboxEstatica.x + hitboxEstatica.width - miHitbox.x;
        float solapeArriba = miHitbox.y + miHitbox.height - hitboxEstatica.y;
        float solapeAbajo = hitboxEstatica.y + hitboxEstatica.height - miHitbox.y;

        float solapeMinimo = Math.min(Math.min(solapeDerecha, solapeIzquierda),
                                      Math.min(solapeArriba, solapeAbajo));

        if (solapeMinimo == solapeAbajo) {
            this.posicion.y = hitboxEstatica.y + hitboxEstatica.height;
            this.velocidad.y = 0;
            this.enElSuelo = true;
        } else if (solapeMinimo == solapeArriba) {
            this.posicion.y = hitboxEstatica.y - this.altoColision;
            this.velocidad.y = 0;
        } else if (solapeMinimo == solapeDerecha) {
            this.posicion.x = hitboxEstatica.x - this.anchoColision;
            this.velocidad.x = 0;
        } else if (solapeMinimo == solapeIzquierda) {
            this.posicion.x = hitboxEstatica.x + hitboxEstatica.width;
            this.velocidad.x = 0;
        }
    }

    protected abstract void actualizarEstado(float delta);

    public void mover(float aceleracionX) {
        this.aceleracion.x = aceleracionX;
        if (aceleracionX < 0) {
            this.direccion = Direccion.IZQUIERDA;
        } else if (aceleracionX > 0) {
            this.direccion = Direccion.DERECHA;
        }
    }

    public void detener() {
        this.aceleracion.x = 0;
    }

    public void saltar() {
        if (this.enElSuelo) {
            this.velocidad.y = Constantes.FUERZA_SALTO;
            this.enElSuelo = false;
        }
    }

    protected void voltearSprite(boolean derecha) {
        boolean deberiaVoltear = !derecha;
        if (isFlipX() != deberiaVoltear) {
            flip(true, false);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        float anchoSprite = getWidth() * getScaleX();
        float altoSprite = getHeight() * getScaleY();
        float offsetX = (anchoSprite - this.anchoColision) / 2f;
        float offsetY = altoSprite / 2f;
        setPosition(this.posicion.x - offsetX, this.posicion.y - offsetY);
        draw(batch);
    }

    @Override
    public Rectangle obtenerLimites() {
        return new Rectangle(this.posicion.x, this.posicion.y, this.anchoColision, this.altoColision);
    }

    public Vector2 getVelocidad() {
        return this.velocidad;
    }

    public Vector2 getAceleracion() {
        return this.aceleracion;
    }

    public boolean estaEnElSuelo() {
        return this.enElSuelo;
    }

    public Direccion getDireccion() {
        return this.direccion;
    }

    public TablaEstados getTablaEstados() {
        return this.tablaEstados;
    }

    protected Entrada getEntrada() {
        return this.entrada;
    }
}
