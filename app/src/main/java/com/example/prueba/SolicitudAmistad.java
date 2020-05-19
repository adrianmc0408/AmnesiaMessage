package com.example.prueba;

import android.os.Bundle;
import android.util.Log;

import com.example.prueba.Adaptadores.SolicitudAmistadAdapter;
import com.example.prueba.Adaptadores.UserAdapterBusqueda;
import com.example.prueba.Objetos.Solicitud;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SolicitudAmistad extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SolicitudAmistadAdapter solicitudAmistadAdapter;
    private ArrayList<Usuario2> listaPeticiones;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_amistad);
        Toolbar toolbar = findViewById(R.id.toolbar_solicitud_amistad);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Solicitudes de amistad");
        mAuth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Solicitudes");

         listaPeticiones = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview_solicitud_amistad);
        recyclerView.setHasFixedSize(true);
        leerSolicitudes();



    }
    public void leerSolicitudes(){

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPeticiones.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Solicitud solicitud= data.getValue(Solicitud.class);
                    FirebaseUser user=mAuth.getCurrentUser();
                    if(solicitud!=null ){
                        if(solicitud.getId_destino().equals(user.getUid())) {
                            Usuario2 usuario=new Usuario2(data.getKey(),solicitud.getId(),solicitud.getNombre_usuario(),solicitud.getEmail(),solicitud.getTelefono()
                            ,solicitud.getUrl_imagen());
                            listaPeticiones.add(usuario);
                        }

                    }
                }

                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                solicitudAmistadAdapter = new SolicitudAmistadAdapter( listaPeticiones );
                recyclerView.setAdapter(solicitudAmistadAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
