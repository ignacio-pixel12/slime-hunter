package com.slimehunter.entidad;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.slimehunter.Constantes;
import com.slimehunter.estado.TablaEstados;
import com.slimehunter.grafico.Direccion;
import com.slimehunter.grafico.GestorSprites;
import com.slimehunter.input.Entrada;

public abstract class Entidad extends Sprite {

    private final Vector2 posicion;
    private final Vector2 velocidad;
    private final Vector2 aceleracion;
    private final Vector2 friccion;
    private final TablaEstados tablaEstados;
    private boolean enElSuelo;
    private Direccion direccion;
    private Entrada entrada;

    protected Entidad(TextureRegion region, float x, float y,
                      float aceleracionMovimiento, float friccionMovimiento,
                      int cantidadEstados, GestorSprites gestorSprites, Entrada entrada) {
        super(region);
        this.posicion = new Vector2(x, y);
        this.velocidad = new Vector2(0, 0);
        this.aceleracion = new Vector2(0, 0);
        this.friccion = new Vector2(friccionMovimiento, 0);
        this.tablaEstados = new TablaEstados(cantidadEstados);
        this.enElSuelo = true;
        this.direccion = Direccion.DERECHA;
        this.entrada = entrada;
        setPosition(x, y);
    }

    public void actualizar(float delta) {
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

        if (this.posicion.y <= Constantes.NIVEL_SUELO) {
            this.posicion.y = Constantes.NIVEL_SUELO;
            this.velocidad.y = 0;
            this.enElSuelo = true;
        } else {
            this.enElSuelo = false;
        }

        setPosition(this.posicion.x, this.posicion.y);
    }

    protected abstract void actualizarEstado(float delta);

    protected abstract TextureRegion obtenerFrameSegunEstado();

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

    public void render(SpriteBatch batch) {
        draw(batch);
    }

    public Rectangle obtenerLimites() {
        return new Rectangle(this.posicion.x, this.posicion.y, getWidth(), getHeight());
    }

    public TablaEstados obtenerTablaEstados() {
        return this.tablaEstados;
    }

    public Vector2 obtenerPosicion() {
        return this.posicion;
    }

    public Vector2 obtenerVelocidad() {
        return this.velocidad;
    }

    public Vector2 obtenerAceleracion() {
        return this.aceleracion;
    }

    public boolean estaEnElSuelo() {
        return this.enElSuelo;
    }

    public Direccion obtenerDireccion() {
        return this.direccion;
    }

    protected Entrada obtenerEntrada() {
        return this.entrada;
    }
}
