package com.example.tfg_raul.actividades;
/*
    @Author Raúl Corporales Díaz
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/*
    Clase recuperar_cuenta

    Contiene la lógica para la recuperación de datos del usuario validando sus datos
 */
public class Recuperar_cuenta extends AppCompatActivity {
    String nombre,correo,contraseña;
    String id_correo="";
    FirebaseFirestore firebase= FirebaseFirestore.getInstance();
    Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    /*
    Método onCreate el cual es el encargado de ejecutar el código de su interior una vez se llama
    a su pantalla desde cualquier parte de la aplicación.
    No devuelve ningún valor.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_cuenta);
        TextInputEditText correo= findViewById(R.id.valor_correo_eletrónico_recup);
        Button recuperar= findViewById(R.id.boton_recuperar);
        Button volver= findViewById(R.id.boton_volver_inicio_sesion_recup);
        View vista= findViewById(R.id.recuperar_cuenta_layout);
        String logo="https://i.pinimg.com/originals/48/fc/70/48fc7025c43087805236c8997f82d6d4.gif";
        ImageView logo_app= findViewById(R.id.logo_recuperar);

        Glide.with(this).load(logo).into(logo_app);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio= new Intent(Recuperar_cuenta.this, inicio_sesion.class);
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
                                    Snackbar.make(vista, "Se te enviará un correo electrónico con tus datos, comprueba tu bandeja de entrada", Snackbar.LENGTH_INDEFINITE).setAction("Aceptar", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();
                                    nombre= documentSnapshot.getString("nombre");
                                    correo= documentSnapshot.getString("correo");
                                    contraseña= documentSnapshot.getString("contraseña");
                                    envio_de_datos();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                /*
                                Método publico onFailure, recibe como parámetro un objeto de tipo Exception.
                                Este método es llamado cuando el llamado a los datos de su documento no ha tenido éxito, procediendo a ejecutar su contenido.
                                No devuelve ningún valor.
                                */
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(vista,"No se pudieron recuperar los datos", Snackbar.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    /*
                    Método publico onFailure, recibe como parámetro un objeto de tipo Exception.
                    Este método es llamado cuando el llamado a los datos de su documento no ha tenido éxito, procediendo a ejecutar su contenido.
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
    Cuando se ha confirmado el correo electrónico del usuario se llama a este método, el cual recoge los datos del usuario
    y se los envia por correo por protocolo smtp.
    No devuelve ningún valor.
     */
    public void envio_de_datos(){
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
                mensaje.setContent("Solicitaste tus datos de inicio de sesión<br>Correo electrónico: "+correo+"<br>Contraseña: "+
                        contraseña+"<br>Si no enviaste la petición simplemente ignora este correo","text/html; charset=utf-8");
                Transport.send(mensaje);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}