package com.naranjatradicionaldegandia.elias.robotdomotico.presentacion;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;

import com.naranjatradicionaldegandia.elias.robotdomotico.R;
import com.naranjatradicionaldegandia.elias.robotdomotico.ui.tools.ToolsFragment;


public class AjustesActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new ToolsFragment())
                .commit();
    }

        //cargar los ajusstes
       // getFragmentManager().beginTransaction().replace(android.R.id.content, new MainAjustesFragment()).commit();
    }




