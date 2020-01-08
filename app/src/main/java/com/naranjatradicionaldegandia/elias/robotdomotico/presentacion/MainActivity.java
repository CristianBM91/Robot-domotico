package com.naranjatradicionaldegandia.elias.robotdomotico.presentacion;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.naranjatradicionaldegandia.elias.robotdomotico.R;
import com.naranjatradicionaldegandia.elias.robotdomotico.usuario.Usuarios;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.broker;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.clientApp;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.qos;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.topicRoot;
import static com.naranjatradicionaldegandia.elias.robotdomotico.ui.home.HomeFragment.modoManual;
import static java.lang.Integer.valueOf;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "666";
    public StorageReference storageRef;
    private AppBarConfiguration mAppBarConfiguration;
    public TextView textoModo;
    public static MqttClient client;
    private Context context;
    SharedPreferences pref;
    public static boolean modoAmenaza = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getBaseContext();
        pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //comprobador de modos por si vigilancia está activo


        if(pref.getBoolean("configrp", false)){
            pref.edit().putBoolean("configrp", true).commit();
            Log.d(TAG, "Tratando de poner en true configrp");

        }
        try {
            Log.i("MQTT: ", "Conectando al broker " + broker);
            client = new MqttClient(broker, clientApp, new MemoryPersistence());
            client.connect();
            client.subscribe(topicRoot+"modo", qos);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                     System.out.println("MQTT Connection was lost!" + cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String mensaje =  new String(message.getPayload());
                    System.out.println(" MQTT Message Arrived!: " + topic + ": " + mensaje);
                    if(mensaje.contains("0x002")){
                        textoModo = (TextView)findViewById(R.id.textoModo) ;



                        if(modoAmenaza){
                            Log.d("MQTT Mensaje Recibido", "Ejecutando Amenaza Detectada");
                            final Task<QuerySnapshot> query = FirebaseFirestore.getInstance()
                                    .collection("Grabaciones")
                                    .orderBy("tiempo", Query.Direction.DESCENDING)
                                    .limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("FIREBASE", document.getId() + " => " + document.getData());

                                                    final String url = document.getString("url");

                                                    Bitmap a;


                                                    Glide.with(context)
                                                            .asBitmap()
                                                            .load(url)
                                                            .into(new CustomTarget<Bitmap>() {
                                                                @Override
                                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                                    Log.d("GLIDE", "Imagen amenaza lista");
                                                                    if(pref.getBoolean("noti", true)){
                                                                        createNotification("Se ha detectado una posible amenaza", "¡Amenaza detectada!", resource, url);
                                                                    }

                                                                }

                                                                @Override
                                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                                }
                                                            });




                                                }
                                            } else {
                                                Log.d("FIREBASE", "Error al cargar imagenes: ", task.getException());
                                            }



                                        }
                                    });
                        }


//Mensaje de activacion de modo amenaza
                    } else if(mensaje.contains("0x0002")){
                        Log.d("MQTT", "Mensaje de activación modo amenaza recibido");
                        modoAmenaza = true;


                    }else{
                        Log.d("MQTT", "No se ha reconocido el mensaje.");
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("MQTT Delivery Complete!");
                }
            });

        } catch (MqttException e) {
            Log.e("MQTT", "Error al conectar.", e);
        }
        try {
            Log.i("MQTT", "Publicando mensaje: " + "hola");
            String mensaje = "Una aplicación con la sesión iniciada de " + user.getEmail() + " se ha conectado con exito.";
            MqttMessage message = new MqttMessage(mensaje.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot+"conexion", message);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al publicar.", e);
        }


        try {
            Log.i("MQTT", "Publicando mensaje: " + "el correo " + user.getEmail());
            String mensaje = user.getEmail();
            MqttMessage message = new MqttMessage(mensaje.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot+"correo", message);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al publicar.", e);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Inicializando perimetro", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(context, DibujarActivity.class);
                startActivity(i);
            }
        });

        Bundle extras = getIntent().getExtras();
        storageRef = FirebaseStorage.getInstance().getReference();



        DrawerLayout drawer = findViewById(R.id.drawer_layout);//da error pero es porque el android studio es gilipollas

        NavigationView navigationView = findViewById(R.id.nav_view);//da error pero es porque el android studio es gilipollas
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        View headerView = navigationView.getHeaderView(0);
        TextView correo = (TextView) headerView.findViewById(R.id.nav_correo);
        TextView nombre = (TextView) headerView.findViewById(R.id.nav_nombre);
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        Usuarios.getNombre(usuario, nombre);
        String email = usuario.getEmail();
        if (usuario != null) {
            // Name, email address, and profile photo Url



            nombre.setText(usuario.getDisplayName());
            correo.setText(email);

        }


    }

    private NotificationManager notificationManager;
    static final int NOTIFICACION_ID = 1;
    private static final String TAG = "SERVICIO ON";
    NotificationChannel mChannel;
    private NotificationManager mManager;
    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISOS","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permiso dado", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    public void createNotification(String msg, String title, Bitmap imagen, String url) {
        Intent intent = null;

        intent = new Intent(this, AmenazaActivity.class);


        intent.putExtra("url", url);
                mManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.amenaza);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mManager.createNotificationChannel(androidChannel);
            Notification.Builder nb = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setTicker(title)
                    .setShowWhen(true)
                    .setSound(sound, attributes)
                    .setStyle(new Notification.BigPictureStyle().bigPicture(imagen))
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                            R.mipmap.ic_launcher_round))
                    .setAutoCancel(true).setContentIntent(contentIntent);

            if(isPermissionGranted()) {
                String numero = pref.getString("numero_emergencia", "112");
                Intent llamar_intent = new Intent(Intent.ACTION_CALL,
                        Uri.parse("tel:"+ numero));
                PendingIntent llamar = PendingIntent.getActivity(this, 0, llamar_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                nb.addAction(R.drawable.vigilancia, "LLAMAR",llamar);
            }
            mManager.notify(101, nb.build());

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if(!modoManual){
                // launch settings activity
                Log.d("MainActivity - OptionsItemSelected", "Inicializando AjustesActivity");
                startActivity(new Intent(context, AjustesActivity.class));
                return true;
            }else {
                Toast.makeText(getApplicationContext(), "Por favor, desactiva el modo manual para poder continuar", Toast.LENGTH_SHORT).show();
            }

        }
        if(id == R.id.action_robot){
            if(!modoManual){
                Log.d("MainActivity - OptionsItemSelected", "Inicializando EnlaceActivity");
                Intent i = new Intent(context, EnlaceActivity.class);
                i.putExtra("inicio", "voluntario");
                startActivity(i);
                return true;

            }else{
                Toast.makeText(getApplicationContext(), "Por favor, desactiva el modo manual para poder continuar", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override public void onDestroy() {
        try {
            Log.i("MQTT", "Desconectado");
            client.disconnect();
        } catch (MqttException e) {
            Log.e("MQTT", "Error al desconectar.", e);
        }
        super.onDestroy();
    }

}
