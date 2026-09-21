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
import com.slimehunter.grafico.GestorCajas;
import com.slimehunter.grafico.GestorSprites;

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
    private final List<Rectangle> pinches;
    private final Map<String, Rectangle> habilidades;
    private final Map<String, Rectangle> regiones;
    private final ShapeRenderer shapeRenderer;
    private final TextureRegion texturaColision;
    private final GestorSprites gestorSpritesSlime1;
    private final GestorCajas gestorCajasSlime1;
    private final GestorSprites gestorSpritesSlime2;
    private final GestorCajas gestorCajasSlime2;
    private Vector2 spawn;

    public MapaJuego() {
        this.colisiones = new ArrayList<>();
        this.plataformas = new ArrayList<>();
        this.pinches = new ArrayList<>();
        this.habilidades = new HashMap<>();
        this.regiones = new HashMap<>();
        this.shapeRenderer = new ShapeRenderer();
        this.texturaColision = crearTexturaCompartida();
        this.gestorSpritesSlime1 = new GestorSprites("slime1-sheet.png", "slime1-data.json");
        this.gestorCajasSlime1 = new GestorCajas("slime1-cajas.json");
        this.gestorSpritesSlime2 = new GestorSprites("slime2-sheet.png", "slime2-data.json");
        this.gestorCajasSlime2 = new GestorCajas("slime2-cajas.json");
    }

    public void cargar(String archivoTmx) {
        this.mapa = new TmxMapLoader().load(archivoTmx);
        this.rendererMapa = new OrthogonalTiledMapRenderer(this.mapa, 1f);

        cargarCapa(CAPA_SOLIDOS, this.colisiones, false);
        cargarCapa(CAPA_PLATAFORMAS, this.plataformas, true);
        cargarPinches();
        cargarHabilidades();
        this.buscarSpawn();
    }

    public float obtenerAnchoMapa() {
        if (this.mapa == null) return 0;
        Integer ancho = this.mapa.getProperties().get("width", Integer.class);
        Integer tileWidth = this.mapa.getProperties().get("tilewidth", Integer.class);
        if (ancho != null && tileWidth != null) {
            return ancho * tileWidth;
        }
        return 0;
    }

    public float obtenerAltoMapa() {
        if (this.mapa == null) return 0;
        Integer alto = this.mapa.getProperties().get("height", Integer.class);
        Integer tileHeight = this.mapa.getProperties().get("tileheight", Integer.class);
        if (alto != null && tileHeight != null) {
            return alto * tileHeight;
        }
        return 0;
    }

    private void buscarSpawn() {
        for (com.badlogic.gdx.maps.MapLayer capa : this.mapa.getLayers()) {
            MapObjects objetos = capa.getObjects();
            for (MapObject objeto : objetos) {
                if ("spawn".equals(objeto.getName()) && objeto instanceof RectangleMapObject) {
                    this.spawn = obtenerCentroSpawn((RectangleMapObject) objeto);
                    return;
                }
            }
        }

        com.badlogic.gdx.maps.MapLayer capaSpawn = this.mapa.getLayers().get("spawn");
        if (capaSpawn != null) {
            for (MapObject objeto : capaSpawn.getObjects()) {
                if (objeto instanceof RectangleMapObject) {
                    this.spawn = obtenerCentroSpawn((RectangleMapObject) objeto);
                    return;
                }
            }
        }
    }

    private static Vector2 obtenerCentroSpawn(RectangleMapObject objeto) {
        Rectangle rect = objeto.getRectangle();
        return new Vector2(rect.x + rect.width / 2f, rect.y + rect.height);
    }

    public Vector2 obtenerSpawn() {
        return this.spawn;
    }

    public List<Vector2> obtenerSpawnsEnemigos() {
        return obtenerSpawnsCapa("slime_spawn");
    }

    public List<Vector2> obtenerSpawnsEnemigosSaltarines() {
        return obtenerSpawnsCapa("slime2_spawn");
    }

    private List<Vector2> obtenerSpawnsCapa(String nombreCapa) {
        List<Vector2> spawns = new ArrayList<>();
        com.badlogic.gdx.maps.MapLayer capa = this.mapa.getLayers().get(nombreCapa);
        if (capa == null) {
            return spawns;
        }
        for (MapObject objeto : capa.getObjects()) {
            try {
                Object xRaw = objeto.getProperties().get("x");
                Object yRaw = objeto.getProperties().get("y");
                if (xRaw != null && yRaw != null) {
                    float x = Float.parseFloat(xRaw.toString());
                    float y = Float.parseFloat(yRaw.toString());
                    spawns.add(new Vector2(x, y));
                }
            } catch (Exception e) {
                System.err.println("Error leyendo spawn: " + e.getMessage());
            }
        }
        return spawns;
    }

    private void cargarPinches() {
        com.badlogic.gdx.maps.MapLayer capa = this.mapa.getLayers().get("pinches");
        if (capa == null) {
            return;
        }
        for (MapObject objeto : capa.getObjects()) {
            if (objeto instanceof RectangleMapObject) {
                this.pinches.add(((RectangleMapObject) objeto).getRectangle());
            }
        }
    }

    public List<Rectangle> obtenerPinches() {
        return this.pinches;
    }

    private void cargarHabilidades() {
        com.badlogic.gdx.maps.MapLayer capa = this.mapa.getLayers().get("abilidades");
        if (capa == null) {
            System.out.println("MAPA: capa 'abilidades' no encontrada");
            return;
        }
        System.out.println("MAPA: capa 'abilidades' encontrada");
        for (MapObject objeto : capa.getObjects()) {
            if (objeto instanceof RectangleMapObject) {
                String nombre = objeto.getName();
                Rectangle rect = ((RectangleMapObject) objeto).getRectangle();
                System.out.println("MAPA: obj='" + nombre + "' rect=" + rect);
                if (nombre != null) {
                    if (rect.width == 0 || rect.height == 0) {
                        rect = new Rectangle(rect.x - 16, rect.y - 16, 32, 32);
                    }
                    this.habilidades.put(nombre, rect);
                }
            } else {
                System.out.println("MAPA: obj NO es RectangleMapObject: " + objeto.getClass().getSimpleName());
            }
        }
        System.out.println("MAPA: habilidades cargadas: " + this.habilidades.size());
    }

    public Map<String, Rectangle> obtenerHabilidades() {
        return this.habilidades;
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

    public GestorSprites getGestorSpritesSlime1() { return this.gestorSpritesSlime1; }
    public GestorCajas getGestorCajasSlime1() { return this.gestorCajasSlime1; }
    public GestorSprites getGestorSpritesSlime2() { return this.gestorSpritesSlime2; }
    public GestorCajas getGestorCajasSlime2() { return this.gestorCajasSlime2; }

    public void dispose() {
        if (this.mapa != null) {
            this.mapa.dispose();
        }
        if (this.texturaColision != null && this.texturaColision.getTexture() != null) {
            this.texturaColision.getTexture().dispose();
        }
        this.shapeRenderer.dispose();
        this.gestorSpritesSlime1.dispose();
        this.gestorSpritesSlime2.dispose();
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
