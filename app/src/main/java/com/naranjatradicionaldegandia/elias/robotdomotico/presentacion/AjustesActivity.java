package com.naranjatradicionaldegandia.elias.robotdomotico.presentacion;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;

import com.naranjatradicionaldegandia.elias.robotdomotico.R;


public class AjustesActivity extends PreferenceActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //cargar los ajusstes
       // getFragmentManager().beginTransaction().replace(android.R.id.content, new MainAjustesFragment()).commit();
    }
    public static class MainAjustesFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreate(Bundle savedInstanceStat) {
            super.onCreate(savedInstanceStat);

        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.preferencias, rootKey);
        }
    }
}
