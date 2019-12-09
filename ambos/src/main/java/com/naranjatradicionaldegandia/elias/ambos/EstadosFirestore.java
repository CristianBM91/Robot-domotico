package com.naranjatradicionaldegandia.elias.ambos;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class EstadosFirestore{
    private CollectionReference estados;

        public EstadosFirestore() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            estados = db.collection("estados");
        }

        public void elemento(String id, final EstadosAsinc.EscuchadorElemento escuchador) {
            estados.document(id).get().addOnCompleteListener(
                    new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Estado estado = task.getResult().toObject(Estado.class);
                                escuchador.onRespuesta(estado);
                            } else {
                                Log.e("Firebase", "Error al leer", task.getException());
                                escuchador.onRespuesta(null);
                            }
                        }
                    });
        }

        public void anyade(Estado estado) {
            estados.document().set(estado); //o datos.add(dato);
        }

        public String nuevo() {
            return estados.document().getId();
        }

        public void borrar(String id) {
            estados.document(id).delete();
        }

        public void actualiza(String id, Dato dato) {
            estados.document(id).set(dato);
        }

        public void tamanyo(final DatosAsinc.EscuchadorTamanyo escuchador) {
            estados.get().addOnCompleteListener(
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
