package com.naranjatradicionaldegandia.elias.robotdomotico.presentacion;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.naranjatradicionaldegandia.elias.robotdomotico.plano.PlanoVista;
import com.naranjatradicionaldegandia.elias.robotdomotico.R;

import java.util.Arrays;
import java.util.List;

public class DibujarActivity extends AppCompatActivity {

    List<String> estados = Arrays.asList(new String[]{
            "AVANZA","GIRO_IZQ","AVANZA", "GIRO_IZQ","AVANZA", "GIRO_IZQ","AVANZA", "GIRO_IZQ","AVANZA", "PARADO"});
    List<Long> tiempos = Arrays.asList(new Long[]{
            0L,       20000L,   3000L     ,40000L,   3000L,    40000L,    3000L,   30000L,    3000L,    10000L });
    PlanoVista planoVista;
    private double zoom = 0.015;
    SeekBar seekbar;

    private  int progresoAnterior = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dibujar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        FloatingActionButton fab = findViewById(R.id.fab);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(progresoAnterior < progress){
                    zoom = zoom*1.05;
                    planoVista.setZoom(zoom); //Aplicamos Zoom
                    System.out.println("Aumentando zoom: PROGRESO ANTERIOR " + progresoAnterior + ", PROGRESO ACTUAL " + progress);
                    planoVista.invalidate();  //Repintamos
                    progresoAnterior = progress;

                } else if (progresoAnterior> progress){
                    zoom = zoom*0.95;
                    planoVista.setZoom(zoom);
                    System.out.println("Disminuyendo zoom: PROGRESO ANTERIOR " + progresoAnterior + ", PROGRESO ACTUAL " + progress);
                    planoVista.invalidate();
                    progresoAnterior = progress;

                }



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoom = zoom*1.1; // Aumentamos un 10%
                planoVista.setZoom(zoom); //Aplicamos Zoom
                planoVista.invalidate();  //Repintamos
                Snackbar.make(view, "Aumentando Zoom", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        //He creado mal la lista de tiempos. He puesto duraciones
        //Con este bucle lo paso a tiempos absolutos
        for (int n=1; n<tiempos.size(); n++) {
            tiempos.set(n, tiempos.get(n-1)+tiempos.get(n));
        }
        planoVista = findViewById(R.id.planoVista);
        planoVista.setDatos(estados,tiempos);
        planoVista.setZoom(zoom);
    }
}