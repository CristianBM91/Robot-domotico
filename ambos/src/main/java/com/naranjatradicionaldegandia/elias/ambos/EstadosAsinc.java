package com.naranjatradicionaldegandia.elias.ambos;

public interface EstadosAsinc {
    interface EscuchadorElemento{
        void onRespuesta(Estado estado);
    }
    interface EscuchadorTamanyo{
        void onRespuesta(long tamanyo);
    }
    void elemento(String id, EstadosAsinc.EscuchadorElemento escuchador);
    void anyade(Estado estado);
    String nuevo();
    void borrar(String id);
    void actualiza(String id, Estado estado);
    void tamanyo(DatosAsinc.EscuchadorTamanyo escuchador);
}

