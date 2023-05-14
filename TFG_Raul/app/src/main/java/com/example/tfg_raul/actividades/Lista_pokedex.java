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
/*
    Clase listado_pokedex

    Muestra el listado de los pokemon que contiene la base de datos
 */
public class Lista_pokedex extends AppCompatActivity {
    Preferencias preferencia=null;
    Adaptador_pokemons adaptadorPokemons;
    RecyclerView recycle;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firebase.collection("Pokemon");
    /*
    Método onCreate el cual es el encargado de ejecutar el código de la pantalla del listado de pokemons
    una vez se llama a su pantalla desde cualquier parte de la aplicación.
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
        setContentView(R.layout.activity_lista_pokedex);
        BottomNavigationView menu= findViewById(R.id.menu_pokedex);
        ConstraintLayout pokedex = findViewById(R.id.layout_pokedex);
        List<Pokemon> listado_pokes= new ArrayList<>();
        if(preferencia.estado_modo_noche()){
            pokedex.setBackground(getResources().getDrawable(R.drawable.fondo_dark));
        }
        else {
            pokedex.setBackground(getResources().getDrawable(R.drawable.fondo));
        }
        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /*
            Método público onComplete, recibe como parámetro una serie de operaciones.
            Este método es llamado en el proceso de ejecución de la pantalla, cargando los datos de los pokemon de
            la base de datos y guardandolos en un listado y llamando al adaptador para colocarlos.
            No devuelve ningún valor.
            */
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
                            Intent envio= new Intent(Lista_pokedex.this, Elemento_pokedex.class);
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
            /*
            Método publico onNavigationItemSelected, recibe como parámetro un menú de objetos.
            Este método es llamado cuando se pulsa uno de los sub-menus de la barra inferior de la pantalla Lista_pokedex,
            llamando a su correspondiente pantalla dependiendo del botón que sea pulsado, navegando hasta la pantalla seleccionada.
            Devuelve un valor de tipo booleano.
            */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.boton_menu_inicio){
                    Intent cambio= new Intent(Lista_pokedex.this, Inicio_app.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_guia){
                    Intent cambio= new Intent(Lista_pokedex.this, Lista_guias.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_caza){
                    Intent cambio= new Intent(Lista_pokedex.this, Lista_cazas.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_config){
                    Intent cambio= new Intent(Lista_pokedex.this, Configuracion_app.class);
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

    }
}