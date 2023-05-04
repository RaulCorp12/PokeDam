package com.example.tfg_raul.actividades;

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

public class perfil_usuario extends AppCompatActivity {
    Preferencias preferencia=null;
    private ConstraintLayout perfil;
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
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
        setContentView(R.layout.activity_perfil_usuario);
        TextInputEditText nombre= findViewById(R.id.contenido_nombre);
        TextInputEditText correo= findViewById(R.id.contenido_correo);
        TextView tituloPerfil= findViewById(R.id.titulo_perfil);
        Button volver= findViewById(R.id.salida_pefil);
        View vista= findViewById(R.id.layout_perfil);
        perfil= findViewById(R.id.layout_perfil);
        if(preferencia.cargar_modo_noche()==true){
            perfil.setBackground(getResources().getDrawable(R.drawable.fondo_dark));
        }
        else {
            perfil.setBackground(getResources().getDrawable(R.drawable.fondo));
        }

        Bundle datos= getIntent().getExtras();
        String id_usu= datos.getString("id");

        DocumentReference documento= firebase.collection("Usuario").document(id_usu);
        documento.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                nombre.setText(documentSnapshot.getString("nombre"));
                correo.setText(documentSnapshot.getString("correo"));
                tituloPerfil.setText("Perfil de "+documentSnapshot.getString("nombre"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(vista,"No se pudieron recuperar tus datos", Snackbar.LENGTH_LONG).show();
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cambio= new Intent(getApplicationContext(), inicio_app.class);
                cambio.putExtra("id",id_usu);
                startActivity(cambio);
            }
        });
    }
    @Override
    public void onBackPressed(){

    }
}