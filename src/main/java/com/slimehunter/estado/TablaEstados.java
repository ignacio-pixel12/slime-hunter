package com.slimehunter.estado;

import com.slimehunter.grafico.EstadoAnimacion;

public class TablaEstados {

    private final int[][] transiciones;
    private EstadoAnimacion estadoActual;
    private EstadoAnimacion estadoAnterior;

    public TablaEstados(int cantidadEstados) {
        this.transiciones = new int[cantidadEstados][cantidadEstados];
        this.estadoActual = EstadoAnimacion.INACTIVO;
        this.estadoAnterior = null;
    }

    public void registrarTransicion(EstadoAnimacion origen, EstadoAnimacion destino) {
        this.transiciones[origen.ordinal()][destino.ordinal()] = 1;
    }

    public boolean cambiarEstado(EstadoAnimacion nuevoEstado) {
        if (this.transiciones[this.estadoActual.ordinal()][nuevoEstado.ordinal()] == 1) {
            this.estadoAnterior = this.estadoActual;
            this.estadoActual = nuevoEstado;
            return true;
        }
        return false;
    }

    public EstadoAnimacion getEstadoActual() {
        return this.estadoActual;
    }

    public EstadoAnimacion getEstadoAnterior() {
        return this.estadoAnterior;
    }
}
