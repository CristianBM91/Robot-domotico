package com.example.comun;

public enum TipoDato {
    DISTANCIA ("Distancia"),
    PRESENCIA ("Presencia"),
    LUZ ("Luz"),
    OTRO ("Otro");

    private final String texto;

    TipoDato(String texto) {
        this.texto = texto;
    }

    public String getTexto() { return texto; }
}
