package com.slimehunter.mapa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;

import com.slimehunter.entidad.EntidadEstatica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapaJuego {

    private TiledMap mapa;
    private final List<EntidadEstatica> colisiones;
    private final Map<String, Rectangle> regiones;
    private final ShapeRenderer shapeRenderer;

    public MapaJuego() {
        this.colisiones = new ArrayList<>();
        this.regiones = new HashMap<>();
        this.shapeRenderer = new ShapeRenderer();
    }

    public void cargar(String archivoTmx) {
        this.mapa = new TmxMapLoader().load(archivoTmx);

        MapObjects objetos = this.mapa.getLayers().get("colisiones").getObjects();
        for (MapObject objeto : objetos) {
            if (objeto instanceof RectangleMapObject) {
                RectangleMapObject rectObj = (RectangleMapObject) objeto;
                Rectangle rect = rectObj.getRectangle();

                TextureRegion textura = crearTextura((int) rect.width, (int) rect.height, Color.DARK_GRAY);
                EntidadEstatica estatica = new EntidadEstatica(textura, rect.x, rect.y, rect.width, rect.height);

                this.colisiones.add(estatica);
                this.regiones.put(objeto.getName(), rect);
            }
        }
    }

    public void renderizarColisiones(com.badlogic.gdx.math.Matrix4 matrizProyeccion) {
        this.shapeRenderer.setProjectionMatrix(matrizProyeccion);
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.shapeRenderer.setColor(new Color(0.2f, 0.2f, 0.2f, 1f));

        for (EntidadEstatica estatica : this.colisiones) {
            this.shapeRenderer.rect(
                estatica.getPosicion().x,
                estatica.getPosicion().y,
                estatica.getAnchoColision(),
                estatica.getAltoColision()
            );
        }

        this.shapeRenderer.end();
    }

    public Rectangle obtenerRegion(String nombre) {
        return this.regiones.get(nombre);
    }

    public List<EntidadEstatica> obtenerColisiones() {
        return this.colisiones;
    }

    public TiledMap getMapa() {
        return this.mapa;
    }

    public void dispose() {
        if (this.mapa != null) {
            this.mapa.dispose();
        }
        this.shapeRenderer.dispose();
    }

    private static TextureRegion crearTextura(int ancho, int alto, Color color) {
        Pixmap pixmap = new Pixmap(ancho, alto, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture textura = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegion(textura);
    }
}
