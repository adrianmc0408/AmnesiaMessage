package com.example.prueba.Objetos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class ReceptorInicio extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, ServicioNotificaciones.class));
        } else {
            context.startService(new Intent(context, ServicioNotificaciones.class));
        }

    }
}
