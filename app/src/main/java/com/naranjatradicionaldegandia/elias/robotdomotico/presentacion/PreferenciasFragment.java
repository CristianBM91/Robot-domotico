package com.naranjatradicionaldegandia.elias.robotdomotico.presentacion;

import android.os.Bundle;
import android.preference.PreferenceFragment;


import android.preference.Preference;


import android.preference.EditTextPreference;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.naranjatradicionaldegandia.elias.robotdomotico.R;
import com.naranjatradicionaldegandia.elias.robotdomotico.usuario.Usuarios;

import java.util.regex.Pattern;

public class PreferenciasFragment extends PreferenceFragment {
    public static boolean notificacionesModoAmenazaActivadas = false;
    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
        //notificaciones

        //Tema
       /* final SwitchPreference tema = (SwitchPreference) findPreference("tema");
        tema.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
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

       */
        //numero emergencia
        final EditTextPreference emergencia = (EditTextPreference) findPreference("numero_emergencia");
        final String regex = "[0-9]+";
        emergencia.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(Pattern.matches(regex, newValue.toString())){
                        Toast.makeText(getActivity(), "El número de emergencia se ha actualizado con éxito",
                                Toast.LENGTH_LONG).show();
                        return true;
                    }
                return false;
            }
        });

        //Nombre robot
        final EditTextPreference robot = (EditTextPreference) findPreference("nombre_robot_preferencia");
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("usuarios").document(usuario.getEmail()).get()
                .addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                                if (task.isSuccessful()) {
                                    String nombre = task.getResult().getString("robot");
                                    robot.setDefaultValue(nombre);
                                    Log.d("Firestore", "robot:" + nombre);
                                } else {
                                    Log.e("Firestore", "Error al leer", task.getException());
                                }
                            }
                        });
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

                        Usuarios.addNombreRobot(usuario, nombre);
                            return true;
                    }


                }


            });

            final SwitchPreference config = (android.preference.SwitchPreference) findPreference("configrp");


    }

    public void actualizarConfig(){
        final SwitchPreference config = (android.preference.SwitchPreference) findPreference("configrp");
    }
}
