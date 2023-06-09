package com.example.tfg_raul.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
/*
    Clase pública Lista_cazas

    Contiene el listado de las cazas registradas en la base de datos
 */
public class Lista_cazas extends AppCompatActivity {
    Preferencias preferencia=null;
    Adaptador_cazas adaptadorCazas;
    RecyclerView recycle;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firebase.collection("Caza");
    /*
    Método onCreate el cual se encargada de ejecutar el código de la pantalla llamada Lista_cazas
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
        setContentView(R.layout.activity_lista_cazas);
        BottomNavigationView menu= findViewById(R.id.menu_caza);
        ConstraintLayout cazas = findViewById(R.id.layout_cazas);
        ImageButton nueva_caza= findViewById(R.id.boton_nueva_caza);
        List<Caza> listado_cazas= new ArrayList<>();
        if(preferencia.estado_modo_noche()){
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
            /*
            Método público onComplete, recibe como parámetro una serie de operaciones.
            Este método es llamado en el proceso de ejecución de la pantalla, cargando los datos de las cazas de
            la base de datos y guardandolas en un listado, para después llamar al adaptador para colocarlos en el listado.
            No devuelve ningún valor.
            */
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
                            new MaterialAlertDialogBuilder(Lista_cazas.this)
                                    .setMessage("Que quieres hacer con esta caza")
                                    .setPositiveButton("Acceder", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent envio= new Intent(Lista_cazas.this, Elemento_caza.class);
                                            envio.putExtra("id",id_usu);
                                            envio.putExtra("caza",elegida);
                                            startActivity(envio);
                                        }
                                    }).setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new MaterialAlertDialogBuilder(Lista_cazas.this)
                                                    .setMessage("Estas seguro de querer eliminar la caza de "+elegida.getNombre())
                                                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            eliminarCaza(elegida.getId_caza());
                                                            reinicio(id_usu);
                                                        }
                                                    })
                                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {}
                                                    }).show();
                                        }
                                    }).show();
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
                Intent cambio= new Intent(Lista_cazas.this, Crear_caza.class);
                cambio.putExtra("id",id_usu);
                startActivity(cambio);
            }
        });
        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            /*
            Método público onNavigationItemSelected, recibe como parámetro un menú de objetos.
            Este método es llamado cuando se pulsa uno de los sub-menus de la barra inferior de la pantalla Lista_cazas,
            llamando a su correspondiente pantalla dependiendo del botón que sea pulsado, navegando hasta la pantalla seleccionada.
            Devuelve un valor de tipo booleano.
            */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.boton_menu_pokedex){
                    Intent cambio= new Intent(Lista_cazas.this, Lista_pokedex.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_guia){
                    Intent cambio= new Intent(Lista_cazas.this, Lista_guias.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_inicio){
                    Intent cambio= new Intent(Lista_cazas.this, Inicio_app.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_config){
                    Intent cambio= new Intent(Lista_cazas.this, Configuracion_app.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                return true;
            }
        });
    }
    /*
    Método público eliminarCaza, recibe un String como parámetro.
    Elimina de la base de datos una caza, la cual recibe su identificador como parámetro.
    Una vez eliminada llama al método para refrescar el listado
    No devuelve ningún valor.
    */
    public void eliminarCaza(String id_caza){
        View vista= findViewById(R.id.listado_cazas);
        Query sentencia= collectionReference.whereEqualTo("id_caza", id_caza);
        sentencia.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /*
            Método publico onComplete, recibe como parámetro una serie de operaciones.
            Este método es llamado se va a eliminar una caza, una vez se ha encontrado la colección en la
            que se encuentra la caza que se va a borrar.
            No devuelve ningún valor.
            */
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot id: task.getResult()) {
                    String idCaza = id.getId();
                    DocumentReference documento = firebase.collection("Caza").document(idCaza);
                    documento.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        /*
                        Método publico onSuccess, recibe como parámetro un objeto de tipo DocumentSnapshot
                        Este método es llamado cuando se logra encontrar el documento que contiene la caza del listado,
                        borrandolor inmediatamente de la base de datos.
                        No devuelve ningún valor.
                        */
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            documento.delete();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                /*
                                Método publico onFailure, recibe como parámetro un objeto de tipo Exception.
                                Este método es llamado cuando no se consigue encontrar el documento que representa a la caza
                                en el listado por algún error de la base de datos.
                                No devuelve ningún valor.
                                */
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(vista,"No se pudo borrar la caza en este momento", Snackbar.LENGTH_LONG);
                                }
                            });
                }
            }
        });
    }
    /*
    Método público reinicio, recibe un String como parámetro.
    Este hace un llamado a la pantalla de inicio para refrescar el listado de las cazas
    de pokemon actualizado
    No devuelve ningún valor.
        */
    public void reinicio(String id){
        Intent cambio= new Intent(Lista_cazas.this, Inicio_app.class);
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