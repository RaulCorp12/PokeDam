package com.example.tfg_raul.clases;

import java.io.Serializable;

public class Caza implements Serializable {
    public String nombre;
    public String modelo;
    public String imagen;
    public String tiempo;
    public String intentos;
    public String id_usuario;
    public String metodo;
    public String id_caza;

    public Caza(String nombre, String modelo, String imagen, String tiempo, String intentos, String id_usuario, String metodo, String id_caza) {
        this.nombre = nombre;
        this.modelo = modelo;
        this.imagen = imagen;
        this.tiempo = tiempo;
        this.intentos = intentos;
        this.id_usuario = id_usuario;
        this.metodo = metodo;
        this.id_caza = id_caza;
    }
    public Caza() {
    }

    @Override
    public String toString(){
        return nombre+"/"+ modelo +"/"+tiempo+"/"+intentos+"/"+id_usuario+"/"+metodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getIntentos() {
        return intentos;
    }

    public void setIntentos(String intentos) {
        this.intentos = intentos;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getId_caza() {
        return id_caza;
    }

    public void setId_caza(String id_caza) {
        this.id_caza = id_caza;
    }
}
