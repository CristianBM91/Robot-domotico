package com.naranjatradicionaldegandia.elias.robotdomotico;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.naranjatradicionaldegandia.elias.robotdomotico.presentacion.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GeneratePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = "NotificadorModoAmenaza";
    private Context mContext;
    private String title, message, imageUrl;

    public GeneratePictureStyleNotification(Context context, String title, String message, String imageUrl) {
        super();
        this.mContext = context;
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        InputStream in;
        try {
            Log.d(TAG, "Generando notificacion ...");
            URL url = new URL(this.imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        Notification notif = new Notification.Builder(mContext)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(result)
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(result))
                .build();

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("key", "value");
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, intent, PendingIntent.FLAG_ONE_SHOT);


    }
}