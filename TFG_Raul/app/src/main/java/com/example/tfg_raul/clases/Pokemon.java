package com.example.tfg_raul.clases;

import androidx.annotation.NonNull;
import java.io.Serializable;
/*
    Clase pública caza. Implementa la interfaz Serializable
    */
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
    /*
    Constructor con parámetros de la clase Pokemon
     */
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

    /*
    Constructor sin parámetros de la clase Pokemon
     */
    public Pokemon() {
    }
    /*
    Método público toString. No recibe ningún parámetro.
    Genera un String con los datos del Pokemon.
    Retorna los datos del Pokemon.
    */
    @NonNull
    @Override
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

    public String getTipo1() {
        return tipo1;
    }

    public String getTipo2() {
        return tipo2;
    }

    public Double getTamaño() {
        return tamaño;
    }

    public Double getPeso() {
        return peso;
    }

    public String getImagen() {
        return imagen;
    }

    public String getImagenShiny() {return imagenShiny;}

    public String getModelo() {return modelo;}

    public String getModeloVariocolor() {return modeloVariocolor;}

}
