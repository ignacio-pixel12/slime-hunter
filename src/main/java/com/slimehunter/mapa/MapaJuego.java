package com.slimehunter.mapa;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.slimehunter.entidad.EntidadEstatica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapaJuego {

    private static final String CAPA_SOLIDOS = "colisiones-solidas";
    private static final String CAPA_PLATAFORMAS = "colisiones_plataformas";

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer rendererMapa;
    private final List<EntidadEstatica> colisiones;
    private final List<EntidadEstatica> plataformas;
    private final Map<String, Rectangle> regiones;
    private final ShapeRenderer shapeRenderer;
    private final TextureRegion texturaColision;
    private Vector2 spawn;

    public MapaJuego() {
        this.colisiones = new ArrayList<>();
        this.plataformas = new ArrayList<>();
        this.regiones = new HashMap<>();
        this.shapeRenderer = new ShapeRenderer();
        this.texturaColision = crearTexturaCompartida();
    }

    public void cargar(String archivoTmx) {
        this.mapa = new TmxMapLoader().load(archivoTmx);
        this.rendererMapa = new OrthogonalTiledMapRenderer(this.mapa, 1f);

        cargarCapa(CAPA_SOLIDOS, this.colisiones, false);
        cargarCapa(CAPA_PLATAFORMAS, this.plataformas, true);
        this.buscarSpawn();
    }

    private void buscarSpawn() {
        for (com.badlogic.gdx.maps.MapLayer capa : this.mapa.getLayers()) {
            MapObjects objetos = capa.getObjects();
            for (MapObject objeto : objetos) {
                if ("spawn".equals(objeto.getName()) && objeto instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) objeto).getRectangle();
                    this.spawn = new Vector2(rect.x + rect.width / 2f, rect.y + rect.height);
                    return;
                }
            }
        }
    }

    public Vector2 obtenerSpawn() {
        return this.spawn;
    }

    private void cargarCapa(String nombreCapa, List<EntidadEstatica> destino, boolean unidireccional) {
        if (this.mapa.getLayers().get(nombreCapa) == null) {
            return;
        }

        MapObjects objetos = this.mapa.getLayers().get(nombreCapa).getObjects();
        for (MapObject objeto : objetos) {
            if (objeto instanceof RectangleMapObject) {
                RectangleMapObject rectObj = (RectangleMapObject) objeto;
                Rectangle rect = rectObj.getRectangle();

                EntidadEstatica estatica = new EntidadEstatica(
                    this.texturaColision, rect.x, rect.y, rect.width, rect.height);
                estatica.setPlataformaUnidireccional(unidireccional);

                this.regiones.put(objeto.getName(), rect);
                destino.add(estatica);
            }
        }
    }

    public void render(OrthographicCamera camara) {
        this.rendererMapa.setView(camara);
        this.rendererMapa.render();
    }

    public void renderizarColisiones(Matrix4 matrizProyeccion) {
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

    public List<EntidadEstatica> obtenerPlataformas() {
        return this.plataformas;
    }

    public TiledMap getMapa() {
        return this.mapa;
    }

    public void dispose() {
        if (this.mapa != null) {
            this.mapa.dispose();
        }
        if (this.texturaColision != null && this.texturaColision.getTexture() != null) {
            this.texturaColision.getTexture().dispose();
        }
        this.shapeRenderer.dispose();
    }

    private static TextureRegion crearTexturaCompartida() {
        Pixmap pixmap = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GRAY);
        pixmap.fill();
        Texture textura = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegion(textura);
    }
}
