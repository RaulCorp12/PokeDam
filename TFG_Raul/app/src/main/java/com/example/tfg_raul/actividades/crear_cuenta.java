package com.example.tfg_raul.actividades;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tfg_raul.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class crear_cuenta extends AppCompatActivity {
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    FirebaseAuth autenticacion= FirebaseAuth.getInstance();
    Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
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

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio= new Intent(crear_cuenta.this, inicio_sesion.class);
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
                    if(validacion.find()==false){
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
                                saveDataToDatabase(nombre.getText().toString(), correo.getText().toString(), contraseña.getText().toString());
                            }
                        }
                    }
                }
            }
        });
    }
    public void saveDataToDatabase(String nombre, String correo, String contraseña){

        View vista= findViewById(R.id.pantalla_crear_cuenta);

        Map<String, Object> usuario= new HashMap<>();
        usuario.put("nombre",nombre.toString());
        usuario.put("correo",correo.toString());
        usuario.put("contraseña",contraseña.toString());

        autenticacion.createUserWithEmailAndPassword(correo.toString(), contraseña.toString());

        firebase.collection("Usuario")
                .add(usuario)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent cambio= new Intent(crear_cuenta.this, inicio_sesion.class);
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
}