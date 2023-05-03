package com.example.tfg_raul.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import com.example.tfg_raul.R;
import com.example.tfg_raul.clases.Caza;
import com.example.tfg_raul.utilidades.Preferencias;

public class elemento_caza extends AppCompatActivity {
    Preferencias preferencia=null;
    private ConstraintLayout cazas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferencia= new Preferencias(this);
        if(preferencia.cargar_modo_noche()==true){
            setTheme(R.style.Theme_TFG_Raul_dark);
        }
        else {
            setTheme(R.style.Theme_TFG_Raul);
        }
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_elemento_caza);
        cazas = findViewById(R.id.layout_cazar_pokemon);
        if(preferencia.cargar_modo_noche()==true){
            cazas.setBackground(getResources().getDrawable(R.drawable.fondo_dark));
        }
        else {
            cazas.setBackground(getResources().getDrawable(R.drawable.fondo));
        }

        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");

        Caza elegida= (Caza) datos.get("caza");
    }

    @Override
    public void onBackPressed(){

    }
}