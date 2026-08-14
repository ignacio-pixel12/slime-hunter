package com.slimehunter.grafico;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

import com.slimehunter.entidad.Entidad;

import java.util.List;

public class DebugColisiones {

    private static final Color COLOR_COLBOX = Color.GRAY;
    private static final Color COLOR_HITBOX = Color.RED;
    private static final Color COLOR_HURTBOX = Color.BLUE;

    private final ShapeRenderer shapeRenderer;

    public DebugColisiones() {
        this.shapeRenderer = new ShapeRenderer();
    }

    public void renderizar(List<Entidad> entidades, Matrix4 matrizProyeccion) {
        this.shapeRenderer.setProjectionMatrix(matrizProyeccion);

        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (Entidad entidad : entidades) {
            Rectangle colbox = entidad.obtenerLimites();
            if (colbox != null) {
                this.renderizarRectangulo(colbox, COLOR_COLBOX);
            }

            Rectangle hurtbox = entidad.obtenerHurtbox();
            if (hurtbox != null) {
                this.renderizarRectangulo(hurtbox, COLOR_HURTBOX);
            }

            Rectangle hitbox = entidad.obtenerHitboxAtaque();
            if (hitbox != null) {
                this.renderizarRectangulo(hitbox, COLOR_HITBOX);
            }
        }

        this.shapeRenderer.end();
    }

    private void renderizarRectangulo(Rectangle rectangulo, Color color) {
        this.shapeRenderer.setColor(color);
        this.shapeRenderer.rect(rectangulo.x, rectangulo.y, rectangulo.width, rectangulo.height);
    }

    public void dispose() {
        this.shapeRenderer.dispose();
    }
}
