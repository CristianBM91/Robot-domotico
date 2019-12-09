package com.naranjatradicionaldegandia.elias.robotdomotico.presentacion;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;


import android.preference.Preference;


import android.preference.EditTextPreference;
import android.widget.Toast;

import com.naranjatradicionaldegandia.elias.robotdomotico.R;

import java.util.regex.Pattern;

public class PreferenciasFragment extends PreferenceFragment {
    public static boolean notificacionesModoAmenazaActivadas = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
        //notificaciones
        final SwitchPreference notificaciones = (SwitchPreference) findPreference("noti");
        notificaciones.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean a;
                if(Boolean.getBoolean(newValue.toString())){
                    notificacionesModoAmenazaActivadas = true;
                } else {
                    notificacionesModoAmenazaActivadas = false;
                }
                return false;
            }
        });
        //Tema
        final SwitchPreference tema = (SwitchPreference) findPreference("tema");
        notificaciones.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean a;
                if(Boolean.getBoolean(newValue.toString())){
                    getActivity().setTheme(R.style.AppTheme);


                } else {
                    getActivity().setTheme(R.style.AppThemeLight);

                }
                return false;
            }
        });

        //Nombre robot
        final EditTextPreference robot = (EditTextPreference) findPreference("nombre_robot_preferencia");
            robot.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String nombre = newValue.toString();
                    if(nombre.length() < 6 || nombre.length() > 25){

                            Toast.makeText(getActivity(), "Por favor, introduce un nombre con un número de carácteres entre 6 y 25",
                                    Toast.LENGTH_LONG).show();
                            return false;

                        } else if(!Pattern.matches("^[a-zA-Z0-9\\s]+$", nombre)) {

                        Toast.makeText(getActivity(), "El nombre no puede contener carácteres raros o no admitidos",
                                Toast.LENGTH_LONG).show();

                        return false;
                    } else {




                            Toast.makeText(getActivity(), "¡Nombre cambiado con éxito! Bienvenido a la vida, " + nombre,
                                    Toast.LENGTH_LONG).show();
                            return false;
                    }


                }


            });
    }
}
