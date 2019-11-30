package com.naranjatradicionaldegandia.elias.robotdomotico;

public class Usuario {
    private String nombre;
    private String correo;
    private String telefono;
    private long inicioSesion;

    public Usuario () {}

    public Usuario (String nombre, String correo, String telefono, long inicioSesion) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.inicioSesion = inicioSesion;
    }
    public Usuario (String nombre, String correo, String telefono) {
        this(nombre, correo, telefono, System.currentTimeMillis());
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono (String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}

