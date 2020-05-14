package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prueba.Objetos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity implements Button.OnClickListener{
    private EditText email;
    private EditText telefono;
    private EditText nombre_usuario;
    private EditText contraseña;
    private Button registrar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        email=findViewById(R.id.registro_campo_correo);
        telefono=findViewById(R.id.registro_campo_telf);
        nombre_usuario=findViewById(R.id.registro_campo_usuario);
        contraseña=findViewById(R.id.registro_campo_pass1);
        registrar=findViewById(R.id.login_boton_registro);
        registrar.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Usuarios");
    }

    @Override
    public void onClick(View v) {
        String mail=email.getText().toString();
        String contra=contraseña.getText().toString();
        String telef=telefono.getText().toString();
        String nu=nombre_usuario.getText().toString();
        boolean valido=validarCampos(mail,contra,nu,telef);
        if(valido) {
            mAuth.createUserWithEmailAndPassword(mail, contra).addOnCompleteListener(Registro.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(Registro.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                    } else {

                        FirebaseUser user=mAuth.getCurrentUser();
                        Toast.makeText(Registro.this,"Registro completado",Toast.LENGTH_SHORT).show();
                        Usuario usuario=new Usuario(user.getUid(),nombre_usuario.getText().toString(),email.getText().toString(),telefono.getText().toString(),"default");
                        reference.push().setValue(usuario);
                        Intent intent=new Intent(Registro.this,Login.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomePrincipal.class));
        finish();
        return;
    }

    private boolean validarCampos(String c, String p,String nu,String t) {

        boolean camposValidos=true;
        //Comprobamos si el campo "correo" no esta vacio
        if (c.isEmpty()) {
            //Muestra mensaje de error y cambia colores a rojo
            email.setError("Campo obligatorio");
            camposValidos=false;
        }
        else {
            Pattern pattern = Pattern
                    .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher mather = pattern.matcher(c);
            if(mather.find()==false){
                email.setError("Formato email incorrecto");
                camposValidos=false;
            }
            else {
                email.setError(null);
            }
        }
        //Comprobamos si el campo "contraseña" no esta vacio
        if (p.isEmpty()) {
            //Muestra mensaje de error y cambia colores a rojo
            contraseña.setError("Campo obligatorio");
            camposValidos=false;
        } else {
            contraseña.setError(null);
        }
        //Comprobamos si el campo "usuario" no esta vacio
        if (nu.isEmpty()) {
            //Muestra mensaje de error y cambia colores a rojo
            nombre_usuario.setError("Campo obligatorio");
            camposValidos=false;
        } else {
            nombre_usuario.setError(null);
        }
        //Comprobamos si el campo "telefono" no esta vacio y si tiene 9 digitos
        if (t.isEmpty()) {
            //Muestra mensaje de error y cambia colores a rojo
            telefono.setError("Campo obligatorio");
            camposValidos=false;
        } else {
            if(t.trim().length()==9) {
                telefono.setError(null);
            }
            else{
                telefono.setError("Teléfono debe de tener 9 digitos");
                camposValidos=false;
            }
        }



        return camposValidos;
    }
}
