package com.slimehunter.estado;

public class TablaEstados {

    private final int[][] transiciones;
    private int estadoActual;

    public TablaEstados(int cantidadEstados) {
        if (cantidadEstados <= 0) {
            throw new IllegalArgumentException("La cantidad de estados debe ser mayor a 0");
        }
        this.transiciones = new int[cantidadEstados][cantidadEstados];
        this.estadoActual = 0;
    }

    public void registrarTransicion(int origen, int destino) {
        validarIndice(origen);
        validarIndice(destino);
        transiciones[origen][destino] = 1;
    }

    public boolean puedeTransitir(int origen, int destino) {
        validarIndice(origen);
        validarIndice(destino);
        return transiciones[origen][destino] == 1;
    }

    public boolean intentarCambioEstado(int nuevoEstado) {
        if (puedeTransitir(estadoActual, nuevoEstado)) {
            estadoActual = nuevoEstado;
            return true;
        }
        return false;
    }

    public int getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(int estado) {
        validarIndice(estado);
        this.estadoActual = estado;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= transiciones.length) {
            throw new IllegalArgumentException("Índice de estado fuera de rango: " + indice);
        }
    }
}
