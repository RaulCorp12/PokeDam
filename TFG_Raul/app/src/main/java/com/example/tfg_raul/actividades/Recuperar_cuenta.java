package com.example.tfg_raul.actividades;
/*
    @Author Raúl Corporales Díaz
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.tfg_raul.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
    Clase recuperar_cuenta

    Contiene la lógica para la recuperación de datos del usuario validando sus datos
 */
public class Recuperar_cuenta extends AppCompatActivity {
    String id_correo="";
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    FirebaseAuth autenticacion= FirebaseAuth.getInstance();
    Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    /*
    Método onCreate el cual es el encargado de ejecutar el código de la pantalla de recuperar
    cuentas una vez se llama a su pantalla desde cualquier parte de la aplicación.
    No devuelve ningún valor.
     */
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_cuenta);
        TextInputEditText correo= findViewById(R.id.valor_correo_eletrónico_recup);
        Button recuperar= findViewById(R.id.boton_recuperar);
        Button volver= findViewById(R.id.boton_volver_inicio_sesion_recup);
        View vista= findViewById(R.id.recuperar_cuenta_layout);
        ImageView logo_app= findViewById(R.id.logo_recuperar);
        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/pokedam-dba0a.appspot.com/o/Pokemon%2Fgifs%2Fgif_app_1.gif?alt=media&token=a30ceb1b-c13f-4194-8575-0e7150e618a2").into(logo_app);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio= new Intent(Recuperar_cuenta.this, Inicio_sesion.class);
                startActivity(cambio);
            }
        });

        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correo.getText().toString().isEmpty()){
                    Snackbar.make(vista,"Debes indicar un correo electrónico", Snackbar.LENGTH_LONG).show();
                }
                else{
                    Matcher validacion= pattern.matcher(correo.getText().toString());
                    if(!validacion.find()){
                        Snackbar.make(vista,"Debes indicar un correo electrónico válido", Snackbar.LENGTH_LONG).show();
                    }
                    else{
                        recuperar_id(correo.getText().toString());
                    }
                }
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
        Query sentencia= collectionReference.whereEqualTo("correo", correo_recup.trim());
        sentencia.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /*
                    Método publico onComplete, recibe como parámetro una serie de operaciones.
                    Este método es llamado cuando el usuario intenta recuperar sus datos y se llegan a recuperar, pasando a validar
                    si los datos son los correctos.
                    No devuelve ningún valor.
                    */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot id: task.getResult()){
                            id_correo= id.getId();
                            if(id_correo.isEmpty()){
                                Snackbar.make(vista,"No se pudieron recuperar tus datos en este momento", Snackbar.LENGTH_LONG).show();
                            }
                            else{
                                envio_de_datos(correo_recup);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    /*
                    Método publico onFailure, recibe como parámetro un objeto de tipo Exception.
                    Este método es llamado cuando el llamado a los datos de su documento no ha tenido éxito, procediendo a mostrar un mensaje de
                    error por pantalla al usuario.
                    No devuelve ningún valor.
                    */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(vista,"No se pudieron recuperar los datos", Snackbar.LENGTH_LONG).show();
                    }
                });
    }
    /*
    Método envio_de_datos, no recibe ningún parámetro.
    Cuando se ha confirmado el correo electrónico del usuario se llama a este método, el cual envia al usuario un correo
    electrónico con un enlace para que resetee sus datos de inicio de sesion.
    No devuelve ningún valor.
     */
    public void envio_de_datos(String correo){
        View vista= findViewById(R.id.recuperar_cuenta_layout);
        autenticacion.setLanguageCode("es");
        autenticacion.sendPasswordResetEmail(correo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(vista,"Comprueba tu bandeja de entrada", Snackbar.LENGTH_INDEFINITE).setAction("ACEPTAR", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {}
                        }).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(vista,"No se pudo enviar el correo", Snackbar.LENGTH_LONG).show();
                    }
                });
    }
}