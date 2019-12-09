package com.naranjatradicionaldegandia.elias.ambos;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Map;

public class Imagen {
    String titulo;
    String url;
    long tiempo;
    Map<String, Float> anotaciones;
    public Imagen() {}
    public Imagen(String titulo, String url, Map<String, Float> anotationes) {
        this.titulo = titulo;
        this.url = url;
        this.tiempo = new Date().getTime();
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

    static void registrarImagen(String titulo, String url, Map<String, Float> anotaciones) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Imagen imagen = new Imagen(titulo, url, anotaciones);
        db.collection("imagenes").document().set(imagen);
    }
}