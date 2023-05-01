package com.example.tfg_raul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.tfg_raul.actividades.lista_cazas;
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

        crear_caza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio= new Intent(com.example.tfg_raul.crear_caza.this, lista_cazas.class);
                startActivity(cambio);
            }
        });
    }
    @Override
    public void onBackPressed(){

    }
}