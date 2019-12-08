package com.naranjatradicionaldegandia.elias.robotdomotico.presentacion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.naranjatradicionaldegandia.elias.robotdomotico.R;
import com.naranjatradicionaldegandia.elias.robotdomotico.usuario.Usuarios;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.broker;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.clientId;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.qos;
import static com.naranjatradicionaldegandia.elias.ambos.Mqtt.topicRoot;

public class MainActivity extends AppCompatActivity {
    public StorageReference storageRef;
    private AppBarConfiguration mAppBarConfiguration;
    public TextView correo;
    public static MqttClient client;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getBaseContext();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        try {
            Log.i("MQTT: ", "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.connect();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, AjustesActivity.class));
            return true;
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
