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

public class inicio_app extends AppCompatActivity {

    Preferencias preferencia=null;
    private ConstraintLayout inicio;
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
        setContentView(R.layout.activity_inicio_app);
        BottomNavigationView menu= findViewById(R.id.menu_inicio);
        ImageButton perfil= findViewById(R.id.ver_perfil);
        inicio= findViewById(R.id.layout_inicio);
        if(preferencia.cargar_modo_noche()==true){
            inicio.setBackground(getResources().getDrawable(R.drawable.fondo_dark));
        }
        else {
            inicio.setBackground(getResources().getDrawable(R.drawable.fondo));
        }

        Bundle recibo= getIntent().getExtras();
        String id_usu= recibo.getString("id");

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cambio= new Intent(inicio_app.this,perfil_usuario.class);
                cambio.putExtra("id",id_usu);
                startActivity(cambio);
            }
        });

        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.boton_menu_pokedex){
                    Intent cambio= new Intent(inicio_app.this,lista_pokedex.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_guia){
                    Intent cambio= new Intent(inicio_app.this,lista_guias.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_caza){
                    Intent cambio= new Intent(inicio_app.this, lista_cazas.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_config){
                    Intent cambio= new Intent(inicio_app.this,lista_configuracion.class);
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