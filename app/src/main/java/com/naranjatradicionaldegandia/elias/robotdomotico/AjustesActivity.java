package com.naranjatradicionaldegandia.elias.robotdomotico;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceFragment;


public class AjustesActivity extends PreferenceActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //cargar los ajusstes
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainAjustesFragment()).commit();
    }
    public static class MainAjustesFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceStat) {
            super.onCreate(savedInstanceStat);
            addPreferencesFromResource(R.xml.preferencias);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        }
    }
}
