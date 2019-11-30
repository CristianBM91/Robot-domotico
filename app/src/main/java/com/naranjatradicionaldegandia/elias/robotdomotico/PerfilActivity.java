package com.naranjatradicionaldegandia.elias.robotdomotico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.naranjatradicionaldegandia.elias.robotdomotico.MainActivity;
import com.naranjatradicionaldegandia.elias.robotdomotico.R;

import static com.naranjatradicionaldegandia.elias.robotdomotico.Usuarios.actualizarUsuario;


public class PerfilActivity extends Activity {

    Button guardarCambios;
    Button cancelarCambios;
    EditText valorNombre;
    EditText valorCorreo;
    EditText valorTelefono;
    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

    public void endOperation(){

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_de_usuario);


        guardarCambios = (Button) this.findViewById(R.id.btn_guardar);
        cancelarCambios  = (Button) this.findViewById(R.id.btn_cancelar);
        valorNombre  = (EditText) this.findViewById(R.id.nameText);
        valorCorreo  = (EditText) this.findViewById(R.id.mailText);
        valorTelefono = (EditText) this.findViewById(R.id.phoneText);

        valorNombre.setText(usuario.getDisplayName());
        valorCorreo.setText(usuario.getEmail());
        valorTelefono.setText(usuario.getPhoneNumber());

        guardarCambios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String nombre = valorNombre.getText().toString();
                String correo = valorCorreo.getText().toString();
                String telefono = valorTelefono.getText().toString();
                actualizarUsuario(usuario, nombre, correo, telefono);
                endOperation();
            }
        });

        cancelarCambios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                endOperation();
            }
        });
    }}

