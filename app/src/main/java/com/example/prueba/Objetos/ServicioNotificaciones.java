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
    //Declaramos los atributos
    private FirebaseDatabase base;
    private FirebaseAuth auth;
    private DatabaseReference referencia;
    private DatabaseReference referencia2;
    private FirebaseUser user;
    private final static String CHANNEL_ID="NOTIFICACION";
    private final static String CHANNEL_ID2="NOTIFICACION2";
    private final static int NOTIFICACION_ID=0;
    private final static int NOTIFICACION_ID2=0;
    private PendingIntent pi;
    private PendingIntent pi2;
    private ValueEventListener listener1;
    private ValueEventListener listener2;
    public ServicioNotificaciones() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Al devolver START_STICKY indicamos al sistema que debe reiniciar el servicio en caso de que sea destruido por el propio sistema por temas
        // de optimización de la memoria.
         return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Obtenemos los elementos de Firebase necesarios
        base=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        referencia=base.getReference("Solicitudes");
        referencia2=base.getReference("Chats");
        //Llamada a los metodos de creación de los canales de notificación y los pending intent necesarios para poder ir a la activity deseada al pulsar
        //en la notificación
        crearPending();
        crearPending2();
        crearCanal();
        crearCanal2();
        //Creamos la notificación necesaria para el funcionamiento del proceso en primer plano
        Notification notificacion = new NotificationCompat.Builder(this, CHANNEL_ID2)
                .setContentTitle("Servicio de notificaciones")
                .setContentText("Leyendo los datos")
                .setSmallIcon(R.drawable.logo_final)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .build();
        //Iniciamos el servicio en prime plano
        startForeground(1,notificacion);
        //Iniciamos los listener encargados de recibir los cambios en la BD y notificar en consecuencia
        leerDatos();
        leerDatos2();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    //Este método recibe las nuevas notificaciones , si hay alguna cuyo atributo notificada=false procederemos a notificar al usuario
    public void leerDatos(){
        referencia.addValueEventListener(listener1=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user=auth.getCurrentUser();
                boolean notificar=false;
                for(DataSnapshot datos:dataSnapshot.getChildren()){
                    Solicitud solicitud=datos.getValue(Solicitud.class);
                    if ((user.getUid().equals(solicitud.getId_destino()))&&(solicitud.isNotificado()!=true)){
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
//Este método recibe los cambios en el nodo Chats y notifica al usuario de cuantos mensajes tiene por leer.
    public void leerDatos2(){
        referencia2.addValueEventListener(listener2=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user=auth.getCurrentUser();
                boolean notificar=false;
                int contador=0;
                for(DataSnapshot datos:dataSnapshot.getChildren()){
                    Chat chat=datos.getValue(Chat.class);
                    //Todos los mensajes recibidos y sin leer los ponemos como notificados y vamos incrementando el contador
                    //para poder notificar cuantos mensajes tenemos sin leer
                    if ((user.getUid().equals(chat.getReceiver()))&&(chat.isLeido()!=true)){
                        if (chat.isNotificado()!=true) {
                            chat.setNotificado(true);
                            referencia2.child(datos.getKey()).setValue(chat);
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
    //Método utilizado para crear el PendingIntent necesario para desplazarnos a una activity concreta al pulsar la notificación
    public void crearPending(){
        Intent intent=new Intent(getApplicationContext(), SolicitudAmistad.class);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomePrincipal.class);
        stackBuilder.addNextIntent(intent);
        pi=stackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);
    }
    //Método utilizado para crear el PendingIntent necesario para desplazarnos a una activity concreta al pulsar la notificación
    public void crearPending2(){
        Intent intent=new Intent(getApplicationContext(), HomePrincipal.class);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        pi2=stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }
//Método únicamente necesario para las versiones superiores a Android Oreo, creamos un canal de notificación necesario para establecer las opciones de notificación
    //tales como su importancia , vibracion etc
    public void crearCanal(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel canal=new NotificationChannel(CHANNEL_ID,"Notificacion", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            canal.enableLights(true);
            canal.enableVibration(true);
            canal.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(canal);

        }
    }
    //Método únicamente necesario para las versiones superiores a Android Oreo, creamos un canal de notificación necesario para establecer las opciones de notificación
    //tales como su importancia , vibracion etc
    public void crearCanal2(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel canal2=new NotificationChannel(CHANNEL_ID2,"Notificacion2", NotificationManager.IMPORTANCE_MIN);
            NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(canal2);

        }
    }
    //Creamos la notificación necesaria para indicar la solicitud nueva entrante y definimos como queremos que sea es decir , como vibra
    //cómo suena , el color del led de notificaciones, el texto a mostrar , el intent para poder desplazarse y que cuando la toquemos se borre
    public void crearNotificacion(){

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
    //Creamos la notificación necesaria para indicar los chats sin leer que tenemos y definimos como queremos que sea es decir , como vibra
    //cómo suena , el color del led de notificaciones, el texto a mostrar , el intent para poder desplazarse y que cuando la toquemos se borre
    public void crearNotificacion(int num){

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        builder.setSmallIcon(R.drawable.logo_final);
        builder.setContentTitle("Amnesia Message");
        builder.setContentText("Tienes "+num+" mensajes sin leer");
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setColor(Color.BLUE);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentIntent(pi2);
        builder.setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID2,builder.build());
    }
//Cuando el servicio se pare , detendremos tambien los listener ya que sino se quedarían esperando el segundo plano
    @Override
    public void onDestroy() {
        super.onDestroy();
        referencia.removeEventListener(listener1);
        referencia2.removeEventListener(listener2);
    }
}
