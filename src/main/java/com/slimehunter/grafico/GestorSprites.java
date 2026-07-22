package com.slimehunter.grafico;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

import com.slimehunter.Constantes;

public class GestorSprites {

    private final Texture textura;
    private final TextureRegion[][] frames;
    private final Map<String, Animation<TextureRegion>> animaciones;
    private final int anchoFrame;
    private final int altoFrame;

    public GestorSprites(String archivoTextura, String archivoData) {
        if (archivoTextura == null || archivoTextura.isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo de textura no puede estar vacío");
        }

        this.textura = new Texture(Gdx.files.internal(archivoTextura));
        this.animaciones = new HashMap<>();

        int[] dimensionesFrame = obtenerDimensionesDelJSON(archivoData);
        this.anchoFrame = dimensionesFrame[0];
        this.altoFrame = dimensionesFrame[1];

        TextureRegion regionCompleta = new TextureRegion(textura);
        int columnas = textura.getWidth() / anchoFrame;
        int filas = textura.getHeight() / altoFrame;
        this.frames = regionCompleta.split(anchoFrame, altoFrame);

        if (archivoData != null && !archivoData.isBlank()) {
            cargarAnimacionesDesdeJSON(archivoData, columnas, filas);
        }
    }

    private int[] obtenerDimensionesDelJSON(String archivoData) {
        if (archivoData == null || archivoData.isBlank()) {
            return new int[]{ 64, 64 };
        }

        JsonReader lector = new JsonReader();
        JsonValue raiz = lector.parse(Gdx.files.internal(archivoData));
        JsonValue primerFrame = raiz.get("frames").child;

        if (primerFrame != null) {
            JsonValue frame = primerFrame.get("frame");
            if (frame != null) {
                return new int[]{ frame.getInt("w"), frame.getInt("h") };
            }
        }

        return new int[]{ 64, 64 };
    }

    public GestorSprites(String archivoTextura, int anchoFrame, int altoFrame) {
        if (archivoTextura == null || archivoTextura.isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo de textura no puede estar vacío");
        }
        if (anchoFrame <= 0 || altoFrame <= 0) {
            throw new IllegalArgumentException("Las dimensiones del frame deben ser mayores a 0");
        }

        this.textura = new Texture(Gdx.files.internal(archivoTextura));
        this.animaciones = new HashMap<>();
        this.anchoFrame = anchoFrame;
        this.altoFrame = altoFrame;

        TextureRegion regionCompleta = new TextureRegion(textura);
        this.frames = regionCompleta.split(anchoFrame, altoFrame);
    }

    private void cargarAnimacionesDesdeJSON(String archivoData, int columnas, int filas) {
        JsonReader lector = new JsonReader();
        JsonValue raiz = lector.parse(Gdx.files.internal(archivoData));

        JsonValue meta = raiz.get("meta");
        if (meta == null) {
            return;
        }

        JsonValue frameTags = meta.get("frameTags");
        if (frameTags == null) {
            return;
        }

        JsonValue framesArray = raiz.get("frames");

        for (JsonValue tag : frameTags) {
            String nombre = tag.getString("name");
            int desde = tag.getInt("from");
            int hasta = tag.getInt("to");

            Array<TextureRegion> framesAnimacion = new Array<>();
            int indice = 0;

            for (JsonValue frameEntry = framesArray.child; frameEntry != null; frameEntry = frameEntry.next) {
                if (indice >= desde && indice <= hasta) {
                    JsonValue frameData = frameEntry.get("frame");
                    int x = frameData.getInt("x");
                    int y = frameData.getInt("y");
                    int columna = x / anchoFrame;
                    int fila = y / altoFrame;
                    if (fila < filas && columna < columnas) {
                        framesAnimacion.add(frames[fila][columna]);
                    }
                }
                indice++;
            }

            float duracionFrame = Constantes.DURACION_FRAME_DEFAULT;
            indice = 0;
            for (JsonValue frameEntry = framesArray.child; frameEntry != null; frameEntry = frameEntry.next) {
                if (indice == desde) {
                    duracionFrame = frameEntry.getFloat("duration") / 1000f;
                    break;
                }
                indice++;
            }

            if (framesAnimacion.size > 0) {
                animaciones.put(nombre, new Animation<>(duracionFrame, framesAnimacion));
            }
        }
    }

    public void registrarAnimacion(String nombre, Animation<TextureRegion> animacion) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la animación no puede estar vacío");
        }
        if (animacion == null) {
            throw new IllegalArgumentException("La animación no puede ser nula");
        }
        animaciones.put(nombre, animacion);
    }

    public Animation<TextureRegion> obtenerAnimacion(String nombre) {
        Animation<TextureRegion> animacion = animaciones.get(nombre);
        if (animacion == null) {
            throw new IllegalArgumentException("Animación no encontrada: " + nombre);
        }
        return animacion;
    }

    public TextureRegion obtenerFrame(String nombreAnimacion, float tiempo) {
        return obtenerAnimacion(nombreAnimacion).getKeyFrame(tiempo, true);
    }

    public TextureRegion obtenerFrameSinLoop(String nombreAnimacion, float tiempo) {
        return obtenerAnimacion(nombreAnimacion).getKeyFrame(tiempo, false);
    }

    public TextureRegion obtenerFrameInactivo() {
        if (animaciones.containsKey("inactivo")) {
            return animaciones.get("inactivo").getKeyFrame(0, true);
        }
        return frames[0][0];
    }

    public boolean existeAnimacion(String nombre) {
        return animaciones.containsKey(nombre);
    }

    public Texture getTextura() {
        return textura;
    }

    public int getAnchoFrame() {
        return anchoFrame;
    }

    public int getAltoFrame() {
        return altoFrame;
    }

    public void dispose() {
        textura.dispose();
    }
}
