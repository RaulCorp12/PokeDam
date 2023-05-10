package com.example.tfg_raul.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.tfg_raul.R;
import com.example.tfg_raul.clases.Pokemon;
import com.example.tfg_raul.utilidades.Preferencias;
/*
    Clase pública Elemento_pokedex

    Contiene la información del pokemon seleccionado de el listado de pokemons
 */
public class Elemento_pokedex extends AppCompatActivity {
    Preferencias preferencia=null;
    /*
    Método protected onCreate el cual se encargada de ejecutar el código de la pantalla llamada Elemento_pokedex
    una vez se llama a su pantalla desde el listado de pokemons.
    No devuelve ningún valor.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferencia= new Preferencias(this);
        if(preferencia.estado_modo_noche()){
            setTheme(R.style.Theme_TFG_Raul_dark);
        }
        else {
            setTheme(R.style.Theme_TFG_Raul);
        }
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_elemento_pokedex);
        ImageView imagen= findViewById(R.id.imagen_poke_unico);
        TextView nombre= findViewById(R.id.nombre_poke_unico);
        ImageView tipo1= findViewById(R.id.tipo1_poke_unico);
        ImageView tipo2= findViewById(R.id.tipo2_poke_unico);
        ImageView modelo= findViewById(R.id.modelo_poke);
        ImageView modelo_variocolor= findViewById(R.id.modelo_poke_variocolor);
        String tipo1TXT, tipo2TXT;
        TextView descripcion= findViewById(R.id.descripcion_poke_unico);
        TextView peso= findViewById(R.id.peso_unico_poke);
        TextView tamaño= findViewById(R.id.tamaño_unico_pokemon);
        Button salir= findViewById(R.id.salida_pokemon_unico);
        ConstraintLayout elemento = findViewById(R.id.layout_elemento_pokedex);
        if(preferencia.estado_modo_noche()){
            elemento.setBackground(getResources().getDrawable(R.drawable.fondo_dark));
        }
        else {
            elemento.setBackground(getResources().getDrawable(R.drawable.fondo));
        }
        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");

        Pokemon elegido= (Pokemon) datos.get("pokemon");
        Glide.with(this).load(elegido.getImagen()).into(imagen);
        nombre.setText(elegido.getNombre());
        tipo1TXT=elegido.getTipo1();
        tipo2TXT=elegido.getTipo2();
        Glide.with(this).load(tipo1TXT).into(tipo1);
        Glide.with(this).load(tipo2TXT).into(tipo2);
        descripcion.setText(elegido.getDescripcion());
        peso.setText(elegido.getPeso() +"Kg");
        tamaño.setText(elegido.getTamaño() +"M");
        Glide.with(this).load(elegido.getModelo()).into(modelo);
        Glide.with(this).load(elegido.getModeloVariocolor()).into(modelo_variocolor);

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio= new Intent(Elemento_pokedex.this, Lista_pokedex.class);
                cambio.putExtra("id",id_usu);
                startActivity(cambio);
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

    }
}