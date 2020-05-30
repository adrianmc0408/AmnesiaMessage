package com.example.prueba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RecuperarContrasena extends AppCompatActivity {
    private TextView tituloActivity;
    private ImageView closeActivity;
    private EditText correo;
    private Button enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);

        correo=findViewById(R.id.email_recuperar_contrasena);
        enviar=findViewById(R.id.btn_recuperar_contrasena);
        tituloActivity=findViewById(R.id.titulo_activity);
        tituloActivity.setText("Restaurar contraseña");

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
                correo.getText();
            }
        });



    }
}
