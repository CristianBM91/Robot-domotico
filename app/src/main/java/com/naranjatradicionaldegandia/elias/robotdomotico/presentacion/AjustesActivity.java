package com.naranjatradicionaldegandia.elias.robotdomotico.presentacion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.naranjatradicionaldegandia.elias.robotdomotico.R;
import com.naranjatradicionaldegandia.elias.robotdomotico.ui.tools.ToolsFragment;


public class AjustesActivity extends Activity {

    private static final String TAG = "PREFERENCIAS";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Cargando... AjustesActivity");
        getFragmentManager().beginTransaction()

                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();
        Log.d(TAG, "Inicializando AjustesActivity");
    }
    @Override
    public void onBackPressed() {
        //Execute your code here

        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    }


