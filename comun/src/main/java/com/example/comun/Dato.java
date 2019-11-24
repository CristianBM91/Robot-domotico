package com.example.comun;

public class Dato {
    private long fecha;
    private TipoDato tipo;
    private String cuerpo;


    public Dato(TipoDato tipo, String cuerpo) {
        fecha = System.currentTimeMillis();
        this.tipo = tipo;
        this.cuerpo = cuerpo;
    }

    public Dato() {
        fecha = System.currentTimeMillis();
        tipo = TipoDato.OTRO;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public TipoDato getTipo() {
        return tipo;
    }

    public void setTipo(TipoDato tipo) {
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
