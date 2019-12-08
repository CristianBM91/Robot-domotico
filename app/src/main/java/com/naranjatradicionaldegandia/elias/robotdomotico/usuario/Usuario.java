package com.naranjatradicionaldegandia.elias.robotdomotico.usuario;

public class Usuario {
    private String nombre;
    private String correo;
    private String telefono, robot;
    private long inicioSesion;

    public Usuario () {}

    public Usuario (String nombre, String correo, String telefono, long inicioSesion, String robot) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.inicioSesion = inicioSesion;
        this.robot = robot;
    }
    public Usuario (String nombre, String correo, String telefono, String robot) {
        this(nombre, correo, telefono, System.currentTimeMillis(), robot);
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

    public String getRobot() {
        return robot;
    }

    public void setRobot(String robot) {
        this.robot = robot;
    }
}

