package com.naranjatradicionaldegandia.elias.ambos;

import java.util.Date;

public class Dato {
    private long fecha;
    private String tipo;
    private String cuerpo;


    public Dato(String tipo, String cuerpo) {
        fecha = new Date().getTime();
        this.tipo = tipo;
        this.cuerpo = cuerpo;
    }

    public Dato() {
        fecha = new Date().getTime();
        tipo = "otro";
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    @Override
    public String toString() {
        return "Dato{" +
                "fecha=" + fecha +
                ", tipo_de_dato=" + tipo +
                ", cuerpo=" + cuerpo +
                '}';
    }
}