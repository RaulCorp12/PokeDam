package com.example.tfg_raul.clases;

import java.io.Serializable;

public class Caza implements Serializable {
    public String nombre;
    public String imagen;
    public String tiempo;
    public Long intentos;
    public String id_usuario;
    public Caza(String nombre, String imagen, String tiempo, Long intentos, String id_usuario) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.tiempo = tiempo;
        this.intentos = intentos;
        this.id_usuario = id_usuario;
    }
    public Caza() {
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public Long getIntentos() {
        return intentos;
    }

    public void setIntentos(Long intentos) {
        this.intentos = intentos;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }
}
