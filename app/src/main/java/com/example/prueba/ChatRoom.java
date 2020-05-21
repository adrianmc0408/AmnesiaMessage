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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoom extends AppCompatActivity {

    private TextView username;
    private CircleImageView image_profile;
    private Intent intent;
    private ImageButton sendButton;
    private EditText message_field;
    private ChatAdapter chatAdapter;
    private ArrayList<Chat> listaChats;
    private RecyclerView recyclerView;


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

        recyclerView = findViewById(R.id.recyclerview_chatroom);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());


        recyclerView.setLayoutManager(linearLayoutManager);
        for(int i = 0 ; i<10 ; i++){
            listaChats.add(new Chat("2","1","Hola1"));
            listaChats.add(new Chat("2","1","Que tal"));
            listaChats.add(new Chat("1","2","Hola2"));
            listaChats.add(new Chat("1","2","Que tal2"));
        }
        chatAdapter = new ChatAdapter(getApplicationContext(),listaChats,"ded");
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setAdapter(chatAdapter);
        image_profile = findViewById(R.id.image_profile_chatroom);
        username = findViewById(R.id.username_chatroom);
        sendButton = findViewById(R.id.btn_send_chatroom);
        message_field = findViewById(R.id.message_field_chatroom);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message_field.getText().toString();
                if (!msg.equals("")){
                    listaChats.add(new Chat("1","2",message_field.getText().toString()));
                }
                message_field.setText("");
            }
        });



        intent=getIntent();

        username.setText(intent.getStringExtra("username"));
        image_profile.setImageResource(R.drawable.profile);

    }




}
