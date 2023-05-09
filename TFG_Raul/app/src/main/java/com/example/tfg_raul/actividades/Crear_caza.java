package com.example.tfg_raul.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.bumptech.glide.Glide;
import com.example.tfg_raul.R;
import com.example.tfg_raul.clases.Caza;
import com.example.tfg_raul.clases.Pokemon;
import com.example.tfg_raul.utilidades.Preferencias;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
    Clase pública Crear_caza

    Contiene los métodos y elementos para crear una nueva caza en la base de datos
 */
public class Crear_caza extends AppCompatActivity {
    Preferencias preferencia=null;
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firebase.collection("Pokemon");
    private ConstraintLayout cazas;
    /*
    Método protected onCreate el cual se encarga de ejecutar el código de la pantalla llamada Crear_caza
    una vez se llama a su pantalla desde el listado de cazas.
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
        setContentView(R.layout.activity_crear_caza);
        Button crear_caza= findViewById(R.id.boton_crear_caza);
        Button cancelar= findViewById(R.id.boton_salir_caza);
        Spinner pokemons= findViewById(R.id.selector_pokemon);
        Spinner metodos= findViewById(R.id.selector_método);
        ImageView imagen= findViewById(R.id.imagen_pokemon_caza);
        List<Pokemon> listado_pokes= new ArrayList<>();
        List<String> nombres= new ArrayList<>();
        cazas = findViewById(R.id.layout_crear_cazas);
        if(preferencia.cargar_modo_noche()){
            cazas.setBackground(getResources().getDrawable(R.drawable.fondo_dark));
        }
        else {
            cazas.setBackground(getResources().getDrawable(R.drawable.fondo));
        }
        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");

        Caza nueva= new Caza();
        nueva.setId_usuario(id_usu);
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /*
            Método publico onComplete, recibe como parámetro una serie de operaciones.
            Este método es llamado cuando el spinner de la pantalla tiene que rellenarse con los nombres de los pokemon
            de la base de datos para poder agregarlos a la caza.
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
                        String imagenVariocolor= doc.getString("Imagen_variocolor");
                        String modelo= doc.getString("modelo");
                        String modeloVariocolor= doc.getString("modeloVariocolor");
                        Pokemon poke= new Pokemon(nombre,descrip,tipo1,tipo2,altura,peso,imagen,imagenVariocolor,modelo,modeloVariocolor);
                        listado_pokes.add(poke);
                    }
                    nombres.add(0,"Seleccionar");
                    for(Pokemon p: listado_pokes){
                        nombres.add(p.getNombre());
                    }
                    ArrayAdapter<String> adaptador= new ArrayAdapter<>(Crear_caza.this,
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, nombres);
                    pokemons.setAdapter(adaptador);

                    pokemons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        /*
                        Método publico onItemSelected, recibe como parámetro un adaptador, un objeto de tipo View, un entero que representa la posición
                        del spinner y un long representando la id del elemento.
                        Este método es llamado cuando el usuario selecciona un nombre del spinner, guardando los datos de la caza
                        en caso de que quiera crearla inmediatamente después de elegirlo.
                        No devuelve ningún valor.
                        */
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).toString().matches("Seleccionar")){}
                            else{
                                nueva.setNombre(parent.getItemAtPosition(position).toString());
                                nueva.setIntentos("0");
                                nueva.setTiempo("00:00:00");
                                nueva.setModelo(listado_pokes.get(position-1).getModeloVariocolor());
                                nueva.setImagen(listado_pokes.get(position-1).getImagenShiny());
                                nueva.setId_caza(generarIdCaza());
                                Glide.with(cazas.getContext()).load(nueva.getImagen()).into(imagen);
                                Snackbar.make(view,"Seleccionaste "+parent.getItemAtPosition(position), Snackbar.LENGTH_LONG).show();
                            }
                        }
                        /*
                        Método publico onNothingSelected, recibe como parámetro un adaptador
                        Este método es llamado cuando el usuario no selecciona ningún valor del spinner de nombres.
                        No devuelve ningún valor.
                        */
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                }
            }
        });

        metodos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /*
            Método publico onItemSelected, recibe como parámetro un adaptador, un objeto de tipo View, un entero que representa la posición
            del spinner y un long representando la id del elemento.
            Este método es llamado cuando el usuario selecciona un método de caza de su spinner, guardando los datos en su objeto
            de tipo caza, por defecto se selecciona el método de encuentros.
            No devuelve ningún valor.
            */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nueva.setMetodo(String.valueOf(parent.getItemAtPosition(position)));
            }
            /*
            Método publico onNothingSelected, recibe como parámetro un adaptador
            Este método es llamado cuando el usuario no selecciona ningún valor del spinner de métodos.
            No devuelve ningún valor.
            */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        crear_caza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nueva.getMetodo()==null || nueva.getNombre()==null){
                    Snackbar.make(v,"Debes seleccionar todos los datos de la caza",Snackbar.LENGTH_LONG).show();
                }
                else{
                    Map<String, Object> caza= new HashMap<>();
                    caza.put("nombre_pokemon",nueva.getNombre());
                    caza.put("imagen",nueva.getImagen());
                    caza.put("modelo",nueva.getModelo());
                    caza.put("id_usuario",nueva.getId_usuario());
                    caza.put("intentos", nueva.getIntentos());
                    caza.put("metodo", nueva.getMetodo());
                    caza.put("tiempo",nueva.getTiempo());
                    caza.put("id_caza", nueva.getId_caza());
                    firebase.collection("Caza").add(caza)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                /*
                                Método público onSuccess, recibe como parámetro un objeto de tipo DocumentSnapshot.
                                Este método es llamado cuando se insertó correctamente la caza en la base de datos.
                                No devuelve ningún valor.
                                */
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Intent cambio= new Intent(Crear_caza.this, Lista_cazas.class);
                                    cambio.putExtra("id",id_usu);
                                    startActivity(cambio);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                /*
                                Método publico onFailure, recibe como parámetro un objeto de tipo Exception.
                                Este método es llamado cuando no se realizó bien la inserción de la caza en la base de datos.
                                No devuelve ningún valor.
                                */
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(v,"No se pudo crear la caza debido a un error",Snackbar.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio= new Intent(Crear_caza.this, Lista_cazas.class);
                cambio.putExtra("id",id_usu);
                startActivity(cambio);
            }
        });
    }
    /*
    Método público generarIdCaza, no recibe ningun valor como parámetro.
    Genera un identificador para la caza creada en base a valores aleatorios.
    Devuelve un String que representa el id generado aleatoriamente.
    */
    public String generarIdCaza(){
        String caracteres="ABCDEFGHIJKLMÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz1234567890";
        String id="";
        Log.i("Mensaje", String.valueOf(caracteres.charAt(0)));
        for(int i=0; i<8; i++){
            id+=caracteres.charAt((int) (Math.random()*caracteres.length()));
        }
        return id;
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