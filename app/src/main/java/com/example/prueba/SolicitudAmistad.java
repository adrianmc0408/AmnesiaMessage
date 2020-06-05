package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prueba.Adaptadores.SolicitudAmistadAdapter;
import com.example.prueba.Adaptadores.UserAdapterBusqueda;
import com.example.prueba.Objetos.ServicioNotificaciones;
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
    //Declaramos los atributos
    private RecyclerView recyclerView;
    private SolicitudAmistadAdapter solicitudAmistadAdapter;
    private ArrayList<Usuario2> listaPeticiones;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private TextView sinSolicitudes;
    private FirebaseAuth mAuth;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enlazamos los atributos con las vistas
        setContentView(R.layout.activity_solicitud_amistad);
        Toolbar toolbar = findViewById(R.id.toolbar_solicitud_amistad);
        //Definimos los parámetros de la toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Solicitudes de amistad");
        //Obtenemos los elementos de Firebase necesarios
        mAuth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Solicitudes");

        sinSolicitudes = findViewById(R.id.solicitudAmistad_sin);
         listaPeticiones = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview_solicitud_amistad);
        recyclerView.setHasFixedSize(true);
        //Llamamos al método para leer solicitudes
        leerSolicitudes();



    }
    //Método encargado de mostrar las solicitudes recibidas por el usuario actual accediendo a la BD y rellenando el adaptar de recycler
    public void leerSolicitudes(){
        //Añadimos el listener con el fin de detectar cambios en la bd
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPeticiones.clear();
                //Recorremos el objeto DataSnapshot , guardando los datos en objetos Solicitud
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Solicitud solicitud= data.getValue(Solicitud.class);
                    //Obtenemos el usuario actual
                    FirebaseUser user=mAuth.getCurrentUser();
                    if(solicitud!=null ){
                        //Comprobamos si la solicitud está dirigida a nosotros , si es así la añadimos a nuestra lista de solicitudes
                        if(solicitud.getId_destino().equals(user.getUid())) {
                            Usuario2 usuario=new Usuario2(data.getKey(),solicitud.getId(),solicitud.getNombre_usuario(),solicitud.getEmail(),solicitud.getTelefono()
                            ,solicitud.getUrl_imagen());
                            listaPeticiones.add(usuario);
                            sinSolicitudes.setVisibility(View.INVISIBLE);
                        }

                    }



                }
                if(listaPeticiones.size()==0){
                    sinSolicitudes.setVisibility(View.VISIBLE);
                }
                //Agregamos los datos al adapter
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                solicitudAmistadAdapter = new SolicitudAmistadAdapter( listaPeticiones,SolicitudAmistad.this );
                recyclerView.setAdapter(solicitudAmistadAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(this, ServicioNotificaciones.class));
    }
}
