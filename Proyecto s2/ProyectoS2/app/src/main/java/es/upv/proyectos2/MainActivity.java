package es.upv.proyectos2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    MediaPlayer mp;

    private Button btEncendido;
    private Button btSeguridad;
    private Button btAhorro;
    private Button btRegistros;
    private Button btControlManual;

    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static  int NOTIFICACION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //boton registros
        btRegistros = findViewById(R.id.btRegistros);
        btRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registros = new Intent(MainActivity.this, RegistrosActivity.class);
                startActivity(registros);
            }
        });


        //boton control manual
        btControlManual = findViewById(R.id.btControlManual);
        btControlManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent controlManual = new Intent(MainActivity.this, ControlManualActivity.class);
                startActivity(controlManual);
            }
        });


        mp = MediaPlayer.create(this, R.raw.audio);
        mp.start();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//Notificaciones------------------------------------------------------------------------------------------------------------------------------------------------

        btEncendido = findViewById(R.id.btEncendido);
        btEncendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNotificationChannel();
                createNotificationEncendido();

            }
        });//notificacion boton ON/OFF


        btSeguridad  = findViewById(R.id.btSeguridad    );
        btSeguridad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPendingIntent();//te lleva a RegistrosActivity
                createNotificationChannel();
                createNotificationSeguridad();

            }
        });//notificacion seguridad


        btAhorro  = findViewById(R.id.btAhorro);
        btAhorro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNotificationChannel();
                createNotificationAhorro();

            }
        });//notificacion ahorro energia



    }//onCreate



    private void setPendingIntent(){//se tocan cosas del manifest
        Intent intent = new Intent(this, RegistrosActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);//minimo api 16
        stackBuilder.addParentStack(RegistrosActivity.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    private void createNotificationEncendido(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_power_settings_new_black_24dp);
        builder.setContentTitle("Notificación del robot");
        builder.setContentText("El robot se ha encendido");
        builder.setColor(Color.GREEN);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.GREEN, 1000, 1000);
        builder.setVibrate(new long[]{1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }//notificacion on off


    private void createNotificationSeguridad(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_warning_black_24dp);
        builder.setContentTitle("Notificación del robot");
        builder.setContentText("Se ha detectado movimiento en su casa, pulsa aqui para ver el video");
        builder.setColor(Color.RED);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setLights(Color.RED, 1000, 1000);
        builder.setVibrate(new long[]{1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        builder.setContentIntent(pendingIntent);//te lleva a RegistrosActivity

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }//notificacion seguridad

    private void createNotificationAhorro(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_battery_full_black_24dp);
        builder.setContentTitle("Notificación del robot");
        builder.setContentText("Ahorro de energia activo");
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.GREEN, 1000, 1000);
        builder.setVibrate(new long[]{1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }//notificacion ahorro energia activado



//Musica de fondo-----------------------------------------------------------------------------------------------------------------------------------------------
    @Override protected void  onPause() {
        super.onPause();
        mp.pause();
    }

    @Override protected void  onResume() {
        super.onResume();
        mp.start();
    }

    protected void onSaveInstanceState(Bundle guardarEstado) {
        super.onSaveInstanceState(guardarEstado);
        int pos= mp.getCurrentPosition();
        guardarEstado.putInt("posicion", pos);
    }

    protected void onRestoreInstanceState(Bundle recEstado) {
        super.onRestoreInstanceState(recEstado);
        int pos= recEstado.getInt("posicion");
        mp.seekTo(pos);
    }


    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }


    public void lanzarCamaraDirecto(View view){
        Intent i = new Intent(this, CamaraDirectoActivity.class);
        startActivity(i);
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true; /** true -> el menú ya está visible */
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.camaraDirecto) {
            lanzarCamaraDirecto(null);
            return true;
        }


        if (id == R.id.acercaDe) {
            lanzarAcercaDe(null);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}//MainActivity
