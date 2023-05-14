package com.example.tfg_raul.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.tfg_raul.R;
import com.example.tfg_raul.clases.Caza;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
/*
    Clase pública Elemento_caza

    Contiene la información de la caza una vez es seleccionada de su listado
 */
public class Elemento_caza extends AppCompatActivity {
    FirebaseFirestore firebaseID= FirebaseFirestore.getInstance();
    TextView tiempo;
    Button reanudar;
    boolean tiempoIniciado= false;
    Timer temporizador;
    TimerTask tarea;
    Double tempo;
    /*
    Método protected onCreate el cual se encarga de ejecutar el código de la pantalla llamada Elemento_caza
    una vez se llama a su pantalla desde el listado de cazas seleccionando una caza.
    No devuelve ningún valor.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_elemento_caza);
        Button aumentar= findViewById(R.id.boton_aumentar_caza);
        reanudar= findViewById(R.id.boton_reanudar_caza);
        Button guardar= findViewById(R.id.boton_guardar_caza);
        TextView nombre= findViewById(R.id.nombre_pokemon_caza);
        TextView intentos= findViewById(R.id.intentos_caza);
        tiempo= findViewById(R.id.tiempo_caza);
        ImageView imagen= findViewById(R.id.imagen_caza);
        temporizador= new Timer();
        ConstraintLayout cazas = findViewById(R.id.layout_cazar_pokemon);
        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");

        Caza elegida= (Caza) datos.get("caza");
        Glide.with(this).load(elegida.getModelo()).into(imagen);
        nombre.setText(elegida.getNombre());

        switch (elegida.getMetodo()) {
            case "Pesca":
                cazas.setBackgroundResource(R.drawable.pesca);
                break;
            case "Encuentros":
                cazas.setBackgroundResource(R.drawable.encuentros);
                break;
            case "Cadenas":
                cazas.setBackgroundResource(R.drawable.cadenas);
                break;
        }

        intentos.setText(elegida.getIntentos());
        tiempo.setText(elegida.getTiempo());
        aumentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentos.setText(String.valueOf(Integer.parseInt(intentos.getText().toString())+1));
            }
        });
        reanudar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tiempoIniciado){
                    tiempoIniciado=true;
                    reanudar.setBackgroundResource(R.drawable.pause_button);
                    iniciarTiempo();
                }
                else{
                    tiempoIniciado=false;
                    reanudar.setBackgroundResource(R.drawable.play_button);
                    tarea.cancel();
                }
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos(elegida.getId_caza().trim(), intentos.getText().toString(), tiempo.getText().toString(), id_usu);
            }
        });
    }
    /*
    Método público iniciarTiempo, no recibe ningun valor como parámetro.
    Este inicia o detiene el temporizador de la caza dependiendo del valor booleano que contempla
    si esta activo o no.
    No devuelve ningún valor.
    */
    public void iniciarTiempo(){
        tempo= Double.valueOf(tiempo.getText().toString().substring(6,8));
        tarea= new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tempo++;
                        tiempo.setText(recogerTiempo());
                    }
                });
            }
        };
        temporizador.scheduleAtFixedRate(tarea, 0,1000);
    }
    /*
    Método público recogerTiempp, no recibe ningun valor como parámetro.
    Recoge el tiempo que esté registrado en el temporizador una vez es detenido por el usuario,
    despué envia los datos a otro método para su formateo.
    Devuelve un String que representa el tiempo recogido.
    */
    private String recogerTiempo(){
        int redondeo= (int) Math.round(tempo);
        int segundos= ((redondeo % 86400) % 3600) % 60;
        int minutos= ((redondeo % 86400) % 3600) / 60;
        int horas= ((redondeo % 86400) / 3600);
        return formateoTiempo(segundos,minutos,horas);
    }
    /*
    Método público formateoTiempo, recibe como parámetros los valores de segundos, minutos y horas del temporizador.
    Formatea los datos que le llegan con el módelo del temporizador.
    Devuelve un String que representa el tiempo formateado.
    */
    private String formateoTiempo(int segundos, int minutos, int horas){
        return String.format("%02d",horas)+":"+ String.format("%02d",minutos)+":" +String.format("%02d",segundos);
    }
    /*
    Método público actualizarDatos, recibe como parámetros los diferentes campos que contiene una caza en la base de datos.
    Actualiza la caza con los nuevos datos que se han registrado en la pantalla, para después volver al listado de cazas
    No devuelve nigún valor.
    */
    public void actualizarDatos(String id_caza, String intentos, String tiempo, String id_usu){
        CollectionReference collectionReference = firebaseID.collection("Caza");
        Query sentencia= collectionReference.whereEqualTo("id_caza", id_caza);
        sentencia.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /*
            Método público onComplete. Recibe como parámetro un listado de tareas.
            Este método es llamado cuendo se logra acceder al documento que representa las cazas
            de pokemon en la base de datos.
            No devuelve ningun valor.
            */
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot id : task.getResult()) {
                    DocumentReference documento= firebaseID.collection("Caza").document(id.getId());
                    documento.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        /*
                        Método público onComplete. Recibe como parámetro un listado de tareas.
                        Este método es llamado cuendo se logra acceder al documento que representa las cazas
                        de pokemon en la base de datos.
                        No devuelve ningun valor.
                        */
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Map<String, Object> actualizacion= new HashMap<>();
                            actualizacion.put("intentos",intentos);
                            actualizacion.put("tiempo", tiempo);
                            documento.update(actualizacion);

                            Intent cambio= new Intent(Elemento_caza.this, Lista_cazas.class);
                            cambio.putExtra("id",id_usu);
                            startActivity(cambio);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        /*
                        Método onFailure. Recibe como parámetro un objeto de tipo Exception.
                        Este método es llamado cuendo no se consigue insertar correctamente la caza en la
                        base de datos.
                        No devuelve ningun valor.
                        */
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    /*
                    Método onFailure. Recibe como parámetro un objeto de tipo Exception.
                    Este método es llamado cuendo no se logra acceder al documento que representa las cazas
                    de pokemon en la base de datos.
                    No devuelve ningun valor.
                    */
                    @Override
                    public void onFailure(@NonNull Exception e) {
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