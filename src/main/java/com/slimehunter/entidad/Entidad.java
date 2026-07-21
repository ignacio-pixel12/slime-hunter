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

public abstract class Entidad extends Sprite {

    private final Vector2 posicion;
    private final Vector2 velocidad;
    private final float velocidadMovimiento;
    private final TablaEstados tablaEstados;
    private boolean enElSuelo;
    private Direccion direccion;

    protected Entidad(TextureRegion region, float x, float y, float velocidadMovimiento,
                      int cantidadEstados, GestorSprites gestorSprites) {
        super(region);
        if (Float.isNaN(x) || Float.isInfinite(x)) {
            throw new IllegalArgumentException("La posición X no puede ser NaN o infinita");
        }
        if (Float.isNaN(y) || Float.isInfinite(y)) {
            throw new IllegalArgumentException("La posición Y no puede ser NaN o infinita");
        }
        if (velocidadMovimiento <= 0) {
            throw new IllegalArgumentException("La velocidad de movimiento debe ser mayor a 0");
        }

        this.posicion = new Vector2(x, y);
        this.velocidad = new Vector2(0, 0);
        this.velocidadMovimiento = velocidadMovimiento;
        this.tablaEstados = new TablaEstados(cantidadEstados);
        this.enElSuelo = true;
        this.direccion = Direccion.DERECHA;
        setPosition(x, y);
    }

    public void actualizar(float delta) {
        this.velocidad.y -= Constantes.GRAVEDAD * delta;

        this.posicion.add(this.velocidad.x * delta, this.velocidad.y * delta);

        if (this.posicion.y <= Constantes.NIVEL_SUELO) {
            this.posicion.y = Constantes.NIVEL_SUELO;
            this.velocidad.y = 0;
            this.enElSuelo = true;
        } else {
            this.enElSuelo = false;
        }

        setPosition(this.posicion.x, this.posicion.y);
        actualizarEstado(delta);
    }

    protected abstract void actualizarEstado(float delta);

    protected abstract TextureRegion obtenerFrameSegunEstado();

    public void moverIzquierda() {
        this.velocidad.x = -this.velocidadMovimiento;
        this.direccion = Direccion.IZQUIERDA;
    }

    public void moverDerecha() {
        this.velocidad.x = this.velocidadMovimiento;
        this.direccion = Direccion.DERECHA;
    }

    public void detener() {
        this.velocidad.x = 0;
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
        return new Rectangle(posicion.x, posicion.y, getWidth(), getHeight());
    }

    protected TablaEstados getTablaEstados() {
        return tablaEstados;
    }

    public Vector2 getPosicion() {
        return posicion;
    }

    public Vector2 getVelocidad() {
        return velocidad;
    }

    public float getVelocidadMovimiento() {
        return velocidadMovimiento;
    }

    public boolean estaEnElSuelo() {
        return enElSuelo;
    }

    public Direccion getDireccion() {
        return direccion;
    }
}
