package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {

    private EditText correo;
    private TextView contrasna_olvidada;
    private  EditText contraseña;
    private Button btn_entrar;
    private TextInputLayout IL_mail, IL_password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        contrasna_olvidada=findViewById(R.id.contrasena_olvidada);
        correo = findViewById(R.id.login_campo_correo);
        contraseña = findViewById(R.id.login_campo_password);
        btn_entrar = findViewById(R.id.login_boton_entrar);
        IL_mail = findViewById(R.id.IL_email);
        IL_password = findViewById(R.id.IL_password);
        mAuth=FirebaseAuth.getInstance();

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = correo.getText().toString().trim();
                String password = contraseña.getText().toString().trim();
                //SOLO COMPRUEBO QUE LOS CAMPOS ESTEN COMPLETOS
                if(validarCampos(email,password)==true){
                    String mail=correo.getText().toString();
                    String contra=contraseña.getText().toString();
                    mAuth.signInWithEmailAndPassword(mail,contra).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Login.this,"Login correcto",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HomePrincipal.class));
                                finish();
                            }
                            else{
                                Toast.makeText(Login.this,"Login inrrecto",Toast.LENGTH_SHORT).show();
                                IL_mail.setError("Datos de Login incorrectos");
                                IL_password.setError("Datos de Login incorrectos");
                            }

                        }
                    });

                }

            }
        });

                    contrasna_olvidada.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), RecuperarContrasena.class));

                        }
                    });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SelectorLoginRegistro.class));
        finish();
        return;
    }
    private boolean validarCampos(String email, String password) {

        boolean camposValidos=true;
        //Comprobamos si el campo "correo" no esta vacio
        if (email.isEmpty()) {
            //Muestra mensaje de error y cambia colores a rojo
            IL_mail.setError("Campo obligatorio");
            camposValidos=false;
        } else {
            IL_mail.setError(null);
        }
        //Comprobamos si el campo "contraseña" no esta vacio
        if (password.isEmpty()) {
            //Muestra mensaje de error y cambia colores a rojo
            IL_password.setError("Campo obligatorio");
            camposValidos=false;
        } else {
            IL_password.setError(null);
        }


        return camposValidos;
    }


}
