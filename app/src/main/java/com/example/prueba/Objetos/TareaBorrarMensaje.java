package com.example.prueba.Objetos;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TareaBorrarMensaje extends TimerTask {
    private FirebaseDatabase base;
    private DatabaseReference referencia;
    public TareaBorrarMensaje(){
        base=FirebaseDatabase.getInstance();
        referencia=base.getReference("Chats");
    }

    @Override
    public void run() {
        referencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Date fecha_actual=new Date();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Chat chat=data.getValue(Chat.class);
                    long diferencia=fecha_actual.getTime()-chat.getFecha().getTime();
                    long horas= TimeUnit.MILLISECONDS.toHours(diferencia);
                    if(horas>=24){
                        referencia.child(data.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
