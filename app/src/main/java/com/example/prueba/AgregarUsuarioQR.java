package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prueba.Objetos.Amistad;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgregarUsuarioQR extends AppCompatActivity {
    //Declaramos los atributos
    private TextView tituloActivity;
    private ImageView closeActivity;

    private Button escaner;
    private Dialog dialog_confirm;

    private FirebaseDatabase base;
    private DatabaseReference referencia;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private Usuario usuario;
    private String resultado2=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Enlazamos las vistas con los atributos
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_q_r);

        dialog_confirm = new Dialog(this);
        dialog_confirm.setContentView(R.layout.popup_confirmacion_qr);
        dialog_confirm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Intanciamos los objetos de Firebase necesarios para trabajar
        base=FirebaseDatabase.getInstance();
        referencia=base.getReference("Usuarios");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        escaner = findViewById(R.id.btn_prueba);
        //Iniciamos el scaner al presionar el botón
        escaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(AgregarUsuarioQR.this).initiateScan();
            }
        });



        tituloActivity=findViewById(R.id.titulo_activity);
        tituloActivity.setText("Agregar por código QR");

        closeActivity=findViewById(R.id.close_activity);
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
//Esperamos la respuesta del escaner de QR
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Almacenamos la respuesta en un objeto IntentResult
        final IntentResult resultado=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(resultado.getContents()!=null){
            //Obtenemos la cadena leida
            resultado2=resultado.getContents();
            //Buscamos el usuario que hemos leido
            referencia.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot datos:dataSnapshot.getChildren()){
                        Usuario usuario2=datos.getValue(Usuario.class);
                        if(usuario2.getId().equals(resultado2)){
                            usuario=datos.getValue(Usuario.class);
                        }
                    }
                    //Si el código QR contiene a un usuario de nuestra BD
                    if(usuario!=null) {
                        //Enlazamos los atributos de dialog con sus vistas
                        CircleImageView image = (CircleImageView) dialog_confirm.findViewById(R.id.dialog_qr_img_profile);
                        TextView username = (TextView) dialog_confirm.findViewById(R.id.dialog_qr_username);
                        Button confirmar = (Button) dialog_confirm.findViewById(R.id.dialog_qr_btn_confirmar);
                        image.setImageResource(R.drawable.profile);
                        username.setText(usuario.getNombre_usuario());
                        //Dotamos de funcionalidad al botón confirmar del dialog , el cual agregará al usuarios leido mediante,
                        //el código QR directamente a nuestros amigos
                        confirmar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference referencia2 = base.getReference("Amigos");
                                referencia2.push().setValue(new Amistad(usuario.getId(), user.getUid()));
                                Toast.makeText(AgregarUsuarioQR.this,usuario.getNombre_usuario()+" agregado a amigos",Toast.LENGTH_SHORT).show();
                                dialog_confirm.cancel();

                            }
                        });
                        //Mostramos el dialog
                        dialog_confirm.show();
                    }
                    //Sino mostramos mensaje de QR no válido
                    else{
                        Toast.makeText(AgregarUsuarioQR.this,"QR incorrecto",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }
}