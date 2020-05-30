package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.mbms.StreamingServiceInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.prueba.Adaptadores.ChatAdapter;
import com.example.prueba.Adaptadores.UserChatDisplayAdapter;
import com.example.prueba.Fragments.ChatsDisplayFragment;
import com.example.prueba.Objetos.Chat;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoom extends AppCompatActivity {

    private TextView username;
    private CircleImageView image_profile;
    private Intent intent;
    private ImageButton sendButton;
    private EditText message_field;
    private TextView message_hour;
    private ChatAdapter chatAdapter;
    private ArrayList<Chat> listaChats;
    private RecyclerView recyclerView;
    private FirebaseDatabase base;
    private ImageButton btn_adjuntar;

    private DatabaseReference referencia;
    private FirebaseAuth auth;
    private FirebaseUser usuario;
    LinearLayoutManager linearLayoutManager;
    Usuario2 user;
    ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Toolbar toolbar = findViewById(R.id.tooalbar_chatroom);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listaChats = new ArrayList<>();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        base=FirebaseDatabase.getInstance();
        referencia=base.getReference("Chats");
        auth=FirebaseAuth.getInstance();
        usuario=auth.getCurrentUser();
        recyclerView = findViewById(R.id.recyclerview_chatroom);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(getApplicationContext(),listaChats,"ded");
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setAdapter(chatAdapter);

        user =(Usuario2) getIntent().getExtras().getSerializable("usuario");

        btn_adjuntar = findViewById(R.id.ic_adjuntar);
        image_profile = findViewById(R.id.image_profile_chatroom);
        message_hour = findViewById(R.id.txt_hour);
        username = findViewById(R.id.username_chatroom);
        sendButton = findViewById(R.id.btn_send_chatroom);
        message_field = findViewById(R.id.message_field_chatroom);
        username.setText(user.getNombre_usuario());
        image_profile.setImageResource(R.drawable.profile);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message_field.getText().toString();
                Date fecha=new Date();
                if (!msg.equals("")){
                    Chat mensaje=new Chat(usuario.getUid(),user.getId(),msg,fecha,"txt");
                    referencia.push().setValue(mensaje);
                }
                message_field.setText("");
            }
        });
        leerMensajes();

        btn_adjuntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }
   public void  leerMensajes(){

        referencia.addValueEventListener(valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaChats.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Chat chat=data.getValue(Chat.class);
                    if (((chat.getReceiver().equals(usuario.getUid()))&&(chat.getSender().equals(user.getId())))||((chat.getReceiver().equals(user.getId()))&&(chat.getSender().equals(usuario.getUid())))) {
                        if((chat.getReceiver().equals(usuario.getUid()))&&(chat.isLeido()==false)){
                            chat.setLeido(true);
                            referencia.child(data.getKey()).setValue(chat);
                        }
                        listaChats.add(chat);
                    }
                }
                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                chatAdapter = new ChatAdapter(getApplicationContext(),listaChats,"ded");
                linearLayoutManager.setStackFromEnd(true);
                recyclerView.setAdapter(chatAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });{
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        referencia.removeEventListener(valueEventListener);
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        referencia.removeEventListener(valueEventListener);
        this.finish();
    }
}
