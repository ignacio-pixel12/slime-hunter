package com.slimehunter.entidad;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entidad extends Sprite {

    protected final Vector2 posicion;
    protected float anchoColision;
    protected float altoColision;

    protected Entidad(TextureRegion region, float x, float y, float anchoColision, float altoColision) {
        super(region);
        this.posicion = new Vector2(x, y);
        this.anchoColision = anchoColision;
        this.altoColision = altoColision;
        setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        float anchoSprite = getWidth() * getScaleX();
        float offsetX = (anchoSprite - this.anchoColision) / 2f;
        setPosition(this.posicion.x - offsetX, this.posicion.y);
        draw(batch);
    }

    public Rectangle obtenerLimites() {
        return new Rectangle(this.posicion.x, this.posicion.y, this.anchoColision, this.altoColision);
    }

    public Vector2 getPosicion() {
        return this.posicion;
    }

    public float getAnchoColision() {
        return this.anchoColision;
    }

    public float getAltoColision() {
        return this.altoColision;
    }
}
