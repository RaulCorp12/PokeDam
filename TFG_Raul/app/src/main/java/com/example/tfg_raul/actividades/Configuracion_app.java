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
/*
    Clase pública Configuracion_app

    Contiene los métodos para cambiar ciertos aspectos de la configuración de la aplicación
 */
public class Configuracion_app extends AppCompatActivity {
    Preferencias preferencia=null;
    /*
    Método protected onCreate el cual se encargada de ejecutar el código de la pantalla llamada Configuración_app
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
        setContentView(R.layout.activity_lista_configuracion);
        ConstraintLayout configuracion = findViewById(R.id.layout_config);
        BottomNavigationView menu= findViewById(R.id.menu_config);
        Switch modo_oscuro= findViewById(R.id.activar_oscuro);
        Button cerrar_sesion= findViewById(R.id.boton_cerrar_sesion);

        if(preferencia.cargar_modo_noche()){
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
                Intent envio= new Intent(Configuracion_app.this, Inicio_sesion.class);
                startActivity(envio);
            }
        });

        modo_oscuro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /*
            Método público onCheckedChanged. Recibe como parametros un objeto de tipo CompoundButton y un valor booleano.
            Este método se ejecuta cuando se cambia el estado del switch de la ventana de configuración, haciendo que se
            alterne el modo oscuro de la aplicación.
            No devuelve ningún valor.
             */
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
            /*
            Método público onNavigationItemSelected, recibe como parámetro un menú de objetos.
            Este método es llamado cuando se pulsa uno de los sub-menus de la barra inferior de la pantalla Configuracion_app,
            llamando a su correspondiente pantalla dependiendo del botón que sea pulsado, navegando hasta la pantalla seleccionada.
            Devuelve un valor de tipo booleano.
            */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.boton_menu_pokedex){
                    Intent cambio= new Intent(Configuracion_app.this, Lista_pokedex.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_guia){
                    Intent cambio= new Intent(Configuracion_app.this, Lista_guias.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_caza){
                    Intent cambio= new Intent(Configuracion_app.this, Lista_cazas.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_inicio){
                    Intent cambio= new Intent(Configuracion_app.this, Inicio_app.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                return true;
            }
        });
    }
    /*
    Método público reinicio, recibe un String como parámetro.
    Este hace un llamado a su misma pantalla para reiniciarla y que se visualicen los cambios
    en el modo oscuro de la aplicación.
    No devuelve ningún valor.
    */
    public void reinicio(String id){
        Intent cambio= new Intent(Configuracion_app.this, Configuracion_app.class);
        cambio.putExtra("id",id);
        startActivity(cambio);
        finish();
    }
    /*
    Método público onBackPressed, no recibe ningun valor como parámetro.
    Este ejecuta su contenido una vez el usuario pulse el botón de retroceder de su dispositivo movil.
    No devuelve ningún valor.
    */
    @Override
    public void onBackPressed(){

    }
}