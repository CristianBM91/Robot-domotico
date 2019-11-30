package com.naranjatradicionaldegandia.elias.robotdomotico.ui.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.naranjatradicionaldegandia.elias.robotdomotico.R;

public class EditarPerfil extends Fragment {

    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
    TextView nombre;
    TextView correo;
    TextView telefono;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.perfil_de_usuario, container, false);

        nombre = (TextView) root.findViewById(R.id.nombre);
        correo = (TextView) root.findViewById(R.id.correo);
        telefono = (TextView) root.findViewById(R.id.telefono);

        cargarValores();

        return root;

    }

    public void cargarValores(){
        nombre.setText(usuario.getDisplayName());
        correo.setText(usuario.getEmail());
        telefono.setText(usuario.getPhoneNumber());
    }
}
