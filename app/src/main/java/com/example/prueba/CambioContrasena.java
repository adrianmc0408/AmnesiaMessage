package com.example.prueba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CambioContrasena extends AppCompatActivity {

    private EditText oldPassword;
    private EditText password;
    private EditText repeatPassword;
    private Button confirmar;
    private TextView tituloActivity;
    private ImageView closeActivity;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals("") || repeatPassword.getText().toString().equals("") ){
                    Toast.makeText(CambioContrasena.this, "Campos incompletos", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(password.getText().toString().equals(repeatPassword.getText().toString())     ){

                    }
                    else{
                        Toast.makeText(CambioContrasena.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });





    }



}
