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

public class ServicioNotificaciones extends Service {
    private FirebaseDatabase base;
    private FirebaseAuth auth;
    private DatabaseReference referencia;
    private FirebaseUser user;
    private final static String CHANNEL_ID="NOTIFICACION";
    private final static int NOTIFICACION_ID=0;
    private PendingIntent pi;
    public ServicioNotificaciones() {
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
        referencia=base.getReference("Solicitudes");
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
                for(DataSnapshot datos:dataSnapshot.getChildren()){
                    Solicitud solicitud=datos.getValue(Solicitud.class);
                    if ((solicitud.getId_destino().equals(user.getUid()))&&(solicitud.isNotificado()!=true)){
                       solicitud.setNotificado(true);
                       referencia.child(datos.getKey()).setValue(solicitud);
                        notificar=true;
                    }
                }
                if(notificar){
                    crearNotificacion();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void crearPending(){
        Intent intent=new Intent(getApplicationContext(), SolicitudAmistad.class);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomePrincipal.class);
        stackBuilder.addNextIntent(intent);
        pi=stackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void crearNotificacion(){
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
        builder.setContentText("Tienes solicitudes por atender");
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setColor(Color.BLUE);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setDefaults(Notification.DEFAULT_SOUND);
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
