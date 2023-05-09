package com.example.tfg_raul.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.example.tfg_raul.R;
import com.example.tfg_raul.utilidades.Preferencias;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;
/*
    Clase pública Inicio_app

    Representa el inicio de la aplicación para comenzar a navegar y acceder al perfil de usuario.
 */
public class Inicio_app extends AppCompatActivity {
    Preferencias preferencia=null;
    /*
    Método protected onCreate el cual se encargada de ejecutar el código de la pantalla llamada Inicio_app
    una vez se llama a su pantalla desde cualquier parte de la aplicación.
    No devuelve ningún valor.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferencia= new Preferencias(this);
        if(preferencia.cargar_modo_noche()){
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
        ConstraintLayout inicio = findViewById(R.id.layout_inicio);
        if(preferencia.cargar_modo_noche()){
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
                Intent cambio= new Intent(Inicio_app.this, Perfil_usuario.class);
                cambio.putExtra("id",id_usu);
                startActivity(cambio);
            }
        });

        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            /*
            Método público onNavigationItemSelected, recibe como parámetro un menú de objetos.
            Este método es llamado cuando se pulsa uno de los sub-menus de la barra inferior de la pantalla Inicio_app,
            llamando a su correspondiente pantalla dependiendo del botón que sea pulsado, navegando hasta la pantalla seleccionada.
            Devuelve un valor de tipo booleano.
            */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.boton_menu_pokedex){
                    Intent cambio= new Intent(Inicio_app.this, Lista_pokedex.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_guia){
                    Intent cambio= new Intent(Inicio_app.this, Lista_guias.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_caza){
                    Intent cambio= new Intent(Inicio_app.this, Lista_cazas.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_config){
                    Intent cambio= new Intent(Inicio_app.this, Configuracion_app.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                return true;
            }
        });

    }
    /*
    Método público onBackPressed, no recibe ningun valor como parámetro.
    Este ejecuta su contenido una vez el usuario pulse el botón de retroceder de su dispositivo movil.
    No devuelve ningún valor.
    */
    @Override
    public void onBackPressed(){
        new MaterialAlertDialogBuilder(this)
                .setMessage("¿Quieres salir al inicio de sesión?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent cambio= new Intent(Inicio_app.this, Inicio_sesion.class);
                        startActivity(cambio);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .show();
    }
}