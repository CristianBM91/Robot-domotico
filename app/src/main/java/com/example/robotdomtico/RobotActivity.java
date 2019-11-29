package com.example.robotdomtico;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class RobotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);


        // SE INFLATEA LA TOOLBAR <--------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRobot);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Estado");


        // SE CREA LA FLECHA ATRAS
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);








    } // ONCREATE






    // VOLVER ATRAS AL PULSAR FLECHA
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }





    // SE INFLATEA EL MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_robot, menu);
        return true;



    }

    // COSAS QUE PASAN AL PULSAR MENU <------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // LO QUE PASA AL PULSAR CERRAR SESION
        if (id == R.id.menu_cerrar_sesion) {

            FirebaseAuth.getInstance().signOut();

            Intent i = new Intent(this, CustomLoginActivity.class);
            startActivity(i);

            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}