package com.slimehunter.grafico;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import com.slimehunter.entidad.Entidad;

import java.util.List;

public class DebugColisiones {

    private final ShapeRenderer shapeRenderer;

    public DebugColisiones() {
        this.shapeRenderer = new ShapeRenderer();
    }

    public void renderizar(List<Entidad> entidades) {
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.setColor(Color.RED);

        for (Entidad entidad : entidades) {
            Rectangle limites = entidad.obtenerLimites();
            this.shapeRenderer.rect(limites.x, limites.y, limites.width, limites.height);
        }

        this.shapeRenderer.end();
    }

    public void dispose() {
        this.shapeRenderer.dispose();
    }
}
