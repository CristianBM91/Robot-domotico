package com.naranjatradicionaldegandia.elias.robotdomotico.presentacion;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jsibbold.zoomage.ZoomageView;
import com.naranjatradicionaldegandia.elias.robotdomotico.R;

public class AmenazaActivity extends Activity {
    private static final String TAG = "AmenazaActivity";

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_amenaza);
        Log.d(TAG, "Cargando... AmenazaActivity");
        final SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        Bundle extras = getIntent().getExtras();
      // String url = extras.getString("url");
        String url = "https://firebasestorage.googleapis.com/v0/b/robotdomotico.appspot.com" +
                "/o/imagenes%2Felies1324%40gmail.com%2F288afc31-8ecc-4ed7-a682-f78a62222b99?alt=media&token=0360aac4-259a-442f-8004-6a52a9d5496e";
        final TextView txt = (TextView) findViewById(R.id.texto_anotacion);
        Button atras = findViewById(R.id.atras);
        Button llamar = findViewById(R.id.llamar_emergencias);
        final ZoomageView img = findViewById(R.id.imagen_amenaza_zoom);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionGranted()) {
                    String numero = pref.getString("emergencia", "112");
                    Intent intent = new Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:"+ numero));
                    startActivity(intent);
                }
            }
        });
        //final ImageView img = findViewById(R.id.imagen_amenaza);
        final Task<QuerySnapshot> query = FirebaseFirestore.getInstance()
                .collection("Grabaciones").whereEqualTo("url", url).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("FIREBASE Amenaza", document.getId() + " => " + document.getData());

                                final String url = document.getString("url");
                                txt.setText(document.get("anotaciones").toString());



                                Glide.with(getApplicationContext())
                                        .asBitmap()
                                        .load(url)
                                        .into(img);




                            }
                        } else {
                            Log.d("FIREBASE", "Error al cargar imagenes: ", task.getException());
                        }



                    }
                });
    }


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
}
