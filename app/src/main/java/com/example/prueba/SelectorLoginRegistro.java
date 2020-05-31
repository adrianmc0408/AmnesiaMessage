package com.example.prueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectorLoginRegistro extends AppCompatActivity {
    //Declaramos los atributos
    private Button btn_login;
    private Button btn_regisgtro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio);
        //enlazamos los atributos y las vistas
        btn_login = findViewById(R.id.BTN_LOGIN);
        btn_regisgtro = findViewById(R.id.btn_registro);
        //Dotamos de funcionalidad al botón login , el cual nos llevará a la activity de login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        //Dotamos de funcionalidad al botón registro , el cual nos llevará a la activity de registro
        btn_regisgtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Registro.class));
                finish();
            }
        });
    }
}
