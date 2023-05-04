package com.example.tfg_raul.actividades;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.example.tfg_raul.R;
import com.example.tfg_raul.utilidades.Preferencias;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class lista_configuracion extends AppCompatActivity {

    Preferencias preferencia=null;
    private ConstraintLayout configuracion;
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
        setContentView(R.layout.activity_lista_configuracion);
        configuracion= findViewById(R.id.layout_config);
        BottomNavigationView menu= findViewById(R.id.menu_config);
        Switch modo_oscuro= findViewById(R.id.activar_oscuro);
        Button cerrar_sesion= findViewById(R.id.boton_cerrar_sesion);

        if(preferencia.cargar_modo_noche()==true){
            modo_oscuro.setChecked(true);
            configuracion.setBackground(getResources().getDrawable(R.drawable.fondo_dark));
        }
        else{
            modo_oscuro.setChecked(false);
            configuracion.setBackground(getResources().getDrawable(R.drawable.fondo));
        }

        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");

        cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent envio= new Intent(lista_configuracion.this, inicio_sesion.class);
                startActivity(envio);
            }
        });

        modo_oscuro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    preferencia.activar_modo_noche(true);
                    reinicio(id_usu);
                }
                else{
                    preferencia.activar_modo_noche(false);
                    reinicio(id_usu);
                }
            }
        });

        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.boton_menu_pokedex){
                    Intent cambio= new Intent(lista_configuracion.this,lista_pokedex.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_guia){
                    Intent cambio= new Intent(lista_configuracion.this,lista_guias.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_caza){
                    Intent cambio= new Intent(lista_configuracion.this, lista_cazas.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_inicio){
                    Intent cambio= new Intent(lista_configuracion.this,inicio_app.class);
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
    public void reinicio(String id){
        Intent cambio= new Intent(lista_configuracion.this, lista_configuracion.class);
        cambio.putExtra("id",id);
        startActivity(cambio);
        finish();
    }
}