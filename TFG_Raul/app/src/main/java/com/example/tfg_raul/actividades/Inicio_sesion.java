package com.example.tfg_raul.actividades;
/*
    @Author Raúl Corporales Díaz
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.tfg_raul.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
/*
    Clase inicio_sesion

    Representa la lógica del inicio de sesión en la aplicación
 */
public class Inicio_sesion extends AppCompatActivity {
    String id_correo="";
    FirebaseAuth autenticacion= FirebaseAuth.getInstance();
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    /*
    Método onCreate el cual es el encargado de ejecutar el código de la pantalla de iniciar sesión
    una vez se llama a su pantalla desde cualquier parte de la aplicación.
    No devuelve ningún valor.
     */
    @SuppressLint("ResourceType")
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
        ImageView logo_app= findViewById(R.id.logo_app);
        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/pokedam-dba0a.appspot.com/o/Pokemon%2Fgifs%2Fgif_app_1.gif?alt=media&token=a30ceb1b-c13f-4194-8575-0e7150e618a2").into(logo_app);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio= new Intent(Inicio_sesion.this, Crear_cuenta.class);
                startActivity(cambio);
            }
        });
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(correo.getText().toString().isEmpty()){
                    Snackbar.make(vista,"Debes introducir un correo electrónico", Snackbar.LENGTH_LONG).show();
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
                Intent cambio= new Intent(getApplicationContext(), Recuperar_cuenta.class);
                startActivity(cambio);
            }
        });
    }
    /*
    Método público iniciar_Sesion, recibe como parametros el correo y la contraseña introducidos por el usuario.
    Intenta realizar el inicio de sesión en la base de datos con los datos recibidos.
    No devuelve ningún valor.
     */
    public void iniciar_Sesion(String correo, String contraseña){

        View vista= findViewById(R.id.pantalla_inicio_sesion);
        autenticacion.signInWithEmailAndPassword(correo, contraseña).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
           /*
           Método público onSuccess, recibe como parámetro un objeto de tipo DocumentSnapshot.
           Este método es llamado cuando el llamado a los datos de su documento ha tenido éxito, procediendo a ejecutar su contenido.
           No devuelve ningún valor.
           */
            @Override
            public void onSuccess(AuthResult authResult) {
                recuperar_id(correo);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    /*
                    Método público onFailure, recibe como parámetro un objeto de tipo Exception.
                    Este método es llamado cuando el llamado a los datos de su documento no ha tenido éxito, procediendo a ejecutar su contenido.
                    No devuelve ningún valor.
                    */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(vista, "Los datos de inicio no son correctos", Snackbar.LENGTH_LONG).show();
                    }
                });
    }
    /*
    Método recuperar_id, recibe como parámetro el correo electrónico del usuario.
    Este método intenta recuperar de la base de datos la id del documento que contenga el correo electrónico del usuario pasado como parámetro.
    No devuelve ningun valor.
     */
    public void recuperar_id(String correo_recup){
        View vista= findViewById(R.id.recuperar_cuenta_layout);
        CollectionReference collectionReference = firebase.collection("Usuario");
        Query sentencia= collectionReference.whereEqualTo("correo", correo_recup);
        sentencia.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot id: task.getResult()){
                            id_correo= id.getId();
                            DocumentReference documento= firebase.collection("Usuario").document(id_correo);
                            documento.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                /*
                                Método público onSuccess, recibe como parámetro un objeto de tipo DocumentSbaoshot.
                                Este método es llamado cuando el llamado a los datos de su documento ha tenido éxito, procediendo a ejecutar su contenido.
                                No devuelve ningún valor.
                                */
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Intent cambio= new Intent(getApplicationContext(), Inicio_app.class);
                                    cambio.putExtra("id",id_correo);
                                    startActivity(cambio);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                /*
                                Método público onFailure, recibe como parámetro un objeto de tipo Exception.
                                Este método es llamado cuando el llamado a los datos de su documento no ha tenido éxito, procediendo a ejecutar su contenido.
                                No devuelve ningún valor.
                                */
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(vista,"No se pudieron recuperar tus datos", Snackbar.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /*
                    Método público onFailure, recibe como parámetro un objeto de tipo Exception.
                    Este método es llamado cuando el llamado a los datos de su documento no ha tenido éxito, procediendo a ejecutar su contenido.
                    No devuelve ningún valor.
                    */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(vista,"No se pudieron recuperar tus datos", Snackbar.LENGTH_LONG).show();
                    }
                });
    }
    /*
    Método público onBackPressed, no recibe ningun valor como parámetro.
    Este ejecuta su contenido una vez el usuario pulse el botón de retroceder de su dispositivo movil, mostrando
    un menú para salir o no de la aplicación.
    No devuelve ningún valor.
     */
    @Override
    public void onBackPressed(){
        new MaterialAlertDialogBuilder(this)
                .setMessage("¿Quieres salir de la aplicación?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}