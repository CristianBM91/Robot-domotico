package com.naranjatradicionaldegandia.elias.robotdomotico.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.naranjatradicionaldegandia.elias.ambos.Imagen;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.naranjatradicionaldegandia.elias.ambos.RP;
import com.naranjatradicionaldegandia.elias.ambos.Robot;
import com.naranjatradicionaldegandia.elias.robotdomotico.GeneratePictureStyleNotification;
import com.naranjatradicionaldegandia.elias.robotdomotico.R;
import com.naranjatradicionaldegandia.elias.robotdomotico.ServicioOn;
import com.naranjatradicionaldegandia.elias.robotdomotico.presentacion.MainActivity;
import com.naranjatradicionaldegandia.elias.robotdomotico.usuario.Usuarios;

import org.w3c.dom.Text;

import java.util.Locale;

import static android.content.Context.SENSOR_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment{

    private HomeViewModel homeViewModel;
    private View vista;
    private StorageReference storageRef;
    TextView textX, textY, textZ;
    SensorManager sensorManager;
    Sensor sensor;
    TextView textoModo;
    Vibrator vibrator;
    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
    private TextToSpeech textToSpeechSystem;
    public View onCreateView(@NonNull LayoutInflater inflater,
                                            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        actualizarImagen();
        storageRef = FirebaseStorage.getInstance().getReference();
        vista = root;
        vibrator = (Vibrator) this.getActivity().getSystemService(Context.VIBRATOR_SERVICE);



        initActivityScreenOrientPortrait();
        // DEFINICIONES <---------------------------------------------------------------------------
        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        final TextView txt_estado = (TextView)vista.findViewById(R.id.txt_estado);
       Usuarios.getNombreRobotParaTextView(usuario, txt_estado, getActivity());
        textX = (TextView) vista.findViewById(R.id.textX);
        textY = (TextView) vista.findViewById(R.id.textY);
        textZ = (TextView) vista.findViewById(R.id.textZ);
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(getContext());



        final RadioGroup rGroup = (RadioGroup)vista.findViewById(R.id.radioGroup);
// This will get the radiobutton in the radiogroup that is checked
        RadioButton checkedRadioButton = (RadioButton)rGroup.findViewById(rGroup.getCheckedRadioButtonId());
        textoModo = (TextView)vista.findViewById(R.id.textoModo) ;

        //Selector de modos
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()



        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                vibrator.vibrate(45);
                if (isChecked)
                {


                    if(checkedRadioButton.getText().toString().contains("Manual")){
                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        MainActivity.modoAmenaza = false;
                        textoModo.setText("Modo Manual activado");
                        getActivity().stopService(new Intent(getContext(), ServicioOn.class));
                    }
                    if(checkedRadioButton.getText().toString().contains("Auto")){

                        Robot.activarModoAutomatico();


                        getActivity().startService(new Intent(getContext(), ServicioOn.class));
                        MainActivity.modoAmenaza = false;

                        textoModo.setText("Modo Automático activado");
                    }
                    if(checkedRadioButton.getText().toString().contains("Vigi")){
                        getActivity().stopService(new Intent(getContext(), ServicioOn.class));
                        textoModo.setText("Modo Vigilancia activado");
                        MainActivity.modoAmenaza = true;
                        Robot.activarModoVigilancia();
                    }
                }
            }
        });

        //Botones
        ImageButton camara =  vista.findViewById(R.id.imageButton);
        ImageButton derecha = vista.findViewById(R.id.botonDerecha);
        ImageButton izquierda = vista.findViewById(R.id.botonIzquierda);
        derecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Robot.girarDerecha();
                vibrator.vibrate(25);
            }
        });

        izquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(25);
               Robot.girarIzquierda();
            }
        });
        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RP.tomarFoto();
                vibrator.vibrate(35);
            }
        });

        Button desactivarManual =  vista.findViewById(R.id.desactivarModoManual);

        desactivarManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), MainActivity.class));
                textoModo.setText("Ha desactivado el modo manual. Seleccione otro modo, por favor.");
            }
        });


        // onCreate
        return root;
    }

    private void initActivityScreenOrientPortrait()
    {
        // Avoid screen rotations (use the manifests android:screenOrientation setting)
        // Set this to nosensor or potrait

        // Set window fullscreen


        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Test if it is VISUAL in portrait mode by simply checking it's size
        boolean bIsVisualPortrait = ( metrics.heightPixels >= metrics.widthPixels );

        if( !bIsVisualPortrait )
        {
            // Swap the orientation to match the VISUAL portrait mode
            if(  getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT )
            {  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); }
            else {  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ); }
        }
        else {  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR); }

    }
    void borrarImagen(final StorageReference imagenReferencia){

        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {


            public void run() {


                imagenReferencia.delete();
            }

        };
        handler.postDelayed(runnable, 1000);
    }
    public void actualizarImagen(){

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {


            public void run() {

                final Task<QuerySnapshot> query = FirebaseFirestore.getInstance()
                        .collection("Grabaciones")
                        .orderBy("tiempo", Query.Direction.DESCENDING)
                        .limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(HomeFragment.this.isVisible()){
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d("FIREBASE", document.getId() + " => " + document.getData());

                                            String url = document.getString("url");
                                            String titulo = document.getString("titulo");
                                            ImageView imagen = getActivity().findViewById(R.id.imgrp);
                                            StorageReference imagenReferencia = storageRef.child("imagenes/" + document.getId());
                                            if(HomeFragment.this.isVisible()) {
                                                Glide.with(getContext())
                                                        .load(url)

                                                        .into(imagen);

                                                borrarImagen(imagenReferencia);
                                            }

                                        }
                                    } else {
                                        Log.d("FIREBASE", "Error al cargar imagenes: ", task.getException());
                                    }
                                }


                            }
                        });

                handler.postDelayed(this, 500);
            }
        };
        handler.postDelayed(runnable, 600);

    }




    // bool

}