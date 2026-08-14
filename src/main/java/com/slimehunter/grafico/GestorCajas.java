package com.slimehunter.grafico;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class GestorCajas {

    public enum TipoCaja {
        COLBOX("colbox"),
        HITBOX("hitbox"),
        HURTBOX("hurtbox");

        private final String nombreJson;

        TipoCaja(String nombreJson) {
            this.nombreJson = nombreJson;
        }

        public String getNombreJson() {
            return this.nombreJson;
        }
    }

    private final Map<TipoCaja, Map<String, Map<Integer, Rectangle>>> cajas;

    public GestorCajas(String archivoCajas) {
        this.cajas = new HashMap<>();

        if (!Gdx.files.internal(archivoCajas).exists()) {
            return;
        }

        JsonReader lector = new JsonReader();
        JsonValue raiz = lector.parse(Gdx.files.internal(archivoCajas));

        for (TipoCaja tipo : TipoCaja.values()) {
            JsonValue porTipo = raiz.get(tipo.getNombreJson());
            if (porTipo == null) {
                continue;
            }

            Map<String, Map<Integer, Rectangle>> cajasPorAnimacion = new HashMap<>();

            for (JsonValue animacion = porTipo.child; animacion != null; animacion = animacion.next) {
                String nombreAnimacion = animacion.name;
                Map<Integer, Rectangle> cajasPorFrame = new HashMap<>();

                for (JsonValue caja = animacion.child; caja != null; caja = caja.next) {
                    int frame = caja.getInt("frame");
                    float x = caja.getFloat("x");
                    float y = caja.getFloat("y");
                    float w = caja.getFloat("w");
                    float h = caja.getFloat("h");
                    cajasPorFrame.put(frame, new Rectangle(x, y, w, h));
                }

                cajasPorAnimacion.put(nombreAnimacion, cajasPorFrame);
            }

            this.cajas.put(tipo, cajasPorAnimacion);
        }
    }

    public Rectangle getCaja(TipoCaja tipo, String nombreAnimacion, int frame) {
        Map<String, Map<Integer, Rectangle>> cajasPorAnimacion = this.cajas.get(tipo);
        if (cajasPorAnimacion == null) {
            return null;
        }
        Map<Integer, Rectangle> cajasPorFrame = cajasPorAnimacion.get(nombreAnimacion);
        if (cajasPorFrame == null) {
            return null;
        }
        return cajasPorFrame.get(frame);
    }

    public boolean existeCaja(TipoCaja tipo, String nombreAnimacion, int frame) {
        return this.getCaja(tipo, nombreAnimacion, frame) != null;
    }

    public static Rectangle convertirAMundo(Rectangle caja, float xSprite, float ySprite,
                                            float escala, float altoFrame) {
        return new Rectangle(
            xSprite + caja.x * escala,
            ySprite + (altoFrame - caja.y - caja.height) * escala,
            caja.width * escala,
            caja.height * escala
        );
    }

    public static Rectangle voltearHorizontalmente(Rectangle caja, float xInicioSprite, float anchoSprite) {
        return new Rectangle(
            2 * xInicioSprite + anchoSprite - (caja.x + caja.width),
            caja.y,
            caja.width,
            caja.height
        );
    }
}
