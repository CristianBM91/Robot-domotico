package com.naranjatradicionaldegandia.elias.comun;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class DatosFirestore {
    private CollectionReference datos;

    public DatosFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        datos = db.collection("datos");
    }

    public void elemento(String id, final DatosAsinc.EscuchadorElemento escuchador) {
        datos.document(id).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Dato dato = task.getResult().toObject(Dato.class);
                            escuchador.onRespuesta(dato);
                        } else {
                            Log.e("Firebase", "Error al leer", task.getException());
                            escuchador.onRespuesta(null);
                        }
                    }
                });
    }

    public void anyade(Dato dato) {
        datos.document().set(dato); //o datos.add(dato);
    }

    public String nuevo() {
        return datos.document().getId();
    }

    public void borrar(String id) {
        datos.document(id).delete();
    }

    public void actualiza(String id, Dato dato) {
        datos.document(id).set(dato);
    }

    public void tamanyo(final DatosAsinc.EscuchadorTamanyo escuchador) {
        datos.get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            escuchador.onRespuesta(task.getResult().size());
                        } else {
                            Log.e("Firebase", "Error en tamanyo", task.getException());
                            escuchador.onRespuesta(-1);
                        }
                    }
                });
    }
}
