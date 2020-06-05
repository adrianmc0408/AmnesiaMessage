package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prueba.Adaptadores.ChatAdapter;
import com.example.prueba.Objetos.Chat;
import com.example.prueba.Objetos.ServicioNotificaciones;
import com.example.prueba.Objetos.Usuario;
import com.example.prueba.Objetos.Usuario2;
import com.example.prueba.Objetos.Usuario3;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


import net.glxn.qrgen.android.QRCode;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoom extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISION=1;
    private static final int REQUEST_CODE_SELECT_IMAGE=2;

    private

    Uri image_uri = null;

    private TextView username;
    private CircleImageView image_profile;
    private Intent intent;
    private ImageButton sendButton;
    private EditText message_field;
    private TextView message_hour;
    private ChatAdapter chatAdapter;
    private ArrayList<Chat> listaChats;
    private RecyclerView recyclerView;

    private ImageButton btn_adjuntar;
    private static final int IMAGE_REQUEST=1;

    private DatabaseReference referencia;
    private FirebaseAuth auth;
    private FirebaseUser usuario;
    private FirebaseDatabase base;
    private StorageReference storageReference;

    private Uri imageUri;
    private StorageTask uploadTask;

    LinearLayoutManager linearLayoutManager;
    Usuario2 user;
    ValueEventListener listener;
    public static boolean foto=false;

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
                referencia.removeEventListener(listener);
                finish();
            }
        });
        base = FirebaseDatabase.getInstance();
        referencia = base.getReference("Chats");
        auth = FirebaseAuth.getInstance();
        usuario = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("fotosChat");
        recyclerView = findViewById(R.id.recyclerview_chatroom);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        user = (Usuario2) getIntent().getExtras().getSerializable("usuario");
        chatAdapter = new ChatAdapter(getApplicationContext(), listaChats, user.getUrl_imagen() );
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setAdapter(chatAdapter);


        btn_adjuntar = findViewById(R.id.ic_adjuntar);
        image_profile = findViewById(R.id.image_profile_chatroom);

        if(user.getUrl_imagen().equals("default")){

            image_profile.setImageResource(R.drawable.profile);
        }
        else{
            Glide.with(getApplicationContext()).load(user.getUrl_imagen()).into(image_profile);
        }

        message_hour = findViewById(R.id.txt_hour);
        username = findViewById(R.id.username_chatroom);
        sendButton = findViewById(R.id.btn_send_chatroom);
        message_field = findViewById(R.id.message_field_chatroom);
        username.setText(user.getNombre_usuario());
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message_field.getText().toString();
                Date fecha = new Date();
                if (!msg.equals("")) {
                    Chat mensaje = new Chat(usuario.getUid(), user.getId(), msg, fecha, "txt","");
                    referencia.push().setValue(mensaje);
                }
                message_field.setText("");
            }
        });
        leerMensajes();

        btn_adjuntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });


    }

    public void leerMensajes() {

        referencia.addValueEventListener(listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaChats.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Chat chat = data.getValue(Chat.class);
                    if (((chat.getReceiver().equals(usuario.getUid())) && (chat.getSender().equals(user.getId()))) || ((chat.getReceiver().equals(user.getId())) && (chat.getSender().equals(usuario.getUid())))) {
                        if ((chat.getReceiver().equals(usuario.getUid())) && (chat.isLeido() == false)) {
                            chat.setLeido(true);
                            referencia.child(data.getKey()).setValue(chat);
                        }
                        listaChats.add(chat);
                    }
                }
                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                chatAdapter = new ChatAdapter(getApplicationContext(), listaChats, user.getUrl_imagen());
                linearLayoutManager.setStackFromEnd(true);
                recyclerView.setAdapter(chatAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        {
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        referencia.removeEventListener(listener);
        this.finish();
    }

    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Enviando imagen");
        pd.show();
        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot>task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        String msg = "Imagen";
                        Date fecha = new Date();
                        //Enviamo un mensaje con la uri de la imagen
                            Chat mensaje = new Chat(usuario.getUid(), user.getId(), msg, fecha, "img",mUri);
                            referencia.push().setValue(mensaje);
                        pd.dismiss();
                    }else{
                        Toast.makeText(ChatRoom.this, "error", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChatRoom.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else{
            Toast.makeText(this, "No ha seleccionado imagen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(ChatRoom.this, "Envío en progreso", Toast.LENGTH_SHORT).show();

            } else{
                uploadImage();
            }

        }
        else{
            Toast.makeText(ChatRoom.this, "Error onActivityResult", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        referencia.removeEventListener(listener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!foto) {
            leerMensajes();
            stopService(new Intent(this, ServicioNotificaciones.class));
        }
        else{
            foto=false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }



}
