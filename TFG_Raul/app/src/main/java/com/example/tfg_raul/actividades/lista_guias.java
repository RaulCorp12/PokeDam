package com.example.tfg_raul.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.tfg_raul.R;
import com.example.tfg_raul.utilidades.Preferencias;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class lista_guias extends AppCompatActivity {

    Preferencias preferencia=null;
    private ConstraintLayout guias;

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
        setContentView(R.layout.activity_lista_guias);
        BottomNavigationView menu= findViewById(R.id.menu_guias);
        YouTubePlayerView videoShinys= findViewById(R.id.youtube_player1);
        YouTubePlayerView videoCrianza= findViewById(R.id.youtube_player2);
        YouTubePlayerView videoPesca= findViewById(R.id.youtube_player3);
        guias = findViewById(R.id.layout_guias);

        if(preferencia.cargar_modo_noche()==true){
            guias.setBackground(getResources().getDrawable(R.drawable.fondo_dark));
        }
        else {
            guias.setBackground(getResources().getDrawable(R.drawable.fondo));
        }

        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");

        IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                .controls(1)
                .autoplay(1)
                .fullscreen(0)
                .build();

        videoShinys.setEnableAutomaticInitialization(false);
        videoCrianza.setEnableAutomaticInitialization(false);
        videoPesca.setEnableAutomaticInitialization(false);

        videoShinys.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                String id= "-biN--CCjwk";
                youTubePlayer.cueVideo(id,0);
            }
        }, iFramePlayerOptions);

        videoCrianza.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                String id= "ZYZOm3fs8kk";
                youTubePlayer.cueVideo(id,0);
            }
        }, iFramePlayerOptions);

        videoPesca.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                String id= "IZf9jwARDC8";
                youTubePlayer.cueVideo(id,0);
            }
        }, iFramePlayerOptions);

        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id= item.getItemId();
                if(id==R.id.boton_menu_pokedex){
                    Intent cambio= new Intent(lista_guias.this,lista_pokedex.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_inicio){
                    Intent cambio= new Intent(lista_guias.this,inicio_app.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_caza){
                    Intent cambio= new Intent(lista_guias.this, nueva_caza.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_config){
                    Intent cambio= new Intent(lista_guias.this,lista_configuracion.class);
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