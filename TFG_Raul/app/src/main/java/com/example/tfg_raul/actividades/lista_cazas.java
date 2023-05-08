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
import android.widget.ImageButton;
import com.example.tfg_raul.R;
import com.example.tfg_raul.clases.Caza;
import com.example.tfg_raul.utilidades.Adaptador_cazas;
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

public class lista_cazas extends AppCompatActivity {
    Preferencias preferencia=null;
    Adaptador_cazas adaptadorCazas;
    RecyclerView recycle;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firebase.collection("Caza");

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
        setContentView(R.layout.activity_lista_cazas);
        BottomNavigationView menu= findViewById(R.id.menu_caza);
        ConstraintLayout cazas = findViewById(R.id.layout_cazas);
        ImageButton nueva_caza= findViewById(R.id.boton_nueva_caza);
        List<Caza> listado_cazas= new ArrayList<>();

        if(preferencia.cargar_modo_noche()){
            cazas.setBackground(getResources().getDrawable(R.drawable.fondo_dark));
            nueva_caza.setImageResource(R.drawable.ic_baseline_add_to_photos_white_24);
        }
        else {
            cazas.setBackground(getResources().getDrawable(R.drawable.fondo));
            nueva_caza.setImageResource(R.drawable.baseline_add_to_photos_24);
        }

        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc: task.getResult()){
                        String nombre= doc.getString("nombre_pokemon");
                        String imagen= doc.getString("imagen");
                        String modelo= doc.getString("modelo");
                        String intentos= doc.getString("intentos");
                        String id_usuario= doc.getString("id_usuario");
                        String tiempo= doc.getString("tiempo");
                        String metodo= doc.getString("metodo");
                        String id_caza= doc.getString("id_caza");
                        Caza caza= new Caza(nombre,modelo,imagen,tiempo,intentos,id_usuario,metodo, id_caza);
                        if(caza.getId_usuario().matches(id_usu)){
                            listado_cazas.add(caza);
                        }
                    }
                    adaptadorCazas= new Adaptador_cazas(R.id.elemento_caza, getApplicationContext(), listado_cazas);
                    adaptadorCazas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int posicion= recycle.getChildAdapterPosition(view);
                            Caza elegida= listado_cazas.get(posicion);
                            Intent envio= new Intent(lista_cazas.this, elemento_caza.class);
                            envio.putExtra("id",id_usu);
                            envio.putExtra("caza",elegida);
                            startActivity(envio);
                        }
                    });
                    recycle= findViewById(R.id.listado_cazas);
                    recycle.setAdapter(adaptadorCazas);
                    layoutManager= new LinearLayoutManager(getApplicationContext());
                    recycle.setLayoutManager(layoutManager);
                }

            }
        });
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