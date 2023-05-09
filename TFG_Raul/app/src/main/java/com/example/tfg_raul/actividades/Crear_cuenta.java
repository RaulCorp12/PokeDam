package com.example.tfg_raul.actividades;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
    Clase pública Crear_cuenta

    Contiene los campos y métodos para crear y validar una nueva cuenta para el uso de la aplicación.
 */
public class Crear_cuenta extends AppCompatActivity {
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    FirebaseAuth autenticacion= FirebaseAuth.getInstance();
    String id_correo="";
    boolean existe;
    Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    /*
    Método protected onCreate el cual se encargada de ejecutar el código de la pantalla llamada Crear_cuenta
    una vez se llama a su pantalla desde el listado de cazas seleccionando una caza.
    No devuelve ningún valor.
    */
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);
        View vista= findViewById(R.id.pantalla_crear_cuenta);
        Button volver= findViewById(R.id.boton_volver_inicio_sesion);
        Button crear= findViewById(R.id.boton_crear);
        TextInputEditText nombre= findViewById(R.id.valor_crear_usuario);
        TextInputEditText correo= findViewById(R.id.valor_crear_correo);
        TextInputEditText contraseña= findViewById(R.id.valor_crear_contraseña);
        TextInputEditText repeticion= findViewById(R.id.valor_repetir_contraseña);
        ImageView logo_app= findViewById(R.id.logo_crear);
        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/pokedam-dba0a.appspot.com/o/Pokemon%2Fgifs%2Fgif_app_1.gif?alt=media&token=a30ceb1b-c13f-4194-8575-0e7150e618a2").into(logo_app);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio= new Intent(Crear_cuenta.this, Inicio_sesion.class);
                startActivity(cambio);
            }
        });

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombre.getText().toString().isEmpty() || correo.getText().toString().isEmpty() || contraseña.getText().toString().isEmpty() || repeticion.getText().toString().isEmpty()){
                    Snackbar.make(vista,"Debes rellenar todos los campos para crear una cuenta",Snackbar.LENGTH_LONG).show();
                }
                else {
                    Matcher validacion= pattern.matcher(correo.getText().toString());
                    if(!validacion.find()){
                        Snackbar.make(vista,"Debes insertar un correo electrónico válido",Snackbar.LENGTH_LONG).show();
                    }
                    else{
                        if(!contraseña.getText().toString().equals(repeticion.getText().toString())){
                            Snackbar.make(vista,"La contraseña debe coincidir",Snackbar.LENGTH_LONG).show();
                        }
                        else {
                            if (!contraseña.getText().toString().contains(".")) {
                                Snackbar.make(vista, "La contraseña debe contener letras en mayuscula y en minuscula, numeros y caracteres especiales", Snackbar.LENGTH_LONG).show();
                            } else {
                                if(contraseña.getText().toString().length()<8){
                                    Snackbar.make(vista, "La contraseña debe de tener 8 o más caracteres y numeros en total", Snackbar.LENGTH_LONG).show();
                                }
                                else{
                                    recuperarId(correo.getText().toString());
                                    if(!existe){
                                        guardarUsuario(nombre.getText().toString(), correo.getText().toString(), contraseña.getText().toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    /*
    Método public guardarUsuario. Recibe como parámetros los datos para crear la cuenta del usuario.
    Utilizando los datos introducidos por el usuario y validados por la app, se crea una nueva cuenta
    en la base de datos.
    No devuelve ningún valor.
    */
    public void guardarUsuario(String nombre, String correo, String contraseña){
        View vista= findViewById(R.id.pantalla_crear_cuenta);
        Map<String, Object> usuario= new HashMap<>();
        usuario.put("nombre",nombre);
        usuario.put("correo",correo);
        usuario.put("contraseña",contraseña);
        autenticacion.createUserWithEmailAndPassword(correo, contraseña);
        firebase.collection("Usuario")
                .add(usuario)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent cambio= new Intent(Crear_cuenta.this, Inicio_sesion.class);
                        startActivity(cambio);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(vista,"No se pudo crear la cuenta",Snackbar.LENGTH_LONG).show();
                    }
                });
    }
    /*
    Método recuperar_id. Recibe como parámetro el correo electrónico del usuario.
    Este método intenta recuperar de la base de datos la id del documento que contenga el correo electrónico del usuario pasado como parámetro
    para comprobar si la cuenta ya existe en la base de datos.
    No devuelve ningun valor.
    */
    public void recuperarId(String correo_recup){
        View vista= findViewById(R.id.pantalla_crear_cuenta);
        CollectionReference collectionReference = firebase.collection("Usuario");
        Query sentencia= collectionReference.whereEqualTo("correo", correo_recup.trim());
        sentencia.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot id: task.getResult()){
                    id_correo= id.getId();
                    DocumentReference documento= firebase.collection("Usuario").document(id_correo);
                    documento.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Snackbar.make(vista, "Este correo ya tiene una cuenta enlazada", Snackbar.LENGTH_LONG).show();
                            existe= true;
                        }
                   }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            existe=false;
                        }
                    });
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        existe= false;
                    }
                });
    }
}