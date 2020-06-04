package com.example.prueba.Objetos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.prueba.HomePrincipal;
import com.example.prueba.R;
import com.example.prueba.SolicitudAmistad;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServicioNotificaciones2 extends Service {
    private FirebaseDatabase base;
    private FirebaseAuth auth;
    private DatabaseReference referencia;
    private FirebaseUser user;
    private final static String CHANNEL_ID="NOTIFICACION2";
    private final static int NOTIFICACION_ID=1;
    PendingIntent pi;
    public ServicioNotificaciones2() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        base=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        referencia=base.getReference("Chats");
        crearPending();
        leerDatos();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void leerDatos(){
        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user=auth.getCurrentUser();
                boolean notificar=false;
                int contador=0;
                for(DataSnapshot datos:dataSnapshot.getChildren()){
                    Chat chat=datos.getValue(Chat.class);
                    if ((chat.getReceiver().equals(user.getUid()))&&(chat.isLeido()!=true)){
                        if (chat.isNotificado()!=true) {
                            chat.setNotificado(true);
                            referencia.child(datos.getKey()).setValue(chat);
                            notificar = true;
                        }
                        contador++;

                    }
                }
                if(notificar){
                    crearNotificacion(contador);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void crearPending(){
        Intent intent=new Intent(getApplicationContext(), HomePrincipal.class);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        pi=stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public void crearNotificacion(int num){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel canal=new NotificationChannel(CHANNEL_ID,"Notificacion", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            canal.enableLights(true);
            canal.enableVibration(true);
            canal.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(canal);

        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        builder.setSmallIcon(R.drawable.logo_final);
        builder.setContentTitle("Amnesia Message");
        builder.setContentText("Tienes "+num+" mensajes sin leer");
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setColor(Color.BLUE);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentIntent(pi);
        builder.setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID,builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
