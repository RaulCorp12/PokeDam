package com.example.tfg_raul.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.example.tfg_raul.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class recuperar_cuenta extends AppCompatActivity {
    String nombre,correo,contraseña;
    String id_correo="";
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_cuenta);
        TextInputEditText correo= findViewById(R.id.valor_correo_eletrónico_recup);
        Button recuperar= findViewById(R.id.boton_recuperar);
        Button volver= findViewById(R.id.boton_volver_inicio_sesion_recup);
        View vista= findViewById(R.id.recuperar_cuenta_layout);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio= new Intent(recuperar_cuenta.this, inicio_sesion.class);
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
                    if(validacion.find()==false){
                        Snackbar.make(vista,"Debes indicar un correo electrónico válido", Snackbar.LENGTH_LONG).show();
                    }
                    else{
                        recuperarId(correo.getText().toString());
                    }
                }
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
                                    nombre= documentSnapshot.getString("nombre");
                                    correo= documentSnapshot.getString("correo");
                                    contraseña= documentSnapshot.getString("contraseña");
                                    envioDeDatos();
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
    public void envioDeDatos(){
        String remitente="raul.cordia@sanviatorvalladolid.com";
        String contraseña_remitente= "RxWh7878";

        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Properties propiedades= new Properties();
        propiedades.put("mail.smtp.host","smtp.googlemail.com");
        propiedades.put("mail.smtp.socketFactory.port","465");
        propiedades.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        propiedades.put("mail.smtp.auth","true");
        propiedades.put("mail.smtp.port","465");

        try {
            Session sesion = Session.getDefaultInstance(propiedades, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(remitente, contraseña_remitente);
                }
            });

            if (sesion != null) {
                Message mensaje = new MimeMessage(sesion);
                mensaje.setFrom(new InternetAddress(remitente));
                mensaje.setSubject("Recuperación de datos para "+correo);
                mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(correo.trim()));
                mensaje.setContent("Solicitaste tus datos de inicio de sesión\nCorreo electrónico: "+correo+"\nContraseña: "+
                        contraseña+"\nSi no enviaste la petición simplemente ignora este correo","text/html; charset=utf-8");
                Transport.send(mensaje);
            }
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}