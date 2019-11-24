package com.example.comun;

public interface DatosAsinc {
    interface EscuchadorElemento{
        void onRespuesta(Dato dato);
    }
    interface EscuchadorTamanyo{
        void onRespuesta(long tamanyo);
    }
    void elemento(String id, EscuchadorElemento escuchador);
    void anyade(Dato dato);
    String nuevo();
    void borrar(String id);
    void actualiza(String id, Dato dato);
    void tamanyo(EscuchadorTamanyo escuchador);
}
