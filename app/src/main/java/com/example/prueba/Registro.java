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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity implements Button.OnClickListener{
    //Declaramos los atributos
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
        //Enlazamos los atributos con las vistas
        setContentView(R.layout.activity_registro);
        email=findViewById(R.id.registro_campo_correo);
        telefono=findViewById(R.id.registro_campo_telf);
        nombre_usuario=findViewById(R.id.registro_campo_usuario);
        contraseña=findViewById(R.id.registro_campo_pass1);
        registrar=findViewById(R.id.login_boton_registro);
        registrar.setOnClickListener(this);
        //Obtenemos los elementos necesarios de Firebase
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Usuarios");
    }

    @Override
    //Dotamos de funcionalidad al botón
    public void onClick(View v) {
        final String mail=email.getText().toString();
        final String contra=contraseña.getText().toString();
        final String telef=telefono.getText().toString();
        final String nu=nombre_usuario.getText().toString();
        //Validamos los campos (que no estén vacios y que el formato sea el correcto)
        boolean valido=validarCampos(mail,contra,nu,telef);
        if(valido) {
            //ahora vamos a comprobar si los datos introducidos no se encuentran ya en la BD
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Usuario> usuarios=new ArrayList<>();
                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        usuarios.add(data.getValue(Usuario.class));
                    }
                    Boolean valido=true;
                    //Comprobamos los datos introducidos con todos los usuarios de la BD
                    for(Usuario user:usuarios){
                        if(nu.equals(user.getNombre_usuario())){
                            nombre_usuario.setError("Ya presente en la base de datos");
                            valido=false;
                        }
                        if(mail.equals(user.getEmail())){
                            email.setError("Ya presente en la base de datos");
                            valido=false;
                        }
                        if(telef.equals(user.getTelefono())){
                            telefono.setError("Ya presente en la base de datos");
                            valido=false;
                        }
                    }
                    //Si todo está correcto registramos el usuario (solo el email y contraseña ya que estamos registrandolo en el apartado
                    // de autentificación)
                    if(valido) {
                        mAuth.createUserWithEmailAndPassword(mail, contra).addOnCompleteListener(Registro.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Registro.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Si es correcto guardamos los demás datos en la Bd , junto a el ID generado en el registro para poder
                                    //identificar al usuario más facilmente
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(Registro.this, "Registro completado", Toast.LENGTH_SHORT).show();
                                    Usuario usuario = new Usuario(user.getUid(), nombre_usuario.getText().toString(), email.getText().toString(), telefono.getText().toString(), "default");
                                    reference.push().setValue(usuario);
                                    //Iniciamos la activity de login y cerramos la actual
                                    Intent intent = new Intent(Registro.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
    @Override
    //si pulsamos el botón atrás se cerrará la activity
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SelectorLoginRegistro.class));
        finish();
        return;
    }
    //Método usado para validar el formato de los datos introducidos
    private boolean validarCampos(String c, String p,String nu,String t) {

        boolean camposValidos=true;
        //Comprobamos si el campo "correo" no esta vacio
        if (c.isEmpty()) {
            //Muestra mensaje de error y cambia colores a rojo
            email.setError("Campo obligatorio");
            camposValidos=false;
        }
        else {
            //Comprobamos mediante un pattern si el correo sigue el formato correcto es decir:
            //lo que sea @ lo que sea . com/es..
            Pattern patron = Pattern
                    .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher comparador = patron.matcher(c);
            if(comparador.find()==false){
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
            //Comprobamos si el teléfono sigue el formato normal (9 dígitos)
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