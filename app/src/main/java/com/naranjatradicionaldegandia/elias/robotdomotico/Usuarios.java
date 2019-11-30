package com.naranjatradicionaldegandia.elias.robotdomotico;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.naranjatradicionaldegandia.elias.robotdomotico.Usuario;

public class Usuarios {


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
