package com.naranjatradicionaldegandia.elias.robotdomotico;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.naranjatradicionaldegandia.elias.robotdomotico.Usuario;

public class Usuarios {


    private static final String TAG = "CLASE USUARIOS";

    public static void guardarUsuario(final FirebaseUser user) {
        Usuario usuario = new Usuario(user.getDisplayName(),user.getEmail(), user.getPhoneNumber())
                ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuarios").document(user.getUid()).set(usuario);
    }

    public static void actualizarUsuario(final FirebaseUser user, String name, String mail, String phone){
        Usuario usuario = new Usuario (name, mail, phone);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuarios").document(user.getUid()).set(usuario);
    }

}
