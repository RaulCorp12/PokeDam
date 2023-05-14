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
/*
    Clase pública Lista_guias

    Muestra una serie de guias relacionadas con el contenido de la aplicación
 */
public class Lista_guias extends AppCompatActivity {
    Preferencias preferencia=null;
    /*
    Método onCreate el cual es el encargado de ejecutar el código de la pantalla del listado de guias
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
        setContentView(R.layout.activity_lista_guias);
        BottomNavigationView menu= findViewById(R.id.menu_guias);
        YouTubePlayerView videoShinys= findViewById(R.id.youtube_player1);
        YouTubePlayerView videoCrianza= findViewById(R.id.youtube_player2);
        YouTubePlayerView videoPesca= findViewById(R.id.youtube_player3);
        ConstraintLayout guias = findViewById(R.id.layout_guias);
        if(preferencia.estado_modo_noche()){
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
        /*
        Método público onNavigationItemSelected, recibe como parámetro un menú de objetos.
        Este método es llamado cuando se pulsa uno de los sub-menus de la barra inferior de la pantalla Lista_guias, llamando
        a su correspondiente pantalla dependiendo del botón que sea pulsado, navegando hasta ella como consecuencia.
        Devuelve un valor de tipo booleano.
        */
        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.boton_menu_pokedex){
                    Intent cambio= new Intent(Lista_guias.this, Lista_pokedex.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_inicio){
                    Intent cambio= new Intent(Lista_guias.this, Inicio_app.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_caza){
                    Intent cambio= new Intent(Lista_guias.this, Lista_cazas.class);
                    cambio.putExtra("id",id_usu);
                    startActivity(cambio);
                }
                else if(id==R.id.boton_menu_config){
                    Intent cambio= new Intent(Lista_guias.this, Configuracion_app.class);
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