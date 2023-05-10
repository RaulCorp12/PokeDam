package com.example.tfg_raul.actividades;
/*
    @Author Raúl Corporales Díaz
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.tfg_raul.R;
import com.example.tfg_raul.utilidades.Preferencias;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
/*
    Clase publica Perfil_usuario.

    Esta representa los datos que contendrá el perfil del usuario una vez acceda a ellos y los
    mostrará por pantalla.
 */
public class Perfil_usuario extends AppCompatActivity {
    Preferencias preferencia=null;
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    /*
    Método onCreate el cual es el encargado de ejecutar el código del perfil del usuario una vez se llama
    a su pantalla desde cualquier parte de la aplicación.
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
        setContentView(R.layout.activity_perfil_usuario);
        TextInputEditText nombre= findViewById(R.id.contenido_nombre);
        TextInputEditText correo= findViewById(R.id.contenido_correo);
        TextView tituloPerfil= findViewById(R.id.titulo_perfil);
        Button volver= findViewById(R.id.salida_pefil);
        View vista= findViewById(R.id.layout_perfil);
        ConstraintLayout perfil = findViewById(R.id.layout_perfil);
        if(preferencia.estado_modo_noche()){
            perfil.setBackground(getResources().getDrawable(R.drawable.fondo_dark));
        }
        else {
            perfil.setBackground(getResources().getDrawable(R.drawable.fondo));
        }

        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");

        DocumentReference documento= firebase.collection("Usuario").document(id_usu);
        documento.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            /*
            Método publico onSuccess, recibe como parámetro un objeto de tipo DocumentSbaoshot.
            Este método es llamado cuando el llamado a los datos de su documento ha tenido éxito, procediendo a ejecutar su contenido.
            No devuelve ningún valor.
             */
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                nombre.setText(documentSnapshot.getString("nombre"));
                correo.setText(documentSnapshot.getString("correo"));
                tituloPerfil.setText("Perfil de "+documentSnapshot.getString("nombre"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            /*
            Método publico onFailure, recibe como parámetro un objeto de tipo Exception.
            Este método es llamado cuando el llamado a los datos de su documento no ha tenido éxito, procediendo a ejecutar su contenido.
            No devuelve ningún valor.
             */
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(vista,"No se pudieron recuperar tus datos", Snackbar.LENGTH_LONG).show();
            }
        });
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cambio= new Intent(getApplicationContext(), Inicio_app.class);
                cambio.putExtra("id",id_usu);
                startActivity(cambio);
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