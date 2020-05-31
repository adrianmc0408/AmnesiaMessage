package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarContrasena extends AppCompatActivity {
    private TextView tituloActivity;
    private ImageView closeActivity;
    private EditText correo;
    private Button enviar;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);

        correo=findViewById(R.id.email_recuperar_contrasena);
        enviar=findViewById(R.id.btn_recuperar_contrasena);
        tituloActivity=findViewById(R.id.titulo_activity);
        tituloActivity.setText("Restaurar contraseña");
        auth=FirebaseAuth.getInstance();

        closeActivity=findViewById(R.id.close_activity);
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!correo.getText().toString().isEmpty()){
                    auth.setLanguageCode("es");
                    auth.sendPasswordResetEmail(correo.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Correo enviado correctamente", Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(getApplicationContext(),Login.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"No se pudo enviar enlace de recuperación a ese correo, comprobar que esta bien escrito", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"Campo no puede estar vacio", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
