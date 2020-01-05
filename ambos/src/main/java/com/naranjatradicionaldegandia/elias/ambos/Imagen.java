package com.naranjatradicionaldegandia.elias.ambos;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Map;

public class Imagen {
    String titulo;
    String url;
    long tiempo;
    String correo;
    Map<String, Float> anotaciones;
    public Imagen() {}
    public Imagen(String titulo, String url, String correo, Map<String, Float> anotationes) {
        this.titulo = titulo;
        this.url = url;
        this.tiempo = new Date().getTime();
        this.correo = correo;
        this.anotaciones = anotaciones;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo() {
        this.tiempo = new Date().getTime();
    }

    public Map<String, Float> getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(Map<String, Float> anotaciones) {
        this.anotaciones = anotaciones;
    }


    public void setCorreo(String correo) {
        this.correo = correo;
    }

}