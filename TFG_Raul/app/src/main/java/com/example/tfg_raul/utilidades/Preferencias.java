package com.example.tfg_raul.utilidades;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {

    SharedPreferences preferencia;
    public Preferencias(Context contexto){
        preferencia= contexto.getSharedPreferences("archivo", Context.MODE_PRIVATE);
    }

    public void activar_modo_noche(Boolean estado){
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putBoolean("Noche",estado);
        editor.commit();
    }

    public Boolean cargar_modo_noche(){
        Boolean estado= preferencia.getBoolean("Noche",false);
        return estado;
    }
}