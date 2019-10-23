package com.example.robotdomtico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login();
    }

    private void login() {
        //Se crea la instancia del posible usuario actual.
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        //Si el usuario actual se encuentra en el FireBase de nuestra aplicación...
        if (usuario != null) {
            //Y si está verificado...
            if (usuario.isEmailVerified()) {
                //Se inicia sesión y se pasa a la actividad principal.
                Toast.makeText(this, "inicia sesión: " +
                        usuario.getDisplayName() + " - " + usuario.getEmail() + " - " +
                        usuario.getProviders().get(0), Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            //En caso contrario...
            }else {
                //Se manda un correo de verificación.
                usuario.sendEmailVerification();
                //Y se refresca el usuario para que la verificación surja efecto.
                usuario = FirebaseAuth.getInstance().getCurrentUser();
                usuario.reload();
                Toast.makeText(getApplicationContext(),
                        "Correo de verificación enviado.",
                        Toast.LENGTH_SHORT).show();
            }
        //En caso de que el usuario actual no tenga cuenta sale el repertorio de opciones para
        //crearse una.
        } else {
            startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(true)
                                            .build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                    new AuthUI.IdpConfig.FacebookBuilder().build())).build()
                    //.setIsSmartLockEnabled(false)
                    , RC_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK/*ResultCodes.OK*/) {
                login();
                finish();
            } else {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
                    return;
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "Sin conexión a Internet",
                            Toast.LENGTH_LONG).show();
                    return;
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Error desconocido",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
}