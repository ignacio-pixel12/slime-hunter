package com.slimehunter.estado;

import com.slimehunter.grafico.EstadoAnimacion;

public class TablaEstados {

    private final int[][] transiciones;
    private EstadoAnimacion estadoActual;
    private EstadoAnimacion estadoAnterior;
    private boolean cambioEstado;

    public TablaEstados(int cantidadEstados) {
        this.transiciones = new int[cantidadEstados][cantidadEstados];
        this.estadoActual = EstadoAnimacion.INACTIVO;
        this.estadoAnterior = null;
        this.cambioEstado = true;
    }

    public void registrarTransicion(EstadoAnimacion origen, EstadoAnimacion destino) {
        this.transiciones[origen.ordinal()][destino.ordinal()] = 1;
    }

    public boolean cambiarEstado(EstadoAnimacion nuevoEstado) {
        if (this.transiciones[this.estadoActual.ordinal()][nuevoEstado.ordinal()] == 1) {
            this.estadoAnterior = this.estadoActual;
            this.estadoActual = nuevoEstado;
            this.cambioEstado = true;
            return true;
        }
        return false;
    }

    public boolean huboCambioEstado() {
        boolean resultado = this.cambioEstado;
        this.cambioEstado = false;
        return resultado;
    }

    public EstadoAnimacion getEstadoActual() {
        return this.estadoActual;
    }

    public EstadoAnimacion getEstadoAnterior() {
        return this.estadoAnterior;
    }
}
