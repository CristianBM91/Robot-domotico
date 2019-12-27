package com.naranjatradicionaldegandia.elias.robotdomotico.ui.acercaDe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.naranjatradicionaldegandia.elias.robotdomotico.R;

public class AcercaDeFragment extends Fragment {
    private Context contexto;

    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(contexto, android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISOS","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(contexto, "Permiso dado", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(contexto, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_acercade, container, false);
      ;
        final TextView textView = root.findViewById(R.id.text_share);

        //Intenciones del Fragment de Info (OJO, LA DE COMPARTIR VA POR SEPARADO EN CompartirAppFragment)------------
        Button btnllamar = (Button) root.findViewById(R.id.btn_llamar);
        Button btnweb = (Button)  root.findViewById(R.id.btn_web);
        Button btncorreo = (Button)  root.findViewById(R.id.btn_correo);
        contexto = root.getContext();
        Button btnmap = (Button)  root.findViewById(R.id.btn_map);
        Button btncompartir = (Button) root.findViewById(R.id.btn_compartir);
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("text/plain");
                startActivity(intent);

            }
        });
    btncompartir.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,
                    "¡Mira esta aplicación! Sirve para controlar a un robot muy chulo.");
            startActivity(intent);

        }
    });
        btnllamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionGranted()) {
                    Intent intent = new Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:965644955"));
                    startActivity(intent);
                }
            }
        });
        btnweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.robomovil.com/"));
                startActivity(intent);



            }
        });

        btncorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String asunto = getResources().getString(R.string.correo_asunto);
                String texto = getResources().getString(R.string.correo_texto);
                intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
                intent.putExtra(Intent.EXTRA_TEXT, texto);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"informacion@robomovil.com"});
                startActivity(intent);


            }
        });
        return root;
    }

}