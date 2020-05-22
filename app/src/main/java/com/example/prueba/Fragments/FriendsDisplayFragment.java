package com.example.prueba.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.Adaptadores.SolicitudAmistadAdapter;
import com.example.prueba.Adaptadores.UserAdapter;
import com.example.prueba.Objetos.Amistad;
import com.example.prueba.Objetos.Amistad2;
import com.example.prueba.Objetos.Solicitud;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario2;
import com.example.prueba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsDisplayFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<Usuario2> usuarioList;
    private ArrayList<Amistad2> amigosList;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private FirebaseAuth mAuth;
    private FirebaseUser usuario;
    public FriendsDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        mAuth= FirebaseAuth.getInstance();
        usuario=mAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Amigos");
        reference2=database.getReference("Usuarios");
        recyclerView = view.findViewById(R.id.recycler_view_friends);
        recyclerView.setHasFixedSize(true);
        usuarioList = new ArrayList<>();
        amigosList= new ArrayList<>();
        leerAmigos();
        return view;
    }

    public void leerAmigos(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                amigosList.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Amistad amigo= data.getValue(Amistad.class);
                    FirebaseUser user=mAuth.getCurrentUser();
                    if(amigo!=null ){
                        if((amigo.getId1().equals(usuario.getUid()))){
                            amigosList.add(new Amistad2(amigo.getId2(),data.getKey()));
                        }
                        else if((amigo.getId2().equals(usuario.getUid()))){
                            amigosList.add(new Amistad2(amigo.getId1(),data.getKey()));
                        }

                    }
                }
                if(!amigosList.isEmpty()) {
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            usuarioList.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Usuario user = data.getValue(Usuario.class);
                                for (int i = 0; i < amigosList.size(); i++) {
                                    if (amigosList.get(i).getId().equals(user.getId())) {
                                        usuarioList.add(new Usuario2(amigosList.get(i).getReferencia(),user.getId(),user.getNombre_usuario(),
                                                user.getEmail(),user.getTelefono(),user.getUrl_imagen()));

                                    }
                                }
                            }
                            layoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(layoutManager);
                            userAdapter = new UserAdapter(getContext(), usuarioList);
                            recyclerView.setAdapter(userAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    userAdapter = new UserAdapter(getContext(), usuarioList);
                    recyclerView.setAdapter(userAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
