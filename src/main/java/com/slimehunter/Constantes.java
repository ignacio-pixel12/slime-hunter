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

    public static final float JUGADOR_ANCHO_COLISION = 48f;
    public static final float JUGADOR_ALTO_COLISION = 64f;

    public static final float DURACION_FRAME_DEFAULT = 0.15f;

    public static final float COLOR_FONDO_R = 0.1f;
    public static final float COLOR_FONDO_G = 0.1f;
    public static final float COLOR_FONDO_B = 0.1f;
    public static final float COLOR_FONDO_A = 1f;

    public static final String ARCHIVO_SPRITE_KNIGHT = "knight-sheet.png";
    public static final String ARCHIVO_DATA_KNIGHT = "knight-data.json";
}
