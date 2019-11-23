package com.example.robotdomtico;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class Mas extends Fragment {

    EditText cantidad;
    Button btn_cerrar_sesion;
    public Mas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_mas, container, false);
        Button botonpreferencias = v.findViewById(R.id.btn_preferencias);
        Button botonacercade = v.findViewById(R.id.btn_acercade);
        Button cerrarSesion = v.findViewById(R.id.btn_cerrar_sesion);
        botonacercade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sePulsa(view);
            }
        });
        botonpreferencias.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sePulsa(view);
            }
        });
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), CustomLoginActivity.class);
                startActivity(intent);
            }
        });

        botonacercade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AcercaDeActivity.class);
                startActivity(intent);
            }
        });

        botonpreferencias.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), PreferenciasActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    public void sePulsa(View view) {
        cantidad.setText(cantidad.getText() + (String) view.getTag());
    }
}
