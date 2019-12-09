package com.naranjatradicionaldegandia.elias.robotdomotico;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.naranjatradicionaldegandia.elias.robotdomotico.presentacion.MainActivity;

public class ServicioOn extends IntentService {
    private NotificationManager notificationManager;
    static final String CHANNEL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;
    private static final String TAG = "SERVICIO ON";
    NotificationChannel mChannel;
    private NotificationManager mManager;
    private String title, msg, actionCode;
    public ServicioOn(){
        super("ServicioOn");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Inicializado");
        createNotification("El robot ha sido encendido", "Robot Domótico");


        }

    public void createNotification(String msg, String title) {
        Intent intent = null;

            intent = new Intent(this, MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel androidChannel = new NotificationChannel(CHANNEL_ID,
                    title, NotificationManager.IMPORTANCE_DEFAULT);
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.GREEN);

            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(androidChannel);
            Notification.Builder nb = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setTicker(title)
                    .setShowWhen(true)
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                            R.mipmap.ic_launcher_round))
                    .setAutoCancel(true).setContentIntent(contentIntent);


            getManager().notify(101, nb.build());

        }
    }


    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

}
