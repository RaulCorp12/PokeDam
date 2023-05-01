package com.example.tfg_raul.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tfg_raul.R;
import com.example.tfg_raul.utilidades.Preferencias;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class crear_caza extends AppCompatActivity {

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
        setContentView(R.layout.activity_crear_caza);
        Button crear_caza= findViewById(R.id.boton_crear_caza);
        Button cancelar= findViewById(R.id.boton_salir_caza);
        cazas = findViewById(R.id.layout_crear_cazas);
        if(preferencia.cargar_modo_noche()==true){
            cazas.setBackground(getResources().getDrawable(R.drawable.fondo_dark));
        }
        else {
            cazas.setBackground(getResources().getDrawable(R.drawable.fondo));
        }

        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");

        crear_caza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio= new Intent(com.example.tfg_raul.actividades.crear_caza.this, lista_cazas.class);
                cambio.putExtra("id",id_usu);
                startActivity(cambio);
            }
        });
    }
    @Override
    public void onBackPressed(){

    }
}