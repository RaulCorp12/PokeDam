package com.example.tfg_raul.actividades;
/*
    @Author Raúl Corporales Díaz
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
public class inicio_sesion extends AppCompatActivity {
    String id_correo="";
    FirebaseAuth autenticacion= FirebaseAuth.getInstance();
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    /*
    Método onCreate el cual es el encargado de ejecutar el código de su interior una vez se llama
    a su pantalla desde cualquier parte de la aplicación.
    No devuelve ningún valor.
     */
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
        String logo="https://i.pinimg.com/originals/48/fc/70/48fc7025c43087805236c8997f82d6d4.gif";
        Glide.with(this).load(logo).into(logo_app);

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
    Método iniciar_Sesion, recibe como parametros el correo y la contraseña introducidos por el usuario.
    Intenta realizar el inicio de sesión en la base de datos con los datos recibidos.
    No devuelve ningún valor.
     */
    public void iniciar_Sesion(String correo, String contraseña){

        View vista= findViewById(R.id.pantalla_inicio_sesion);

        autenticacion.signInWithEmailAndPassword(correo, contraseña).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
           /*
           Método publico onSuccess, recibe como parámetro un objeto de tipo DocumentSbaoshot.
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
                    Método publico onFailure, recibe como parámetro un objeto de tipo Exception.
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
                                Método publico onSuccess, recibe como parámetro un objeto de tipo DocumentSbaoshot.
                                Este método es llamado cuando el llamado a los datos de su documento ha tenido éxito, procediendo a ejecutar su contenido.
                                No devuelve ningún valor.
                                */
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Intent cambio= new Intent(getApplicationContext(), inicio_app.class);
                                    cambio.putExtra("id",id_correo);
                                    startActivity(cambio);
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
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
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
    }
    /*
    Método publico onBackPressed, no recibe ningun valor como parámetro.
    Este ejecuta su contenido una vez el usuario pulse el botón de retroceder de su dispositivo movil.
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