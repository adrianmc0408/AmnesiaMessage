package com.example.prueba.Objetos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class ReceptorInicio extends BroadcastReceiver {
//Se trata de un receptor de Broadcast que recibe un Intent cuando el movil se enciende , cuando el inicio se haya completado
    //Iniciaremos el servicio de notificaciones
    @Override
    public void onReceive(Context context, Intent intent) {
        //Depende de la versión instalada iniciaremos el servicio en primer plano de una manera u otra.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, ServicioNotificaciones.class));
        } else {
            context.startService(new Intent(context, ServicioNotificaciones.class));
        }

    }
}
