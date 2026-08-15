package com.slimehunter;

public final class Constantes {

    private Constantes() {
        throw new AssertionError("No se debe instanciar una clase de constantes");
    }

    public static final int ANCHO_VENTANA = 1280;
    public static final int ALTO_VENTANA = 720;
    public static final int FPS_OBJETIVO = 60;

    public static final float VELOCIDAD_JUGADOR = 200f;

    public static final float FUERZA_SALTO = 500f;
    public static final float GRAVEDAD = 980f;
    public static final float FRICCION_AIRE = 3f;

    public static final float ACELERACION_JUGADOR = 1800f;
    public static final float FRICCION_JUGADOR = 8f;

    public static final float INICIO_JUGADOR_X = 100f;
    public static final float INICIO_JUGADOR_Y = 1400f;

    public static final float TOLERANCIA_PLATAFORMA = 4f;

    public static final String ARCHIVO_MAPA = "mapa/diseño.tmx";

    public static final float JUGADOR_ANCHO_COLISION = 48f;
    public static final float JUGADOR_ALTO_COLISION = 64f;

    public static final float DURACION_FRAME_DEFAULT = 0.15f;

    public static final float COLOR_FONDO_R = 0.1f;
    public static final float COLOR_FONDO_G = 0.1f;
    public static final float COLOR_FONDO_B = 0.1f;
    public static final float COLOR_FONDO_A = 1f;

    public static final String ARCHIVO_SPRITE_KNIGHT = "knight-sheet.png";
    public static final String ARCHIVO_DATA_KNIGHT = "knight-data.json";
    public static final String ARCHIVO_CAJAS_KNIGHT = "knight-cajas.json";

    public static final float JUGADOR_ESCALA = 3f;

    public static final int JUGADOR_VIDA_MAXIMA = 5;
    public static final float DURACION_INVULNERABILIDAD = 1.0f;

    public static final boolean DEBUG_CAJAS = true;

    public static final float HUD_X = 20f;
    public static final float HUD_ANCHO = 200f;
    public static final float HUD_ALTO = 20f;
    public static final float HUD_Y = ALTO_VENTANA - HUD_ALTO - 20f;
    
    public static final int SLIME_VIDA_MAXIMA = 3;
    public static final float SLIME_ANCHO_COLISION = 20f;
    public static final float SLIME_ALTO_COLISION = 13f;
    public static final float SLIME_VELOCIDAD = 50f;
    public static final int SLIME_DANO = 1;
    public static final float SLIME_ESCALA = 2f;
}
