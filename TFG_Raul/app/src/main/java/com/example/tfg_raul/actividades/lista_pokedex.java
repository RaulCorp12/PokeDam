package com.example.tfg_raul.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.example.tfg_raul.R;
import com.example.tfg_raul.utilidades.Adaptador_pokemons;
import com.example.tfg_raul.clases.Pokemon;
import com.example.tfg_raul.utilidades.Preferencias;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class lista_pokedex extends AppCompatActivity {
    Preferencias preferencia=null;
    Adaptador_pokemons adaptadorPokemons;
    RecyclerView recycle;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firebase.collection("Pokemon");
    private ConstraintLayout pokedex;
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
        setContentView(R.layout.activity_lista_pokedex);
        BottomNavigationView menu= findViewById(R.id.menu_pokedex);
        pokedex= findViewById(R.id.layout_pokedex);
        List<Pokemon> listado_pokes= new ArrayList<Pokemon>();

        if(preferencia.cargar_modo_noche()==true){
            pokedex.setBackground(getResources().getDrawable(R.drawable.fondo_dark));
        }
        else {
            pokedex.setBackground(getResources().getDrawable(R.drawable.fondo));
        }

        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){
                        String nombre= doc.getString("Nombre");
                        String tipo1= doc.getString("Tipo_1");
                        String tipo2= doc.getString("Tipo_2");
                        Double altura= doc.getDouble("Altura");
                        Double peso= doc.getDouble("Peso");
                        String descrip= doc.getString("Descripcion");
                        String imagen= doc.getString("Imagen");
                        String shiny= doc.getString("Imagen_variocolor");
                        String modelo= doc.getString("modelo");
                        String variocolor= doc.getString("modeloVariocolor");
                        Pokemon poke= new Pokemon(nombre,descrip,tipo1,tipo2,altura,peso,imagen,shiny,modelo,variocolor);
                        listado_pokes.add(poke);
                    }
                    adaptadorPokemons = new Adaptador_pokemons(R.id.elemento_pokedex,getApplicationContext(),listado_pokes);
                    adaptadorPokemons.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int posicion= recycle.getChildAdapterPosition(v);
                            Pokemon elegido= listado_pokes.get(posicion);
                            Intent envio= new Intent(lista_pokedex.this, elemento_pokedex.class);
                            envio.putExtra("id",id_usu);
                            envio.putExtra("pokemon",elegido);
                            startActivity(envio);
                        }
                    });
                    recycle= findViewById(R.id.listado_pokemon);
                    recycle.setAdapter(adaptadorPokemons);
                    layoutManager= new LinearLayoutManager(getApplicationContext());
                    recycle.setLayoutManager(layoutManager);
                }
            }
        });

        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id= item.getItemId();

                if(id==R.id.boton_menu_inicio){
                    Intent cambio= new Intent(lista_pokedex.this, inicio_app.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_guia){
                    Intent cambio= new Intent(lista_pokedex.this, lista_guias.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_caza){
                    Intent cambio= new Intent(lista_pokedex.this, lista_cazas.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_config){
                    Intent cambio= new Intent(lista_pokedex.this, lista_configuracion.class);
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