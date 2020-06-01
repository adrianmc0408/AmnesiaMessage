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
    //Declaramos los atributos
    private FirebaseDatabase base;
    private DatabaseReference referencia;
    //Obtenemos los elementos de Firebase necesarios
    public TareaBorrarMensaje(){
        base=FirebaseDatabase.getInstance();
        referencia=base.getReference("Chats");
    }

    @Override
    //Método que se inicia al crear la tarea
    public void run() {
        //Consulta a la base de datos , aunque sea un listener este tipo de consulta no responde a eventos, solo
        //se ejecuta una vez
        referencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Obtenemos la fecha actual
                Date fecha_actual=new Date();
                //Recorremos los nodos hijos de el nodo Chats
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    //Serializamos los nodos hijos a un objeto Chat
                    Chat chat=data.getValue(Chat.class);
                    //Obtenemos la diferencia de tiempo entre el mensaje leido y la hora actual en milisegundos
                    long diferencia=fecha_actual.getTime()-chat.getFecha().getTime();
                    //Transformamos los milisegundos a horas
                    long horas= TimeUnit.MILLISECONDS.toHours(diferencia);
                    //Si el mensaje tiene 24 o mas horas lo borramos de la BD
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
