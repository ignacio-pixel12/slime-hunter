package com.slimehunter.mapa;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.slimehunter.entidad.EntidadEstatica;

public class Plataforma extends EntidadEstatica {

    public Plataforma(float x, float y, float ancho, float alto, Color color) {
        super(crearTextura(ancho, alto, color), x, y, ancho, alto);
    }

    private static TextureRegion crearTextura(float ancho, float alto, Color color) {
        Pixmap pixmap = new Pixmap((int) ancho, (int) alto, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture textura = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegion(textura);
    }
}
