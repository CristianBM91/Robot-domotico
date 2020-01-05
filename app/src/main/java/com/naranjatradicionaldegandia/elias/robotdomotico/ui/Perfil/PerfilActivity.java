package com.naranjatradicionaldegandia.elias.robotdomotico.ui.Perfil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.naranjatradicionaldegandia.elias.robotdomotico.presentacion.MainActivity;
import com.naranjatradicionaldegandia.elias.robotdomotico.R;
import com.naranjatradicionaldegandia.elias.robotdomotico.usuario.Usuarios;

import static com.naranjatradicionaldegandia.elias.robotdomotico.usuario.Usuarios.actualizarUsuario;


public class PerfilActivity extends Activity {

    Button guardarCambios;
    Button cancelarCambios;
    EditText valorNombre, valorContra;
    EditText valorCorreo;
    EditText valorTelefono;
    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
    PhoneAuthProvider mCallbacks;
    private Context context;
    String nombre_robot;
    private String antiguoTelefono;

    public void endOperation(){

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_de_usuario);

        if(savedInstanceState != null){
            Bundle e = getIntent().getExtras();
            antiguoTelefono = e.getString("tel");
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        context = getBaseContext();
        guardarCambios = (Button) this.findViewById(R.id.btn_guardar);
        cancelarCambios  = (Button) this.findViewById(R.id.btn_cancelar);
        valorNombre  = (EditText) this.findViewById(R.id.nameText);
        valorCorreo  = (EditText) this.findViewById(R.id.mailText);
        valorTelefono = (EditText) this.findViewById(R.id.phoneText);


        Usuarios.getNombre(usuario, valorNombre);
        valorCorreo.setText(usuario.getEmail());
        Usuarios.getTelefono(usuario, valorTelefono);

        db.collection("usuarios").document(usuario.getEmail()).get()
                .addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                                if (task.isSuccessful()) {
                                     nombre_robot = task.getResult().getString("robot");

                                    Log.d("Firestore", "nombre robot:" + nombre_robot);
                                } else {
                                    Log.e("Firestore", "Error al leer", task.getException());
                                }
                            }
                        });

        guardarCambios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {






                String nombre = valorNombre.getText().toString();
                String correo = valorCorreo.getText().toString();
                String telefono = valorTelefono.getText().toString();
                boolean isValid = telefono.matches("\\+\\d(-\\d{3}){2}-\\d{4}");
                if(isValid){

                }

                actualizarUsuario(usuario, nombre, correo, telefono, nombre_robot);

                endOperation();
            }
        });

        cancelarCambios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                endOperation();
            }
        });
    }}

