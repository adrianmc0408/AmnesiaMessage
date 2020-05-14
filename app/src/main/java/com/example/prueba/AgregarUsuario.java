package com.example.prueba;

import android.app.Dialog;
import android.os.Bundle;


import com.example.prueba.Adaptadores.UserAdapterBusqueda;
import com.example.prueba.Objetos.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AgregarUsuario extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapterBusqueda userAdapterBusqueda;
    private RecyclerView.LayoutManager layoutManager;
    private TextView user_follow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario);


        Toolbar toolbar = findViewById(R.id.toolbar_agregarUsuario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Usuarios");

        final ArrayList<Usuario> usuarioList = new ArrayList<>();

        for (int i=0;i<10; i++){

            usuarioList.add(new Usuario("","user","","",""));
            usuarioList.add(new Usuario("","user1","","",""));
            usuarioList.add(new Usuario("","user2","","",""));

        }

        recyclerView = findViewById(R.id.recyclerview_agregar_usuarios);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        userAdapterBusqueda = new UserAdapterBusqueda( usuarioList,AgregarUsuario.this);

        recyclerView.setAdapter(userAdapterBusqueda);




    }

}
