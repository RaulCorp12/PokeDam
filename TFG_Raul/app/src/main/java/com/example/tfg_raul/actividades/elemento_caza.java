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
import com.example.tfg_raul.clases.Caza;
import java.util.Timer;
import java.util.TimerTask;

public class elemento_caza extends AppCompatActivity {
    private ConstraintLayout cazas;
    TextView tiempo;
    Button reanudar;
    boolean tiempoIniciado= false;
    Timer temporizador;
    TimerTask tarea;
    Double tempo=0.0;
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

        intentos.setText(String.valueOf(elegida.getIntentos().intValue()));
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
                Intent cambio= new Intent(elemento_caza.this, lista_cazas.class);
                cambio.putExtra("id",id_usu);
                startActivity(cambio);
            }
        });
    }

    @Override
    public void onBackPressed(){

    }

    public void iniciarTiempo(){
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
}