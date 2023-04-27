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

public class elemento_pokedex extends AppCompatActivity {

    Preferencias preferencia=null;

    private ConstraintLayout elemento;
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
        setContentView(R.layout.activity_elemento_pokedex);
        ImageView imagen= findViewById(R.id.imagen_poke_unico);
        TextView nombre= findViewById(R.id.nombre_poke_unico);
        TextView tipo1= findViewById(R.id.tipo1_poke_unico);
        TextView tipo2= findViewById(R.id.tipo2_poke_unico);
        TextView descripcion= findViewById(R.id.descripcion_poke_unico);
        TextView peso= findViewById(R.id.peso_unico_poke);
        TextView tamaño= findViewById(R.id.tamño_unico_pokemon);
        Button salir= findViewById(R.id.salida_pokemon_unico);
        elemento = findViewById(R.id.layout_elemento_pokedex);

        if(preferencia.cargar_modo_noche()==true){
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
        tipo1.setText(elegido.getTipo1());
        if(elegido.getTipo2()!=""){
            tipo2.setText(elegido.getTipo2());
        }
        else{
            tipo2.setText("");
        }
        descripcion.setText(elegido.getDescripcion());
        peso.setText(String.valueOf(elegido.getPeso()));
        tamaño.setText(String.valueOf(elegido.getTamaño()));

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio= new Intent(elemento_pokedex.this,lista_pokedex.class);
                cambio.putExtra("id",id_usu);
                startActivity(cambio);
            }
        });
    }

    @Override
    public void onBackPressed(){

    }
}