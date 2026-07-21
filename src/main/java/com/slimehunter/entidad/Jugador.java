package com.slimehunter.entidad;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.slimehunter.Constantes;
import com.slimehunter.grafico.Direccion;
import com.slimehunter.grafico.EstadoAnimacion;
import com.slimehunter.grafico.GestorSprites;

public class Jugador extends Entidad {

    private static GestorSprites gestorCache;

    private final GestorSprites gestorSprites;
    private float tiempoAnimacion;

    public Jugador(float x, float y, float velocidadMovimiento) {
        super(obtenerFrameInicial(), x, y, velocidadMovimiento,
              EstadoAnimacion.values().length, gestorCache);
        this.gestorSprites = gestorCache;
        gestorCache = null;
        this.tiempoAnimacion = 0f;

        setSize(gestorSprites.getAnchoFrame() * 3f, gestorSprites.getAltoFrame() * 3f);
        setOriginCenter();

        registrarTransiciones();
    }

    private static TextureRegion obtenerFrameInicial() {
        gestorCache = new GestorSprites(
            Constantes.ARCHIVO_SPRITE_KNIGHT,
            Constantes.ARCHIVO_DATA_KNIGHT
        );
        return gestorCache.obtenerFrameInactivo();
    }

    private void registrarTransiciones() {
        var tabla = getTablaEstados();

        tabla.registrarTransicion(EstadoAnimacion.INACTIVO.ordinal(), EstadoAnimacion.CAMINANDO.ordinal());
        tabla.registrarTransicion(EstadoAnimacion.INACTIVO.ordinal(), EstadoAnimacion.SALTANDO.ordinal());
        tabla.registrarTransicion(EstadoAnimacion.INACTIVO.ordinal(), EstadoAnimacion.ATACANDO.ordinal());
        tabla.registrarTransicion(EstadoAnimacion.INACTIVO.ordinal(), EstadoAnimacion.DESPLAZANDO.ordinal());

        tabla.registrarTransicion(EstadoAnimacion.CAMINANDO.ordinal(), EstadoAnimacion.INACTIVO.ordinal());
        tabla.registrarTransicion(EstadoAnimacion.CAMINANDO.ordinal(), EstadoAnimacion.SALTANDO.ordinal());
        tabla.registrarTransicion(EstadoAnimacion.CAMINANDO.ordinal(), EstadoAnimacion.ATACANDO.ordinal());
        tabla.registrarTransicion(EstadoAnimacion.CAMINANDO.ordinal(), EstadoAnimacion.DESPLAZANDO.ordinal());

        tabla.registrarTransicion(EstadoAnimacion.SALTANDO.ordinal(), EstadoAnimacion.CAYENDO.ordinal());

        tabla.registrarTransicion(EstadoAnimacion.CAYENDO.ordinal(), EstadoAnimacion.INACTIVO.ordinal());
        tabla.registrarTransicion(EstadoAnimacion.CAYENDO.ordinal(), EstadoAnimacion.CAMINANDO.ordinal());

        tabla.registrarTransicion(EstadoAnimacion.ATACANDO.ordinal(), EstadoAnimacion.INACTIVO.ordinal());

        tabla.registrarTransicion(EstadoAnimacion.RECIBIENDO_DANO.ordinal(), EstadoAnimacion.INACTIVO.ordinal());

        tabla.registrarTransicion(EstadoAnimacion.DESPLAZANDO.ordinal(), EstadoAnimacion.INACTIVO.ordinal());
        tabla.registrarTransicion(EstadoAnimacion.DESPLAZANDO.ordinal(), EstadoAnimacion.CAMINANDO.ordinal());
    }

    @Override
    protected void actualizarEstado(float delta) {
        tiempoAnimacion += delta;

        int estadoSugerido = calcularEstadoSugerido();
        getTablaEstados().intentarCambioEstado(estadoSugerido);

        TextureRegion frameActual = obtenerFrameSegunEstado();
        setRegion(frameActual);

        boolean mirandoDerecha = getDireccion() == Direccion.DERECHA;
        voltearSprite(mirandoDerecha);
    }

    private int calcularEstadoSugerido() {
        if (!estaEnElSuelo()) {
            if (getVelocidad().y > 0) {
                return EstadoAnimacion.SALTANDO.ordinal();
            } else {
                return EstadoAnimacion.CAYENDO.ordinal();
            }
        }

        if (Math.abs(getVelocidad().x) > 0) {
            return EstadoAnimacion.CAMINANDO.ordinal();
        }

        return EstadoAnimacion.INACTIVO.ordinal();
    }

    @Override
    protected TextureRegion obtenerFrameSegunEstado() {
        EstadoAnimacion estado = EstadoAnimacion.values()[getTablaEstados().getEstadoActual()];
        String nombreAnimacion = mapearEstadoANombre(estado);

        if (gestorSprites.existeAnimacion(nombreAnimacion)) {
            boolean loop = estado != EstadoAnimacion.SALTANDO
                        && estado != EstadoAnimacion.CAYENDO
                        && estado != EstadoAnimacion.ATACANDO
                        && estado != EstadoAnimacion.RECIBIENDO_DANO
                        && estado != EstadoAnimacion.MURIENDO
                        && estado != EstadoAnimacion.DESPLAZANDO;
            if (loop) {
                return gestorSprites.obtenerFrame(nombreAnimacion, tiempoAnimacion);
            } else {
                return gestorSprites.obtenerFrameSinLoop(nombreAnimacion, tiempoAnimacion);
            }
        }
        return gestorSprites.obtenerFrameInactivo();
    }

    private String mapearEstadoANombre(EstadoAnimacion estado) {
        return switch (estado) {
            case INACTIVO -> "inactivo";
            case CAMINANDO -> "caminar";
            case SALTANDO -> "salto";
            case CAYENDO -> "caida";
            case ATACANDO -> "ataque";
            case RECIBIENDO_DANO -> "hit";
            case MURIENDO -> "muerte";
            case DESPLAZANDO -> "dash";
        };
    }

    public void render(SpriteBatch batch) {
        draw(batch);
    }

    public void dispose() {
        gestorSprites.dispose();
    }

    public GestorSprites getGestorSprites() {
        return gestorSprites;
    }
}
