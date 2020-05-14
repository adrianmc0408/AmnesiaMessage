package com.example.prueba;

import android.os.Bundle;

import com.example.prueba.Adaptadores.SolicitudAmistadAdapter;
import com.example.prueba.Adaptadores.UserAdapterBusqueda;
import com.example.prueba.Objetos.Usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SolicitudAmistad extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SolicitudAmistadAdapter solicitudAmistadAdapter;
    private ArrayList<Usuario> listaPeticiones;
    private RecyclerView.LayoutManager layoutManager;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_amistad);
        Toolbar toolbar = findViewById(R.id.toolbar_solicitud_amistad);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Solicitudes de amistad");

         ArrayList<Usuario> listaPeticiones = new ArrayList<>();

        for (int i=0;i<10; i++){

            listaPeticiones.add(new Usuario("","user","","",""));
            listaPeticiones.add(new Usuario("","user1","","",""));
            listaPeticiones.add(new Usuario("","user2","","",""));

        }

        recyclerView = findViewById(R.id.recyclerview_solicitud_amistad);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        solicitudAmistadAdapter = new SolicitudAmistadAdapter( listaPeticiones );
        recyclerView.setAdapter(solicitudAmistadAdapter);


    }

}
