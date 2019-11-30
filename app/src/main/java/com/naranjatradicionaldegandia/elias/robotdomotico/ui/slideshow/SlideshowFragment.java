package com.naranjatradicionaldegandia.elias.robotdomotico.ui.slideshow;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.naranjatradicionaldegandia.elias.robotdomotico.R;

public class SlideshowFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_info, container, false);
        //Intenciones del Fragment de Info (OJO, LA DE COMPARTIR VA POR SEPARADO EN CompartirAppFragment)------------
        Button btnllamar = (Button) root.findViewById(R.id.btn_llamar);
        Button btnweb = (Button)  root.findViewById(R.id.btn_web);
        Button btncorreo = (Button)  root.findViewById(R.id.btn_correo);

        Button btnmap = (Button)  root.findViewById(R.id.btn_map);
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:38.1840491,-3.6852374"));
                startActivity(intent);

            }
        });

        btnllamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:965644955"));
                startActivity(intent);

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