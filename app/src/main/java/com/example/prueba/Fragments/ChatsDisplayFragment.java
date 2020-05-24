package com.example.prueba.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.Adaptadores.UserAdapter;
import com.example.prueba.Adaptadores.UserChatDisplayAdapter;
import com.example.prueba.ChatRoom;
import com.example.prueba.Objetos.Chat;
import com.example.prueba.Objetos.TareaBorrarMensaje;
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
import java.util.Collections;
import java.util.Timer;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsDisplayFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserChatDisplayAdapter userChatDisplayAdapter;
    private ArrayList<Usuario2> usuarioList;
    private ArrayList<String> id_list;
    private FirebaseDatabase base;
    private DatabaseReference referencia;
    private DatabaseReference referencia2;
    private FirebaseAuth auth;
    private FirebaseUser user;
    DividerItemDecoration divider;


    public ChatsDisplayFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chatdisplay, container, false);

        Timer timer=new Timer();
        TareaBorrarMensaje tarea=new TareaBorrarMensaje();
        timer.scheduleAtFixedRate(tarea,0,300000);
        base=FirebaseDatabase.getInstance();
        referencia=base.getReference("Chats");
        referencia2=base.getReference("Usuarios");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        id_list=new ArrayList<>();
        usuarioList=new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_view_chat_display);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        userChatDisplayAdapter = new UserChatDisplayAdapter(getContext(),usuarioList);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        divider.setDrawable(recyclerView.getContext().getResources().getDrawable(R.drawable.reycler_divider));
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(userChatDisplayAdapter);
        leerChats();





        return view;

    }
    public void leerChats(){
        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                id_list.clear();

                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Chat chat= data.getValue(Chat.class);
                    Boolean existe_ya=false;
                    String id;
                    int posicion=0;
                    if(chat.getReceiver().equals(user.getUid())){
                        id=chat.getSender();
                        for(int i=0;i<id_list.size();i++){
                            if(id_list.get(i).equals(id)){
                                existe_ya=true;
                                posicion=i;
                            }
                        }
                        if(existe_ya==false){
                            id_list.add(id);
                        }
                        else{
                            id_list.remove(posicion);
                            id_list.add(id);
                        }
                    }
                    else if(chat.getSender().equals(user.getUid())){
                        id=chat.getReceiver();
                        for(int i=0;i<id_list.size();i++){
                            if(id_list.get(i).equals(id)){
                                existe_ya=true;
                                posicion=i;
                            }
                        }
                        if(existe_ya==false){
                            id_list.add(id);
                        }else{
                            id_list.remove(posicion);
                            id_list.add(id);
                        }
                    }
                }
                referencia2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Collections.reverse(id_list);
                        usuarioList.clear();
                        ArrayList<Usuario> usuarios=new ArrayList<>();
                        Usuario usuario=new Usuario();
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                           usuarios.add(usuario=data.getValue(Usuario.class));
                        }

                        for(int i=0;i<id_list.size();i++){
                            for(int j=0;j<usuarios.size();j++){
                                if(id_list.get(i).equals(usuarios.get(j).getId())){
                                    usuarioList.add(new Usuario2(dataSnapshot.getKey(),usuarios.get(j).getId(),usuarios.get(j).getNombre_usuario(),
                                            usuarios.get(j).getEmail(),usuarios.get(j).getTelefono(),usuarios.get(j).getUrl_imagen()));
                                }
                            }
                        }
                        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
                        userChatDisplayAdapter = new UserChatDisplayAdapter(getContext(),usuarioList);
                        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
                        divider.setDrawable(recyclerView.getContext().getResources().getDrawable(R.drawable.reycler_divider));
                        recyclerView.addItemDecoration(divider);
                        userChatDisplayAdapter.setOnClickListener(new UserChatDisplayAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(int position) {
                                Intent intent = new Intent(getContext(), ChatRoom.class);
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("usuario",usuarioList.get(position));
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        });
                        recyclerView.setAdapter(userChatDisplayAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }






}
