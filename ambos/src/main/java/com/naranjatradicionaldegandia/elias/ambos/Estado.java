package com.naranjatradicionaldegandia.elias.ambos;

import java.util.Date;

public class Estado {
    private long fecha;
    private String estado;

    public Estado(String estado) {
        fecha = new Date().getTime();
        this.estado = estado;
    }

    public Estado() {
        fecha = System.currentTimeMillis();
        estado = "Muerto";
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Estado{" +
                "fecha=" + fecha +
                ", estado=" + estado +
                '}';
    }
}
