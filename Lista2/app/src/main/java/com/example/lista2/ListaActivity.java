package com.example.lista2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    RecyclerView recycler;
    ArrayList<Video> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_activity);

        recycler = findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setHasFixedSize(true);

        arrayList = new ArrayList<Video>();

        Video video = new Video("https://www.youtube.com/watch?v=rUWxSEwctFU");
        arrayList.add(video);
        video = new Video("https://www.youtube.com/watch?v=_S3H8rVCTis");
        arrayList.add(video);
        video = new Video("https://www.youtube.com/watch?v=EZY-lDjB9e0");
        arrayList.add(video);
        video = new Video("https://www.youtube.com/watch?v=zOWJqNPeifU");
        arrayList.add(video);

        /*Video video = new Video ("http://techslides.com/demos/sample-videos/small.mp4");
        arrayList.add(video);
        video = new Video ("android.resource://" + getPackageName() + "/" + R.raw.video);
        arrayList.add(video);*/

        Adaptador adapter = new Adaptador(arrayList, getApplicationContext());
        recycler.setAdapter(adapter);

    }

}
