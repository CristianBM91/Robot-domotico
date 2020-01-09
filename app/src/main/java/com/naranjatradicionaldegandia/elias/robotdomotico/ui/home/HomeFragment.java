package com.naranjatradicionaldegandia.elias.robotdomotico.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.naranjatradicionaldegandia.elias.ambos.RP;
import com.naranjatradicionaldegandia.elias.ambos.Robot;
import com.naranjatradicionaldegandia.elias.robotdomotico.R;
import com.naranjatradicionaldegandia.elias.robotdomotico.ServicioOn;
import com.naranjatradicionaldegandia.elias.robotdomotico.presentacion.MainActivity;
import com.naranjatradicionaldegandia.elias.robotdomotico.usuario.Usuarios;

import static android.content.Context.SENSOR_SERVICE;

public class HomeFragment extends Fragment{
    private ProgressBar mProgressBar;
    private HomeViewModel homeViewModel;
    private View vista;
    private StorageReference storageRef;
    private   TextView textX, textY, textZ;
    private    SensorManager sensorManager;
    private  Sensor sensor;
    private   TextView textoModo;
    private RadioGroup rGroup;
   private Vibrator vibrator;
    private  ImageButton camara;
    private ImageButton atras, avanzar;
   public static boolean modoManual = false;
    private  TextView txt_estado;
    private   boolean primeraVez=true;
    private  SharedPreferences pref;

    private FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
    private int mProgressStatus = 0;
    private TextToSpeech textToSpeechSystem;
    public View onCreateView(@NonNull LayoutInflater inflater,
                                            ViewGroup container, Bundle savedInstanceState) {


        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        storageRef = FirebaseStorage.getInstance().getReference();
        vista = root;
        vibrator = (Vibrator) this.getActivity().getSystemService(Context.VIBRATOR_SERVICE);

    pref =
            PreferenceManager.getDefaultSharedPreferences(getContext());


        // DEFINICIONES <---------------------------------------------------------------------------
        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        txt_estado = (TextView)vista.findViewById(R.id.txt_estado);
       Usuarios.getNombreRobotParaTextView(usuario, txt_estado, getActivity());
        textX = (TextView) vista.findViewById(R.id.textX);
        textY = (TextView) vista.findViewById(R.id.textY);
        textZ = (TextView) vista.findViewById(R.id.textZ);
        atras = vista.findViewById(R.id.botonAtras);
        avanzar = vista.findViewById(R.id.botonAvanzar);
        mProgressBar = (ProgressBar) vista.findViewById(R.id.progressbar);
        if(primeraVez){
            mProgressBar.setProgress(13);
        }

         RadioGroup rGroup = (RadioGroup)vista.findViewById(R.id.radioGroup);

        RadioButton checkedRadioButton = (RadioButton)rGroup.findViewById(rGroup.getCheckedRadioButtonId());
        textoModo = (TextView)vista.findViewById(R.id.textoModo) ;

        actualizarImagen();
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
                        modoManual = true;
                        getActivity().stopService(new Intent(getContext(), ServicioOn.class));
                        mProgressBar.setProgress(0);



                    }
                    if(checkedRadioButton.getText().toString().contains("Auto")){

                        Robot.activarModoAutomatico();
                        hablar("Modo automático iniciado. ", getActivity());

                        getActivity().startService(new Intent(getContext(), ServicioOn.class));
                        MainActivity.modoAmenaza = false;

                        textoModo.setText("Modo Automático activado");
                    }
                    if(checkedRadioButton.getText().toString().contains("Vigi")){
                        getActivity().stopService(new Intent(getContext(), ServicioOn.class));
                        textoModo.setText("Modo Vigilancia activado");
                        hablar("Modo vigilancia activado. ", getActivity());
                        MainActivity.modoAmenaza = true;
                        Robot.activarModoVigilancia();
                    }

                }
            }
        });

        //Botones
         camara =  vista.findViewById(R.id.imageButton);

        textoModo.setVisibility(View.INVISIBLE);
        camara.setVisibility(View.INVISIBLE);
        TextView txt4 = vista.findViewById(R.id.textView4);
        txt4.setVisibility(View.INVISIBLE);
        txt_estado.setVisibility(View.INVISIBLE);

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
        avanzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(25);
                Robot.avanzar();
            }
        });
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(25);
                Robot.parar();
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
                hablar("Modo manual desactivado. ", getActivity());
                textoModo.setText("Ha desactivado el modo manual. ");
                modoManual = false;
                startActivity(new Intent(getContext(), MainActivity.class));


            }
        });


        // onCreate
        return root;
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
        if(primeraVez){

            mProgressBar.setProgress(26);
        }
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {


            public void run() {
                if(primeraVez){
                    mProgressBar.setProgress(44);
                    if(modoManual){
                        hablar("Activando modo manual. ", getActivity());
                    }
                }

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
                                            if(primeraVez){
                                                mProgressBar.setProgress(68);
                                            }

                                            String url = document.getString("url");
                                            String titulo = document.getString("titulo");
                                            ImageView imagen = getActivity().findViewById(R.id.imgrp);
                                            StorageReference imagenReferencia = storageRef.child("imagenes/" + document.getId());
                                            if(HomeFragment.this.isVisible()) {
                                                if(primeraVez){
                                                    mProgressBar.setProgress(85);
                                                }
                                                Glide.with(getContext())
                                                        .load(url)

                                                        .into(imagen);

                                                borrarImagen(imagenReferencia);
                                                if(primeraVez && modoManual == false){
                                                    mProgressBar.setProgress(100);

                                                    imagen.setVisibility(View.VISIBLE);
                                                    textoModo.setVisibility(View.VISIBLE);
                                                    camara.setVisibility(View.VISIBLE);
                                                    TextView txt4 = vista.findViewById(R.id.textView4);
                                                    txt4.setVisibility(View.VISIBLE);
                                                    txt_estado.setVisibility(View.VISIBLE);


                                                }else{
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                }

                                                primeraVez = false;
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
        handler.postDelayed(runnable, 150);

    }


    public void hablar(final String mensaje, Activity actividad){
        String lastMensaje = " ";
        if(!(lastMensaje.equals(mensaje))){
            if(pref.getBoolean("voces", true)){
                textToSpeechSystem = new TextToSpeech(actividad, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            String textToSay = mensaje;
                            Log.d("TTS", "Reproduciendo mensaje: " + mensaje);
                            textToSpeechSystem.speak(textToSay, TextToSpeech.QUEUE_FLUSH, null);

                        }
                    }
                });
            }
            lastMensaje = mensaje;
        }


    }

    // bool

}