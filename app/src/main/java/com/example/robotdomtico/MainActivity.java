package com.example.robotdomtico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comun.Imagen;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static android.provider.MediaStore.Images.Media.getBitmap;

public class MainActivity extends AppCompatActivity {
    private StorageReference storageRef;
    private RecyclerView recyclerView;
    private AdaptadorImagenes adaptador;

    //=========================================================>
// ---------------------------- ONCREATE ------------------------>
    //=========================================================>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // DEFINICIONES <---------------------------------------------------------------------------

        // boton limpieza
        ImageButton limpieza = findViewById(R.id.limpiezabtn);
        ImageButton limpiezaactivo = findViewById(R.id.limpiezabtnactivo);

        // boton vigilancia
        ImageButton vigilancia = findViewById(R.id.vigilanciabtn);
        ImageButton vigilanciaactivo = findViewById(R.id.vigilanciabtnactivo);

        // boton encender
        ImageButton onoff = findViewById(R.id.onoffbtn);
        ImageButton onoffactivo = findViewById(R.id.onoffbtnactivo);

        // estados
        TextView limpiando = findViewById(R.id.textolimpiando);
        TextView vigilando = findViewById(R.id.textovigilando);
        TextView limpiandovigilando = findViewById(R.id.textotodo);
        TextView recargando = findViewById(R.id.textorecargando);

        // Bateria
        Button bateriallena = findViewById(R.id.bateriallena);
        //Button bateriacasillena = findViewById(R.id.bateriacasillena);
        //Button bateriamitad = findViewById(R.id.bateriamitad);
        Button bateriacasivacia = findViewById(R.id.bateriacasivacia);
        //Button bateriavacia = findViewById(R.id.bateriavacia);


        // VARIABLES <------------------------------------------------------------------------------
        final int[] contadorLimpieza = {1};
        final int[] contadorVigilancia = {1};
        final int[] contadorOnoff = {1};

        final boolean[] limpiar = {false};
        final boolean[] vigilar = {false};
        final boolean[] activado = {false};


        // LLAMADAS <-------------------------------------------------------------------------------

        //cargarToolbar();



        pulsarLimpieza(limpieza, limpiezaactivo, contadorLimpieza, limpiar, activado);

        pulsarVigilancia(vigilancia, vigilanciaactivo, contadorVigilancia, vigilar, activado);

        pulsarEncendido(onoff, onoffactivo, contadorOnoff, activado, limpiar, vigilar, limpiando, vigilando, limpiandovigilando);


    } // onCreate

    //=========================================================>
// ---------------------------- FUNCIONES ------------------------>
    //=========================================================>

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // --------------------- Cargar toolbar --------------------------------------------------------
   /* void cargarToolbar(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Obtenemos la ActionBar instalada por AppCompatActivity
        ActionBar actionBar = getSupportActionBar();

//Establecemos el icono en la ActionBar
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Robot");

    } // ()
*/

    void intermitenciapocabateria (){


    }




    // ---------------------------------------------------------------------------------------------
    // --------------------- pulsar limpieza -------------------------------------------------------
    void pulsarLimpieza(ImageButton limpieza, ImageButton limpiezaactivo, int[] contadorLimpieza, boolean[] limpiar, boolean[] activado){

        // ACCIONES AL CLICKAR BOTON LIMPIEZA <--------------------------------------------------
        limpieza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!activado[0]){
                    contadorLimpieza[0] = contadorLimpieza[0] + 1;
                    limpiar[0] = true;

                    limpieza.setVisibility(View.INVISIBLE);
                    limpiezaactivo.setVisibility(View.VISIBLE);
                }
            } // onClick
        });
        // ACCIONES AL CLICKAR BOTON LIMPIEZA ACTIVO <-------------------------------------------
        limpiezaactivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!activado[0]) {
                    contadorLimpieza[0] = contadorLimpieza[0] + 1;
                    limpiar[0] = false;

                    limpieza.setVisibility(View.VISIBLE);
                    limpiezaactivo.setVisibility(View.INVISIBLE);
                }
            }
        });

    } // ()

    // ---------------------------------------------------------------------------------------------
    // --------------------- Pulsar vigilancia -----------------------------------------------------
    void pulsarVigilancia(ImageButton vigilancia, ImageButton vigilanciaactivo, int[] contadorVigilancia, boolean[] vigilar, boolean[] activado){

        // ACCIONES AL CLICKAR BOTON VIGILANCIA <--------------------------------------------------
        vigilancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!activado[0]) {
                    contadorVigilancia[0] = contadorVigilancia[0] + 1;
                    vigilar[0] = true;

                    vigilancia.setVisibility(View.INVISIBLE);
                    vigilanciaactivo.setVisibility(View.VISIBLE);
                }
            }
        });
        // ACCIONES AL CLICKAR BOTON VIGILANCIA ACTIVO <-------------------------------------------
        vigilanciaactivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!activado[0]) {
                    contadorVigilancia[0] = contadorVigilancia[0] + 1;
                    vigilar[0] = false;

                    vigilancia.setVisibility(View.VISIBLE);
                    vigilanciaactivo.setVisibility(View.INVISIBLE);

                }
            }
        });


    } // ()

    // ---------------------------------------------------------------------------------------------
    // --------------------- Pulsar encendido ------------------------------------------------------
    void pulsarEncendido (ImageButton onoff, ImageButton onoffactivo, int[] contadorOnoff, boolean[] activado, boolean[] limpiar, boolean[] vigilar, TextView limpiando, TextView vigilando, TextView limpiandovigilando){

        // ACCIONES AL CLICKAR BOTON ON/OFF <--------------------------------------------------
        onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (limpiar[0] || vigilar[0]) {
                    contadorOnoff[0] = contadorOnoff[0] + 1;
                    activado[0] = true;

                    onoff.setVisibility(View.INVISIBLE);
                    onoffactivo.setVisibility(View.VISIBLE);


                    // TEXTO QUE MUESTRA ESTADO <-------------

                    if (limpiar[0] & !vigilar[0]) {

                        limpiando.setVisibility(View.VISIBLE);


                    } else if (!limpiar[0] && vigilar[0]) {

                        vigilando.setVisibility(View.VISIBLE);

                    } else if (limpiar[0] && vigilar[0]) {

                        limpiandovigilando.setVisibility(View.VISIBLE);

                    }
                    // PARA EL MODO RECARGA //
                /*
                if(recargando){

                    // texto recargando

                }
                */
                }
            }
        });
// ACCIONES AL CLICKAR BOTON ON/OFF ACTIVO <--------------------------------------------------
        onoffactivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contadorOnoff[0] = contadorOnoff[0] +1;
                activado[0] = false;

                onoff.setVisibility(View.VISIBLE);
                onoffactivo.setVisibility(View.INVISIBLE);




                limpiando.setVisibility(View.INVISIBLE);
                vigilando.setVisibility(View.INVISIBLE);
                limpiandovigilando.setVisibility(View.INVISIBLE);


            }
        });


    } // ()


    //=========================================================>
// ---------------------------- ANIMACIONES ------------------------>
    //=========================================================>
    // ---------------------------------------------------------------------------------------------
    // -------------------intermitencia poca bateria -----------------------------------------------




    // menu toolbar ---------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    } // bool

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // LO QUE PASA AL PULSAR CERRAR SESION <-------------------------------------------------
        if (id == R.id.menu_cerrar_sesion) {

            FirebaseAuth.getInstance().signOut();
            //finish();
            Intent i = new Intent(this, CustomLoginActivity.class);
            startActivity(i);

            return true;
        }

        // LO QUE PASA AL PULSAR ESTADO ROBOT <-------------------------------------------------
        if (id == R.id.robot) {


            Intent i = new Intent(this, RobotActivity.class);
            startActivity(i);


            return true;
        }

        // LO QUE PASA AL PULSAR GALERIA <-------------------------------------------------
        if (id == R.id.galeria) {
            Intent i = new Intent(this, GaleriaActivity.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    } // bool


}