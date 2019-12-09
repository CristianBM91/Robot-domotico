package com.naranjatradicionaldegandia.elias.robotdomotico.ui.tools;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceFragmentCompat;

import com.naranjatradicionaldegandia.elias.robotdomotico.R;

public class ToolsFragment extends PreferenceFragment {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }





}