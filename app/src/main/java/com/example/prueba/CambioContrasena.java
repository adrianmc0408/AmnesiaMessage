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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class CambioContrasena extends AppCompatActivity {
    //Inicializamos los atributos
    private EditText oldPassword;
    private EditText password;
    private EditText repeatPassword;
    private Button confirmar;
    private TextView tituloActivity;
    private ImageView closeActivity;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private AuthCredential credenciales;
    private String email;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Enlazamos los atributos con las vistas
        setContentView(R.layout.activity_cambio_contrasena);
        tituloActivity=findViewById(R.id.titulo_activity);
        tituloActivity.setText("Cambio de contraseña");
        closeActivity=findViewById(R.id.close_activity);
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        oldPassword=findViewById(R.id.miperfil_edit_contrasena__antigua);
        password=findViewById(R.id.miperfil_edit_contrasena);
        repeatPassword=findViewById(R.id.miperfil_edit_repetir_contrasena);
        confirmar=findViewById(R.id.miperfil_edit_btn_contrasena);
        //Intanciamos los objetos de Firebase necesarios para trabajar
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        //Obtenemos el email del Intent
        Intent intent=getIntent();
        email=intent.getStringExtra("email");
        //listener que dota de funcionaliad al botón confirmar
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobaremos que la contraseña antigua es correcta reautentificandonos ya que por seguridad no podemos obtener la contraseña,
                //actual de nuestro usuario. Además para cambiar la contraseña es necesario que el usuario se haya logueado recientemente.
                //Creamos un objeto EmailAuthProvider con el email pasado por el intent y la contraseña introducida por el usuario en el
                //EditText
                credenciales= EmailAuthProvider.getCredential(email,oldPassword.getText().toString());
                //Intentamos reauntenficarnos
                user.reauthenticate(credenciales).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Si la contraseña introducida es correcta
                        if (task.isSuccessful()) {
                            //Comprobamos que los campos no estén vacios
                            if(password.getText().toString().equals("") || repeatPassword.getText().toString().equals("") ){
                                Toast.makeText(CambioContrasena.this, "Campos incompletos", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //Comprobamos si los dos campos de nueva contraseña son iguales
                                if(password.getText().toString().equals(repeatPassword.getText().toString())     ){
                                    //Comprobamos si la contraseña nueva no coincide con la antigua
                                    if(password.getText().toString().equals(oldPassword.getText().toString())){
                                        Toast.makeText(CambioContrasena.this, "La contraseña no puede ser igual a la antigua", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        //Si todas estas condiciones se cumplen modificaremos la contraseña del usuario
                                        user.updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    Toast.makeText(CambioContrasena.this, "Contraseña modificada correctamente", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                                else{
                                    Toast.makeText(CambioContrasena.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                        else{
                            Toast.makeText(CambioContrasena.this, "Antigua contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });





    }



}
