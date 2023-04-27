package com.example.tfg_raul.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tfg_raul.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class inicio_sesion extends AppCompatActivity {
    String id_correo="";
    FirebaseAuth autenticacion= FirebaseAuth.getInstance();
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        View vista= findViewById(R.id.pantalla_inicio_sesion);
        TextInputEditText correo= findViewById(R.id.valor_correo);
        TextInputEditText contraseña= findViewById(R.id.valor_contraseña);
        Button iniciar= findViewById(R.id.boton_iniciar_sesion);
        Button crear= findViewById(R.id.boton_crear_cuenta);
        Button recordar= findViewById(R.id.boton_recordar_cuenta);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio= new Intent(inicio_sesion.this,crear_cuenta.class);
                startActivity(cambio);
            }
        });

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(correo.getText().toString().isEmpty()){
                    Snackbar.make(vista,"Debes introducir un nombre de usuario", Snackbar.LENGTH_LONG).show();
                }
                else if(contraseña.getText().toString().isEmpty()){
                    Snackbar.make(vista,"Debes introducir una contraseña", Snackbar.LENGTH_LONG).show();
                }
                else{
                    iniciar_Sesion(correo.getText().toString(),contraseña.getText().toString());
                }
            }
        });
        recordar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio= new Intent(getApplicationContext(), recuperar_cuenta.class);
                startActivity(cambio);
            }
        });
    }

    public void iniciar_Sesion(String correo, String contraseña){

        View vista= findViewById(R.id.pantalla_inicio_sesion);

        autenticacion.signInWithEmailAndPassword(correo, contraseña).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                recuperarId(correo);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(vista, "Los datos de inicio no son correctos", Snackbar.LENGTH_LONG).show();
                    }
                });
    }
    public void recuperarId(String correo_recup){
        View vista= findViewById(R.id.recuperar_cuenta_layout);
        CollectionReference collectionReference = firebase.collection("Usuario");
        Query sentencia= collectionReference.whereEqualTo("correo", correo_recup.toString());
        sentencia.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot id: task.getResult()){
                            id_correo= id.getId();
                            DocumentReference documento= firebase.collection("Usuario").document(id_correo);
                            documento.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Intent cambio= new Intent(getApplicationContext(), inicio_app.class);
                                    cambio.putExtra("id",id_correo);
                                    startActivity(cambio);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(vista,"No se pudieron recuperar tus datos", Snackbar.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(vista,"No se pudieron recuperar tus datos", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }
}

