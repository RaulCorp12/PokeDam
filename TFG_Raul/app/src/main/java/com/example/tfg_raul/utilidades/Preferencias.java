package com.example.tfg_raul.utilidades;

import android.content.Context;
import android.content.SharedPreferences;
/*
    Clase pública Preferencias.

    Esta contiene las preferencias de la aplicacióna la hora de mostrar su interfaz o ciertos
    datos, como el modo oscuro interno de la aplicación.
 */
public class Preferencias {
    SharedPreferences preferencia;
    public Preferencias(Context contexto){
        preferencia= contexto.getSharedPreferences("archivo", Context.MODE_PRIVATE);
    }
    /*
        Método público activar_modo_noche. Recibe un valor boolean que representa el estado del modo oscuro.

        Este método es el encargado de activar el modo oscuro en la aplicación una vez es llamado desde
        la pestañá de configuración.
        No devuelve ningún valor
     */
    public void activar_modo_noche(Boolean estado){
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putBoolean("Noche",estado);
        editor.commit();
    }
    /*
        Método público cargar_modo_noche. No recibe ningún parámetro

        Este método devuelve al llamado un valor booleano que representa si el modo noche está o no
        activado para realizar diferentes acciones al respecto.
        Devuelve un valor booleano.
     */
    public Boolean estado_modo_noche(){
        Boolean estado= preferencia.getBoolean("Noche",false);
        return estado;
    }
}