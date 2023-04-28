package com.example.tfg_raul.clases;

import java.io.Serializable;

public class Pokemon implements Serializable {

    public String nombre;
    public String descripcion;
    public String tipo1;
    public String tipo2;
    public Double tamaño;
    public Double peso;
    public String imagen;
    public String imagenShiny;
    public String modelo;
    public String modeloVariocolor;

    public Pokemon(String nombre, String descripcion, String tipo1, String tipo2, Double tamaño, Double peso, String imagen, String imagenShiny, String modelo, String modeloVariocolor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo1 = tipo1;
        this.tipo2 = tipo2;
        this.tamaño = tamaño;
        this.peso = peso;
        this.imagen = imagen;
        this.imagenShiny= imagenShiny;
        this.modelo= modelo;
        this.modeloVariocolor= modeloVariocolor;
    }

    public Pokemon() {
    }

    public String toString(){
        return nombre+"/"+descripcion+"/"+tipo1+"/"+tipo2+"/"+tamaño+"/"+peso+"/"+imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo1() {
        return tipo1;
    }

    public void setTipo1(String tipo1) {
        this.tipo1 = tipo1;
    }

    public String getTipo2() {
        return tipo2;
    }

    public void setTipo2(String tipo2) {
        this.tipo2 = tipo2;
    }

    public Double getTamaño() {
        return tamaño;
    }

    public void setTamaño(Double tamaño) {
        this.tamaño = tamaño;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getImagenShiny() {return imagenShiny;}

    public void setImagenShiny(String imagenShiny) {this.imagenShiny = imagenShiny;}

    public String getModelo() {return modelo;}

    public void setModelo(String modelo) {this.modelo = modelo;}

    public String getModeloVariocolor() {return modeloVariocolor;}

    public void setModeloVariocolor(String modeloVariocolor) {this.modeloVariocolor = modeloVariocolor;}
}
