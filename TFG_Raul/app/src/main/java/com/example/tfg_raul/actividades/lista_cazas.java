package com.example.tfg_raul.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.example.tfg_raul.R;
import com.example.tfg_raul.utilidades.Preferencias;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class lista_cazas extends AppCompatActivity {

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
        setContentView(R.layout.activity_lista_cazas);
        BottomNavigationView menu= findViewById(R.id.menu_caza);
        cazas = findViewById(R.id.layout_cazas);
        ImageButton nueva_caza= findViewById(R.id.boton_nueva_caza);

        if(preferencia.cargar_modo_noche()==true){
            cazas.setBackground(getResources().getDrawable(R.drawable.fondo_dark));
            nueva_caza.setImageResource(R.drawable.baseline_add_to_photos_24);
        }
        else {
            cazas.setBackground(getResources().getDrawable(R.drawable.fondo));
            nueva_caza.setImageResource(R.drawable.ic_baseline_add_to_photos_white_24);
        }

        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");

        nueva_caza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cambio= new Intent(lista_cazas.this,crear_caza.class);
                cambio.putExtra("id",id_usu);
                startActivity(cambio);
            }
        });

        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.boton_menu_pokedex){
                    Intent cambio= new Intent(lista_cazas.this,lista_pokedex.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_guia){
                    Intent cambio= new Intent(lista_cazas.this,lista_guias.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_inicio){
                    Intent cambio= new Intent(lista_cazas.this, inicio_app.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_config){
                    Intent cambio= new Intent(lista_cazas.this,lista_configuracion.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                return true;
            }
        });
    }
    @Override
    public void onBackPressed(){

    }
}