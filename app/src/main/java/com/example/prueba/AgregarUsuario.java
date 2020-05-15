package com.example.prueba;

import android.app.Dialog;
import android.os.Bundle;


import com.example.prueba.Adaptadores.UserAdapterBusqueda;
import com.example.prueba.Objetos.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private ArrayList<Usuario> usuarioList;
    private Usuario usuario_actual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario);


        Toolbar toolbar = findViewById(R.id.toolbar_agregarUsuario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Usuarios");

         usuarioList = new ArrayList<>();
        mAuth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Usuarios");
        recyclerView = findViewById(R.id.recyclerview_agregar_usuarios);
        recyclerView.setHasFixedSize(true);
        leerUsuarios();








    }
    public void leerUsuarios(){

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarioList.clear();

                for(DataSnapshot data:dataSnapshot.getChildren()){
                   Usuario usuario= data.getValue(Usuario.class);
                    FirebaseUser user=mAuth.getCurrentUser();
                   if(usuario!=null ){
                       if(!usuario.getId().equals(user.getUid())) {
                           usuarioList.add(usuario);
                       }
                       else{
                           usuario_actual=usuario;
                       }
                   }
                }
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                userAdapterBusqueda = new UserAdapterBusqueda( usuarioList,AgregarUsuario.this,usuario_actual);

                recyclerView.setAdapter(userAdapterBusqueda);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
