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

public class elemento_caza extends AppCompatActivity {
    private ConstraintLayout cazas;
    FirebaseFirestore firebaseID= FirebaseFirestore.getInstance();
    TextView tiempo;
    Button reanudar;
    boolean tiempoIniciado= false;
    Timer temporizador;
    TimerTask tarea;
    Double tempo;
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
        cazas = findViewById(R.id.layout_cazar_pokemon);

        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");

        Caza elegida= (Caza) datos.get("caza");

        Glide.with(this).load(elegida.getModelo()).into(imagen);
        nombre.setText(elegida.getNombre());

        if(elegida.getMetodo().equals("Pesca")){
            cazas.setBackgroundResource(R.drawable.pesca);
        }
        else if(elegida.getMetodo().equals("Encuentros")){
            cazas.setBackgroundResource(R.drawable.encuentros);
        }
        else if(elegida.getMetodo().equals("Cadenas")){
            cazas.setBackgroundResource(R.drawable.cadenas);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                intentos.setTextColor(getColor(R.color.white));
            }
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
                if(tiempoIniciado== false){
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

    @Override
    public void onBackPressed(){

    }

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

    private String recogerTiempo(){
        int redondeo= (int) Math.round(tempo);
        int segundos= ((redondeo % 86400) % 3600) % 60;
        int minutos= ((redondeo % 86400) % 3600) / 60;
        int horas= ((redondeo % 86400) / 3600);
        return formateoTiempo(segundos,minutos,horas);
    }

    private String formateoTiempo(int segundos, int minutos, int horas){
        return String.format("%02d",horas)+":"+ String.format("%02d",minutos)+":" +String.format("%02d",segundos);
    }

    public void actualizarDatos(String id_caza, String intentos, String tiempo, String id_usu){

        CollectionReference collectionReference = firebaseID.collection("Caza");
        Query sentencia= collectionReference.whereEqualTo("id_caza", id_caza);
        sentencia.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot id : task.getResult()) {
                    DocumentReference documento= firebaseID.collection("Caza").document(id.getId());
                    documento.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Map<String, Object> actualizacion= new HashMap<>();
                            actualizacion.put("intentos",intentos);
                            actualizacion.put("tiempo", tiempo);
                            documento.update(actualizacion);

                            Intent cambio= new Intent(elemento_caza.this, lista_cazas.class);
                            cambio.putExtra("id",id_usu);
                            startActivity(cambio);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}