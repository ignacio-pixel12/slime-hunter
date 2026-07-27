package com.slimehunter.pantalla;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

import com.slimehunter.Constantes;
import com.slimehunter.entidad.Jugador;

public class InterfazHUD {

    private final ShapeRenderer shapeRenderer;

    public InterfazHUD() {
        this.shapeRenderer = new ShapeRenderer();
    }

    public void renderizar(Jugador jugador, Matrix4 matrixProjecion) {
        float porcentaje = Math.max(0, (float) jugador.getVida() / jugador.getMaxVida());

        this.shapeRenderer.setProjectionMatrix(matrixProjecion);

        Gdx.gl.glEnable(GL20.GL_BLEND);

        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        this.shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
        this.shapeRenderer.rect(Constantes.HUD_X, Constantes.HUD_Y,
                                Constantes.HUD_ANCHO, Constantes.HUD_ALTO);

        float r = 1f - porcentaje;
        float g = porcentaje;
        this.shapeRenderer.setColor(r, g, 0f, 1f);
        this.shapeRenderer.rect(Constantes.HUD_X, Constantes.HUD_Y,
                                Constantes.HUD_ANCHO * porcentaje, Constantes.HUD_ALTO);

        this.shapeRenderer.end();

        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.setColor(Color.WHITE);
        this.shapeRenderer.rect(Constantes.HUD_X, Constantes.HUD_Y,
                                Constantes.HUD_ANCHO, Constantes.HUD_ALTO);
        this.shapeRenderer.end();
    }

    public void dispose() {
        this.shapeRenderer.dispose();
    }
}
